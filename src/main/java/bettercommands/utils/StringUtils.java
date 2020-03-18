package bettercommands.utils;

import org.bukkit.Location;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.TextComponent;

public class StringUtils {

	public static String format(String source, Object... replacements) {
		for(int i = 0; i < replacements.length; i++) {
			String replacement = replacements[i].toString();
			if(replacement.startsWith("%") && replacement.endsWith("%")) continue;
			
			String node = replacements[i - 1].toString();
			source = source.replace(node, replacement);
		}
		return source;
	}
	
	public static BaseComponent[] replace(BaseComponent[] source, String replacement, BaseComponent value) {
		for(int i = 0; i < source.length; i++) {
			BaseComponent comp = source[i];
			String text = comp.toPlainText();
			if(text.contains(replacement)) {
				value.copyFormatting(comp, FormatRetention.FORMATTING, true);
				
				if(!text.equals(replacement)) {
					comp = TextComponent.fromLegacyText(text.replace(replacement, value.toPlainText()))[0];
					comp.copyFormatting(value, FormatRetention.ALL, true);
					source[i] = comp;
				} else source[i] = value;
				
				return source;
			}
		}
		return source;
	}
	
	public static String capitalizeFirst(String source) {
		return Character.toUpperCase(source.charAt(0)) + source.substring(1);
	}
	
	public static String getStringFromLocation(Location location) {
		String output = location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
		return output;
	}
	
	public static String getTimeString(long time) {
		int hour = (int) (time / 1000);
		int minutes = (int) (time % 1000 * 60 / 1000);
		
		String hourStr = String.valueOf(hour == 24 ? 0 : hour);
		
		String minStr = String.valueOf(minutes);
		if(minStr.length() == 1) minStr = "0" + minStr;
		
		return hourStr + ":" + minStr;
	}
	
	public static String getPotionTime(int duration) {
		int minutes = duration / 60;
		duration = duration % 60;
		
		int hours = minutes / 60;
		minutes = minutes % 60;
		
		String durstr = duration + "";
		String minstr = minutes + "";
		
		String output = "";
		if(durstr.length() < 2) durstr = "0" + durstr;
		if(hours != 0) {
			if(minstr.length() < 2) minstr = "0" + minstr;
			output += hours + ":";
		}
		
		output += minstr + ":" + durstr;
		return output;
	}
	
}
