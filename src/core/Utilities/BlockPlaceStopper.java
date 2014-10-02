package core.Utilities;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import core.Custody.CustodySwitchEvent;

public class BlockPlaceStopper implements Listener
{
	private static ArrayList<UUID> canPlace = new ArrayList<UUID>();
	
	public static void setCanPlace(UUID playerUUID)
	{
		canPlace.add(playerUUID);
	}

	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		canPlace.remove(event.getPlayer().getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.getPlayer().hasPermission("core.override.place"))
		{
			event.setCancelled(false);
			event.getPlayer().updateInventory();
			return;
		}
		else if (!canPlace.contains(event.getPlayer().getUniqueId()))
		{
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}
}
