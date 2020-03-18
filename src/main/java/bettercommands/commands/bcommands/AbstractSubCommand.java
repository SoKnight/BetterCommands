package bettercommands.commands.bcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.files.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class AbstractSubCommand implements ISubCommand {

	private final CommandSender sender;
	
	private String[] args;
	private String permission;
    private int minArgsLength = 0;

    @Override
    public boolean hasPermission() {
    	if(sender.hasPermission(permission)) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-no-permissions"));
        return true;
    }
    
    @Override
    public boolean hasPermission(String permission) {
    	if(sender.hasPermission(permission)) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-no-permissions"));
        return true;
    }
    
    @Override
    public boolean isPlayerRequired() {
    	if(sender instanceof Player) return true;
    	
    	sender.sendMessage(Messages.getMessage("error-only-for-players"));
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
