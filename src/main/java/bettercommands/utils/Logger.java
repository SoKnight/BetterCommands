package bettercommands.utils;

import bettercommands.BetterCommands;

public class Logger {

	private static BetterCommands instance = BetterCommands.getInstance();
	
	public static void info(String info) {
		instance.getLogger().info(info);
	}
	
	public static void warning(String warning) {
		instance.getLogger().warning(warning);
	}
	
	public static void error(String error) {
		instance.getLogger().severe(error);
	}
	
}
