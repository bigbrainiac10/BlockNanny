package com.bigbrainiac10.blocknanny;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class BlockNanny extends JavaPlugin {

	private static BlockNanny instance;
	private static Logger logger;
	
	public void onEnable(){
		instance = this;
		logger = this.getLogger();
		
		/*saveDefaultConfig();
		reloadConfig();
		new SHOConfigManager(getConfig());*/
		
		initializeDatabase();
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
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
