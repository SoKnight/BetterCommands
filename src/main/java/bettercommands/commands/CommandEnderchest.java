package bettercommands.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import bettercommands.StorageManager;
import bettercommands.files.Config;
import bettercommands.objects.patterns.EnderchestPattern;

public class CommandEnderchest extends AbstractCommand {
	
	private final StorageManager storage;
	private final DateFormat format;
	
	public CommandEnderchest(StorageManager storage) {
		super("enderchest", "bcommands.enderchest", 1);
		this.storage = storage;
		this.format = new SimpleDateFormat(Config.getConfig().getString("titles-date-format"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSender(sender);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		
		Player p = (Player) sender;
		String name = p.getName();
		boolean self = true;
		
		if(args.length != 0) {
			name = args[0];
			self = false;
		}
		
		String title;
		Inventory inv;
		
		EnderchestPattern enderchestPattern = Config.getEnderchestPattern();
		
		if(self) {
			title = enderchestPattern.getSelfTitle();
			inv = p.getEnderChest();
		} else {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
			if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
				sendMessage("error-unknown-player", "%player%", name);
				return true;
			}

			boolean online = offlinePlayer.isOnline();
			Player target = online ? offlinePlayer.getPlayer() : storage.getPlayer(offlinePlayer);
		
			if(target == null) {
				sendMessage("error-unknown-player", "%player%", name);
				return true;
			}
			
			title = online ? enderchestPattern.getOnlineTitle() : enderchestPattern.getOfflineTitle();
			title = title.replace("%player%", name);
			
			if(!online) {
				long lastPlayed = offlinePlayer.getLastPlayed();
				int modifier = Config.getConfig().getInt("time-modifier") * 60000;
				
				Date dateObject = new Date(lastPlayed + modifier);
				String date = format.format(dateObject);
				title = title.replace("%date%", date);
			}
			
			inv = target.getEnderChest();
			
		}
		
		Inventory pattern = Bukkit.createInventory(p, 27, title);
		pattern.setContents(inv.getStorageContents());

		InventoryView view = p.openInventory(pattern);
		storage.startSession(p, view);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.enderchest")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addAllPlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
