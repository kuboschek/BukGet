package is.hw.get.executors;

import is.hw.api.get.PackInfo;
import is.hw.get.settings.GetConfig;
import is.hw.get.util.PluginUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PackCommandExecutor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(GetConfig.Localization.tooFewArguments);
			outputHelp(sender);
			return false;
		}
		//
		for (String pack_id : args) {
			PackInfo pack = new PackInfo(Integer.parseInt(pack_id));
			try {
				pack.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			sender.sendMessage(String.format(GetConfig.Localization.installingPack, pack.name, pack.creator));
			for (PackInfo.PackPlugin plugin : pack.plugins) {
				PluginUtils.installFromSlug(plugin.slug, sender, plugin.version);
			}
		}
		//
		return true;
	}
	
	private void outputHelp(CommandSender sender) {
		sender.sendMessage("get " + GetConfig.Internal.packCmd + " [id]\tInstall pack #id");
	}
}
