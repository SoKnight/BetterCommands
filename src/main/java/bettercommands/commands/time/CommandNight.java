package bettercommands.commands.time;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.AbstractCommand;
import bettercommands.commands.TabCompletionHelper;
import bettercommands.files.Config;

public class CommandNight extends AbstractCommand {

	public CommandNight() {
		super("night", "bcommands.time.night");
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
		
		world.setTime(Config.getConfig().getLong("time.night", 14000));
		sendMessage("time-set-night", "%world%", world.getName());
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
