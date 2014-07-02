package is.hw.get.executors;

import is.hw.api.bukget.BukGetData;
import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginListing;
import is.hw.api.bukget.cache.CacheUpdateInformable;
import is.hw.get.chat.ChatFormatter;
import is.hw.get.chat.PluginInfoChatSection;
import is.hw.get.plugin.GetPlugin;
import is.hw.get.settings.GetConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class InfoCommandExecutor implements TabExecutor, CacheUpdateInformable {
	
	private Set<String> pluginNames;
	private GetPlugin plg;
	private boolean caching_started = false;
	
	public InfoCommandExecutor(GetPlugin plug) {
		pluginNames = new HashSet<String>();
		plug.getCache().addListener(plug.getPluginListingCache(), this);
		plg = plug;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length < 1) {
			// Output information about this plugin!
			sender.sendMessage("This is Get v" + plg.getDescription().getVersion() + "!");
			return true;
		}
		
		if (!caching_started) {
			sender.sendMessage(GetConfig.Localization.noCache);
			return true;
		}

		if(pluginExists(args[0])) {
			PluginInfo info = new PluginInfo(args[0], GetConfig.General.standardFields, 2);
			
			try {
				info.execute();
				//
				
				ChatFormatter.sendChatSection(sender, new PluginInfoChatSection(info));
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + "There was an error getting the information.");
				sender.sendMessage(ChatColor.RED + "See the server log for more details.");
			}
		} else {
			sender.sendMessage(String.format(GetConfig.Localization.noSuchPlugin, args[0]));
		}
		

		//
		return true;
	}
		
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String label, String[] cmdArgs) {
		String searchedName = cmdArgs[0].toLowerCase();
		List<String> matches = new ArrayList<String>();
		
		for(String name : pluginNames) {
			if(name.startsWith(searchedName)) {
				matches.add(name);
			}
		}
		
		if(matches.size() > 40) {
			int matchSize = matches.size();
			matches.clear();
			sender.sendMessage(String.format(GetConfig.Localization.tooManyResultsMsg, matchSize));
		}
		
		return matches;
	}
	
	@Override
	public void onCacheUpdated(BukGetData updatedData) {
		pluginNames.clear();
		PluginListing plugins  = (PluginListing) updatedData;
		for(PluginInfo p: plugins.plugins) {
			pluginNames.add(p.slug);
		}
	}
	
	public boolean pluginExists(String slug) {
		return pluginNames.contains(slug);
	}
	
	public Map<String, String> getPluginFieldDescription(Field field) {
		return null;
	}
}
