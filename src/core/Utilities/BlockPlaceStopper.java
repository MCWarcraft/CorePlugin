package core.Utilities;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import core.Custody.CustodySwitchEvent;

public class BlockPlaceStopper implements Listener
{
	private static ArrayList<String> canPlace = new ArrayList<String>();
	
	public static void setCanGetHungry(String playerName)
	{
		canPlace.add(playerName);
	}

	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		canPlace.remove(event.getPlayer().getName());
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
		else if (!canPlace.contains(event.getPlayer().getName()))
		{
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}
}
