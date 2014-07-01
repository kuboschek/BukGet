package is.hw.get.executors;

import is.hw.api.bukget.BukGetData;
import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginListing;
import is.hw.api.bukget.cache.CacheUpdateInformable;
import is.hw.get.plugin.GetPlugin;
import is.hw.get.settings.GetConfig;
import is.hw.get.util.PluginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class InstallCommandExecutor implements TabExecutor, CacheUpdateInformable {
	GetPlugin plg;
	Set<String> pluginNames;
	
	public InstallCommandExecutor(GetPlugin instance) {
		this.plg = instance;
		//
		pluginNames = new HashSet<String>();
		instance.getCache().addListener(instance.getPluginListingCache(), this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String lbl, String[] args) {
		/*
		 * get install [slug]
		 * get install -f [zipfile]
		 */
		boolean installFromFile = false;					///< if all plugins shall be installed from pre-downloaded files (-f switch)
		String[] subArgs;
		//
		// Check number of arguments
		if (args.length < 1) {
			sender.sendMessage(GetConfig.Localization.tooFewArguments);
			outputHelp(sender);
			return false;
		}
		//
		// Check if there is the "-f" switch to install from file
		if (args[0].equals("-f")) {
			installFromFile = true;
			// Then shift all the other arguments
			subArgs = Arrays.copyOfRange(args, 1, args.length);
		} else {
			installFromFile = false;
			subArgs = args;
		}
		
		// iterate over all passed slugs
		for (String slug : subArgs) {
			// if this shall be a file, install from it
			if (installFromFile) {
				PluginUtils.installFromFile(slug, sender);
			// if this is a slug, install from it instead
			} else {
				PluginUtils.installFromSlug(slug, sender);
			}
		}
		//
		return false;
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
	
	private void outputHelp(CommandSender sender) {
		sender.sendMessage("get " + GetConfig.Internal.installCmd + " [slug]\tInstall from dev.bukkit.org");
		sender.sendMessage("get " + GetConfig.Internal.installCmd + " -f [zipfile]\tInstall from zipfile");
	}

	@Override
	public void onCacheUpdated(BukGetData updatedData) {
		pluginNames.clear();
		PluginListing plugins  = (PluginListing) updatedData;
		for(PluginInfo p: plugins.plugins) {
			pluginNames.add(p.slug);
		}
	}
}
