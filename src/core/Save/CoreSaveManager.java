package core.Save;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoreSaveManager
{
	private static ArrayList<CoreSavable> savables = new ArrayList<CoreSavable>();
	
	public static void addSavable(CoreSavable savable)
	{
		savables.add(savable);
	}
	
	public static void saveAll()
	{
		for (Player p : Bukkit.getServer().getOnlinePlayers())
			p.sendMessage(ChatColor.BLUE + "Core Plugins Saving. Please forgive any delay.");
		
		for (CoreSavable savable : savables)
			savable.coreSave();
			
		for (Player p : Bukkit.getServer().getOnlinePlayers())
			p.sendMessage(ChatColor.BLUE + "Core Plugins Saved.");
	}
}
