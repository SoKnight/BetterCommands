package bettercommands.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import bettercommands.files.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractCommand implements ICommand, TabCompleter {

	@Setter private CommandSender sender;
	@Setter private String[] args;
	
	private List<String> completions;
	private String permission;
    private int minArgsLength;
    
    public AbstractCommand(String name, String permission) {
    	PluginCommand command = Bukkit.getPluginCommand(name);
    	command.setExecutor(this);
    	command.setTabCompleter(this);
    	this.permission = permission;
    	this.minArgsLength = 1;
    }
    
    public AbstractCommand(String name, String permission, int minArgsLength) {
    	PluginCommand command = Bukkit.getPluginCommand(name);
    	command.setExecutor(this);
    	command.setTabCompleter(this);
    	this.permission = permission;
    	this.minArgsLength = minArgsLength;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    	return null;
    }
    
    @Override
    public void setSenderAndArgs(CommandSender sender, String[] args) {
    	this.sender = sender;
    	this.args = args;
    }

    @Override
    public boolean hasPermission() {
    	if(sender.hasPermission(permission)) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-no-permissions"));
        return false;
    }
    
    @Override
    public boolean hasPermission(String permission) {
    	if(sender.hasPermission(permission)) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-no-permissions"));
        return false;
    }
    
    @Override
    public boolean isPlayerRequired() {
    	if(sender instanceof Player) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-only-for-players"));
    	return false;
    }
    
    @Override
    public boolean isPlayerOnline(String name) {
    	OfflinePlayer op = Bukkit.getOfflinePlayer(name);
    	if(op != null && op.isOnline()) return true;
    	
    	sender.sendMessage(Messages.formatMessage("error-player-not-found", "%player%", name));
    	return false;
    }
    
    @Override
    public boolean argIsInteger(String arg) {
    	try {
			Integer.parseInt(arg);
			return true;
		} catch (NumberFormatException ignored) {
			sender.sendMessage(Messages.formatMessage("error-arg-is-not-int", "%arg%", arg));
			return false;
		}
    }
    
    @Override
    public boolean isCorrectUsage() {
    	if(args.length >= minArgsLength) return true;
    		
    	sender.sendMessage(Messages.getMessage("error-wrong-syntax"));
    	return false;
    }
    
    @Override
    public boolean isWorldExist(String name) {
    	if(Bukkit.getWorld(name) != null) return true;
    	
    	sender.sendMessage(Messages.formatMessage("error-unknown-world", "%world%", name));
    	return false;
    }
    
    @Override
    public void sendMessage(String section, Object... repls) {
    	String message = repls == null ? Messages.getMessage(section) : Messages.formatMessage(section, repls);
    	sender.sendMessage(message);
    }
    
}
