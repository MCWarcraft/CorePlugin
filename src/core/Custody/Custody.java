package core.Custody;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Custody
{
	public static void switchCustody(Player p)
	{
		Bukkit.getServer().getPluginManager().callEvent(new CustodySwitchEvent(p));
	}
}
