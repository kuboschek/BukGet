package is.hw.get.executors;

import is.hw.get.remote.JsonRPC;
import is.hw.get.remote.JsonResponse;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemoteCommandExecutor implements CommandExecutor {
	private JsonRPC rpc = new JsonRPC();
	
	public RemoteCommandExecutor() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(args.length >= 1)) {
			showHelp(sender);
			return true;
		}
		//
		String command = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		//
		switch (command) {
		case "connect":
			connect(sender, args);
			break;
		case "register":
			register(sender, args);
			break;
		}
		//
		return true;
	}
	
	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "connect" + ChatColor.RESET + "\t-\tConnect the remote control");
	}
	
	private void register(CommandSender sender, String[] args) {
		if (args.length < 3) {
			showHelp(sender);
			return;
		}
		
		String server_key = args[0];
		String server_name = args[1];
		String pubkey = args[2];
		boolean result = false;
		
		sender.sendMessage(ChatColor.YELLOW + "Registering for " + server_key);
		
		try {
			result = rpc.register(server_key, server_name, pubkey);
		} catch (IOException e) {
			sender.sendMessage(ChatColor.RED + "There was an error during registration!");
			e.printStackTrace();
			return;
		}
		
		if (result)
			sender.sendMessage(ChatColor.GREEN + "Registered!");
		else
			sender.sendMessage(ChatColor.RED + "There was an error during registration!");
	}
	
	private void connect(CommandSender sender, String[] args) {
		if (args.length < 1) {
			showHelp(sender);
			return;
		}
		sender.sendMessage(ChatColor.GREEN + "Started listening...");
		// TODO: Generate Pubkey
		rpc.startMessageListener(new JsonRPC.DataCallback() {
			@Override
			public void data(JsonResponse data) {
				System.out.println("Got data:");
				System.out.println(data.toJson());
			}
		}, args[0]);
		// TODO: Irgendwie empfangen
	}
}
