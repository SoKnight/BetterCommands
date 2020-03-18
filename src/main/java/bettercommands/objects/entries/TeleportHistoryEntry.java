package bettercommands.objects.entries;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import bettercommands.objects.TextLocation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = "tphistory")
public class TeleportHistoryEntry {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name, fromWorld, toWorld;
	@DatabaseField
	private TeleportCause cause;
	@DatabaseField
	private long time;
	@DatabaseField
	private int fromX, fromY, fromZ, toX, toY, toZ;
	
	public TeleportHistoryEntry(String name, TeleportCause cause, Location from, Location to) {
		this.name = name;
		this.cause = cause;
		this.time = System.currentTimeMillis();
		this.fromWorld = from.getWorld().getName();
		this.fromX = from.getBlockX();
		this.fromY = from.getBlockY();
		this.fromZ = from.getBlockZ();
		this.toWorld = to.getWorld().getName();
		this.toX = to.getBlockX();
		this.toY = to.getBlockY();
		this.toZ = to.getBlockZ();
	}
	
	public TextLocation getFromLocation() {
		return new TextLocation(fromWorld, fromX, fromY, fromZ);
	}
	
	public TextLocation getToLocation() {
		return new TextLocation(toWorld, toX, toY, toZ);
	}
	
}
