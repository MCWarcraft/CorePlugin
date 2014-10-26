package core.Kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KitLockManager
{
	private static HashMap<UUID, Boolean> canEquip = new HashMap<UUID, Boolean>();
	private static ArrayList<UUID> singleEquipees = new ArrayList<UUID>();
	private static ArrayList<UUID> burnedEquipees = new ArrayList<UUID>();
	
	public static boolean canEquip(UUID playerUUID)
	{
		if (canEquip.get(playerUUID) == null || canEquip.get(playerUUID) == false) return false;
		
		return true;
	}
	
	public static boolean isEquipped(UUID playerUUID)
	{
		return burnedEquipees.contains(playerUUID);
	}
	
	public static boolean justEquipped(UUID playerUUID)
	{
		if (singleEquipees.contains(playerUUID))
		{
			singleEquipees.remove(playerUUID);
			canEquip.put(playerUUID, false);
			burnedEquipees.add(playerUUID);
			return true;
		}
		return false;
	}
	
	public static void setCanEquip(boolean infinite, UUID playerUUID)
	{
		KitLockManager.canEquip.put(playerUUID, true);
		burnedEquipees.remove(playerUUID);
		if (!infinite)
			singleEquipees.add(playerUUID);
	}
	
	public static void setCanNotEquip(UUID playerUUID)
	{
		burnedEquipees.remove(playerUUID);
		KitLockManager.canEquip.put(playerUUID, false);
		singleEquipees.remove(playerUUID);
	}
}