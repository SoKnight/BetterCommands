package bettercommands.commands.coords;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.bcommands.AbstractSubCommand;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.objects.entries.CoordsEntry;
import bettercommands.utils.ListUtils;
import bettercommands.utils.StringUtils;

public class CommandList extends AbstractSubCommand {

	private final CommandSender sender;
	private final String[] args;
	private final DatabaseManager dbm;
	
	public CommandList(CommandSender sender, String[] args, DatabaseManager dbm) {
		super(sender, args, "", 1);
		this.sender = sender;
		this.args = args;
		this.dbm = dbm;
	}

	@Override
	public void execute() {
		if(!isPlayerRequired()) return;
		
		String name = ((Player) sender).getName();
		
		int page = 1;
		if(args.length > 1)
			if(!argIsInteger(args[1])) return;
			else page = Integer.parseInt(args[1]);
		
		List<CoordsEntry> coords = dbm.getCoords(name);
		if(coords == null || coords.isEmpty()) {
			sender.sendMessage(Messages.getMessage("coords-list-empty"));
			return;
		}
		
		int size = Config.getConfig().getInt("messages.list-size");
		List<CoordsEntry> onpage = ListUtils.getCoordsOnPage(coords, size, page);
		
		if(onpage.isEmpty()) {
			sendMessage("coords-list-page-empty", "%page%", page);
			return;
		}
		
		int pages = coords.size() / size;
		if(coords.size() % size != 0) pages++;
		
		String header = Messages.formatMessage("coords-list-header", "%page%", page, "%max_page%", pages);
		String body = Messages.getMessage("coords-list-body");
		String footer = Messages.getMessage("coords-list-footer");
		
		List<String> output = new ArrayList<>();
		onpage.forEach(c -> {
			String comment = c.getComment();
			int x = c.getX();
			int y = c.getY();
			int z = c.getZ();
			output.add(StringUtils.format(body, "%comment%", comment, "%x%", x, "%y%", y, "%z%", z));
		});
		
		sender.sendMessage(header);
		output.forEach(s -> sender.sendMessage(s));
		sender.sendMessage(footer);
	}

}
