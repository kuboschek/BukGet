package is.hw.get.executors;

import is.hw.api.bukget.BukGetData;
import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginListing;
import is.hw.api.bukget.cache.CacheUpdateInformable;
import is.hw.get.plugin.GetPlugin;
import is.hw.get.settings.GetConfig;
import is.hw.get.util.FileUtils;
import is.hw.get.util.PluginUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

public class RemoveCommandExecutor implements TabExecutor, CacheUpdateInformable {
	// TODO: What if there are name duplicates?
	HashMap<String, String> nameToSlug = new HashMap<>();		///< Translates plugin Names into slugs.

	public RemoveCommandExecutor(GetPlugin plug) {
		plug.getCache().addListener(plug.getPluginListingCache(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		// Check if there are enough arguments
		if (args.length < 1) {
			sender.sendMessage(GetConfig.Localization.tooFewArguments);
			return true;
		}
		boolean bigButtonPressed = false;
		if (args.length >= 1 && args[0].equals("--bigredbutton")) {
			bigButtonPressed = true;
		}
		//
		boolean reloadNecessary = false;					///< is a reload necessary (= are there disabled plugins?)
		//
		
		if (bigButtonPressed) {
			/*
			 * THE BIG RED BUTTON: Remove ALL installed plugins!
			 */
			try {
				for (Plugin plg : Bukkit.getPluginManager().getPlugins()) {
					if (!plg.equals(GetPlugin.instance)) {
						Bukkit.getPluginManager().disablePlugin(plg);
					}
				}
				//
				for (File f : (new File("plugins/")).listFiles()) {
					if (f.isDirectory() && !f.equals(GetPlugin.instance.getDataFolder())) {
						FileUtils.removeRecursive(f);
					} else {
						if (!f.getAbsolutePath().equals(PluginUtils.getPluginFile(GetPlugin.instance).getAbsolutePath())) {
							f.delete();
						}
					}
				}
				//
				Bukkit.getServer().reload();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
			//
			return true;
		}
		
		// iterate over each passed slug in the arguments
		for (String plgSlug : args) {
			// At least one plugin has been removed, a reload will be necessary!
			if (PluginUtils.remove(plgSlug, sender)) {
				reloadNecessary = true;
			}
		}
		//
		// reload the server if necessary
		if (reloadNecessary) {
			sender.sendMessage(GetConfig.Localization.reloadingServer);
			Bukkit.getServer().reload();
		}
		//
		return true;
	}

	/***
	 * Creates tab-completion suggestions for either the slug or the name of a plugin
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
		String searchedName = args[0].toLowerCase();
		List<String> matches = new ArrayList<String>();
		//
		for (Plugin plg : PluginUtils.getPlugins()) {
			String slug = nameToSlug.get(plg.getName());
			if (slug == null) {
				slug = plg.getName();
			}
			//
			if (slug.startsWith(searchedName)) {
				matches.add(slug);
			}
		}
		
		//
		if(matches.size() > 40) {
			int matchSize = matches.size();
			matches.clear();
			sender.sendMessage(String.format(GetConfig.Localization.tooManyResultsMsg, matchSize));
		}
		
		return matches;
	}

	@Override
	public void onCacheUpdated(BukGetData updatedData) {
		nameToSlug.clear();
		PluginListing plugins  = (PluginListing) updatedData;
		for(PluginInfo p: plugins.plugins) {
			nameToSlug.put(p.plugin_name, p.slug);
		}
	}
}
