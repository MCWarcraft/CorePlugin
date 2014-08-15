package core.EngagementTracker;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerEngageEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private DamageCause cause;
	
	public PlayerEngageEvent(Player player, DamageCause cause)
	{
		this.player = player;
		this.cause = cause;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public DamageCause getCause()
	{
		return cause;
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