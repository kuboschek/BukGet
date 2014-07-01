package is.hw.get.settings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class GetConfig {
	
	public static class Internal {
		public static String versionString = "1.0.1";
		public static String getName = "Get";

		public static String infoCmd = "info";
		public static String installCmd = "install";
		public static String packCmd = "pack";
		public static String settingsCmd = "settings";
		public static String removeCmd = "remove";
		public static String listCmd = "list";
		public static String updateCmd = "update";
		public static String remoteCmd = "remote";
		
		public static String remoteUrl = "http://hw.is:42324";
	}
	
	@SettingName(humanName="General")
	public static class General {
		@SettingName(humanName="Test")
		public static String testSetting = "This is a test setting.";
		
		@SettingName(humanName="Get Name Color")
		public static String getColor = ChatColor.BLUE + "";
		
		@SettingName(humanName="Info Text Color")
		public static String infoColor = ChatColor.AQUA + "";
		
		@SettingName(humanName="Message Header Format")
		public static String msgFormat = GetConfig.General.infoColor + "[" + GetConfig.General.getColor + "%s : %s" + GetConfig.General.infoColor + "]";
		
		@SettingName(humanName="Message Prefix")
		public static String getMsgPrefix = String.format(GetConfig.General.msgFormat, "Get", "%s");
		
		@SettingName(humanName="Message Suffix")
		public static String getMsgSuffix = String.format(GetConfig.General.msgFormat, "Get", "End");
		
		@SettingName(humanName="Name Value Delimiter")
		public static String nvDelim = ": ";
		
		@SettingName(humanName="Value Value Delimiter")
		public static String vvDelim = ", ";
		
		@SettingName(humanName="Standard Fields")
		public static String standardFields = "plugin_name,authors,description,popularity,versions,date";
	}
	
	@SettingName(humanName="Locales")
	public static class Localization {
		public static String enabledMsg = "%s %s has been enabled.";
		public static String disableMsg = "%s %s has been disabled.";
		
		public static String cacheRefreshedMsg = "Plugin cache renewed.";
		
		public static String selectSettingMsg = "Select a setting from category " + ChatColor.GREEN + "%s" + ChatColor.RESET + ": " + ChatColor.GREEN + "%s";
		public static String selectedSettingMsg = "You have selected " + ChatColor.GREEN + "%s" + ChatColor.RESET + " from category " + ChatColor.GREEN + "%s" + ChatColor.RESET + ". ";
		public static String currentSettingValueMsg = "The value of this setting is " + ChatColor.GREEN + "%s" + ChatColor.RESET + ". ";
		public static String typeSettingValueMsg = "Type a new value for this setting, then press enter.";
		public static String settingSetMsg = "Configuration updated.";
		public static String settingsSavedMsg = "Get Settings saved.";
		
		public static String tooManyResultsMsg = ChatColor.RED + "%s matches. Please narrow down your search.";
		public static String selectSettingCatMsg = "Select a settings category: " + ChatColor.GREEN + "%s";
		
		public static String noPermsMsg = ChatColor.RED + "You ain't got permission.";
		public static String noSuchPlugin = ChatColor.RED + "No such plugin: %s";
		
		public static String tooFewArguments = ChatColor.RED + "Too few arguments!";
		
		// Install Command Messages
		public static String notAllowedMessage = ChatColor.RED + "You are not allowed to install that file!";
		public static String noVersionFound = ChatColor.YELLOW + "Plugin %s has no version to be installed!";
		public static String installSuccessful = ChatColor.GREEN + "Finished installing  %s";
		public static String installSuccessfulBroadcast = ChatColor.GREEN + "%s just installed %s.";
		public static String installingPlugin = "Installing %s";
		public static String noGetScript = ChatColor.YELLOW + "No GetScript found in plugin %s!";
		public static String defaultActions = ChatColor.YELLOW + "Performing default actions";
		public static String downloadError = ChatColor.RED + "The plugin could not be found!";
		public static String unknownDependancy = ChatColor.RED + "The plugin %s has unresolved dependencies!";
		public static String invalidPlugin = ChatColor.RED + "%s is not a valid plugin!";
		public static String unknownError = ChatColor.RED + "There was an unknown error. See server log for details.";
		public static String urlNotAllowed = ChatColor.RED + "The retrieved download URL can not be downloaded from due to dev.bukkit.org regulations.";
		
		// Pack Command Messages
		public static String installingPack = "Installing Pack " + ChatColor.YELLOW + "%s" + ChatColor.RESET +
				" by (" + ChatColor.ITALIC + "%s" + ChatColor.RESET + ")";
		
		
		// Remove Command Message
		public static String notOnDevBukkit = ChatColor.YELLOW + "Plugin %s is not on dev.bukkit.org!";
		public static String useName = ChatColor.YELLOW + "Please use the plugins name to remove it!";
		public static String deleteSuccessful = ChatColor.GREEN + "Successfully deleted %s";
		public static String deleteSuccessfulBroadcast = ChatColor.GREEN + "%s just removed %s from the server.";
		public static String reloadingServer = ChatColor.AQUA + "Reloading server...";
		
		//Plugin Field Names
		
		public static String Name = "Name";
		public static String Author = "Author";
		public static String Authors = "Authors";
		public static String Website = "Website";
		public static String DboPage = "DevBukkit";
		public static String LogoURL = "Logo URL";
		public static String FullLogoURL = "Full Logo URL";
		
		public static String CurrentVersion = "Current Version";
		public static String Version = "Version";
		public static String Versions = "Versions";
		
		public static String Category = "Category";
		public static String Categories = "Categories";
		
		public static String Stage = "Development Stage";
		
		public static String VerStatus = "Status";
		public static String VerChangelog = "Changelog";
		public static String VerGameVer = "Game Version";
		public static String VerGameVers = "Game Versions";
		public static String VerFileName = "File Name";
		public static String VerSoftDep = "Soft Dependency";
		public static String VerSoftDeps = "Soft Dependencies";
		public static String VerHardDep = "Hard Dependency";
		public static String VerHardDeps = "Hard Dependencies";
		public static String VerDate = "Date";
		public static String VerVersion = "Version";
		public static String VerLink = "Link";
		public static String VerDownload = "Download";
		public static String VerMD5 = "Checksum(MD5)";
		public static String VerType = "Type";
		public static String VerSlug = "Slug";
		public static String VerCmds = "Commands";
		public static String VerPerms = "Permissions";
		public static String VerCompatible = ChatColor.GREEN + "Compatible" + ChatColor.RESET;
		public static String VerIncompatible = ChatColor.RED + "Not Compatible" + ChatColor.RESET;
		
		public static String CmdUsage = "Usage";
		public static String CmdAliases = "Aliases";
		public static String CmdCommand = "Command";
		public static String CmdPermsMsg = "Permission Message";
		public static String CmdPerm= "Permission Node";
		
		public static String DefaultPerm = "Default Permission";
		public static String PermRole = "Permission Role";
		
		public static String Server = "Server";
		public static String MainClass = "Main Class";
		public static String Slug = "Slug";
		public static String Description = "Description";
		public static String PopDay = "Popularity(today)";
		public static String PopWeek = "Popularity(weekly)";
		public static String PopMonth = "Popularity(monthly)";
	}
	
	public static Class<?> getConfigCategoryClass(String categoryName) {
		for(Class<?> c : GetConfig.class.getDeclaredClasses()) {
			for(Annotation a : c.getAnnotations()) {
				if((a instanceof SettingName)) {
					if(((SettingName) a).humanName().equals(categoryName)) {
						return c;
					}
				}
			}
		}
		
		return null;
	}
	public static Field getConfigSettingField(String settingName, Class<?> category) {
		for(Field f : category.getDeclaredFields()) {
			for(Annotation a : f.getAnnotations()) {
				if((a instanceof SettingName)) {
					if(((SettingName) a).humanName().equals(settingName)) {
						return f;
					}
				}
			}
		}
		
		return null;
	}
	public static Field getConfigSettingField(String settingName, String categoryName) {
		return getConfigSettingField(settingName, getConfigCategoryClass(categoryName));
	}
	public static List<String> getConfigCategories() {
		List<String> catNames = new ArrayList<String>();
		Class<?>[] fields = GetConfig.class.getDeclaredClasses();
		
		for(Class<?> c : fields) {
			Annotation[] ann = c.getAnnotations();
			
			for(Annotation a : ann) {
				if((a instanceof SettingName)) {
					SettingName name = (SettingName) a;
					catNames.add(name.humanName());
				}
			}
		}
		
		return catNames;
	}
	public static List<String> getSettings(Class<?> c) {
		List<String> catNames = new ArrayList<String>();
		Field[] fields = c.getDeclaredFields();
		
		for(Field f : fields) {
			Annotation[] ann = f.getAnnotations();
			
			for(Annotation a : ann) {
				if((a instanceof SettingName)) {
					SettingName name = (SettingName) a;
					catNames.add(name.humanName());
				}
			}
		}
		
		return catNames;
	}
	public static List<String> getSettings(String categoryName) {
		return GetConfig.getSettings(GetConfig.getConfigCategoryClass(categoryName));
	}
	
}