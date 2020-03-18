package bettercommands.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.coords.CommandAdd;
import bettercommands.commands.coords.CommandList;
import bettercommands.commands.coords.CommandRemove;
import bettercommands.database.DatabaseManager;

public class CommandCoords extends AbstractCommand {
	
	private final DatabaseManager dbm;
	
	public CommandCoords(DatabaseManager dbm) {
		super("coords", "bcommands.coords", 1);
		this.dbm = dbm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;

		switch (args[0].toLowerCase()) {
		case "add":
			new CommandAdd(sender, args, dbm).execute();;
			break;
		case "list":
			new CommandList(sender, args, dbm).execute();;
			break;
		case "remove":
			new CommandRemove(sender, args, dbm).execute();;
			break;
		default:
			break;
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length == 0 || args.length > 4) return null;
		if(!sender.hasPermission("bcommands.tphere")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper(dbm);
		if(args.length == 1) {
			helper.addCoordsSubcommands(args[0].toLowerCase());
		} else {
			switch (args[0].toLowerCase()) {
			case "add": {
				if(!(sender instanceof Player)) break;
				Player p = (Player) sender;
				Location location = p.getLocation();
				switch (args.length) {
				case 2:
					helper.addCompletion(location.getBlockX() + "");
					break;
				case 3:
					helper.addCompletion(location.getBlockY() + "");
					break;
				case 4:
					helper.addCompletion(location.getBlockZ() + "");
					break;
				default:
					break;
				}
				break;
			}
			case "remove":
				if(!(sender instanceof Player)) break;
				Player p = (Player) sender;
				helper.addCoords(p.getName());
				break;
			default:
				break;
			}
		}
		
		return helper.getCompletions();
	}

}
