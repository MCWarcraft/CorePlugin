package core.Utilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UUIDCache implements Listener
{
	private HashMap<String, UUID> nameMap;
	
	private static UUIDCache cache;
	
	public UUIDCache()
	{
		nameMap = new HashMap<String, UUID>();
		
		UUIDCache.cache = this;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event)
	{
		nameMap.put(event.getPlayer().getName().toLowerCase(), event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event)
	{
		nameMap.remove(event.getPlayer().getName().toLowerCase());
	}
	
	private HashMap<String, UUID> getNameMap()
	{
		return nameMap;
	}
	
	public static UUID getPlayerUUID(String playerName)
	{
		return cache.getNameMap().get(playerName.toLowerCase());
	}
}