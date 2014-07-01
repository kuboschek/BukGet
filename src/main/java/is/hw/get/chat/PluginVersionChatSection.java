package is.hw.get.chat;

import static is.hw.get.settings.GetConfig.Localization.VerCompatible;
import static is.hw.get.settings.GetConfig.Localization.VerDate;
import static is.hw.get.settings.GetConfig.Localization.VerDownload;
import static is.hw.get.settings.GetConfig.Localization.VerFileName;
import static is.hw.get.settings.GetConfig.Localization.VerHardDep;
import static is.hw.get.settings.GetConfig.Localization.VerHardDeps;
import static is.hw.get.settings.GetConfig.Localization.VerIncompatible;
import static is.hw.get.settings.GetConfig.Localization.VerLink;
import static is.hw.get.settings.GetConfig.Localization.VerMD5;
import static is.hw.get.settings.GetConfig.Localization.VerSlug;
import static is.hw.get.settings.GetConfig.Localization.VerSoftDep;
import static is.hw.get.settings.GetConfig.Localization.VerSoftDeps;
import static is.hw.get.settings.GetConfig.Localization.VerStatus;
import static is.hw.get.settings.GetConfig.Localization.VerType;
import static is.hw.get.settings.GetConfig.Localization.Version;
import is.hw.api.bukget.PluginInfo.PluginVersion;
import is.hw.get.settings.GetConfig;

public class PluginVersionChatSection extends ChatSection {
	public PluginVersionChatSection(PluginVersion v) {
		contentPrefix = String.format(GetConfig.General.msgFormat, Version, v.version);
		contentSuffix = String.format(GetConfig.General.msgFormat, Version, "End");
		
		addIfNotNull(VerStatus, v.status);
		
		addIfNotNull(VerLink, v.link);
		addIfNotNull(VerDownload, v.download);
		addIfNotNull(VerFileName, v.filename);
		
		addDateFromLongIfNotNull(VerDate, v.date);
		//addIfNotNull(VerChangelog, Base64Decode.decodeString(v.changelog));
		if(v.isVersionCompatible()) {
			addContent(VerCompatible);
		} else {
			addContent(VerIncompatible);
		}
		
		addArrayIfNotNull(v.hard_dependencies, VerHardDep, VerHardDeps);
		addArrayIfNotNull(v.soft_dependencies, VerSoftDep, VerSoftDeps);
		
		addIfNotNull(VerMD5, v.md5);
		addIfNotNull(VerType, v.type);
		addIfNotNull(VerSlug, v.slug);
	}
}
