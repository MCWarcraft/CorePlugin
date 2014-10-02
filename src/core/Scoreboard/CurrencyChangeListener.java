package core.Scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import core.HonorPoints.OnlinePlayerCurrencyUpdateEvent;

public class CurrencyChangeListener implements Listener
{
	@EventHandler
	public void onCurrencyUpdate(OnlinePlayerCurrencyUpdateEvent event)
	{
		CoreScoreboardManager.getDisplayBoard(event.getPlayer()).update(false);
	}
}
