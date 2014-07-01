package is.hw.get.chat;

import is.hw.get.settings.GetConfig;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * ChatFormatter holds static methods to send messages to players. This allows all ChatSections to be formatted similarly.
 * @author Leonhard
 *
 */
public class ChatFormatter {
	/**
	 * Sends a ChatSection to a CommandSender
	 * @param recipient	The sender to receive the ChatSection
	 * @param message	The ChatSection to send
	 */
	public static void sendChatSection(CommandSender recipient, ChatSection message) {
		String[] s = new String[message.getContent().size()];
		message.getContent().toArray(s);
		
		recipient.sendMessage(s);
	}
	
	/**
	 * Sends a one-line message using standard colors
	 * @param recipient	The CommandSender to send the message to
	 * @param message	The message to send
	 */
	public static void sendInfoMsg(CommandSender recipient, String message) {
		recipient.sendMessage(GetConfig.General.getColor + "| " + GetConfig.General.infoColor + message);
	}
	
	/**
	 * Filters a Chat message with the given filters and sends it to all allowed recipients
	 * @param message	The message to be filtered
	 * @param filters	The filters to filter the message with
	 */
	public static void filterAndSend(ChatMessage message, ChatFilter... filters) {
		if(message == null) {
			throw new IllegalArgumentException("message can not be null.");
		}
		
		List<CommandSender> reps = message.recipients;
		List<CommandSender> disallowedReps = new ArrayList<CommandSender>();
		
		//Iterate filters
		for(ChatFilter f : filters) {
			for(CommandSender rep : reps) {
				if(!f.accept(rep)) {
					disallowedReps.add(rep);
				}
			}
		}
		
		reps.removeAll(disallowedReps);
		
		//Iterate remaining recipients
		for(CommandSender s : reps) {
			s.sendMessage(message.message);
		}
	}
}
