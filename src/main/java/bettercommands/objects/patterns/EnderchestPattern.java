package bettercommands.objects.patterns;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnderchestPattern {
	
	private final ConfigurationSection config;
	
	@Getter private String selfTitle, onlineTitle, offlineTitle;
	
	public void initialize() {
		this.selfTitle = config.getString("self-title").replace("&", "\u00a7");
		this.onlineTitle = config.getString("online-title").replace("&", "\u00a7");
		this.offlineTitle = config.getString("offline-title").replace("&", "\u00a7");
	}
	
}
