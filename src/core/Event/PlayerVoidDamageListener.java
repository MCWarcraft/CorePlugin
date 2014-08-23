package core.Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerVoidDamageListener implements Listener
{
	@EventHandler
	public void onPlayerVoidDamage(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player)
			Bukkit.getServer().getPluginManager().callEvent(new PlayerVoidDamageEvent(((Player) event.getEntity())));
	}
}
