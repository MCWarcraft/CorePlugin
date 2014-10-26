package core.Utilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackSerializer
{	
	public static String serialize(ItemStack stack)
	{
		if (stack == null) return serialize(new ItemStack(Material.AIR));
		
		String serial = stack.getType().name() + "|" +
				stack.getAmount() + "|" +
				stack.getDurability() + "|";

		if (!stack.hasItemMeta())
			serial = serial + "||";
		else
		{
			if (stack.getItemMeta().getDisplayName() != null)
			{
				serial = serial + stack.getItemMeta().getDisplayName();
				serial = serial + "|";
			}
			else
				serial = serial + "|";

			if (stack.getItemMeta().getLore() != null)
			{
				for (String s : stack.getItemMeta().getLore())
					serial = serial + s + "`";
				
				serial = serial.substring(0, serial.length() - 1) + "|";
			}
			else
				serial = serial + "|";
		}
		
		if (stack.getEnchantments() != null && stack.getEnchantments().size() != 0)
		{
			for (Enchantment e : stack.getEnchantments().keySet())
				serial = serial + e.getName() + "," + stack.getEnchantmentLevel(e) + "`";
			
			serial = serial.substring(0, serial.length() - 1);
		}
		else
			serial = serial + "none";

		return serial;
	}
	
	public static ItemStack unserialize(String serialized)
	{		
		String[] fields = serialized.split("\\|");
		
		ItemStack stack = new ItemStack(Material.getMaterial(fields[0]));
		stack.setAmount(Integer.parseInt(fields[1]));
		stack.setDurability(Short.parseShort(fields[2]));
		
		ItemMeta tempMeta = stack.getItemMeta();
		
		if (!fields[3].equals(""))
			tempMeta.setDisplayName(fields[3]);
		if (!fields[4].equals(""))
		{
			List<String> lines = Arrays.asList(fields[4].split("`"));
			tempMeta.setLore(lines);
		}
		
		if (tempMeta != null)
			stack.setItemMeta(tempMeta);
		
		if (!fields[5].equals("none"))
		{
			List<String> enchants = Arrays.asList(fields[5].split("`"));
			for (String unsplit : enchants)
			{
				String[] segments = unsplit.split(",");
				stack.addEnchantment(Enchantment.getByName(segments[0]), Integer.parseInt(segments[1]));
			}
		}
		return stack;
	}
}
