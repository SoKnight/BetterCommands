package bettercommands.commands.teleport;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.AbstractCommand;
import bettercommands.commands.TabCompletionHelper;
import bettercommands.files.Messages;

public class CommandUpward extends AbstractCommand {
	
	public CommandUpward() {
		super("upward", "bcommands.teleport.upward");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!hasPermission()) return true;
		
		Player target;
		boolean self = true;
		
		if(args.length != 0) {
			if(!hasPermission("bcommands.upward.other")) return true;
			if(!isPlayerOnline(args[0])) return true;
			target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
			self = false;
		} else {
			if(!isPlayerRequired()) return true;
			target = (Player) sender;
		}
		
		String name = target.getName();
		World world = target.getWorld();
		
		if(!world.getEnvironment().equals(Environment.NORMAL)) {
			if(self) sendMessage("upward-denied-world-self");
			else sendMessage("upward-denied-world-other", "%player%", name);
			return true;
		}
		
		Location location = target.getWorld().getHighestBlockAt(target.getLocation()).getLocation().add(0, 1, 0);
		Block block = world.getBlockAt(location);
		
		while(block != null && !block.getType().equals(Material.AIR)) {
			location.add(0, 1, 0);
			block = world.getBlockAt(location);
		}
		
		Location tloc = target.getLocation();
		location.setPitch(tloc.getPitch());
		location.setYaw(tloc.getYaw());
		target.teleport(location);
		
		if(!self) sendMessage("upward-other", "%player%", name);
		target.sendMessage(Messages.getMessage("upward-self"));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.teleport.upward.other")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addOnlinePlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
