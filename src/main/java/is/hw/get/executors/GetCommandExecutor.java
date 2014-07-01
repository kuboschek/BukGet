package is.hw.get.executors;

import is.hw.get.plugin.GetPlugin;
import is.hw.get.settings.GetConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class GetCommandExecutor implements TabExecutor{
	GetPlugin plg;
	HashMap<String, CommandExecutor> subcommands = new HashMap<>();
	
	public GetCommandExecutor(GetPlugin plg) {
		this.plg = plg;
	}
	
	@Override
	public boolean onCommand(final CommandSender snd, final Command cmd, final String lbl, final String[] cmdArgs) {
		
		if(!plg.isEnabled()) return false;
		if(cmdArgs.length < 1) return false;
		if (subcommands.containsKey(cmdArgs[0])) {
			
			if(isSubcommandAllowed(snd, cmdArgs[0])) {
			
				Thread commandThread = new Thread(new Runnable() {
					@Override
					public void run() {
						snd.sendMessage(String.format(GetConfig.General.getMsgPrefix, cmdArgs[0]));
						
						String[] newArgs = Arrays.copyOfRange(cmdArgs, 1, cmdArgs.length);
						subcommands.get(cmdArgs[0]).onCommand(snd, cmd, lbl, newArgs);
						
						snd.sendMessage(GetConfig.General.getMsgSuffix);
					}
				});
				
				commandThread.start();
			
			} else {
				snd.sendMessage(GetConfig.Localization.noPermsMsg);
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public void registerCommand(String cmd, CommandExecutor exec) {
		subcommands.put(cmd, exec);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length < 1) return null;
		//
		if (args.length == 1) {
			List<String> completions = new ArrayList<String>();
			for (String subcommand : subcommands.keySet()) {
				if (subcommand.startsWith(args[0]) && isSubcommandAllowed(sender, args[0])) {
					completions.add(subcommand);
				}
			}
			return completions;
		} else {
			if (subcommands.containsKey(args[0]) && isSubcommandAllowed(sender, args[0])) {
				String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
				CommandExecutor executor = subcommands.get(args[0]);
				if (executor instanceof TabExecutor) {
					TabExecutor tabexec = (TabExecutor) executor;
					return tabexec.onTabComplete(sender, cmd, alias, newArgs);
				}
			}
		}
		//
		return null;
	}
	
	public boolean isSubcommandAllowed(CommandSender snd, String subCmd) {
		boolean actionAllowed = false;
		
		if(snd.hasPermission("get." + subCmd)) actionAllowed = true;
		if(snd.isOp()) actionAllowed = true;
		if(snd.hasPermission("get.*")) actionAllowed = true;
		
		return actionAllowed;
	}
}
