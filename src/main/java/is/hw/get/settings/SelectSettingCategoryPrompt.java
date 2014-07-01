package is.hw.get.settings;

import is.hw.get.settings.GetConfig.Localization;

import org.apache.commons.lang.StringUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class SelectSettingCategoryPrompt extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		
		context.getForWhom().sendRawMessage(String.format(Localization.selectSettingCatMsg, StringUtils.join(GetConfig.getConfigCategories(), ", ")));
		
		return "";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		
		context.setSessionData("category", input);
		
		return new SelectSettingPrompt(input);
	}

	@Override
	protected boolean isInputValid(ConversationContext arg0, String input) {

		if(GetConfig.getConfigCategories().contains(input)) return true;
		
		return false;
	}
}
