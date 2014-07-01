package bourg.austin.HonorPoints;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class HonorListener implements Listener
{
	@EventHandler
	public void playerLoginEvent(PlayerLoginEvent event)
	{
		DatabaseOperations.addPlayer(event.getPlayer());
	}
}