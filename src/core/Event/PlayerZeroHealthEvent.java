package core.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerZeroHealthEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player, damager;
	private DamageCause cause;
	
	public PlayerZeroHealthEvent(Player player, DamageCause cause, Player damager)
	{
		this.player = player;
		this.damager = damager;
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
	
	public Player getDamager()
	{
		return damager;
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
