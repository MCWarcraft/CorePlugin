package core.Custody;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustodySwitchEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	/**
	 * Call this event when a player is brought to a hub or teleported.
	 * 
	 * @param p The player who has had a custody switch
	 */
	public CustodySwitchEvent(Player p)
	{
		this.p = p;
	}
	
	public Player getPlayer()
	{
		return p;
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
