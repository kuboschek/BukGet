package is.hw.get.chat;

import org.bukkit.command.CommandSender;

public class OnlyOpChatFilter implements ChatFilter {

	@Override
	public boolean accept(CommandSender recipient) {
		if(recipient.isOp()) return true;
		
		return false;
	}

}
