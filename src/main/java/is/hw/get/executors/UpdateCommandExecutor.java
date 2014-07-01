package is.hw.get.executors;

import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginInfo.PluginVersion;
import is.hw.get.plugin.GetPlugin;
import is.hw.get.util.PluginUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UpdateCommandExecutor implements CommandExecutor {
	GetPlugin plg;
	
	public UpdateCommandExecutor(GetPlugin plg) {
		this.plg = plg;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		boolean updateGet = false;
		String newestGetVersion = "";
		//
		for (Plugin plg : Bukkit.getServer().getPluginManager().getPlugins()) {
			try {
				String slug = PluginUtils.getPluginSlug(plg);
				//
				if (slug == null) {
					sender.sendMessage(ChatColor.YELLOW + "Skipping \"" + plg.getName() + "\". Probably not installed via Get...");
					continue;
				}
				//
				// Get newest PluginInfo
				PluginInfo generalInfo = new PluginInfo(slug);
				generalInfo.execute();
				//
				PluginVersion newestVersion = getLatestCompatible(generalInfo);
				//
				if (newestVersion == null) {
					sender.sendMessage(ChatColor.YELLOW + "Skipping \"" + plg.getName() + "\"");
					continue;
				}
				// Calculate MD5 of currently installed version
				PluginInfo currentInfo = new PluginInfo(slug, plg.getDescription().getVersion());
				currentInfo.execute();
				//
				long currentTimestamp = currentInfo.getLatestVersion().date;
				long newestTimestamp = newestVersion.date;
				//
				if (newestTimestamp > currentTimestamp) {
					sender.sendMessage(ChatColor.GREEN + "There's a newer version of " + generalInfo.plugin_name);
					//
					if (plg == this.plg) {
						// update GbukGet after all other plugins
						updateGet = true;
						newestGetVersion = newestVersion.version;
					} else {
						PluginUtils.remove(slug, sender);
						PluginUtils.installFromSlug(slug + ":" + newestVersion.version, sender);
					}
				} else {
					sender.sendMessage(generalInfo.plugin_name + " is up-to-date");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		//
		if (updateGet) {
			PluginUtils.remove("get", sender);
			PluginUtils.installFromFile("get:" + newestGetVersion, sender);
		}
		//
		return true;
	}
	
	private PluginVersion getLatestCompatible(PluginInfo plg) {
		for (PluginVersion v : plg.versions) {
			if (v.isVersionCompatible()) {
				return v;
			}
		}
		//
		return null;
	}
}
