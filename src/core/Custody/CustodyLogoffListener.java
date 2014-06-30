package core.Custody;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import randy.core.CoreScoreboardManager;

public class CustodyLogoffListener implements Listener
{
	@EventHandler
	public void playerLogoffEvent(PlayerQuitEvent event)
	{
		Custody.switchCustody(event.getPlayer());
		CoreScoreboardManager.removeBoard(event.getPlayer().getName());
	}
	
	@EventHandler
	public void custodySwitchEvent(CustodySwitchEvent event)
	{
		CoreScoreboardManager.getDisplayBoard(event.getPlayer()).resetFormat();
	}
}