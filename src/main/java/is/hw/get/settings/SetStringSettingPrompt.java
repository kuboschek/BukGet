package is.hw.get.settings;

import is.hw.get.util.ConfigLoader;

import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class SetStringSettingPrompt extends StringPrompt {
	String category;
	String setting;
	Field settingField;
	
	public SetStringSettingPrompt(String category, String setting) {
		this.category = category;
		this.setting = setting;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		try {
			settingField.set(GetConfig.class, input);
			ConfigLoader ldr = (ConfigLoader) context.getSessionData("loader");
			ldr.saveConfig();
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		context.getForWhom().sendRawMessage(GetConfig.Localization.settingSetMsg);
		
		
		return null;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		settingField = GetConfig.getConfigSettingField(setting, category);
		
		try {
			context.getForWhom().sendRawMessage(String.format(GetConfig.Localization.currentSettingValueMsg, settingField.get(GetConfig.class)));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		context.getForWhom().sendRawMessage(GetConfig.Localization.typeSettingValueMsg);
		
		return "";
	}

}
