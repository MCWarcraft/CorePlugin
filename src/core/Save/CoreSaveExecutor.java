package core.Save;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreSaveExecutor implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getLabel().equalsIgnoreCase("coresave"))
		{
			if (sender.hasPermission("core.forcesave"))
				CoreSaveManager.saveAll();
		
			return true;
		}
		
		return false;
	}
}
