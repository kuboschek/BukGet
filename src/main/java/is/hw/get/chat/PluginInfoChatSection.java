package is.hw.get.chat;


import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginInfo.PluginVersion;
import is.hw.get.settings.GetConfig;
import static is.hw.get.settings.GetConfig.Localization.*;

public class PluginInfoChatSection extends ChatSection {
	public PluginInfoChatSection(PluginInfo i) {
		contentPrefix = String.format(GetConfig.General.msgFormat, "Plugin", i.plugin_name);
		contentSuffix = String.format(GetConfig.General.msgFormat, "Plugin", "End");
		
		//General Info
		addIfNotNull(Description, i.description);
		addArrayIfNotNull(i.authors, Author, Authors);
		addArrayIfNotNull(i.categories, Category, Categories);
		
		addIfNotNull(FullLogoURL, i.logo_full);
		addIfNotNull(LogoURL, i.logo);
		
		addIfNotNull(Slug, i.slug);
		
		addIfNotNull(Website, i.website);
		addIfNotNull(DboPage, i.dbo_page);
		
		addIfNotNull(CurrentVersion, i.getLatestVersion().version);
		
		//Popularity
		if(i.popularity != null) {
			addIfNotNull(PopDay, i.popularity.daily);
			addIfNotNull(PopWeek, i.popularity.weekly);
			addIfNotNull(PopMonth, i.popularity.monthly);
		}
		
		//Versions
		for(PluginVersion ver : i.versions) {
			addSubsection(new PluginVersionChatSection(ver));
		}
		
		//
		
	}
}
