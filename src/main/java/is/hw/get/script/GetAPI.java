package is.hw.get.script;

import is.hw.get.util.UnZip;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.UnknownDependencyException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class GetAPI {
	private File zipFile;
	private CommandSender sender;
	//private Context cx;
	//private Scriptable scope;

	public ChatColor color = ChatColor.WHITE;

	public GetAPI(Context cx, Scriptable scope, File zipFile, CommandSender sender) {
		this.zipFile = zipFile;
		this.sender = sender;
		//this.cx = cx;
		//this.scope = scope;
	}

	public void log(String message) {
		System.out.println(message);
	}

	public void playerMessage(String message) {
		sender.sendMessage(message);
	}

	public void unzip(String destination, String[] files) {
		for (String strFile : files) {

			File toFile = new File("plugins" + File.separator + destination + File.separator + new File(strFile).getName());
			File allowedParent = new File("./");
			//
			try {
				if (toFile.getCanonicalPath().startsWith(allowedParent.getCanonicalPath())) {
					toFile.getParentFile().mkdirs();
					UnZip.unzipFile(zipFile, strFile, toFile);
				} else {
					throw new IOException("You're not allowed to write to " + destination);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void loadPlugin(String jar) {
		File plgFile = new File("plugins/", jar);
		File pluginRoot = new File("plugins/");
		//
		try {
			if (plgFile.getCanonicalPath().startsWith(pluginRoot.getCanonicalPath())) {
				Bukkit.getServer().getPluginManager().loadPlugin(plgFile);
			} else {
				System.out.println("Not allowed!");
			}
		} catch (IOException | UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e) {
			e.printStackTrace();
		}
	}

	public void __start_firework(String color, boolean trail, String type, int power, boolean passenger) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			World w = p.getWorld();
			//
			try {
				Firework firework = w.spawn(p.getLocation().add(0, 3, 0), Firework.class);
				//
				Color col = Color.AQUA;
				//
				if (passenger) {
					firework.setPassenger(p);
				}
				//
				FireworkEffect effect = FireworkEffect.builder()
						.withColor(col)
						.trail(trail)
						.with(FireworkEffect.Type.valueOf(type))
						.build();
				FireworkMeta meta = firework.getFireworkMeta();

				meta.clearEffects();
				meta.addEffect(effect);

				Field powerField;
				powerField = meta.getClass().getDeclaredField("power");
				powerField.setAccessible(true);
				powerField.set(meta, power);
				//
				firework.setFireworkMeta(meta);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}