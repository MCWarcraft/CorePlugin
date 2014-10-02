package core.Kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KitLockManager
{
	private static HashMap<UUID, Boolean> canEquip = new HashMap<UUID, Boolean>();
	private static ArrayList<UUID> singleEquipees = new ArrayList<UUID>();
	
	public static boolean canEquip(UUID playerUUID)
	{
		if (canEquip.get(playerUUID) == null || canEquip.get(playerUUID) == false) return false;
		
		if (singleEquipees.contains(playerUUID))
		{
			singleEquipees.remove(playerUUID);
			canEquip.put(playerUUID, false);
		}
		return true;
	}
	
	public static void setCanEquip(boolean infinite, UUID playerUUID)
	{
		KitLockManager.canEquip.put(playerUUID, true);
		if (!infinite)
			singleEquipees.add(playerUUID);
	}
	
	public static void setCanNotEquip(UUID playerUUID)
	{
		KitLockManager.canEquip.put(playerUUID, false);
		singleEquipees.remove(playerUUID);
	}
}