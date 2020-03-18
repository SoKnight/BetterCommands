package bettercommands.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import bettercommands.database.DatabaseManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.objects.entries.CoordsEntry;

public class CommentsListener implements Listener {

	private Map<String, CoordsEntry> sessions;
	private final DatabaseManager dbm;
	
	public CommentsListener(DatabaseManager dbm) {
		this.dbm = dbm;
		this.sessions = new HashMap<>();
	}
	
	@EventHandler
	public void onCommentSend(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		String name = p.getName();
		
		if(!sessions.containsKey(name)) return;
		
		String comment = event.getMessage();
		event.setCancelled(true);
		
		int length = comment.length();
		int limit = Config.getConfig().getInt("coords-comment-length");
		
		if(length > limit) {
			p.sendMessage(Messages.formatMessage("coords-add-comment-failed", "%length%", length, "%limit%", limit));
			return;
		}
		
		CoordsEntry entry = sessions.get(name);
		entry.setComment(comment);
		dbm.saveCoords(entry);
		
		sessions.remove(name);
		p.sendMessage(Messages.formatMessage("coords-added", "%comment%", comment));
	}
	
	public void addSession(String name, CoordsEntry entry) {
		sessions.put(name, entry);
	}
	
	public boolean hasSession(String name) {
		return sessions.containsKey(name);
	}
	
}
