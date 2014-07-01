package randy.kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import core.Custody.CustodySwitchEvent;

public class PlayerLeave implements Listener
{
	@EventHandler
	public void onCustodySwitch(CustodySwitchEvent event)
	{
		Kits.hasKit.put(event.getPlayer(), false);
	}
}
