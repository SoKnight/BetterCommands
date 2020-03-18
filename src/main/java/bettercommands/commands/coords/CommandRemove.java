package bettercommands.commands.coords;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.commands.bcommands.AbstractSubCommand;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Messages;
import bettercommands.objects.entries.CoordsEntry;

public class CommandRemove extends AbstractSubCommand {

	private final CommandSender sender;
	private final String[] args;
	private final DatabaseManager dbm;
	
	public CommandRemove(CommandSender sender, String[] args, DatabaseManager dbm) {
		super(sender, args, "", 4);
		this.sender = sender;
		this.args = args;
		this.dbm = dbm;
	}

	@Override
	public void execute() {
		if(!isPlayerRequired()) return;
		if(!isCorrectUsage()) return;
		
		Player p = (Player) sender;
		String name = p.getName();
		
		if(!argIsInteger(args[1]) || !argIsInteger(args[2]) || !argIsInteger(args[3])) return;
		
		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int z = Integer.parseInt(args[3]);
		
		CoordsEntry entry = new CoordsEntry(name, x, y, z);
		CoordsEntry removed = dbm.removeCoords(entry);
		
		if(removed == null)
			p.sendMessage(Messages.formatMessage("coords-remove-failed", "%x%", x, "%y%", y, "%z%", z));
		else
			p.sendMessage(Messages.formatMessage("coords-removed", "%comment%", removed.getComment()));
	}

}
