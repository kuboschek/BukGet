package is.hw.get.executors;

import is.hw.get.settings.GetConfig;
import is.hw.get.settings.SelectSettingCategoryPrompt;
import is.hw.get.util.ConfigLoader;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class SettingsCommandExecutor implements CommandExecutor {

	private JavaPlugin plg;
	private ConfigLoader configLoader;
	
	
	public SettingsCommandExecutor(JavaPlugin plg, ConfigLoader cfgLdr) {
		this.plg = plg;
		this.configLoader = cfgLdr;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			// No arguments given, default to conversation
			ConversationFactory cFactory = new ConversationFactory(plg);
			
			if((sender instanceof Conversable)) {
				Conversable c = (Conversable) sender;
				Conversation conv = cFactory.withFirstPrompt(new SelectSettingCategoryPrompt()).buildConversation(c);
				conv.getContext().setSessionData("loader", configLoader);
				
				c.beginConversation(conv);
				return true;
			}
		} else {
			// Kategorie.Setting:Wert
			for (String settingString : args) {
				String[] argumentParts = settingString.split(":");

				if (argumentParts.length < 1) {
					sender.sendMessage(ChatColor.RED + "Invalid setting string:");
					sender.sendMessage(ChatColor.RED + settingString);
					continue;
				}
				//
				String settingPath = argumentParts[0];
				String pathParts[] = settingPath.split("\\.");

				if (pathParts.length < 2) {
					sender.sendMessage(ChatColor.RED + "Invalid setting string:");
					sender.sendMessage(ChatColor.RED + settingString);
					continue;
				}
				//
				String settingCategory = pathParts[0];
				String settingFieldName = pathParts[1];
				String settingValue = (argumentParts.length == 1) ? (null) : (argumentParts[1]);
				//
				sender.sendMessage(String.format(GetConfig.Localization.selectedSettingMsg, settingFieldName, settingCategory));
				try {
					if (settingValue == null) {
						String currentValue = (String) GetConfig.getConfigSettingField(settingFieldName, settingCategory).get(null);
						sender.sendMessage(String.format(GetConfig.Localization.currentSettingValueMsg, currentValue));
					} else {
						GetConfig.getConfigSettingField(settingFieldName, settingCategory).set(null, settingValue);
						sender.sendMessage(String.format(GetConfig.Localization.settingSetMsg));
					}
					
					configLoader.saveConfig();
					
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "There was an error setting the setting.");
					sender.sendMessage(ChatColor.RED + "It probably just doesn't exist.");
					continue;
				}
			}
		}
		
		return false;
	}
}
