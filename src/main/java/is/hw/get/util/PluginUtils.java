package is.hw.get.util;

import is.hw.api.bukget.PluginInfo;
import is.hw.api.bukget.PluginInfo.PluginVersion;
import is.hw.get.chat.ChatFormatter;
import is.hw.get.chat.ChatMessage;
import is.hw.get.chat.ExcludeInitiatorChatFilter;
import is.hw.get.chat.OnlyOpChatFilter;
import is.hw.get.plugin.GetPlugin;
import is.hw.get.script.GetScript;
import is.hw.get.settings.GetConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class PluginUtils {
	public static void loadAndEnable(File pluginFile, String slug) throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException, IOException {
		PluginManager pMan = Bukkit.getServer().getPluginManager();
		
		Plugin plg = pMan.loadPlugin(pluginFile);
		pMan.enablePlugin(plg);
		
		try {
			String md5 = FileUtils.getMD5(pluginFile);
			File slugsFile = new File(GetPlugin.instance.getDataFolder(), "slugs.json");
			Gson gson = new Gson();
			//
			if (!slugsFile.exists()) {
				FileUtils.writeFile(slugsFile, "{slugs:{}}");
			}
			//
			PluginSlugMapper mapper = gson.fromJson(FileUtils.readFile(slugsFile), PluginSlugMapper.class);
			mapper.slugs.put(md5, slug);
			//
			FileUtils.writeFile(slugsFile, gson.toJson(mapper));
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void disablePlugin(Plugin plug) {
		Bukkit.getServer().getPluginManager().disablePlugin(plug);
	}
	
	public static Plugin getPlugin(String plugName) {
		return Bukkit.getServer().getPluginManager().getPlugin(plugName);
	}
	
	public static Plugin[] getPlugins() {
		return Bukkit.getServer().getPluginManager().getPlugins();
	}
	
	public static boolean installFromSlug(String slug, CommandSender cmdSender) {
		return installFromSlug(slug, cmdSender, "");
	}
	
	public static boolean installFromSlug(String slug, CommandSender cmdSender, String version) {
		// The plugin has to be downloaded!
		final PluginInfo plgInfo;
		final CommandSender sender = cmdSender;
		//
		String requestedVersion = version;
		//
		// A ":" in a slug splits of the version number for that plugin.
		// but it is optional
		if (slug.contains(":")) {
			// If there is a version number get it and instantiate the PluginInfo with that version
			
			String parts[] = slug.split(":");
			if (parts.length != 2) {
				sender.sendMessage(ChatColor.RED + "Invalid plugin name string!");
				return false;
			}
			//
			plgInfo = new PluginInfo(parts[0]);
			requestedVersion = parts[1];
		} else {
			// if not, get the latest
			plgInfo = new PluginInfo(slug);
		}
		//
		// try to execute the info
		try {
			plgInfo.execute();
		} catch (Exception ex) {
			sender.sendMessage(String.format(GetConfig.Localization.noSuchPlugin, slug));
			return false;
		}
		//
		if (plgInfo.plugin_name.equals("")) {
			plgInfo.plugin_name = plgInfo.slug;
		}
		//
		sender.sendMessage(String.format(GetConfig.Localization.installingPlugin, plgInfo.plugin_name));
		//
		// if the plugin has no versions, give a warning and continue happily!
		if (plgInfo.versions.length < 1) {
			sender.sendMessage(String.format(GetConfig.Localization.noVersionFound, plgInfo.plugin_name));
			return false;
		}
		
		PluginVersion reqVersion = plgInfo.getVersionBySlug(requestedVersion);
		
		//Check if this plugin's URL is from dev.bukkit.org
		if(! (reqVersion.download.startsWith("http://dev.bukkit.org/media/files"))) {
			sender.sendMessage(String.format(GetConfig.Localization.urlNotAllowed, reqVersion.filename));
			return false;
		}
		
		//
		// create the folder were the plugin will be downloaded to.
		File pluginFolder = new File(GetPlugin.instance.getDataFolder(), plgInfo.slug);
		pluginFolder.mkdirs();
		//
		// and download it!
		File dlFile = new File(pluginFolder, reqVersion.filename);
		try {
			plgInfo.download(dlFile, requestedVersion);
		} catch (Exception ex) {
			sender.sendMessage(GetConfig.Localization.downloadError);
			return false;
		}
		//
		if (dlFile.getName().endsWith(".jar")) {
			// It's just a jar file, so put it in the plugins dir and load it!
			File pluginFile = new File("./plugins/" + dlFile.getName());
			dlFile.renameTo(pluginFile);
			//
			try {
				PluginUtils.loadAndEnable(pluginFile, plgInfo.slug);
			} catch (UnknownDependencyException e) {
				sender.sendMessage(String.format(GetConfig.Localization.unknownDependancy, plgInfo.plugin_name));
				return false;
			} catch (InvalidPluginException | InvalidDescriptionException e) {
				sender.sendMessage(String.format(GetConfig.Localization.invalidPlugin, plgInfo.plugin_name));
				return false;
			} catch (IOException e) {
				sender.sendMessage(GetConfig.Localization.unknownError);
				e.printStackTrace();
				return false;
			}
			// Remove the temporary folder
			pluginFolder.delete();
		} else if (dlFile.getName().endsWith(".zip")) {
			// Zip files may contain GetScripts!
			if (UnZip.fileExists(dlFile, "get.js")) {
				// Has GetScript
				// unzip the GetScript to the temporary folder (for remove)
				File scriptFile = new File(pluginFolder, "get.js");
				try {
					UnZip.unzipFile(dlFile, "get.js", scriptFile);
				} catch (IOException e) {
					sender.sendMessage(GetConfig.Localization.unknownError);
					e.printStackTrace();
					return false;
				}
				//
				// load it
				String scriptRaw = "";
				try {
					scriptRaw = new String(Files.readAllBytes(scriptFile.toPath()));
				} catch (IOException e) {
					sender.sendMessage(GetConfig.Localization.unknownError);
					e.printStackTrace();
					return false;
				}
				GetScript script = new GetScript(scriptRaw, dlFile, sender);
				//
				// and execute it!
				sender.sendMessage(String.format(GetConfig.Localization.installingPlugin, plgInfo.plugin_name));
				script.interpretSection("install");
			} else {
				// No GetScript
				sender.sendMessage(String.format(GetConfig.Localization.noGetScript, plgInfo.plugin_name));
				sender.sendMessage(GetConfig.Localization.defaultActions);
				//
				// Unzip every .jar in the zip to the "plugins" folder
				try {
					UnZip.unzipJars(dlFile, new File("./plugins/"), new UnZip.FileUnzippedAction() {
						@Override
						public void unzipped(File f) {
							try {
								PluginUtils.loadAndEnable(f, plgInfo.slug);
							} catch (UnknownDependencyException e) {
								sender.sendMessage(String.format(GetConfig.Localization.unknownDependancy, plgInfo.plugin_name));
							} catch (InvalidPluginException | InvalidDescriptionException e) {
								sender.sendMessage(String.format(GetConfig.Localization.invalidPlugin, plgInfo.plugin_name));
							} catch (IOException e) {
								sender.sendMessage(GetConfig.Localization.unknownError);
								e.printStackTrace();
								return;
							}
						}
					});
				} catch (IOException e) {
					sender.sendMessage(GetConfig.Localization.unknownError);
					e.printStackTrace();
					return false;
				}
				//
				// remove the temporary folder and all its content
				if (dlFile.delete()) {
					pluginFolder.delete();
				}
			}
		}
		//
		sender.sendMessage(String.format(GetConfig.Localization.installSuccessful, plgInfo.plugin_name));
		ChatMessage broadcast = new ChatMessage(String.format(GetConfig.Localization.installSuccessfulBroadcast, 
				cmdSender.getName(), slug));
		
		ChatFormatter.filterAndSend(broadcast, new OnlyOpChatFilter(), new  ExcludeInitiatorChatFilter(cmdSender));
		
		return true;
	}
	
	public static boolean installFromFile(String filename, CommandSender cmdSender) {
		// try to get it
		File file = new File(GetPlugin.instance.getDataFolder(), filename);
		File allowedRoot = new File("./");
		//
		try {
			// Check if the file is inside the bukkit folder so that nothing evil can happen!
			if (file.getCanonicalPath().startsWith(allowedRoot.getCanonicalPath())) {
				// If that's the case, check if it is a zip or get file
				if (file.getName().endsWith(".zip")) {
					// Get the contents of the GetScript
					String getscript = UnZip.unzipFileToString(file, "get.js");
					GetScript script = new GetScript(getscript, file, cmdSender);
					
					cmdSender.sendMessage(String.format(GetConfig.Localization.installingPlugin, filename));
					// And execute it
					script.interpretSection("install");
				} else if (file.getName().endsWith(".js")) {
					// Its a simple GetScript, so get its contents
					String scriptContent = new String(Files.readAllBytes(file.toPath()));
					//
					GetScript script = new GetScript(scriptContent, null, cmdSender);
					// And execute it.
					script.interpretSection("install");
				}
				
			} else {
				// Youre not allowed to do things outside bukkit's root!
				cmdSender.sendMessage(GetConfig.Localization.notAllowedMessage);
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean remove(Plugin plg, CommandSender sender) {
		// disable it
		PluginUtils.disablePlugin(plg);
		//
		// If there is a data folder for that plugin....
		if (plg.getDataFolder().exists()) {
			//.... delete it too!
			plg.getDataFolder().delete();
		}
		//
		// Remove MD5 sum in slugs.json
		File slugsFile = new File(GetPlugin.instance.getDataFolder(), "slugs.json");
		if (slugsFile.exists()) {
			Gson gson = new Gson();
			try {
				PluginSlugMapper mapper = gson.fromJson(FileUtils.readFile(slugsFile), PluginSlugMapper.class);
				mapper.slugs.remove(FileUtils.getMD5(PluginUtils.getPluginFile(plg)));
				FileUtils.writeFile(slugsFile, gson.toJson(mapper));
			} catch (Exception ex) {
				sender.sendMessage(GetConfig.Localization.unknownError);
				ex.printStackTrace();
				return false;
			}
		}
		//
		// Finally delete the plugin itself
		try {
			if (PluginUtils.getPluginFile(plg).delete()) {
				// and notify the player
				sender.sendMessage(String.format(GetConfig.Localization.deleteSuccessful, plg.getName()));
				ChatFormatter.filterAndSend(new ChatMessage(String.format(GetConfig.Localization.deleteSuccessfulBroadcast, sender.getName(), plg.getName())), 
						new OnlyOpChatFilter(), new ExcludeInitiatorChatFilter(sender));
			} else {
				// something went wrong...
				sender.sendMessage(GetConfig.Localization.unknownError);
				return false;
			}
		} catch (URISyntaxException e) {
			// something went wrong...
			sender.sendMessage(GetConfig.Localization.unknownError);
			e.printStackTrace();
			return false;
		}
		//
		return true;
	}
	
	public static boolean remove(String plgSlug, CommandSender sender) {
		Plugin plg;
		PluginInfo info;
		//
		// Try to get the plugin by its name, i.e there is a plugin installed with that name
		if (PluginUtils.getPlugin(plgSlug) != null) {
			// Then store it
			plg = PluginUtils.getPlugin(plgSlug);
		} else {
			// No plugin with that name was found, try to get it over bukget
			info = new PluginInfo(plgSlug);
			try {
				info.execute();
			} catch (Exception e) {
				// Exception getting the plugin information, it's probably not on dev.bukkit.org or there was another error
				sender.sendMessage(String.format(GetConfig.Localization.notOnDevBukkit, plgSlug));
				sender.sendMessage(GetConfig.Localization.useName);
				return false;
			}
			//
			// Check if there is a matching plugin installed for that PluginInfo
			// It can happen that a slug is passed which is valid but not installed!
			plg = PluginUtils.getPlugin(info.plugin_name);
			// In this case, getPlugin() returns null
			if (plg == null) {
				sender.sendMessage(String.format(GetConfig.Localization.noSuchPlugin, plgSlug));
				return false;
			}
		}
		//
		return remove(plg, sender);
	}
	
	public static File getPluginFile(Plugin plg) throws URISyntaxException {
		// Get the URL (and corresponding File) for the plugins main class
		URL pluginURL = plg.getClass().getProtectionDomain().getCodeSource().getLocation();
		return new File(pluginURL.toURI());
	}
	
	public static String getPluginSlug(Plugin plg) throws URISyntaxException, JsonSyntaxException, IOException, NoSuchAlgorithmException {
		File plgFile = PluginUtils.getPluginFile(plg);
		File slugsFile = new File(GetPlugin.instance.getDataFolder(), "slugs.json");
		if (!slugsFile.exists()) {
			return null;
		}
		
		//
		Gson gson = new Gson();
		PluginSlugMapper mapper = gson.fromJson(FileUtils.readFile(slugsFile), PluginSlugMapper.class);
		//
		return mapper.slugs.get(FileUtils.getMD5(plgFile));
	}
	
	public class PluginSlugMapper {
		public HashMap<String, String> slugs;
	}
}
