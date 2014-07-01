package is.hw.api.bukget;

import is.hw.get.util.UnZip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;

import com.google.gson.annotations.SerializedName;

/**
 * The plug-in details listing all of the information we know about a given plug-in. Optionally a specific plug-in version can be specified, only displaying that version in the versions list.
 *
 * The plug-in version can also be overloaded with the following other names:
 * latest: Returns only the most current version.
 * release: Returns only the latest version tagged as a Stable release.
 * beta: Returns only the latest version tagged as a Beta release.
 * alpha: Returns only the latest version tagged as a Alpha release.
 * 
 * @author simon
 *
 */
public class PluginInfo extends BukGetData {
	//Viele Convenience Constructors
	
	public PluginInfo(String slug) {
		super("plugins/bukkit/" + slug);
	}
	
	public PluginInfo(String slug, String fields, int size) {
		super("plugins/bukkit/" + slug);
		addParam("size", (new Integer(size)).toString());
		addParam("fields", fields);
	}
	
	public PluginInfo(String slug, String version) {
		super("plugins/bukkit/" + slug + "/" + version);
	}
	
	public PluginVersion getLatestVersion() {
		return (versions.length > 0) ? (versions[0]) : (null);
	}
	
	public PluginVersion getVersionBySlug(String slug) {
		if (slug.equals("")) {
			return (versions.length > 0) ? (versions[0]) : (null);
		}
		//
		for (PluginVersion version : versions) {
			if (version.slug.equals(slug)) {
				return version;
			}
		}
		//
		return null;
	}
	
	public void download(File location, String ver_slug) throws Exception {
		/*if (version >= this.versions.length) {
			throw new ArrayIndexOutOfBoundsException("Version " + version + " not found!");
		}*/
		//
		this.execute();
		//
		PluginVersion v = getVersionBySlug(ver_slug);
		if (v == null) {
			throw new Exception("Version " + ver_slug + " not found!");
		}
		//
		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(v.download);
		HttpResponse response = client.execute(get);
		//
		InputStream is = response.getEntity().getContent();
		FileOutputStream fos = new FileOutputStream(location);
		//
		UnZip.copyStream(is, fos);
	}
	
	//Data Fields
	public String website;//
	public String dbo_page;//
	public String description;//
	public String logo_full;//
	public PluginVersion[] versions;
	public String plugin_name;//
	public PluginPopularity popularity;//
	public String server;
	public String main;
	public String[] authors;//
	public String logo;//
	public String slug;
	public String[] categories;//
	public String stage;//
	
	public class PluginVersion {
		public String status;//
		public String changelog;//
		public String[] game_versions;//
		public String filename;//
		public String[] hard_dependencies;//
		public long date;//
		public String version;//
		public String link;//
		public String download;//
		public String md5;//
		public String type;//
		public String slug;//
		public String[] soft_dependencies;//
		public PluginCommand[] commands;
		public PluginPermission[] permissions;
		
		public class PluginCommand {
			public String usage;
			public String[] aliases;
			public String command;
			public String permission_message;
			public String permission;
		}
		
		public class PluginPermission {
			@SerializedName("default")
			public String defaultPermission;
			public String role;
		}
		
		public boolean isVersionCompatible() {
			String serverVersion = Bukkit.getServer().getBukkitVersion();
			for(String version : game_versions) {
				if(serverVersion.contains(version)) return true;
			}
			return false;
		}
	}
	
	public class PluginPopularity {
		public int daily;
		public int weekly;
		public int monthly;
	}
}
