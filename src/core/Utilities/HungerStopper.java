package core.Utilities;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import core.Custody.CustodySwitchEvent;

public class HungerStopper implements Listener
{
	private static ArrayList<UUID> canGetHungry = new ArrayList<UUID>();
	
	public static void setCanGetHungry(UUID playerUUID)
	{
		canGetHungry.add(playerUUID);
	}

	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		if (event.isCoreControlled())
			canGetHungry.remove(event.getPlayer().getUniqueId());
		else
			setCanGetHungry(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event)
	{
		if (!canGetHungry.contains(event.getEntity().getUniqueId()))
			event.setFoodLevel(20);
	}
}
