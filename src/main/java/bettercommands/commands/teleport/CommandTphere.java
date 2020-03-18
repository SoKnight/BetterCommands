package bettercommands.commands.teleport;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.AbstractCommand;
import bettercommands.commands.TabCompletionHelper;
import bettercommands.files.Messages;

public class CommandTphere extends AbstractCommand {
	
	public CommandTphere() {
		super("tphere", "bcommands.teleport.here", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;

		if(!isPlayerOnline(args[0])) return true;
		Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
		Player p = (Player) sender;
		
		String name = target.getName();
		if(name.equals(p.getName())) {
			sender.sendMessage(Messages.getMessage("tphere-self"));
			return true;
		}
		
		Location location = p.getLocation();
		Location tloc = target.getLocation();
		location.setPitch(tloc.getPitch());
		location.setYaw(tloc.getYaw());
		target.teleport(location);
		
		sendMessage("tphere-other", "%player%", name);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.teleport.here")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addOnlinePlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
