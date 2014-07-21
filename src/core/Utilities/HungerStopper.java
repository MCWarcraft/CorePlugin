package core.Utilities;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import core.Custody.CustodySwitchEvent;

public class HungerStopper implements Listener
{
	private static ArrayList<String> canGetHungry = new ArrayList<String>();
	
	public static void setCanGetHungry(String playerName)
	{
		canGetHungry.add(playerName);
	}

	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		canGetHungry.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event)
	{
		if (!canGetHungry.contains(event.getEntity().getName()))
			event.setFoodLevel(20);
	}
}
