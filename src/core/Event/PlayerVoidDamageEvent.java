package core.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerVoidDamageEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private boolean used;
	
	public PlayerVoidDamageEvent(Player player)
	{
		this.player = player;
		used = false;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public boolean isUsed()
	{
		return used;
	}
	
	public void use()
	{
		used = true;
	}
	
	public HandlerList getHandlers() 
	{
	    return handlers;
	}
	 
	public static HandlerList getHandlerList()
	{
	    return handlers;
	}
}
