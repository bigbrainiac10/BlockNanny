package com.bigbrainiac10.blocknanny.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bigbrainiac10.blocknanny.BlockNanny;

public class DatabaseManager {

	private Database db;
	private BlockNanny plugin = BlockNanny.getInstance();
	
	public DatabaseManager(Database db){
		this.db = db;
		if(db.connect()){
			createTables();
		}
	}
	
	private void createTables(){
		db.execute("CREATE TABLE IF NOT EXISTS block_places("
				+ "placement_id int NOT NULL AUTO_INCREMENT,"
				+ "world_id varchar(255) NOT NULL,"
				+ "world_x int NOT NULL,"
				+ "world_y int NOT NULL,"
				+ "world_z int NOT NULL,"
				+ "block_id varchar(255) NOT NULL,"
				+ "player_uuid varchar(36) NOT NULL,"
				+ "PRIMARY KEY(placement_id),"
				+ "INDEX block_places_natural_key(world_id, world_x, world_y, world_z),"
				+ "INDEX player_uuid_index(player_uuid));"
				);
	}
	
	public int playerPlacedBlocks(String blockID, String playerUUID) throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT COUNT(*) FROM block_places WHERE player_uuid=? AND block_id=?;");
		
		ps.setString(1, playerUUID);
		ps.setString(2, blockID);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()){
			int blocksPlaced = rs.getInt(1);
			return blocksPlaced;
		}else{
			return 0;
		}
	}
	
	public void insertNewBlock(String worldID, int worldX, int worldY, int worldZ, String blockID, String playerUUID) throws SQLException{
		PreparedStatement ps = db.prepareStatement("INSERT INTO block_places(world_id, world_x, world_y, world_z, block_id, player_uuid) VALUES(?,?,?,?,?,?);");
		
		ps.setString(1, worldID);
		ps.setInt(2, worldX);
		ps.setInt(3, worldY);
		ps.setInt(4, worldZ);
		ps.setString(5, blockID);
		ps.setString(6, playerUUID);
		
		ps.executeUpdate();
	}
	
	public void deleteBlock(String worldID, int worldX, int worldY, int worldZ) throws SQLException{
		PreparedStatement ps = db.prepareStatement("DELETE FROM block_places WHERE world_id=? AND world_x=? AND world_y=? AND world_z=?;");
		
		ps.setString(1, worldID);
		ps.setInt(2, worldX);
		ps.setInt(3, worldY);
		ps.setInt(4, worldZ);

		ps.executeUpdate();
	}
	
	public boolean updateBlockPosition(String worldID, int worldX, int worldY, int worldZ, int newWorldX, int newWorldY, int newWorldZ) throws SQLException{
		PreparedStatement ps = db.prepareStatement("UPDATE block_places SET world_x=?, world_y=?, world_z=? WHERE world_id=? AND world_x=? AND world_y=? AND world_z=?;");
		
		ps.setInt(1, newWorldX);
		ps.setInt(2, newWorldY);
		ps.setInt(3, newWorldZ);
		ps.setString(4, worldID);
		ps.setInt(5, worldX);
		ps.setInt(6, worldY);
		ps.setInt(7, worldZ);
		
		int rowsAffected = ps.executeUpdate();
		
		return rowsAffected > 0;
	}
}
