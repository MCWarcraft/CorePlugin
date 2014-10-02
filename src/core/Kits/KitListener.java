package core.Kits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import core.Custody.CustodySwitchEvent;

public class KitListener implements Listener
{
	private KitManager kitManager;
	
	public KitListener(KitManager kitManager)
	{
		this.kitManager = kitManager;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		kitManager.loadPlayer(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		kitManager.savePlayer(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onCustodySwitchEvent(CustodySwitchEvent event)
	{
		KitLockManager.setCanNotEquip(event.getPlayer().getUniqueId());
	}
}