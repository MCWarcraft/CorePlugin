package core.Custody;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import core.Scoreboard.CoreScoreboardManager;

public class CustodyLogoffListener implements Listener
{
	@EventHandler
	public void playerLogoffEvent(PlayerQuitEvent event)
	{
		Custody.removeCustody(event.getPlayer().getName());
		CoreScoreboardManager.removeBoard(event.getPlayer().getName());
	}
	
	@EventHandler
	public void custodySwitchEvent(CustodySwitchEvent event)
	{
		CoreScoreboardManager.getDisplayBoard(event.getPlayer()).resetFormat();
		
	}
}