package com.bigbrainiac10.blocknanny;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

	private static FileConfiguration config;
	
	public ConfigManager(FileConfiguration con){
		config = con;
	}
	
	public static ConfigurationSection getLimitations(){
		ConfigurationSection cs = config.getConfigurationSection("limits");
		return cs;
	}
	
	public static String getMsg(String path){
		return config.getString("msgs."+path);
	}
	
	
	//DB Configs
	public static String getHostName(){
		return config.getString("db.hostname");
	}
	
	public static String getUserName(){
		return config.getString("db.user");
	}
	
	public static String getPassword(){
		return config.getString("db.pass");
	}
	
	public static int getPort(){
		return config.getInt("db.port");
	}
	
	public static String getDBName(){
		return config.getString("db.dbname");
	}
	
	public static boolean getDebug(){
		return config.getBoolean("debug");
	}
	
}
