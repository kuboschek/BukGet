package is.hw.get.executors;

import is.hw.get.settings.GetConfig;
import is.hw.get.util.PluginUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public class ListCommandExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Plugin plugins[] = Bukkit.getServer().getPluginManager().getPlugins();
		//
		int start = 0;
		if (args.length < 1) {
			start = 0;
		} else {
			try {
				start = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(ChatColor.RED + "Argument is not a number!");
				return true;
			}
		}
		//
		for (int i = start; i < start + 10; i++) {
			if (i > plugins.length - 1) {
				break;
			}
			String slugMessage;
			String slug;
			try {
				slug = PluginUtils.getPluginSlug(plugins[i]);
			} catch (Exception e) {
				sender.sendMessage(GetConfig.Localization.unknownError);
				e.printStackTrace();
				continue;
			}
			//
			if (slug == null) {
				slugMessage = "";
			} else {
				slugMessage = "(" + slug + ")";
			}
			sender.sendMessage(plugins[i].getName() + slugMessage);
		}
		//
		return true;
	}
}
