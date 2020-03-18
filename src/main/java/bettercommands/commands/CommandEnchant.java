package bettercommands.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.utils.StringUtils;

public class CommandEnchant extends AbstractCommand {
	
	public CommandEnchant() {
		super("enchant", "bcommands.enchant", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!isPlayerRequired()) return true;
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;
		
		Player p = (Player) sender;
		PlayerInventory inventory = p.getInventory();
		
		ItemStack item = inventory.getItemInMainHand();
		if(item == null || item.getType() == null || item.getType().equals(Material.AIR)) {
			sender.sendMessage(Messages.getMessage("enchant-empty-hand"));
			return true;
		}
		
		String encharg = args[0];
		int level = Config.getConfig().getInt("default-ench-level", 1);
		
		if(args.length > 1) {
			if(!argIsInteger(args[1])) return true;
			level = Integer.parseInt(args[1]);
		}
		
		if(level < 1 || level > 32767) {
			sender.sendMessage(Messages.getMessage("enchant-wrong-level"));
			return true;
		}
		
		//BetterCommands instance = BetterCommands.getInstance();
		Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(encharg));
		if(enchantment == null) {
			sendMessage("enchant-unknown-enchantment", "%enchantment%", encharg);
			return true;
		}
		
		item.addUnsafeEnchantment(enchantment, level);
		inventory.setItemInMainHand(item);
		
		String enchname = StringUtils.capitalizeFirst(encharg).replace("_", " ");
		sendMessage("enchant-applied", "%enchantment%", enchname, "%level%", level);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length != 1) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		helper.addEnchantments(args[0].toLowerCase());
		
		return helper.getCompletions();
	}

}
