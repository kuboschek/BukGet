package is.hw.get.settings;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class SelectSettingPrompt extends ValidatingPrompt {
	String category;
	
	public SelectSettingPrompt(String category) {
		this.category = category;
	}
	
	@Override
	public String getPromptText(ConversationContext context) {
		context.getForWhom().sendRawMessage(String.format(GetConfig.Localization.selectSettingMsg, category, StringUtils.join(GetConfig.getSettings(category), ",")));
		
		return "";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		Field setting = GetConfig.getConfigSettingField(input, GetConfig.getConfigCategoryClass(category));
		
		if(setting.getType().equals(String.class)) {
			context.getForWhom().sendRawMessage(String.format(GetConfig.Localization.selectedSettingMsg, input, category));
			return new SetStringSettingPrompt(category, input);
		}
		
		context.getForWhom().sendRawMessage(ChatColor.ITALIC + "This setting can currently not be edited using in-game commands. Please change it in the config file.");
		
		return null;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		
		if(GetConfig.getSettings((String)context.getSessionData("category")).contains(input)) return true;
		
		return false;
	}

}
