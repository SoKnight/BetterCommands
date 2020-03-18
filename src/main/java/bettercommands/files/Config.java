package bettercommands.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import bettercommands.BetterCommands;
import bettercommands.objects.patterns.EnderchestPattern;
import bettercommands.objects.patterns.InventoryPattern;
import bettercommands.utils.Logger;
import lombok.Getter;

public class Config {

	@Getter private static FileConfiguration config;
	@Getter private static InventoryPattern inventoryPattern;
	@Getter private static EnderchestPattern enderchestPattern;
	
	public static void refresh() {
		BetterCommands instance = BetterCommands.getInstance();
		
		File datafolder = instance.getDataFolder();
		if(!datafolder.isDirectory()) datafolder.mkdirs();
		
		File file = new File(datafolder, "config.yml");
		if(!file.exists()) {
			try {
				InputStream input = instance.getResource("config.yml");
				Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				Logger.info("Generated new config file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		Bukkit.getScheduler().runTaskAsynchronously(instance, () -> refreshPatterns());
	}

	private static void refreshPatterns() {
		inventoryPattern = new InventoryPattern(config.getConfigurationSection("inventory"));
		enderchestPattern = new EnderchestPattern(config.getConfigurationSection("enderchest"));
		
		inventoryPattern.initialize();
		enderchestPattern.initialize();
		Logger.info("[Async] Interface patterns initialization finished.");
	}
	
}
