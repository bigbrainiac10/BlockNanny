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
	
	public static int getCactus(){
		int cs = config.getInt("limits.CACTUS");
		return cs;
	}
	
}
