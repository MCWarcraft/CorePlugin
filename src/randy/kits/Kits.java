package randy.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Kits {
	
	//Kit
	public static HashMap<Player, Boolean> hasKit = new HashMap<Player, Boolean>();
	
	public static ArrayList<CustomKit> customKits = new ArrayList<CustomKit>();
	
	public static CustomKit getKitByName(String name, String gamemode){
		for(int i = 0; i < customKits.size(); i++){
			//System.out.print("'"+customKits.get(i).kitname+"'");
			if(customKits.get(i).kitname.equalsIgnoreCase(name) && customKits.get(i).gameMode.equals(gamemode)){
				return customKits.get(i);
			}
		}
		return null;
	}
	
	public static void setNexusInventory(Player player){
		
		//Create the 3 items needed
		
		/*
		 * 	Compass - Game Menu (Right click to go)
			Clock - Kits (Select your kit)
			Nether star - Upgrades (Upgrade your kits)
		 */
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(new ItemStack[4]);
		
		//Compass		
		String name = "" + ChatColor.GREEN + ChatColor.BOLD +"Game menu";
		String lore = "" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Right click to go";
		inventory.addItem(getItem(Material.COMPASS, 1, name, lore, null, null));
		
		//Watch		
		name = ""+ ChatColor.GREEN + ChatColor.BOLD +"Kits";
		lore = ""+ ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Select your kit";
		inventory.addItem(getItem(Material.WATCH, 1, name, lore, null, null));
		
		//Nether star		
		name = ""+ ChatColor.GREEN + ChatColor.BOLD +"Upgrades";
		lore = ""+ ChatColor.DARK_PURPLE + ChatColor.ITALIC + "Upgrade your kits";
		inventory.addItem(getItem(Material.NETHER_STAR, 1, name, lore, null, null));
	}
	
	public static ItemStack getItem(Material material, int amount, String name, String lore, ArrayList<Enchantment> enchantment, ArrayList<Integer> enchantmentLevel){
		
		//System.out.print("Attempting to get item with material: " + material);
		if(material == null || amount == 0) return null;
		
		ItemStack item = new ItemStack(material, amount);
		ItemMeta itemMeta = item.getItemMeta();
		
		//Name
		if(name != null)
			itemMeta.setDisplayName(name);
		
		//Lore
		if(lore != null){
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add(lore);
			itemMeta.setLore(itemLore);
		}
		
		item.setItemMeta(itemMeta);
		
		//Enchantments
		if(enchantment != null && enchantmentLevel != null){
			//System.out.print("Applying enchantment to item with material: " + material);
			for(int i = 0; i < enchantment.size(); i++){
				if(enchantment.get(i) != null && enchantmentLevel.get(i) != null){
					item.addEnchantment(enchantment.get(i), enchantmentLevel.get(i));
				}
			}
		}else{
			Object[] enchantments = item.getEnchantments().keySet().toArray();
			for(int i = 0; i < enchantments.length; i++)
				item.removeEnchantment((Enchantment)enchantments[i]);
		}
		
		item.setDurability(Short.MIN_VALUE);
		
		return item;
	}
	
	public static void RepairInventory(PlayerInventory inventory){
		RepairItem(inventory.getHelmet());
		RepairItem(inventory.getChestplate());
		RepairItem(inventory.getBoots());
		RepairItem(inventory.getLeggings());
		
		for(int i = 0; i < inventory.getSize(); i++){
			RepairItem(inventory.getItem(i));
		}
	}
	
	private static void RepairItem(ItemStack item){
		if(item != null)
			item.setDurability((short)0);
	}
	
	public static void FillInventory(Inventory inventory, Material item){
		if(item == null) return;
		int spaceLeft = GetEmptyInventorySpace(inventory);
		for(int i = 0; i < spaceLeft; i++){
			inventory.addItem(getItem(item, 1, null, null, null, null));
		}
	}
	
	public static int GetEmptyInventorySpace(Inventory inventory){
		int spaceLeft = inventory.getSize();
		int count = 0;
		for(int i = 0; i < spaceLeft; i++){
			if(inventory.getItem(i) != null)
				count++;
		}
		spaceLeft -= count;
		
		//System.out.print("Spaceleft: " + spaceLeft);
		return spaceLeft;
	}
}
