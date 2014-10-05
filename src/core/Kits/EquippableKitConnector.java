package core.Kits;

import org.bukkit.entity.Player;

public class EquippableKitConnector
{
	private static KitManager kitManager;
	
	public static void initialize(KitManager kitManager)
	{
		EquippableKitConnector.kitManager = kitManager;
	}
	
	public static EquippableKit getBaseEquippableKit(Player player, String kitName)
	{
		return kitManager.getBaseEquippableKit(player, kitName);
	}
	
	public static void equipSelectedKit(Player player)
	{
		kitManager.getEquippableKit(player).equip();
	}
}
