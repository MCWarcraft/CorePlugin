package core.Utilities;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import core.Custody.CustodySwitchEvent;

public class DropBlocker implements Listener
{
	private static ArrayList<String> dropAllowed = new ArrayList<String>();
	
	public static void setDropAllowed(String playerName)
	{
		dropAllowed.add(playerName);
	}
	
	//Based on inventory lock
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event)
	{
		if (event.getPlayer().hasPermission("core.override.drop"))
		{
			event.setCancelled(false);
			return;
		}
		else if (!dropAllowed.contains(event.getPlayer().getName()))
			event.setCancelled(true);
	}

	//Based on inventory lock
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getCurrentItem() == null)
			return;
		
		if (event.getWhoClicked().hasPermission("core.override.drop"))
		{
			event.setCancelled(false);
			return;
		}
		else if (!dropAllowed.contains(event.getWhoClicked().getName()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		dropAllowed.remove(event.getPlayer().getName());
	}
}
