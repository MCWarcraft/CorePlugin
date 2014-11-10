package core.Custody;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Custody
{
	private static HashMap<UUID, String> custodyStatus = new HashMap<UUID, String>();
	
	public static void switchCustody(Player p, String custody)
	{
		switchCustody(p, custody, true);
	}
	
	public static void switchCustody(Player p, String custody, boolean coreControl)
	{
		Bukkit.getServer().getPluginManager().callEvent(new CustodySwitchEvent(p, custody, coreControl));
		custodyStatus.put(p.getUniqueId(), custody);
	}
	
	public static String getCustody(UUID playerUUID)
	{
		return custodyStatus.get(playerUUID);
	}
	
	protected static void removeCustody(UUID playerUUID)
	{
		custodyStatus.remove(playerUUID);
	}
	
	public static HashMap<UUID, String> getAllCustody()
	{
		return custodyStatus;
	}
}