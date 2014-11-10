package core.Custody;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import core.CorePlugin;

public class CustodyCommands implements CommandExecutor	
{
	private HashMap<String, String> customs;
	
	public CustodyCommands(CorePlugin plugin)
	{
		customs = new HashMap<String, String>();
		
		ConfigurationSection custodySect = plugin.getConfig().getConfigurationSection("custody");
		
		if (custodySect != null)
		//Load the custom commands given
			for (String key : custodySect.getKeys(false))
				customs.put(key.toLowerCase(), custodySect.getString(key));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getLabel().equalsIgnoreCase("custody"))
		{
			Player player = null;
			if (sender instanceof Player)
				player = (Player) sender;
			
			if (player == null)
				sender.sendMessage(ChatColor.RED + "Only players can use custody commands.");
			else if (args.length == 1 && customs.get(args[0]) != null)
			{
				Custody.switchCustody(player, "cust", false);
				Bukkit.dispatchCommand(player, customs.get(args[0].toLowerCase()));
			}
				
			return true;
		}
		
		return false;
	}
}
