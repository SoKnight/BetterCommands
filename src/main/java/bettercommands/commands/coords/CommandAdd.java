package bettercommands.commands.coords;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.BetterCommands;
import bettercommands.commands.bcommands.AbstractSubCommand;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.listeners.CommentsListener;
import bettercommands.objects.entries.CoordsEntry;

public class CommandAdd extends AbstractSubCommand {

	private final CommandSender sender;
	private final String[] args;
	private final DatabaseManager dbm;
	
	public CommandAdd(CommandSender sender, String[] args, DatabaseManager dbm) {
		super(sender, args, "", 1);
		this.sender = sender;
		this.args = args;
		this.dbm = dbm;
	}

	@Override
	public void execute() {
		if(!isPlayerRequired()) return;
		
		Player p = (Player) sender;
		String name = p.getName();
		
		List<CoordsEntry> coords = dbm.getCoords(name);
		if(coords != null && coords.size() > Config.getConfig().getInt("max-coords")) {
			p.sendMessage(Messages.getMessage("coords-add-failed-max"));
			return;
		}
		
		CommentsListener listener = BetterCommands.getInstance().getCommentsListener();
		if(listener.hasSession(name)) {
			p.sendMessage(Messages.getMessage("coords-add-failed-already"));
			return;
		}
		
		int x = 0, y = 0, z = 0;
		Location location = p.getLocation();
		
		// Setup coordinates
		if(args.length > 1) {
			if(!argIsInteger(args[1])) return;
			else x = Integer.parseInt(args[1]);
			if(args.length > 2) {
				if(!argIsInteger(args[2])) return;
				else y = Integer.parseInt(args[2]);
				if(args.length > 3) {
					if(!argIsInteger(args[3])) return;
					else z = Integer.parseInt(args[3]);
				} else z = location.getBlockZ();
			} else y = location.getBlockY();
		} else x = location.getBlockZ();
		
		CoordsEntry entry = new CoordsEntry(name, x, y, z);
		listener.addSession(name, entry);
		
		int limit = Config.getConfig().getInt("coords-comment-length");
		p.sendMessage(Messages.formatMessage("coords-add-comment", "%x%", x, "%y%", y, "%z%", z, "%limit%", limit));
	}

}
