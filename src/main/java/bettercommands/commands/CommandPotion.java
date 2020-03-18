package bettercommands.commands;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import bettercommands.enums.PotionType;
import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.utils.StringUtils;

public class CommandPotion extends AbstractCommand {
	
	public CommandPotion() {
		super("potion", "bcommands.potion", 2);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;
		
		Player p = (Player) sender;
		PlayerInventory inventory = p.getInventory();
		
		// Checking for full inventory
		if(inventory.firstEmpty() == -1) {
			sender.sendMessage(Messages.getMessage("potion-full-inventory"));
			return true;
		}
		
		// Parsing potion type
		PotionType type = PotionType.valueOf(args[0].toUpperCase());
		if(type == null) {
			sendMessage("potion-unknown-type", "%type%", args[0]);
			return true;
		}
		
		// Parsing effect
		PotionEffectType effect = PotionEffectType.getByName(args[1].toUpperCase());
		if(effect == null) {
			sendMessage("potion-unknown-effect", "%effect%", args[1]);
			return true;
		}
		
		// Parsing amplifier
		int amplifier = Config.getConfig().getInt("default-potion-amplifier", 1);
		if(args.length > 2) {
			if(!argIsInteger(args[2])) return true;
			amplifier = Integer.parseInt(args[2]);
		}
		
		if(amplifier < 1 || amplifier > 255) {
			sender.sendMessage(Messages.getMessage("potion-wrong-amplifier"));
			return true;
		}
		
		// Parsing duration
		int duration = Config.getConfig().getInt("default-potion-duration", 30);
		if(args.length > 3) {
			if(!argIsInteger(args[3])) return true;
			duration = Integer.parseInt(args[3]);
		}
		
		if(duration < 1) {
			sender.sendMessage(Messages.getMessage("potion-wrong-duration"));
			return true;
		}
		
		// Getting material
		Material material = Material.POTION;
		
		switch (type) {
		case POTION:
			material = Material.POTION;
			break;
		case SPLASH:
			material = Material.SPLASH_POTION;
			break;
		case CLOUD:
			material = Material.LINGERING_POTION;
			break;
		default:
			break;
		}
		
		// Applying effect to potion item
		ItemStack potion = new ItemStack(material, 1);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		
		PotionEffect potionEffect = new PotionEffect(effect, duration * 20, amplifier);
		meta.addCustomEffect(potionEffect, true);
		meta.setColor(Color.PURPLE);
		
		potion.setItemMeta(meta);
		inventory.addItem(potion);
		
		// Finishing
		String time = StringUtils.getPotionTime(duration);
		String effectstr = StringUtils.capitalizeFirst(args[1]).replace("_", " ");
		
		sendMessage("potion-received", "%type%", type.getName(), "%effect%", effectstr, "%amplifier%", amplifier, "%time%", time);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length == 0 || args.length > 2) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		
		if(args.length == 1) helper.addPotionTypes(args[0].toLowerCase());
		else helper.addPotionEffects(args[1].toLowerCase());
		
		return helper.getCompletions();
	}

}
