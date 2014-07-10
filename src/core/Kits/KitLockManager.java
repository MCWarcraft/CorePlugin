package core.Kits;

import java.util.HashMap;

public class KitLockManager
{
	private static HashMap<String, Boolean> canEquip = new HashMap<String, Boolean>();
	
	public static boolean canEquip(String playerName)
	{
		if (canEquip.get(playerName) == null || canEquip.get(playerName) == false) return false;
		return true;
	}
	
	public static void setCanEquip(String playerName, Boolean canEquip)
	{
		KitLockManager.canEquip.put(playerName, canEquip);
	}
}
