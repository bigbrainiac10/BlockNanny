package com.bigbrainiac10.blocknanny;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.bigbrainiac10.blocknanny.database.Database;
import com.bigbrainiac10.blocknanny.database.DatabaseManager;
import com.bigbrainiac10.blocknanny.listeners.BlockListener;

public class BlockNanny extends JavaPlugin {

	private static BlockNanny instance;
	private static Logger logger;
	private Database db;
	private DatabaseManager dbManager;
	
	private static boolean debugEnabled;
	
	public static HashMap<Material, Integer> matCache = new HashMap<Material, Integer>();
	
	public void onEnable(){
		instance = this;
		logger = this.getLogger();
		
		saveDefaultConfig();
		reloadConfig();
		new ConfigManager(getConfig());
		
		debugEnabled = ConfigManager.getDebug();
		
		loadLimitations();
		
		initializeDatabase();
		dbManager = new DatabaseManager(db);
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
		db.close();
	}
	
	public static void DebugLog(Level level, String message, Object...vars){
		if(debugEnabled){
			Log(level, message, vars);
		}
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
	
	public DatabaseManager getDBManager(){
		return dbManager;
	}
	
	private void loadLimitations(){
		ConfigurationSection sc = ConfigManager.getLimitations();
		
		for(Map.Entry<String, Object> entry : sc.getValues(false).entrySet()){
			try{ 
				Material mat = Material.valueOf(entry.getKey());
				matCache.put(mat, (Integer)entry.getValue());
				Log(Level.INFO, "Successfully loaded material {0} with a maximum placement limit of {1}", mat.name(), entry.getValue());
			}catch(Exception e){
				Log(Level.WARNING, "Unable to load limitation material {0}. Ignoring...", entry.getKey());
			}
		}
	}
	
	private void initializeDatabase(){
		String host = ConfigManager.getHostName();
		String user = ConfigManager.getUserName();
		String password = ConfigManager.getPassword();
		int port = ConfigManager.getPort();
		String dbName = ConfigManager.getDBName();
		
		db = new Database(host, port, dbName, user, password, getLogger());
	}
	
	private void registerListeners(){
		/*_instance.getServer().getPluginManager().registerEvents(new QuestionListener(), _instance);
		_instance.getServer().getPluginManager().registerEvents(new PlayerListener(), _instance);*/
		instance.getServer().getPluginManager().registerEvents(new BlockListener(), instance);
	}
	
	private void registerCommands(){
		/*_instance.getCommand("helpop").setExecutor(new HelpOPCommand(this));
		_instance.getCommand("viewhelp").setExecutor(new ViewHelpCommand(this));*/
	}
}
