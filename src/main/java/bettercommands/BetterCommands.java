package bettercommands;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import bettercommands.commands.CommandCoords;
import bettercommands.commands.CommandEnchant;
import bettercommands.commands.CommandEnderchest;
import bettercommands.commands.CommandFeed;
import bettercommands.commands.CommandGamemode;
import bettercommands.commands.CommandHeal;
import bettercommands.commands.CommandInventory;
import bettercommands.commands.CommandPotion;
import bettercommands.commands.CommandsHandler;
import bettercommands.commands.teleport.CommandBack;
import bettercommands.commands.teleport.CommandForward;
import bettercommands.commands.teleport.CommandTphere;
import bettercommands.commands.teleport.CommandTphistory;
import bettercommands.commands.teleport.CommandUpward;
import bettercommands.commands.time.CommandClocks;
import bettercommands.commands.time.CommandDay;
import bettercommands.commands.time.CommandEvening;
import bettercommands.commands.time.CommandMorning;
import bettercommands.commands.time.CommandNight;
import bettercommands.commands.weather.CommandRain;
import bettercommands.commands.weather.CommandStorm;
import bettercommands.commands.weather.CommandSun;
import bettercommands.database.Database;
import bettercommands.database.DatabaseManager;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.listeners.CommentsListener;
import bettercommands.listeners.EnchantListener;
import bettercommands.listeners.InventoryListener;
import bettercommands.listeners.TeleportsListener;
import bettercommands.utils.Logger;
import lombok.Getter;

@Getter
public class BetterCommands extends JavaPlugin {

	@Getter private static BetterCommands instance;
	
	private StorageManager storageManager;
	private DatabaseManager databaseManager;
	private CommentsListener commentsListener;
	
	private CommandInventory commandInventory;
	private CommandEnderchest commandEnderchest;
	
	@Override
	public void onEnable() {
		instance = this;
		
		Config.refresh();
		Messages.refresh();
		
		try {
			Database database = new Database();
			this.databaseManager = new DatabaseManager(database);
			this.storageManager = new StorageManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 * Registration listeners
		 */
		
		PluginManager pluginManager = getServer().getPluginManager();
		this.commentsListener = new CommentsListener(databaseManager);
		
		pluginManager.registerEvents(commentsListener, this);
		pluginManager.registerEvents(new TeleportsListener(databaseManager), this);
		pluginManager.registerEvents(new InventoryListener(storageManager), this);
		
		if(Config.getConfig().getBoolean("disable-item-enchanting"))
			pluginManager.registerEvents(new EnchantListener(), this);
		
		/*
		 * Registration commands executors
		 */
		
		new CommandClocks();
		new CommandMorning();
		new CommandDay();
		new CommandEvening();
		new CommandNight();
		
		new CommandSun();
		new CommandRain();
		new CommandStorm();
		
		new CommandHeal();
		new CommandFeed();
		new CommandGamemode();
		
		new CommandInventory(storageManager);
		new CommandEnderchest(storageManager);
		new CommandEnchant();
		new CommandPotion();
		new CommandCoords(databaseManager);
		new CommandsHandler(databaseManager);
		
		new CommandUpward();
		new CommandBack(databaseManager, storageManager);
		new CommandForward(databaseManager, storageManager);
		new CommandTphistory(databaseManager);
		new CommandTphere();
		
		Logger.info("Enabled.");
	}
	
	@Override
	public void onDisable() {
		if(databaseManager != null) databaseManager.shutdown();
	}
	
}
