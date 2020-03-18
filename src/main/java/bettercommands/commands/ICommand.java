package bettercommands.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface ICommand extends CommandExecutor {

	void setSenderAndArgs(CommandSender sender, String[] args);
	
	void sendMessage(String section, Object... replacements);
	
    boolean hasPermission();
    
    boolean hasPermission(String permission);
    
    boolean isPlayerRequired();
    
    boolean isPlayerOnline(String name);
    
    boolean argIsInteger(String arg);
    
    boolean isCorrectUsage();
    
    boolean isWorldExist(String name);
    
}
