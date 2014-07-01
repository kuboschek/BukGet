package is.hw.bukget;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.plugin.java.JavaPlugin;

public class BukgetPlugin extends JavaPlugin {
	public void onEnable() {
		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://hw.is/");
		//
		try {
			HttpResponse resp = client.execute(get);
			String html = IOUtils.toString(resp.getEntity().getContent());
			
			this.getLogger().info("The output of hw.is: " + html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.getLogger().info("Plugin enabled");
	}

	public void onDisable() {
		this.getLogger().info("Plugin disabled");
	}
}
