package bettercommands.commands.bcommands;

import java.util.List;

import org.bukkit.command.CommandSender;

import bettercommands.enums.Node;
import bettercommands.files.Messages;
import bettercommands.utils.HelpMessageFactory;

public class CommandHelp extends AbstractSubCommand {
	
	private final CommandSender sender;
	
	public CommandHelp(CommandSender sender) {
		super(sender, null, "bcommands.help", 1);
		this.sender = sender;
	}

	@Override
	public void execute() {
		if(!hasPermission()) return;
		
		String header = Messages.getMessage("help-header");
		String footer = Messages.getMessage("help-footer");
		
		HelpMessageFactory hmf = new HelpMessageFactory(sender);
		
		hmf.addHelpMessage("bcommands help", "bcommands.help", "help");
		hmf.addHelpMessage("clocks", "bcommands.time.clocks", Node.WORLD);
		hmf.addHelpMessage("morning", "bcommands.time.morning", Node.WORLD);
		hmf.addHelpMessage("day", "bcommands.time.day", Node.WORLD);
		hmf.addHelpMessage("evening", "bcommands.time.evening", Node.WORLD);
		hmf.addHelpMessage("night", "bcommands.time.night", Node.WORLD);
		hmf.addHelpMessage("sun", "bcommands.weather.sun", Node.WORLD);
		hmf.addHelpMessage("rain", "bcommands.weather.rain", Node.WORLD);
		hmf.addHelpMessage("storm", "bcommands.weather.storm", Node.WORLD);
		hmf.addHelpMessage("gamemode", "bcommands.gamemode", Node.GAMEMODE, Node.PLAYEROPT);
		hmf.addHelpMessage("heal", "bcommands.heal", Node.PLAYEROPT);
		hmf.addHelpMessage("feed", "bcommands.feed", Node.PLAYEROPT);
		hmf.addHelpMessage("enchant", "bcommands.enchant", Node.ENCHANTMENT, Node.LEVEL);
		hmf.addHelpMessage("potion", "bcommands.potion", Node.TYPE, Node.EFFECT, Node.AMPLIFIER, Node.DURATION);
		hmf.addHelpMessage("inventory", "bcommands.inventory", Node.PLAYERREQ);
		hmf.addHelpMessage("enderchest", "bcommands.enderchest", Node.PLAYEROPT);
		hmf.addHelpMessage("coords add", "bcommands.coords", "coords-add", Node.XOPT, Node.YOPT, Node.ZOPT);
		hmf.addHelpMessage("coords list", "bcommands.coords", "coords-list", Node.PAGE);
		hmf.addHelpMessage("coords remove", "bcommands.coords", "coords-remove", Node.XREQ, Node.YREQ, Node.ZREQ);
		hmf.addHelpMessage("upward", "bcommands.teleport.upward", Node.PLAYEROPT);
		hmf.addHelpMessage("forward", "bcommands.teleport.forward", Node.STEPS);
		hmf.addHelpMessage("back", "bcommands.teleport.back", Node.STEPS);
		hmf.addHelpMessage("tphistory", "bcommands.teleport.history", Node.PLAYERREQ, Node.PAGE);
		hmf.addHelpMessage("bcommands info", "bcommands.info", "info", Node.PLAYEROPT);
		hmf.addHelpMessage("bcommands reload", "bcommands.reload", "reload");
		
		List<String> body = hmf.create();
		
		sender.sendMessage(header);
		body.forEach(str -> sender.sendMessage(str));
		sender.sendMessage(footer);
	}

}
