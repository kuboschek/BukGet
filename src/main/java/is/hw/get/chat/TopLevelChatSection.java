package is.hw.get.chat;

import is.hw.get.settings.GetConfig;

public class TopLevelChatSection extends ChatSection {
	public TopLevelChatSection(String subCmd) {
		linePrefix = "";
		contentPrefix = String.format(GetConfig.General.getMsgPrefix, subCmd);
		contentSuffix = GetConfig.General.getMsgSuffix;
	}
}
