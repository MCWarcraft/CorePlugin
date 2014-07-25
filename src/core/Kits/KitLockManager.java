package core.Kits;

import java.util.ArrayList;
import java.util.HashMap;

public class KitLockManager
{
	private static HashMap<String, Boolean> canEquip = new HashMap<String, Boolean>();
	private static ArrayList<String> singleEquipees = new ArrayList<String>();
	
	public static boolean canEquip(String playerName)
	{
		if (canEquip.get(playerName) == null || canEquip.get(playerName) == false) return false;
		
		if (singleEquipees.contains(playerName))
		{
			singleEquipees.remove(playerName);
			canEquip.put(playerName, false);
		}
		return true;
	}
	
	public static void setCanEquip(boolean infinite, String playerName)
	{
		KitLockManager.canEquip.put(playerName, true);
		if (!infinite)
			singleEquipees.add(playerName);
	}
	
	public static void setCanNotEquip(String playerName)
	{
		KitLockManager.canEquip.put(playerName, false);
		singleEquipees.remove(playerName);
	}
}