package core.Utilities;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import core.CorePlugin;
import core.Custody.CustodySwitchEvent;

public class DropBlocker implements Listener
{
	private static ArrayList<UUID> dropAllowed = new ArrayList<UUID>();
	
	private CorePlugin plugin;
	
	public DropBlocker(CorePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public static void setDropAllowed(UUID playerUUID)
	{
		dropAllowed.add(playerUUID);
	}
	
	//Based on inventory lock
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event)
	{
		final Player player = event.getPlayer();
		
		if (player.hasPermission("core.override.drop"))
		{
			event.setCancelled(false);
			return;
		}
		else if (event.getItemDrop().getItemStack().getItemMeta().equals(CoreItems.COMPASS.getItemMeta()) ||
				event.getItemDrop().getItemStack().getItemMeta().equals(CoreItems.WATCH.getItemMeta()) || 
				event.getItemDrop().getItemStack().getItemMeta().equals(CoreItems.NETHER_STAR.getItemMeta()))
			event.setCancelled(true);
		else if (!dropAllowed.contains(player.getUniqueId()))
			event.setCancelled(true);
		
		new BukkitRunnable()
		{
			public void run()
			{
				player.updateInventory();
			}
		}.runTaskLater(plugin, 10);
	}

	//Based on inventory lock
	//@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getCurrentItem() == null)
			return;
		
		if (event.getWhoClicked().hasPermission("core.override.drop"))
		{
			event.setCancelled(false);
			return;
		}
		else if (!dropAllowed.contains(event.getWhoClicked().getUniqueId()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		if (event.isCoreControlled())
			dropAllowed.remove(event.getPlayer().getUniqueId());
		else
			setDropAllowed(event.getPlayer().getUniqueId());
	}
}
