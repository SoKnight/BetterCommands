package bettercommands.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bettercommands.files.Config;
import bettercommands.files.Messages;
import bettercommands.objects.TextLocation;
import bettercommands.objects.entries.CoordsEntry;
import bettercommands.objects.entries.TeleportHistoryEntry;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ListUtils {
	
	public static List<CoordsEntry> getCoordsOnPage(List<CoordsEntry> list, int size, int page) {
		List<CoordsEntry> empty = new ArrayList<>();
		if(list.isEmpty()) return empty;

		int start = size * (page - 1), end = size * page - 1;

		if(start >= list.size()) return empty;
		if(end >= list.size()) end = list.size();

		List<CoordsEntry> onpage = list.subList(start, end);
		return onpage;
	}
	
	public static List<BaseComponent[]> getHistoryOnPage(List<TeleportHistoryEntry> list, int size, int page) {
		List<BaseComponent[]> onpage = new ArrayList<>();
		if(list.isEmpty()) return onpage;

		int start = size * (page - 1), end = size * page;

		if(start >= list.size()) return onpage;
		if(end >= list.size()) end = list.size();

		List<TeleportHistoryEntry> sublist = list.subList(start, end);
		if(sublist == null || sublist.isEmpty()) return onpage;
		
		int modifier = Config.getConfig().getInt("time-modifier") * 60000;
			
		DateFormat dateformat = new SimpleDateFormat(Config.getConfig().getString("date-format"));
		DateFormat timeformat = new SimpleDateFormat(Config.getConfig().getString("time-format"));
		
		String tip = Messages.getMessage("tphistory-hovertip");
		HoverEvent hovertip = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(tip));
		
		for(TeleportHistoryEntry entry : sublist) {
			String cause = entry.getCause().name().toLowerCase().replace("_", "");
			String causename = Messages.getMessage("tpcause-" + cause);
			
			Date entrydate = new Date(entry.getTime() + modifier);
			
			String date = dateformat.format(entrydate);
			String time = timeformat.format(entrydate);
			
			String body = Messages.formatMessage("tphistory-body", "%date%", date, "%time%", time, "%cause%", causename);
			BaseComponent[] source = TextComponent.fromLegacyText(body);
			
			TextLocation fromloc = entry.getFromLocation(), toloc = entry.getToLocation();
			int fx = fromloc.getX(), fy = fromloc.getY(), fz = fromloc.getZ();
			int tx = toloc.getX(), ty = toloc.getY(), tz = toloc.getZ();
			
			TextComponent from = new TextComponent(fromloc.getAsString());
			TextComponent to = new TextComponent(toloc.getAsString());
			
			from.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + fx + " " + fy + " " + fz));
			from.setHoverEvent(hovertip);
			
			to.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + tx + " " + ty + " " + tz));
			to.setHoverEvent(hovertip);
			
			source = StringUtils.replace(source, "%from%", from);
			source = StringUtils.replace(source, "%to%", to);
			
			onpage.add(source);
		}
		
		return onpage;
	}
	
}
