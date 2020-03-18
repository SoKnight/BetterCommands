package bettercommands.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import bettercommands.StorageManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryListener implements Listener {

	private final StorageManager storage;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity p = event.getWhoClicked();
		if(!storage.hasSession(p)) return;
		
		Inventory inventory = event.getClickedInventory();
		InventoryView view = storage.getSessionView(p);
		
		if(view.getTopInventory().equals(inventory))
			event.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity p = event.getPlayer();
		if(storage.hasSession(p))
			storage.closeSession(p);
	}
	
}
