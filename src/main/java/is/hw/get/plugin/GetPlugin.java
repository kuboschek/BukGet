package is.hw.get.plugin;

import is.hw.api.bukget.PluginListing;
import is.hw.api.bukget.cache.DataCache;
import is.hw.get.executors.GetCommandExecutor;
import is.hw.get.executors.InfoCommandExecutor;
import is.hw.get.executors.InstallCommandExecutor;
import is.hw.get.executors.ListCommandExecutor;
import is.hw.get.executors.PackCommandExecutor;
import is.hw.get.executors.RemoteCommandExecutor;
import is.hw.get.executors.RemoveCommandExecutor;
import is.hw.get.executors.SettingsCommandExecutor;
import is.hw.get.executors.UpdateCommandExecutor;
import is.hw.get.settings.GetConfig;
import is.hw.get.util.ConfigLoader;
import is.hw.get.util.FileUtils;
import is.hw.get.util.PluginUtils;
import is.hw.get.util.PluginUtils.PluginSlugMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

public class GetPlugin extends JavaPlugin {
	public GetConfig cfg = new GetConfig();
	public Logger log;
	Server srv;
	
	private int pluginListingCache = 0;
	private DataCache cache;
	
	private ConfigLoader configLoader;
	
	public static GetPlugin instance;
	
	public GetPlugin() {
		
	}
	
	@Override
	public void onLoad() {
		this.getDataFolder().mkdirs();
		GetPlugin.instance = this;
	}
	
	@Override
	public void onEnable() {
		log = getLogger();
		//
		cache = new DataCache(this.getServer().getScheduler(), this);
		pluginListingCache = cache.addPeriodicUpdate(new PluginListing(), 120);
		//
		
		//Load that config straight.
		
		configLoader = new ConfigLoader(getDataFolder(), "default");
		try {
			configLoader.loadConfig();
		} catch (IOException ex) {
			try {
				configLoader.saveConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		Gson gson = new Gson();
		File slugsFile = new File(GetPlugin.instance.getDataFolder(), "slugs.json");
		try {
			if (!slugsFile.exists()) {
				FileUtils.writeFile(slugsFile, "{slugs:{}}");
			}
			//
			PluginSlugMapper mapper = gson.fromJson(FileUtils.readFile(slugsFile), PluginSlugMapper.class);
			//
			if (!mapper.slugs.containsValue("get")) {
				String getMd5 = FileUtils.getMD5(PluginUtils.getPluginFile(GetPlugin.instance));
				mapper.slugs.put(getMd5, "Get");
				//
				FileUtils.writeFile(slugsFile, gson.toJson(mapper));
			}
		} catch (URISyntaxException | NoSuchAlgorithmException | IOException e) {
			System.out.println(ChatColor.RED + "Well, something went completely wrong here...");
			System.out.println(ChatColor.RED + "We could not save ourselves into the sluglist!");
			System.out.println(ChatColor.RED + "---------------------------------------------!");
			System.out.println(ChatColor.RED + "Please contact the developers!");
			e.printStackTrace();
		}
		//
		GetCommandExecutor cmdExec = new GetCommandExecutor(this);
		cmdExec.registerCommand(GetConfig.Internal.settingsCmd, new SettingsCommandExecutor(this, this.configLoader));
		cmdExec.registerCommand(GetConfig.Internal.infoCmd, new InfoCommandExecutor(this));		
		cmdExec.registerCommand(GetConfig.Internal.installCmd, new InstallCommandExecutor(this));
		cmdExec.registerCommand(GetConfig.Internal.packCmd, new PackCommandExecutor());
		cmdExec.registerCommand(GetConfig.Internal.removeCmd, new RemoveCommandExecutor(this));
		cmdExec.registerCommand(GetConfig.Internal.listCmd, new ListCommandExecutor());
		cmdExec.registerCommand(GetConfig.Internal.updateCmd, new UpdateCommandExecutor(this));
		cmdExec.registerCommand(GetConfig.Internal.remoteCmd, new RemoteCommandExecutor());
		//
		getCommand("get").setExecutor(cmdExec);
		//
		log.info(String.format(GetConfig.Localization.enabledMsg, GetConfig.Internal.getName, GetConfig.Internal.versionString));
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public int getPluginListingCache() {
		return pluginListingCache;
	}
	public DataCache getCache() {
		return cache;
	}
}
