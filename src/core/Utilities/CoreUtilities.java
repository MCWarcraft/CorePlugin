package core.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CoreUtilities
{
	@SuppressWarnings("deprecation")
	public static void resetPlayerState(Player player, boolean heal)
	{
		if (heal)
		{
			player.setHealth(20);
			player.setFireTicks(0);
		}
		
		player.getInventory().clear();
		
		//Clear armor
		player.getInventory().setArmorContents(new ItemStack[4]);
		
		player.updateInventory();
		
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		
		player.setExp(0);
	}
	
	public static void deathAnimation(Player player)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0));
	}
}