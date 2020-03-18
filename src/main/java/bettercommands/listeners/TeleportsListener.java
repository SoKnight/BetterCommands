package bettercommands.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import bettercommands.database.DatabaseManager;
import bettercommands.objects.entries.TeleportHistoryEntry;

public class TeleportsListener implements Listener {

	private final DatabaseManager databaseManager;
	private final List<TeleportCause> useless;
	
	public TeleportsListener(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
		this.useless = Arrays.asList(TeleportCause.CHORUS_FRUIT, TeleportCause.SPECTATE, TeleportCause.UNKNOWN);
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		TeleportCause cause = event.getCause();
		
		if(useless.contains(cause)) return;
		
		Player p = event.getPlayer();
		String name = p.getName();
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		TeleportHistoryEntry entry = new TeleportHistoryEntry(name, cause, from, to);
		databaseManager.saveHistory(entry);
	}
	
	
}
