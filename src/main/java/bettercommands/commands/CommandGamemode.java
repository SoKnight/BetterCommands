package bettercommands.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bettercommands.files.Messages;

public class CommandGamemode extends AbstractCommand {
	
	public CommandGamemode() {
		super("gamemode", "bcommands.gamemode", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		super.setSenderAndArgs(sender, args);
		
		if(!hasPermission()) return true;
		if(!isCorrectUsage()) return true;
		
		Player target;
		boolean self = true;
		
		String gmarg = args[0];
		
		if(args.length > 1) {
			if(!hasPermission("bcommands.gamemode.other")) return true;
			if(!isPlayerOnline(args[1])) return true;
			target = Bukkit.getOfflinePlayer(args[1]).getPlayer();
			self = false;
		} else {
			if(!isPlayerRequired()) return true;
			target = (Player) sender;
		}
		
		String name = target.getName();
		GameMode current = target.getGameMode();
		
		GameMode gamemode;
		switch (gmarg) {
		case "0":
			gamemode = GameMode.SURVIVAL;
			break;
		case "1":
			gamemode = GameMode.CREATIVE;
			break;
		case "2":
			gamemode = GameMode.ADVENTURE;
			break;
		case "3":
			gamemode = GameMode.SPECTATOR;
			break;
		case "ad":
			gamemode = GameMode.ADVENTURE;
			break;
		case "cr":
			gamemode = GameMode.CREATIVE;
			break;
		case "sr":
			gamemode = GameMode.SURVIVAL;
			break;
		case "sp":
			gamemode = GameMode.SPECTATOR;
			break;
		default:
			gamemode = GameMode.valueOf(gmarg.toUpperCase());
			if(gamemode != null) break;
			
			sendMessage("gamemode-unknown", "%gamemode%", gmarg);
			return true;
		}
		
		String gmname = Messages.getMessage("gamemode-" + gamemode.name().toLowerCase());
		if(current.equals(gamemode)) {
			if(self) sendMessage("gamemode-self-already", "%gamemode%", gmname);
			else sendMessage("gamemode-other-already", "%player%", name, "%gamemode%", gmname);
			return true;
		}
		
		target.setGameMode(gamemode);
		if(!self) sendMessage("gamemode-other", "%player%", name, "%gamemode%", gmname);
		target.sendMessage(Messages.formatMessage("gamemode-self", "%gamemode%", gmname));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length == 0 || args.length > 2) return null;
		
		TabCompletionHelper helper = new TabCompletionHelper();
		
		if(args.length == 1) helper.addGamemodes(args[0].toLowerCase());
		else {
			if(!sender.hasPermission("bcommands.gamemode.other")) return null;
			helper.addOnlinePlayers(args[1].toLowerCase());
		}
		
		return helper.getCompletions();
	}

}
