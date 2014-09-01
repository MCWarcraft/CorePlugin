package core.Kits;

import java.util.HashMap;

public class KitPlayer
{
	private String playerName;
	
	private HashMap<String, HashMap<KitPiece, Integer>> pieceLevelMap;
	private HashMap<String, HashMap<String, Integer>> itemLevelMap;
	private HashMap<String, Integer> potionLevelMap;
	private HashMap<String, Boolean> kitUnlockMap;
	
	private String selectedKit;
	private KitManager kitManager;

	public KitPlayer(String playerName, KitManager kitManager)
	{
		this.playerName = playerName;
		pieceLevelMap = new HashMap<String, HashMap<KitPiece, Integer>>();
		itemLevelMap = new HashMap<String, HashMap<String, Integer>>();
		potionLevelMap = new HashMap<String, Integer>();
		kitUnlockMap = new HashMap<String, Boolean>();
		 
		selectedKit = kitManager.getDefaultKitName();
		this.kitManager = kitManager;
		unlockKit(selectedKit);
	}
	
	public void unlockKit(String name)
	{
		String kitName = name.toLowerCase();
		
		kitUnlockMap.put(kitName, true);
		pieceLevelMap.put(kitName, new HashMap<KitPiece, Integer>());
		for (KitPiece piece : KitPiece.values())
			pieceLevelMap.get(kitName).put(piece, 1);
		itemLevelMap.put(kitName, new HashMap<String, Integer>());
		for (String itemName : kitManager.getKit(name).getItemNames())
			itemLevelMap.get(kitName).put(itemName, 1);
		potionLevelMap.put(kitName, 1);
	}
	
	public boolean upgradePiece(String name, KitPiece piece, int pieceLevel)
	{
		String kitName = name.toLowerCase();
		
		HashMap<KitPiece, Integer> pieces = pieceLevelMap.get(kitName);
		if (pieces == null) return false;
		if (kitManager.getKit(kitName) == null) return false;
		
		int assignPieceLevel = pieceLevel;
		
		if (kitManager.getKit(kitName).getMaxPieceLevel(piece) <= pieceLevel)
			assignPieceLevel = kitManager.getKit(kitName).getMaxPieceLevel(piece);
		
		pieces.put(piece, assignPieceLevel);
		return true;
	}
	
	public boolean upgradePiece(String kitName, KitPiece piece)
	{
		return upgradePiece(kitName, piece, getPieceLevel(kitName, piece) + 1);
	}
	
	public boolean upgradeItem(String name, String itemName, int itemLevel)
	{
		String kitName = name.toLowerCase();
		
		HashMap<String, Integer> items = itemLevelMap.get(kitName);
		if (items == null) return false;
		if (kitManager.getKit(kitName) == null) return false;
		
		int assignItemLevel = itemLevel;
		
		if (kitManager.getKit(kitName).getMaxItemLevel(itemName) <= itemLevel)
			assignItemLevel = kitManager.getKit(kitName).getMaxItemLevel(itemName);
		
		items.put(itemName, assignItemLevel);
		return true;
	}
	
	public boolean upgradeItem(String kitName, String itemName)
	{
		return upgradeItem(kitName, itemName, getItemLevel(kitName, itemName) + 1);
	}
	
	public boolean hasRemainingUpgrades(String name, KitPiece piece)
	{
		String kitName = name.toLowerCase();
		if (kitManager.getKit(kitName).getMaxPieceLevel(piece) > getPieceLevel(kitName, piece))
			return true;
		return false;
	}
	
	public boolean hasRemainingItemUpgrades(String name, String itemName)
	{
		String kitName = name.toLowerCase();
		if (kitManager.getKit(kitName).getMaxItemLevel(itemName) > getItemLevel(kitName, itemName))
			return true;
		return false;
	}
	
	public boolean upgradePotionSet(String name, int potionLevel)
	{
		String kitName = name.toLowerCase();
		
		if (potionLevelMap.get(kitName) == null) return false;
		if (kitManager.getKit(kitName) == null) return false;
		
		int assignPotionLevel = potionLevel;
		
		if (kitManager.getKit(kitName).getMaxPotionEffectSetLevel() <= potionLevel)
			assignPotionLevel = kitManager.getKit(kitName).getMaxPotionEffectSetLevel();
		
		potionLevelMap.put(kitName, assignPotionLevel);
		return true;
	}
	
	public boolean upgradePotionSet(String kitName)
	{
		return upgradePotionSet(kitName, getPotionLevel(kitName) + 1);
	}
	
	public boolean hasRemainingPotionUpgrades(String name)
	{
		String kitName = name.toLowerCase();
		if (kitManager.getKit(kitName).getMaxPotionEffectSetLevel() > getPotionLevel(kitName))
			return true;
		return false;
	}
	
	public boolean setSelectedKit(String toSelect)
	{
		String kitToSelect = toSelect.toLowerCase();
		
		if (kitUnlockMap.get(kitToSelect) == null || kitUnlockMap.get(kitToSelect) == false)
			return false;
		
		selectedKit = kitToSelect;
		return true;
	}
	
	public int getPieceLevel(String kitName, KitPiece piece)
	{
		return pieceLevelMap.get(kitName).get(piece);
	}
	
	public int getItemLevel(String kitName, String itemName)
	{
		return itemLevelMap.get(kitName).get(itemName);
	}
	
	public int getPotionLevel(String kitName)
	{
		return potionLevelMap.get(kitName);
	}
	
	public String getSelectedKit()
	{
		return selectedKit;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public boolean isKitUnlocked(String name)
	{
		String kitName = name.toLowerCase();
		
		if (kitUnlockMap.get(kitName) == null || kitUnlockMap.get(kitName) == false)
			return false;
		return true;
	}
}