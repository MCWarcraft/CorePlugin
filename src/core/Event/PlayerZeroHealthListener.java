package core.Event;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerZeroHealthListener implements Listener
{	
	private HashMap<String, Player> damagers;
	
	public PlayerZeroHealthListener()
	{
		damagers = new HashMap<String, Player>();
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerZeroHealthEvent(EntityDamageEvent event)
	{
		if (event.getEntity() == null)
			return;
		else if (!(event.getEntity() instanceof Player))
			return;

		Player damagedPlayer = (Player) event.getEntity();
		
		//If the player has dipped below 0 HP
		if (damagedPlayer.getHealth() - getDamageArmored(damagedPlayer, event.getDamage()) <= 0)
			Bukkit.getServer().getPluginManager().callEvent(new PlayerZeroHealthEvent(damagedPlayer, event.getCause(), damagers.get(damagedPlayer.getName())));
	
		damagers.remove(damagedPlayer.getName());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerZeroHealthEvent(EntityDamageByEntityEvent event)
	{
		if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))
			return;
		damagers.put(((Player) event.getEntity()).getName(), ((Player) event.getDamager()));
	}
	
	private double getDamageArmored(Player player, double raw)
	{
		org.bukkit.inventory.PlayerInventory inv = player.getInventory();
		ItemStack boots = inv.getBoots();
		ItemStack helmet = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack pants = inv.getLeggings();
		double red = 0.0;

		if (helmet != null)
		{
			if(helmet.getType() == Material.LEATHER_HELMET)
				red = red + 0.04;
			else if(helmet.getType() == Material.GOLD_HELMET)
				red = red + 0.08;
			else if(helmet.getType() == Material.CHAINMAIL_HELMET)
				red = red + 0.08;
			else if(helmet.getType() == Material.IRON_HELMET)
				red = red + 0.08;
			else if(helmet.getType() == Material.DIAMOND_HELMET)
				red = red + 0.12;
		}
		//
		if (boots != null)
		{
			if(boots.getType() == Material.LEATHER_BOOTS)
				red = red + 0.04;
			else if(boots.getType() == Material.GOLD_BOOTS)
				red = red + 0.04;
			else if(boots.getType() == Material.CHAINMAIL_BOOTS)
				red = red + 0.04;
			else if(boots.getType() == Material.IRON_BOOTS)
				red = red + 0.08;
			else if(boots.getType() == Material.DIAMOND_BOOTS)
				red = red + 0.12;
		}
		//
		if (pants != null)
		{
			if(pants.getType() == Material.LEATHER_LEGGINGS)
				red = red + 0.08;
			else if(pants.getType() == Material.GOLD_LEGGINGS)
				red = red + 0.12;
			else if(pants.getType() == Material.CHAINMAIL_LEGGINGS)
				red = red + 0.16;
			else if(pants.getType() == Material.IRON_LEGGINGS)
				red = red + 0.20;
			else if(pants.getType() == Material.DIAMOND_LEGGINGS)
				red = red + 0.24;
		}
		//
		if (chest != null)
		{
			if(chest != null && chest.getType() == Material.LEATHER_CHESTPLATE)
				red = red + 0.12;
			else if(chest.getType() == Material.GOLD_CHESTPLATE)
				red = red + 0.20;
			else if(chest.getType() == Material.CHAINMAIL_CHESTPLATE)
				red = red + 0.20;
			else if(chest.getType() == Material.IRON_CHESTPLATE)
				red = red + 0.24;
			else if(chest.getType() == Material.DIAMOND_CHESTPLATE)
				red = red + 0.32;
		}

		return raw * (1.0 - red);
	}
}
