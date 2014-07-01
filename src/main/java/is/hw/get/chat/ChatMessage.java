package is.hw.get.chat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ChatMessage {
	protected String message;
	protected List<CommandSender> recipients;
	
	
	public ChatMessage(String message, List<CommandSender> recipients) {
		this.message = message;
		this.recipients = recipients;
	}
	
	public ChatMessage(String message) {
		List<CommandSender> allRecipients = Collections.synchronizedList(new Vector<CommandSender>(
				Arrays.asList(Bukkit.getOnlinePlayers())));
		allRecipients.add(Bukkit.getConsoleSender());
		
		this.message = message;
		this.recipients = allRecipients;
	}
}
