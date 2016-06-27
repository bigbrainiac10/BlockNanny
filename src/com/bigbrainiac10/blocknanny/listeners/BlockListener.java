package com.bigbrainiac10.blocknanny.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.material.PistonExtensionMaterial;

import com.bigbrainiac10.blocknanny.BlockNanny;
import com.bigbrainiac10.blocknanny.ConfigManager;
import com.bigbrainiac10.blocknanny.Utility;
import com.bigbrainiac10.blocknanny.database.DatabaseManager;

public class BlockListener implements Listener {

	private BlockNanny plugin = BlockNanny.getInstance();
	private DatabaseManager dbManager = plugin.getDBManager();
	
	private final String limitLeftMsg = Utility.safeToColor(ConfigManager.getMsg("placesleft"));
	private final String noneLeftMsg = Utility.safeToColor(ConfigManager.getMsg("nomore"));
	
	private final Map<BlockFace, Integer> directionMap;
	
	public BlockListener(){
		directionMap = new HashMap<BlockFace, Integer>();
		
		directionMap.put(BlockFace.EAST, 1);
		directionMap.put(BlockFace.WEST, -1);
		directionMap.put(BlockFace.SOUTH, 1);
		directionMap.put(BlockFace.NORTH, -1);
		directionMap.put(BlockFace.UP, 1);
		directionMap.put(BlockFace.DOWN, -1);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void blockPlace(BlockPlaceEvent event){
		if(event.isCancelled())
			return;
		
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		if(BlockNanny.matCache.containsKey(block.getType())){
			if(player.equals(null))
				return;
			
			int placesLeft = -1;
			try {
				placesLeft = (BlockNanny.matCache.get(block.getType()) - dbManager.playerPlacedBlocks(block.getType().name(), player.getUniqueId().toString())) - 1;
			} catch (SQLException e) {
				BlockNanny.Log(Level.SEVERE, "Unable to recall entry(s) from database: ", e.getMessage());
			}
			
			if(placesLeft < 0){
				player.sendMessage(noneLeftMsg.replace("%BLOCK%", block.getType().name()));
				event.setCancelled(true);
				return;
			}
			
			try {
				dbManager.insertNewBlock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), block.getType().name(), player.getUniqueId().toString());
				

				player.sendMessage(limitLeftMsg.replace("%BLOCK%", block.getType().name()).replace("%PLACESLEFT%", Integer.toString(placesLeft)));
				
				BlockNanny.DebugLog(Level.INFO, "Block {0} placed at {1} {2} {3} in world {4}", block.getType().name(), 
						block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
			} catch (SQLException e) {
				BlockNanny.Log(Level.SEVERE, "Unable to insert entry into database: ", e.getMessage());
			}
		}
	}
	
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void entityExplode(EntityExplodeEvent event){
		if(event.isCancelled())
			return;
		
		for (Block block : event.blockList()) {
					
			if(BlockNanny.matCache.containsKey(block.getType())){	
				try {
					dbManager.deleteBlock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
					
					BlockNanny.DebugLog(Level.INFO, "Block {0} exploded at {1} {2} {3} in world {4}", block.getType().name(), 
							block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
				} catch (SQLException e) {
					BlockNanny.Log(Level.SEVERE, "Unable to update entry in database: ", e.getMessage());
				}
			}
		}
	}
	

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void blockBreak(BlockBreakEvent event){
		if(event.isCancelled())
			return;
		
		Block block = event.getBlock();
		
		if(block.getType().equals(Material.PISTON_EXTENSION)){
			if(block.getState().getData() instanceof PistonExtensionMaterial){
				PistonExtensionMaterial mat = (PistonExtensionMaterial)block.getState().getData();
				block = block.getRelative(mat.getAttachedFace());
			}
		}
		
		
		if(BlockNanny.matCache.containsKey(block.getType())){
			try {
				
				
				dbManager.deleteBlock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
				
				BlockNanny.DebugLog(Level.INFO, "Block {0} broken at {1} {2} {3} in world {4}", block.getType().name(), 
						block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
			} catch (SQLException e) {
				BlockNanny.Log(Level.SEVERE, "Unable to insert entry into database: ", e.getMessage());
			}
		}
	}
	
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void pistonExtend(BlockPistonExtendEvent event) {
		if(event.isCancelled())
			return;
		
		for (Block block : event.getBlocks()) {
			
			if(BlockNanny.matCache.containsKey(block.getType())){
				int new_x = block.getX() + ((event.getDirection().equals(BlockFace.EAST) || event.getDirection().equals(BlockFace.WEST)) ? directionMap.get(event.getDirection()) : 0);
				int new_y = block.getY() + ((event.getDirection().equals(BlockFace.UP) || event.getDirection().equals(BlockFace.DOWN)) ? directionMap.get(event.getDirection()) : 0);
				int new_z = block.getZ() + ((event.getDirection().equals(BlockFace.SOUTH) || event.getDirection().equals(BlockFace.NORTH)) ? directionMap.get(event.getDirection()) : 0);
				
				try {
					dbManager.updateBlockPosition(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), new_x, new_y, new_z);
					BlockNanny.DebugLog(Level.INFO, "Piston pushed block {0} from {1} {2} {3} to {4} {5} {6} in world {7}", block.getType().name(), 
							block.getX(), block.getY(), block.getZ(), new_x, new_y, new_z, block.getWorld().getName());
				} catch (SQLException e) {
					BlockNanny.Log(Level.SEVERE, "Unable to update entry in database: ", e.getMessage());
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void pistonRetract(BlockPistonRetractEvent event) {
		if(event.isCancelled())
			return;
		
		for (Block block : event.getBlocks()) {
			
			if(BlockNanny.matCache.containsKey(block.getType())){
				int new_x = block.getX() + ((event.getDirection().equals(BlockFace.EAST) || event.getDirection().equals(BlockFace.WEST)) ? directionMap.get(event.getDirection()) : 0);
				int new_y = block.getY() + ((event.getDirection().equals(BlockFace.UP) || event.getDirection().equals(BlockFace.DOWN)) ? directionMap.get(event.getDirection()) : 0);
				int new_z = block.getZ() + ((event.getDirection().equals(BlockFace.SOUTH) || event.getDirection().equals(BlockFace.NORTH)) ? directionMap.get(event.getDirection()) : 0);
				
				try {
					dbManager.updateBlockPosition(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), new_x, new_y, new_z);
					BlockNanny.DebugLog(Level.INFO, "Piston pulled block {0} from {1} {2} {3} to {4} {5} {6} in world {7}", block.getType().name(), 
							block.getX(), block.getY(), block.getZ(), new_x, new_y, new_z, block.getWorld().getName());
				} catch (SQLException e) {
					BlockNanny.Log(Level.SEVERE, "Unable to update entry in database: ", e.getMessage());
				}
			}
		}
	}
}
