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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import bettercommands.StorageManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.objects.patterns.InventoryPattern;

public class CommandInventory extends AbstractCommand {
	
	private final StorageManager storage;
	private final DateFormat format;
	
	public CommandInventory(StorageManager storage) {
		super("inventory", "bcommands.inventory", 1);
		this.storage = storage;
		this.format = new SimpleDateFormat(Config.getConfig().getString("titles-date-format"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;
		
		Player p = (Player) sender;
		String name = args[0];
		
		if(name.equals(p.getName())) {
			p.sendMessage(Messages.getMessage("inventory-self"));
			return true;
		}

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
		if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
			sendMessage("error-unknown-player", "%player%", name);
			return true;
		}

		InventoryPattern inventoryPattern = Config.getInventoryPattern();
		Inventory pattern = inventoryPattern.getPattern();

		boolean online = offlinePlayer.isOnline();
		Player target = online ? offlinePlayer.getPlayer() : storage.getPlayer(offlinePlayer);
		
		if(target == null) {
			sendMessage("error-unknown-player", "%player%", name);
			return true;
		}
		
		String title = online ? inventoryPattern.getOnlineTitle() : inventoryPattern.getOfflineTitle();
		title = title.replace("%player%", name);
		
		if(!online) {
			long lastPlayed = offlinePlayer.getLastPlayed();
			int modifier = Config.getConfig().getInt("time-modifier") * 60000;
			
			Date dateObject = new Date(lastPlayed + modifier);
			String date = format.format(dateObject);
			title = title.replace("%date%", date);
		}
		
		PlayerInventory inv = target.getInventory();
		
		ItemStack[] content = pattern.getContents();
		pattern = Bukkit.createInventory(p, pattern.getSize(), title);
		pattern.setContents(content);
		
		pattern = inventoryPattern.setMainHandItem(pattern, inv.getItemInMainHand());
		pattern = inventoryPattern.setOffHandItem(pattern, inv.getItemInOffHand());
		pattern = inventoryPattern.setHelmet(pattern, inv.getHelmet());
		pattern = inventoryPattern.setChestplate(pattern, inv.getChestplate());
		pattern = inventoryPattern.setLeggings(pattern, inv.getLeggings());
		pattern = inventoryPattern.setBoots(pattern, inv.getBoots());
		
		int i = 0;
		for(ItemStack item : inv.getStorageContents()) {
			pattern = inventoryPattern.setContentItem(pattern, i, item);
			i++;
		}

		InventoryView view = p.openInventory(pattern);
		storage.startSession(p, view);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		if(!sender.hasPermission("bcommands.inventory")) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addAllPlayers(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
