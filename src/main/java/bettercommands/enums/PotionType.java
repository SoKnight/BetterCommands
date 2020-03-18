package bettercommands.enums;

import bettercommands.files.Messages;

public enum PotionType {

	POTION, SPLASH, CLOUD;
	
	public String getName() {
		return Messages.getMessage("potion-type-" + name().toLowerCase());
	}
	
}
