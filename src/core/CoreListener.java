package core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoreListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//TODO: Fix core database
		//CoreDatabase.LoadPlayerData(event.getPlayer().getName());
	}

	//TODO: Find another way to determine spawn
	/*
	 * else if(gameMode.equals("spawn")){
	 * 
	 *	event.setCancelled(true);
	 *	}
	 */
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		event.getDrops().clear();
		event.setDroppedExp(0);
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event){
		event.getBrokenItem().setDurability((short)0);
	}
}
