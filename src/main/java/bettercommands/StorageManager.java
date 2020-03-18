package bettercommands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_15_R1.DimensionManager;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class StorageManager {

	private final MinecraftServer server;
	private final WorldServer world;
	
	private HashMap<HumanEntity, InventoryView> sessions;
	private HashMap<String, Integer> navigationCache;
	
	public StorageManager() {
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
        this.world = server.getWorldServer(DimensionManager.OVERWORLD);
        this.sessions = new HashMap<>();
        this.navigationCache = new HashMap<>();
	}
	
	public Player getPlayer(OfflinePlayer offlinePlayer) {
		if(offlinePlayer == null) return null;
		
		UUID uuid = offlinePlayer.getUniqueId();
        GameProfile profile = new GameProfile(uuid, offlinePlayer.getName());
        
        EntityPlayer entity = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        Player target = entity == null ? null : (Player) entity.getBukkitEntity();
        
        if(target != null) target.loadData();
        
        return target;
	}
	
	public void closeSession(HumanEntity player) {
		sessions.remove(player);
	}
	
	public InventoryView getSessionView(HumanEntity player) {
		return sessions.get(player);
	}
	
	public boolean hasSession(HumanEntity player) {
		return sessions.containsKey(player);
	}
	
	public void startSession(HumanEntity player, InventoryView view) {
		sessions.put(player, view);
	}
	
	public int getCurrentIndex(String name, int def) {
		return navigationCache.containsKey(name) ? navigationCache.get(name) : def;
	}
	
	public int increaseIndex(String name, int def, int steps) {
		int current = getCurrentIndex(name, def);
		current += steps;
		navigationCache.put(name, current);
		return current;
	}
	
	public int decreaseIndex(String name, int def, int steps) {
		int current = getCurrentIndex(name, def);
		current -= steps;
		navigationCache.put(name, current);
		return current;
	}
	
}
