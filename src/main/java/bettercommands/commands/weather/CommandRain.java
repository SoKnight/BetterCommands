package bettercommands.commands.weather;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.AbstractCommand;
import bettercommands.commands.TabCompletionHelper;

public class CommandRain extends AbstractCommand {
	
	public CommandRain() {
		super("rain", "bcommands.weather.rain");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!hasPermission()) return true;
		
		World world;
		if(args.length != 0) {
			if(!isWorldExist(args[0])) return true;
			world = Bukkit.getWorld(args[0]);
		} else {
			if(!isPlayerRequired()) return true;
			world = ((Player) sender).getWorld();
		}
		
		String name = world.getName();
		boolean storm = world.hasStorm();
		
		if(storm) {
			sendMessage("time-weather-rain-already", "%world%", name);
			return true;
		}
		
		world.setStorm(true);
		world.setThundering(false);
		
		sendMessage("time-weather-rain", "%world%", name);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addAvailableWorlds(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
