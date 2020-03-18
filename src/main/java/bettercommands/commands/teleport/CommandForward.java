package bettercommands.commands.teleport;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import bettercommands.StorageManager;
import bettercommands.commands.AbstractCommand;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Messages;
import bettercommands.objects.entries.TeleportHistoryEntry;

public class CommandForward extends AbstractCommand {

	private final DatabaseManager dbm;
	private final StorageManager storage;
	
	public CommandForward(DatabaseManager dbm, StorageManager storage) {
		super("forward", "bcommands.teleport.forward", 1);
		this.dbm = dbm;
		this.storage = storage;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		
		Player p = (Player) sender;
		String name = p.getName();
		
		List<TeleportHistoryEntry> history = dbm.getHistory(name);
		if(history == null || history.isEmpty()) {
			sender.sendMessage(Messages.getMessage("forward-history-empty"));
			return true;
		}
		
		int steps = 1;
		if(args.length > 1)
			if(!argIsInteger(args[0])) return true;
			else steps = Integer.parseInt(args[0]);
		
		if(steps < 1) steps = 1;
		
		int def = history.size() - 1;
		int current = storage.getCurrentIndex(name, def);
		
		if(current == def) {
			sender.sendMessage(Messages.getMessage("forward-max"));
			return true;
		}
		
		int available = def - current;
		if(steps >= available) steps = available;
		
		current = storage.increaseIndex(name, def, steps);
		
		TeleportHistoryEntry entry = history.get(current);
		Location destination = entry.getToLocation().toLocation();
		
		sendMessage(steps == 1 ? "forward-one-step" : "forward-multisteps", "%steps%", steps);
		p.teleport(destination, TeleportCause.UNKNOWN);
		return true;
	}

}
