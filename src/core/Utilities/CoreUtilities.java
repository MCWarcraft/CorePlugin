package core.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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
	}
}