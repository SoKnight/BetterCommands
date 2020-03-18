package bettercommands.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed extends AbstractCommand {
	
	public CommandFeed() {
		super("feed", "bcommands.feed");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!hasPermission()) return true;
		
		Player target;
		boolean self = true;
		
		if(args.length != 0) {
			if(!hasPermission("bcommands.feed.other")) return true;
			if(!isPlayerOnline(args[0])) return true;
			target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
			self = false;
		} else {
			if(!isPlayerRequired()) return true;
			target = (Player) sender;
		}
		
		String name = target.getName();
		int food = target.getFoodLevel();
		
		if(food == 20) {
			if(self) sendMessage("feed-self-already");
			else sendMessage("feed-other-already", "%player%", name);
			return true;
		}
		
		target.setFoodLevel(20);
		
		if(self) sendMessage("feed-self", "%from%", food);
		else sendMessage("feed-other", "%player%", name, "%from%", food);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.feed.other")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addOnlinePlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
