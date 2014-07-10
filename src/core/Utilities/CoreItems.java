package core.Utilities;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreItems
{
	public static ItemStack COMPASS, WATCH, NETHER_STAR;
	
	public static void initialize()
	{
		ItemMeta tempMeta;
		
		//Compass
		COMPASS = new ItemStack(Material.COMPASS, 1);
		tempMeta = COMPASS.getItemMeta();
		tempMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "Game Menu");
		tempMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Right click to go"));
		COMPASS.setItemMeta(tempMeta);
		
		//Watch	
		WATCH = new ItemStack(Material.WATCH, 1);
		tempMeta = WATCH.getItemMeta();
		tempMeta.setDisplayName(ChatColor.GREEN + "Kits");
		tempMeta.setLore(Arrays.asList("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Select your kit"));
		WATCH.setItemMeta(tempMeta);

		//Nether star
		NETHER_STAR = new ItemStack(Material.NETHER_STAR, 1);
		tempMeta = NETHER_STAR.getItemMeta();
		tempMeta.setDisplayName("" + ChatColor.GREEN + ChatColor.BOLD + "Upgrades");
		tempMeta.setLore(Arrays.asList("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Upgrade your kits"));
		NETHER_STAR.setItemMeta(tempMeta);
	}
}
