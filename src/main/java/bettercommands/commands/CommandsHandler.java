package bettercommands.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import bettercommands.commands.bcommands.CommandHelp;
import bettercommands.commands.bcommands.CommandReload;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Messages;

public class CommandsHandler extends AbstractCommand {

	private final DatabaseManager databaseManager;
	
	public CommandsHandler(DatabaseManager databaseManager) {
		super("bcommands", "");
		this.databaseManager = databaseManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		super.setSender(sender);
		
		if(args.length == 0) {
			sender.sendMessage(Messages.getMessage("error-no-args"));
			return true;
		}
		
		switch(args[0]) {
		case "help":
			new CommandHelp(sender).execute();
			break;
		case "info":
			sender.sendMessage(ChatColor.RED + "Эту функцию пока не завезли :(");
			break;
		case "reload":
			new CommandReload(sender).execute();
			break;
		default:
			sender.sendMessage(Messages.getMessage("error-command-not-found"));
			break;
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length == 0) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper(databaseManager);
		
		if(args.length == 1) {
			helper.addSubcommands(args[0].toLowerCase());
			return helper.getCompletions();
		}
		
		String cmd = args[0].toLowerCase();
		
		if(args.length == 2 && cmd.equals("info"))
			helper.addAllPlayers(args[1].toLowerCase());
		
		return helper.getCompletions();
	}
	
}
