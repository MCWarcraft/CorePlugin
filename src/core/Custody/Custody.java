package core.Custody;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Custody
{
	private static HashMap<String, String> custodyStatus = new HashMap<String, String>();
	
	public static void switchCustody(Player p, String custody)
	{
		Bukkit.getServer().getPluginManager().callEvent(new CustodySwitchEvent(p, custody));
		custodyStatus.put(p.getName(), custody);
	}
	
	public static String getCustody(String playerName)
	{
		return custodyStatus.get(playerName);
	}
	
	protected static void removeCustody(String playerName)
	{
		custodyStatus.remove(playerName);
	}
	
	public static HashMap<String, String> getAllCustody()
	{
		return custodyStatus;
	}
}