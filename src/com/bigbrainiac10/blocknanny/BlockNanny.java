package com.bigbrainiac10.blocknanny;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.bigbrainiac10.blocknanny.database.Database;

public class BlockNanny extends JavaPlugin {

	private static BlockNanny instance;
	private static Logger logger;
	private Database db;
	
	public void onEnable(){
		instance = this;
		logger = this.getLogger();
		
		saveDefaultConfig();
		reloadConfig();
		new ConfigManager(getConfig());
		
		initializeDatabase();
		
		registerListeners();
		registerCommands();
		
		ConfigurationSection sc = ConfigManager.getLimitations();
		
		Log(Level.INFO, "Cactus: " + Integer.toString(ConfigManager.getCactus()));
		
		Log(Level.INFO, Integer.toString(sc.getKeys(false).size()));
		
		for(String key : sc.getKeys(false)){
			Log(Level.INFO, "Players are limited in the placement of " + Material.valueOf(key).name());
		}
		
		/*for(Map.Entry<String, Object> entry : ConfigManager.getLimitations().entrySet()){
			Material mat = Material.valueOf(entry.getKey());
			Log(Level.INFO, "Players can place a limit of " + entry.getValue() + " " + mat.name() + "s");
		}*/
	}
	
	public void onDisable(){
	}
	
	public static void Log(String message){
		Log(Level.INFO, message);
	}
	
	public static void Log(String message, Object...vars){
		Log(Level.INFO, message, vars);
	}

	public static void Log(Level level, String message, Object...vars){
		logger.log(level, message, vars);
	}
	
	public Database getDB(){
		return db;
	}
	
	public static BlockNanny getInstance(){
		return instance;
	}
	
	private void initializeDatabase(){
		/*String host = SHOConfigManager.getHostName();
		String user = SHOConfigManager.getUserName();
		String password = SHOConfigManager.getPassword();
		int port = SHOConfigManager.getPort();
		String dbName = SHOConfigManager.getDBName();
		
		_db = new Database(host, port, dbName, user, password, getLogger());*/
	}
	
	private void registerListeners(){
		/*_instance.getServer().getPluginManager().registerEvents(new QuestionListener(), _instance);
		_instance.getServer().getPluginManager().registerEvents(new PlayerListener(), _instance);*/
	}
	
	private void registerCommands(){
		/*_instance.getCommand("helpop").setExecutor(new HelpOPCommand(this));
		_instance.getCommand("viewhelp").setExecutor(new ViewHelpCommand(this));*/
	}
}
