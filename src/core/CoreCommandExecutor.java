package core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommandExecutor implements CommandExecutor
{
	private CorePlugin plugin;
	
	public CoreCommandExecutor(CorePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getLabel().equalsIgnoreCase("who"))
		{
			sender.sendMessage(ChatColor.RED + "Owner " + ChatColor.GOLD + "Staff " + ChatColor.BLUE + "Player");
			
			String who = "[" + plugin.getServer().getOnlinePlayers().length + "/" + plugin.getServer().getMaxPlayers() + "]: ";
			
			
			for (Player p : plugin.getServer().getOnlinePlayers())
				who = who + plugin.getWhoColor(p.getName()) + p.getName() + " ";
			
			sender.sendMessage(who);
			
			return true;
		}
		
		return false;
	}
}