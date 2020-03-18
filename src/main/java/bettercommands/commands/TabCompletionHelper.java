package bettercommands.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import bettercommands.database.DatabaseManager;
import bettercommands.enums.PotionType;
import bettercommands.objects.entries.CoordsEntry;
import lombok.Getter;

public class TabCompletionHelper {

	private static final List<String> subcommands = Arrays.asList("help", "info", "reload");
	private static final List<String> gamemodes = Arrays.asList("0", "1", "2", "3", "ad", "cr", "sr", "sp");
	private static final List<String> coordsSubcommands = Arrays.asList("add", "list", "remove");
	
	@Getter
	private List<String> completions;
	private DatabaseManager dbm;
	
	public TabCompletionHelper() {
		this.completions = new ArrayList<>();
	}
	
	public TabCompletionHelper(DatabaseManager dbm) {
		this.dbm = dbm;
		this.completions = new ArrayList<>();
	}
	
	public void addCompletion(String completion) {
		completions.add(completion);
	}

	public void addSubcommands(String arg) {
		subcommands.forEach(c -> {
			if(c.startsWith(arg))
				completions.add(c);
		});
	}
	
    public void addAvailableWorlds(String arg) {
    	List<World> worlds = Bukkit.getWorlds();
    	worlds.forEach(w -> {
    		String name = w.getName();
    		if(name.toLowerCase().startsWith(arg))
    			completions.add(name);
    	});
    }
    
    public void addOnlinePlayers(String arg) {
    	Bukkit.getOnlinePlayers().forEach(p -> {
    		String name = p.getName();
    		if(name.toLowerCase().startsWith(arg))
    			completions.add(name);
    	});
    }
    
    public void addAllPlayers(String arg) {
    	addOnlinePlayers(arg);
    	Arrays.stream(Bukkit.getOfflinePlayers()).forEach(op -> {
    		String name = op.getName();
    		if(completions.contains(arg)) return;
    		else if(name.toLowerCase().startsWith(arg)) completions.add(name);
    	});
    }
    
    public void addGamemodes(String arg) {
    	gamemodes.forEach(m -> {
			if(m.startsWith(arg))
				completions.add(m);
		});
    	GameMode[] values = GameMode.values();
    	Arrays.stream(values).forEach(g -> {
    		String name = g.name().toLowerCase();
    		if(name.startsWith(arg))
    			completions.add(name);
    	});
    }
    
    public void addEnchantments(String arg) {
    	Arrays.stream(Enchantment.values()).forEach(e -> {
    		String key = e.getKey().getKey();
    		if(key.startsWith(arg))
    			completions.add(key);
    	});
    }
    
    public void addPotionTypes(String arg) {
    	PotionType[] values = PotionType.values();
    	Arrays.stream(values).forEach(t -> {
    		String name = t.name().toLowerCase();
    		if(name.startsWith(arg))
    			completions.add(name);
    	});
    }
    
    public void addPotionEffects(String arg) {
    	Arrays.stream(PotionEffectType.values()).forEach(e -> {
    		String name = e.getName().toLowerCase();
    		if(name.startsWith(arg))
    			completions.add(name);
    	});
    }
    
    public void addCoordsSubcommands(String arg) {
		coordsSubcommands.forEach(c -> {
			if(c.startsWith(arg))
				completions.add(c);
		});
	}
    
    public void addCoords(String name) {
    	List<CoordsEntry> coords = dbm.getCoords(name);
    	if(coords == null || coords.isEmpty()) return;
    	
    	coords.forEach(c -> completions.add(c.getX() + " " + c.getY() + " " + c.getZ() + " " + c.getComment()));
    }
	
}
