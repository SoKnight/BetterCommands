package bettercommands.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import bettercommands.files.Messages;

public class EnchantListener implements Listener {

	@EventHandler
	public void onEnchantTrying(EnchantItemEvent event) {
		Player p = event.getEnchanter();
		
		event.setCancelled(true);
		
		p.closeInventory();
		p.sendMessage(Messages.getMessage("enchant-trying"));
	}
	
}
