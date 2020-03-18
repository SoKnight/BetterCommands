package bettercommands.objects.patterns;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryPattern {
	
	private final ConfigurationSection config;
	
	@Getter private int size, startIndex, mainHand, offHand, helmet, chestplate, leggings, boots;
	@Getter private Inventory pattern;
	@Getter private String onlineTitle, offlineTitle;
	
	public void initialize() {
		this.onlineTitle = config.getString("online-title").replace("&", "\u00a7");
		this.offlineTitle = config.getString("offline-title").replace("&", "\u00a7");
		
		this.size = config.getInt("rows", 6) * 9;
		this.pattern = Bukkit.createInventory(null, size);
		
		this.startIndex = config.getInt("start-index", 0);
		
		this.helmet = config.getInt("helmet-slot");
		this.chestplate = config.getInt("chestplate-slot");
		this.leggings = config.getInt("leggings-slot");
		this.boots = config.getInt("boots-slot");
		this.offHand = config.getInt("left-hand-slot");
		this.mainHand = config.getInt("right-hand-slot");
		
		initTip(config.getConfigurationSection("helmet-tip"));
		initTip(config.getConfigurationSection("chestplate-tip"));
		initTip(config.getConfigurationSection("leggings-tip"));
		initTip(config.getConfigurationSection("boots-tip"));
		initTip(config.getConfigurationSection("left-hand-tip"));
		initTip(config.getConfigurationSection("right-hand-tip"));
		
		initBackground(config.getConfigurationSection("background"));
	}
	
	private void initBackground(ConfigurationSection config) {
		boolean enabled = config.getBoolean("enabled");
		if(!enabled) return;
		
		List<Integer> slots = config.getIntegerList("slots");
		if(slots.isEmpty()) return;
		
		String materialName = config.getString("material");
		String name = config.getString("name", "").replace("&", "\u00a7");
		
		Material material = Material.valueOf(materialName.toUpperCase());
		if(material == null) return;
		
		ItemStack item = new ItemStack(material, 1);
		if(name != null && !name.equals("")) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}
		
		slots.forEach(i -> pattern.setItem(i, item));
	}
	
	private void initTip(ConfigurationSection config) {
		boolean enabled = config.getBoolean("enabled");
		if(!enabled) return;
		
		int slot = config.getInt("slot");
		
		String materialName = config.getString("material");
		String name = config.getString("name", "").replace("&", "\u00a7");
		
		Material material = Material.valueOf(materialName.toUpperCase());
		if(material == null) return;
		
		ItemStack item = new ItemStack(material, 1);
		if(name != null && !name.equals("")) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}
		
		pattern.setItem(slot, item);
	}
	
//	private void setItem(Inventory inventory, int slot, ItemStack item) {
//		ItemStack[] content = inventory.getContents();
//		content[slot] = item;
//		inventory.setContents(content);
//	}
	
	public Inventory setContentItem(Inventory inventory, int slot, ItemStack item) {
		slot += startIndex;
		if(slot >= size) return inventory;
		inventory.setItem(slot, item);
		return inventory;
	}
	
	public Inventory setMainHandItem(Inventory inventory, ItemStack item) {
		if(item == null || mainHand == -1 || item.getType().equals(Material.AIR)) return inventory;
		inventory.setItem(mainHand, item);
		return inventory;
	}
	
	public Inventory setOffHandItem(Inventory inventory, ItemStack item) {
		if(item == null || offHand == -1 || item.getType().equals(Material.AIR)) return inventory;
		inventory.setItem(offHand, item);
		return inventory;
	}
	
	public Inventory setHelmet(Inventory inventory, ItemStack item) {
		if(item == null || helmet == -1) return inventory;
		inventory.setItem(helmet, item);
		return inventory;
	}
	
	public Inventory setChestplate(Inventory inventory, ItemStack item) {
		if(item == null || chestplate == -1) return inventory;
		inventory.setItem(chestplate, item);
		return inventory;
	}
	
	public Inventory setLeggings(Inventory inventory, ItemStack item) {
		if(item == null || leggings == -1) return inventory;
		inventory.setItem(leggings, item);
		return inventory;
	}
	
	public Inventory setBoots(Inventory inventory, ItemStack item) {
		if(item == null || boots == -1) return inventory;
		inventory.setItem(boots, item);
		return inventory;
	}
	
}
