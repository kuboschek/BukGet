package is.hw.get.chat;


import org.bukkit.command.CommandSender;

public interface ChatFilter {
	public boolean accept(CommandSender recipient);
}
