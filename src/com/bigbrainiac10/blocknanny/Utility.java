package com.bigbrainiac10.blocknanny;

import org.bukkit.ChatColor;

public class Utility {
	
	public static String safeToColor(String str){
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String colorToSafe(String str){
		return str.replace(ChatColor.COLOR_CHAR, '&');
	}
	
}
