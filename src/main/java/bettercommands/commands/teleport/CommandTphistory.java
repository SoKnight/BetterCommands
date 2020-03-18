package bettercommands.commands.teleport;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import bettercommands.commands.AbstractCommand;
import bettercommands.commands.TabCompletionHelper;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.objects.entries.TeleportHistoryEntry;
import bettercommands.utils.ListUtils;
import net.md_5.bungee.api.chat.BaseComponent;

public class CommandTphistory extends AbstractCommand {

	private final DatabaseManager dbm;
	
	public CommandTphistory(DatabaseManager dbm) {
		super("tphistory", "bcommands.teleport.history", 1);
		this.dbm = dbm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;
		
		String name = args[0];
		
		List<TeleportHistoryEntry> history = dbm.getHistory(name);
		if(history == null || history.isEmpty()) {
			sendMessage("tphistory-empty", "%player%", name);
			return true;
		}
		
		int page = 1;
		if(args.length > 1)
			if(!argIsInteger(args[1])) return true;
			else page = Integer.parseInt(args[1]);
		
		int size = Config.getConfig().getInt("messages.list-size");
		List<BaseComponent[]> onpage = ListUtils.getHistoryOnPage(history, size, page);
		
		if(onpage.isEmpty()) {
			sendMessage("tphistory-page-empty", "%page%", page);
			return true;
		}
		
		int pages = history.size() / size;
		if(history.size() % size != 0) pages++;
		
		String header = Messages.formatMessage("tphistory-header", "%page%", page, "%max_page%", pages);
		String footer = Messages.getMessage("tphistory-footer");
		
		sender.sendMessage(header);
		onpage.forEach(bc -> sender.spigot().sendMessage(bc));
		sender.sendMessage(footer);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.teleport.history")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addOnlinePlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
