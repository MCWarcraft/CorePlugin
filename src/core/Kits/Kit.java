package core.Kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Kit
{
	private HashMap<KitPiece, ArrayList<ItemStack>> upgradablePieces;
	private HashMap<KitPiece, ArrayList<Integer>> pieceUpgradeCosts;
	
	private HashMap<String, ArrayList<ItemStack>> upgradableItems;
	private HashMap<String, ArrayList<Integer>> itemUpgradeCosts;
	private ArrayList<String> orderedItems;
	
	private ArrayList<ArrayList<PotionEffect>> potionEffects;
	private ArrayList<Integer> potionUpgradeCosts;
	private ItemStack[] hotbar, inventory;
	
	private String name;
	private int hotbarIndex, inventoryIndex, cost, cooldown;
	
	private boolean finalized;
	
	public Kit(String name, int cost)
	{
		this.cost = cost;
		
		cooldown = 0;
		
		finalized = false;
		
		this.name = name;
		hotbar = new ItemStack[8];
		inventory = new ItemStack[27];
		
		hotbarIndex = 0;
		inventoryIndex = 0;
		
		upgradablePieces = new HashMap<KitPiece, ArrayList<ItemStack>>();
		pieceUpgradeCosts = new HashMap<KitPiece, ArrayList<Integer>>();
		for (KitPiece piece : KitPiece.values())
		{
			upgradablePieces.put(piece, new ArrayList<ItemStack>());
			pieceUpgradeCosts.put(piece, new ArrayList<Integer>());
		}
		
		upgradableItems = new HashMap<String, ArrayList<ItemStack>>();
		itemUpgradeCosts = new HashMap<String, ArrayList<Integer>>();
		orderedItems = new ArrayList<String>();
		
		potionEffects = new ArrayList<ArrayList<PotionEffect>>();
		potionUpgradeCosts = new ArrayList<Integer>();
	}
	
	public void setCooldownSeconds(int cooldownSeconds)
	{
		if (!finalized)
			cooldown = cooldownSeconds;
	}
	
	public int getCooldownSeconds()
	{
		return cooldown;
	}
	
	public void addPiece(KitPiece piece, ItemStack item, int cost)
	{		
		if (!finalized)
		{
			upgradablePieces.get(piece).add(item);
			pieceUpgradeCosts.get(piece).add(cost);
		}
	}
	
	public ItemStack getPiece(KitPiece piece, int pieceLevel)
	{
		if (!finalized) return null;
		if (pieceLevel <= getMaxPieceLevel(piece))
			return upgradablePieces.get(piece).get(pieceLevel - 1);
		return null;
	}
	
	public int getMaxPieceLevel(KitPiece piece)
	{
		if (!finalized) return -1;
		return upgradablePieces.get(piece).size();
	}
	
	public int getPieceUpgradeCost(KitPiece piece, int level)
	{
		if (pieceUpgradeCosts.get(piece).get(level - 1) != null)
			return pieceUpgradeCosts.get(piece).get(level - 1);
		return 0;
	}
	
	public void addItem(String pieceName, ItemStack item, int cost)
	{
		if (!finalized)
		{
			//Test for defined pieces
			if (upgradableItems.get(pieceName) == null)
			{
				upgradableItems.put(pieceName, new ArrayList<ItemStack>());
				orderedItems.add(pieceName);
			}
			if (itemUpgradeCosts.get(pieceName) == null)
				itemUpgradeCosts.put(pieceName, new ArrayList<Integer>());
			
			upgradableItems.get(pieceName).add(item);
			itemUpgradeCosts.get(pieceName).add(cost);
		}
	}
	
	public ItemStack getItem(String itemName, int itemLevel)
	{
		if (!finalized) return null;
		if (itemLevel <= getMaxItemLevel(itemName))
			return upgradableItems.get(itemName).get(itemLevel - 1);
		return null;
	}
	
	public int getMaxItemLevel(String itemName)
	{
		if (!finalized) return -1;
		return upgradableItems.get(itemName).size();
	}
	
	public int getItemUpgradeCost(String itemName, int level)
	{
		if (itemUpgradeCosts.get(itemName).get(level - 1) != null)
			return itemUpgradeCosts.get(itemName).get(level - 1);
		return 0;
	}
	
	public ArrayList<String> getItemNames()
	{
		return orderedItems;
	}
	
	public void addPotionEffectSet(ArrayList<PotionEffect> potionEffects, int cost)
	{
		if (!finalized)
		{
			this.potionEffects.add(potionEffects);
			potionUpgradeCosts.add(cost);
		}
	}
	
	public ArrayList<PotionEffect> getPotionEffectSet(int potionSetLevel)
	{
		if (!finalized) return null;
		if (potionSetLevel > getMaxPotionEffectSetLevel()) return null;
		
		return potionEffects.get(potionSetLevel - 1);
	}
	
	public int getMaxPotionEffectSetLevel()
	{
		if (!finalized) return -1;
		return potionEffects.size();
	}
	
	public int getPotionUpgradeCost(int level)
	{
		if (potionUpgradeCosts.get(level - 1) != null)
			return potionUpgradeCosts.get(level - 1);
		return 0;
	}
	
	public void putHotbar(ItemStack item)
	{
		if (finalized) return;
		if (hotbarIndex >= 8) return;
		hotbar[hotbarIndex] = item;
		hotbarIndex++;
	}
	
	public void putInventory(ItemStack item)
	{
		if (finalized) return;
		if (inventoryIndex >= 27)
			return;
		inventory[inventoryIndex] = item;
		inventoryIndex++;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public ItemStack[] getHotbarItems()
	{
		return hotbar;
	}
	
	public ItemStack[] getInventoryItems()
	{
		return inventory;
	}
	
	public void finalize()
	{
		//Add blanks where nothing has been added
	    if (potionEffects.size() == 0)
	    	potionEffects.add(new ArrayList<PotionEffect>());
	    if (potionUpgradeCosts.size() == 0)
	    	potionUpgradeCosts.add(0);
	    
	    for (KitPiece piece : KitPiece.values())
	    {
	    	if (upgradablePieces.get(piece).size() == 0)
	    		upgradablePieces.get(piece).add(new ItemStack(Material.AIR));
	    	if (pieceUpgradeCosts.get(piece).size() == 0)
	    		pieceUpgradeCosts.get(piece).add(0);
	    }
	    
	    finalized = true;
	}
}