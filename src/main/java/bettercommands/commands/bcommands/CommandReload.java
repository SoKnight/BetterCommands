package bettercommands.commands.bcommands;

import org.bukkit.command.CommandSender;

import bettercommands.files.Config;
import bettercommands.files.Messages;

public class CommandReload extends AbstractSubCommand {
	
	private final CommandSender sender;
	
	public CommandReload(CommandSender sender) {
		super(sender, null, "bcommands.reload", 1);
		this.sender = sender;
	}

	@Override
	public void execute() {
		if(!hasPermission()) return;
		
		Config.refresh();
		Messages.refresh();
		
		sender.sendMessage(Messages.getMessage("plugin-reloaded"));
	}

}
