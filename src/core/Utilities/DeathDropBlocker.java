package core.Utilities;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import core.Custody.CustodySwitchEvent;

public class DeathDropBlocker implements Listener
{
	private static ArrayList<UUID> canDrop = new ArrayList<UUID>();
	
	public static void setCanDrop(UUID playerUUID)
	{
		canDrop.add(playerUUID);
	}

	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		if (event.isCoreControlled())
			canDrop.remove(event.getPlayer().getUniqueId());
		else
			setCanDrop(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (!canDrop.contains(event.getEntity().getUniqueId()))
		{
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
	}
}
