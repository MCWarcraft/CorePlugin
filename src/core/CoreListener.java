package core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class CoreListener implements Listener 
{
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event)
	{
		event.getBrokenItem().setDurability((short)0);
	}
}