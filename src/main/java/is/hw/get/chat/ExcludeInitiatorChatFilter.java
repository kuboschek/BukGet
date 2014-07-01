package is.hw.get.chat;

import org.bukkit.command.CommandSender;

public class ExcludeInitiatorChatFilter implements ChatFilter {

	private CommandSender initiator;
	
	public ExcludeInitiatorChatFilter(CommandSender initiator) {
		this.initiator = initiator;
	}
	
	@Override
	public boolean accept(CommandSender recipient) {
		if(initiator.equals(recipient)) return false;

		return true;
	}

}
