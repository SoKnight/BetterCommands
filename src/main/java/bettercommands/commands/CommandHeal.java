package bettercommands.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal extends AbstractCommand {
	
	public CommandHeal() {
		super("heal", "bcommands.heal");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!hasPermission()) return true;
		
		Player target;
		boolean self = true;
		
		if(args.length != 0) {
			if(!hasPermission("bcommands.heal.other")) return true;
			if(!isPlayerOnline(args[0])) return true;
			target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
			self = false;
		} else {
			if(!isPlayerRequired()) return true;
			target = (Player) sender;
		}
		
		String name = target.getName();
		double health = target.getHealth();
		
		if(health == 20) {
			if(self) sendMessage("heal-self-already");
			else sendMessage("heal-other-already", "%player%", name);
			return true;
		}
		
		double max = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		target.setHealth(max);
		
		if(self) sendMessage("heal-self", "%from%", (int) health, "%to%", (int) max);
		else sendMessage("heal-other", "%player%", name, "%from%", (int) health, "%to%", (int) max);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.heal.other")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addOnlinePlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
