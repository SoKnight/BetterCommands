package bettercommands.objects;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextLocation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String world;
	private int x;
	private int y;
	private int z;
	
	public TextLocation(Location location) {
		this.world = location.getWorld().getName();
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
	}
	
	public Location toLocation() {
		World bukkitWorld = Bukkit.getWorld(world);
		if(bukkitWorld == null) return null;
		
		return new Location(bukkitWorld, x, y, z);
	}
	
	public String getAsString() {
		return world + ": " + x + " " + y + " " + z;
	}
	
}
