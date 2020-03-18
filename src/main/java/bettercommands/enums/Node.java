package bettercommands.enums;

import bettercommands.files.Messages;

public enum Node {

	PLAYEROPT, PLAYERREQ, STEPS, ENCHANTMENT, LEVEL, TYPE, EFFECT, AMPLIFIER,
	DURATION, GAMEMODE, XOPT, YOPT, ZOPT, XREQ, YREQ, ZREQ, PAGE, WORLD;
	
	public String getNode() {
		return Messages.getMessage("help-nodes." + name().toLowerCase());
	}
	
}
