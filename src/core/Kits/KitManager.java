package core.Kits;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import core.CorePlugin;
import core.CavemanSQL.DatabaseConnection;
import core.CavemanSQL.DatabaseQueryAction;
import core.CavemanSQL.DatabaseUpdateAction;

public class KitManager
{
	private String defaultKitName;
	
	private HashMap<String, Kit> kits;
	private HashMap<String, KitPlayer> kitPlayers;
	
	private CorePlugin plugin;
	
	private KitPurchaseConfirmer kitPurchaseConfirmer;
	
	private DatabaseConnection databaseConnection;
	
	//Build map of reverse keys
	public HashMap<String, KitPiece> kitPieceMap;
	
	public KitManager(CorePlugin plugin)
	{
		defaultKitName = plugin.getConfig().getString("defaultkit");
		
		kitPieceMap = new HashMap<String, KitPiece>();
		for (KitPiece piece : KitPiece.values())
			kitPieceMap.put(piece.toString(), piece);
		
		kits = new HashMap<String, Kit>();
		kitPlayers = new HashMap<String, KitPlayer>();
		
		this.plugin = plugin;
		
		kitPurchaseConfirmer = new KitPurchaseConfirmer();
		
		plugin.getServer().getPluginManager().registerEvents(new KitListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(kitPurchaseConfirmer, plugin);
		
		databaseConnection = new DatabaseConnection(plugin.getConfig().getString("sql.ip"), plugin.getConfig().getString("sql.port"), plugin.getConfig().getString("sql.database"), plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
		
		loadKits();
		generateKitTables();
		loadPlayer("basekitdummy");
		
		KitScoreboardConnector.initialize(this);
		EquippableKitConnector.initialize(this);
	}
	
	public void shutdown()
	{
		for (String playerName : kitPlayers.keySet())
			savePlayer(playerName);
	}
	
	public void putKit(Kit kit)
	{
		kits.put(kit.getName(), kit);
	}
	
	public Kit getKit(String kitName)
	{
		return kits.get(kitName);
	}
	
	public void putKitPlayer(KitPlayer kitPlayer)
	{
		kitPlayers.put(kitPlayer.getPlayerName(), kitPlayer);
	}
	
	public KitPlayer getKitPlayer(String playerName)
	{
		return kitPlayers.get(playerName);
	}
	
	protected EquippableKit getEquippableKit(Player player)
	{
		if (kitPlayers.get(player.getName()) == null)
			return null;
		if (kits.get(kitPlayers.get(player.getName()).getSelectedKit()) == null)
			return null;
		
		return new EquippableKit(player, kitPlayers.get(player.getName()), kits.get(kitPlayers.get(player.getName()).getSelectedKit()));				
	}
	
	public EquippableKit getBaseEquippableKit(Player player, String kitName)
	{
		if (kits.get(kitName) == null)
			return null;
		
		return new EquippableKit(player, kitPlayers.get("basekitdummy"), kits.get(kitName));
	}
	
	public void loadKits()
	{
		///////////////////////
		/// START LOAD KITS ///
		///////////////////////
	
		ConfigurationSection kitsSection = plugin.getConfig().getConfigurationSection("kits");
		//Alert if no default kit is found
		if (!kitsSection.getKeys(false).contains(defaultKitName)) plugin.getServer().getLogger().info("No default kit found. Expect erratic behavior.");
		
		//Loop over all kits in config
		for (String kitName : kitsSection.getKeys(false))
		{
			System.out.println("Kit: " + kitName);
			
			ConfigurationSection kitSection = kitsSection.getConfigurationSection(kitName);
			ConfigurationSection upgradableSection = kitSection.getConfigurationSection("upgradable");
			
			Kit tempKit = new Kit(kitName.toLowerCase(), kitSection.getInt("cost"));
			
			//Loop over each piece of the kit
			for (String pieceName : upgradableSection.getKeys(false))
			{
				System.out.println("Piece: " + pieceName);
				
				if (pieceName.equalsIgnoreCase("potions"))
					continue;
					
				ConfigurationSection pieceSection = upgradableSection.getConfigurationSection(pieceName);
				
				//Loop over each level of the piece
				for (String levelNumber : pieceSection.getKeys(false))
				{
					System.out.println("Level: " + levelNumber);
					
					ConfigurationSection levelSection = pieceSection.getConfigurationSection(levelNumber);
					
					int cost = 0;
					int quantity = 1;
					
					String itemName = "";
					ItemStack tempStack;
					
					//If something is defined sub to level
					if (levelSection != null)
					{
						//Loop once to get item name
						for (String item : pieceSection.getConfigurationSection(levelNumber).getKeys(false))
							itemName = item;
						
						ConfigurationSection itemSection = levelSection.getConfigurationSection(itemName);
						
						System.out.println("Item Name:" + itemName);
						
						tempStack = new ItemStack(Material.getMaterial(itemName));
						
						//Loop over enchants
						for (String enchantName : itemSection.getKeys(false))
						{
							if (enchantName.equalsIgnoreCase("cost"))
								cost = itemSection.getInt("cost");
							else if (enchantName.equalsIgnoreCase("quantity"))
								quantity = itemSection.getInt("quantity");
							else
								tempStack.addEnchantment(Enchantment.getByName(enchantName), itemSection.getInt(enchantName));
						}
					}	
					//If nothing is defined sub to level
					else
					{
						//Define stack simple way
						itemName = pieceSection.getString(levelNumber);
						tempStack = new ItemStack(Material.getMaterial(itemName));
					}
					
					tempStack.setAmount(quantity);
					
					//Add piece to the temp kit
					if (kitPieceMap.get(pieceName) != null)
						tempKit.addPiece(kitPieceMap.get(pieceName), tempStack, cost);
					else
						tempKit.addItem(pieceName, tempStack, cost);
				}
			}
			
			//Load hotbar
			ConfigurationSection hotbarSection = kitsSection.getConfigurationSection(kitName + ".hotbar");
			if (hotbarSection != null)
				for (String id : hotbarSection.getKeys(false))
					if (Material.getMaterial(hotbarSection.getString(id)) != null)
							tempKit.putHotbar(new ItemStack(Material.getMaterial(hotbarSection.getString(id))));
			
			//Load inventory
			ConfigurationSection inventorySection = kitsSection.getConfigurationSection(kitName + ".inventory");
			if (inventorySection != null)
				for (String id : inventorySection.getKeys(false))
					if (Material.getMaterial(hotbarSection.getString(id)) != null)
						tempKit.putHotbar(new ItemStack(Material.getMaterial(hotbarSection.getString(id))));
			
			//Load potion effects
			ConfigurationSection potionSection = kitSection.getConfigurationSection("upgradable.potions");
			//Loop over each level of potion
			if (potionSection != null)
				for (String levelNumber : potionSection.getKeys(false))
				{
					ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
					int cost = 0;
					
					//Loop over effects with level
					ConfigurationSection levelSection = potionSection.getConfigurationSection(levelNumber);
					for (String effectName : levelSection.getKeys(false))
					{
						if (effectName.equalsIgnoreCase("cost"))
							cost = levelSection.getInt("cost");
						else
							effects.add(new PotionEffect(PotionEffectType.getByName(effectName), 6000, levelSection.getInt(effectName), false));
					}
					//Add effect to kit
					tempKit.addPotionEffectSet(effects, cost);
	
				}
			
			//Add to KitManager
			tempKit.finalize();
			this.putKit(tempKit);
		}
		
		///////////////////////
		//// END LOAD KITS ////
		///////////////////////

		for (Player p : Bukkit.getServer().getOnlinePlayers())
			loadPlayer(p.getName());
		
	}
	
	public void generateKitTables()
	{
		PreparedStatement openKitTableStatement;
		try
		{
			for (String kitName : kits.keySet())
			{
				//Configure main player data table
				String openKitTableString = "CREATE TABLE IF NOT EXISTS kit_" + kitName +
						"( player varchar(17) not null," +
						"unlocked boolean DEFAULT " + (kitName.equalsIgnoreCase(defaultKitName) ? "1" : "0") + "," +
						"potions int DEFAULT 1," +
						//
						"helmet int DEFAULT 1," +
						"chestplate int DEFAULT 1," +
						"leggings int DEFAULT 1," +
						"boots int DEFAULT 1,";
				
				for (String itemName : kits.get(kitName).getItemNames())
					openKitTableString = openKitTableString + itemName + " int DEFAULT 1,";

				openKitTableString = openKitTableString + "PRIMARY KEY (player))";
				
				openKitTableStatement = databaseConnection.getConnection().prepareStatement(openKitTableString);

				openKitTableStatement.execute();
				openKitTableStatement.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
			
	}
	
	public void loadPlayer(String playerName)
	{
		KitPlayer kitPlayer = new KitPlayer(playerName, this);
		this.putKitPlayer(kitPlayer);
		
		for (String kitName : kits.keySet())
		{{
			
		}
			DatabaseQueryAction queryAction = databaseConnection.getDatabaseQueryAction("kit_" + kitName);
			queryAction.addConstraint("player", playerName);
			
			ResultSet results = queryAction.executeQuery();
			try
			{
				if (results.next())
				{
					if (results.getBoolean("unlocked"))
						kitPlayer.unlockKit(kitName);
					
					kitPlayer.upgradePotionSet(kitName, results.getInt("potions"));
					for (KitPiece piece : KitPiece.values())
						kitPlayer.upgradePiece(kitName, piece, results.getInt(piece.toString()));
					
					for (String itemName : kits.get(kitName).getItemNames())
						kitPlayer.upgradeItem(kitName, itemName, results.getInt(itemName));
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void savePlayer(String playerName)
	{
		KitPlayer kitPlayer = kitPlayers.get(playerName);
		
		for (String kitName : kits.keySet())
		{
			DatabaseUpdateAction updateAction = databaseConnection.getDatabaseUpdateAction("kit_" + kitName);
			
			updateAction.setPrimaryValue(playerName);
			updateAction.setBoolean("unlocked", kitPlayer.isKitUnlocked(kitName));
			
			
			if (kitPlayer.isKitUnlocked(kitName))
			{
				updateAction.setInt("potions", kitPlayer.getPotionLevel(kitName));
				updateAction.setInt("helmet", kitPlayer.getPieceLevel(kitName, KitPiece.HELMET));
				updateAction.setInt("chestplate", kitPlayer.getPieceLevel(kitName, KitPiece.CHESTPLATE));
				updateAction.setInt("leggings", kitPlayer.getPieceLevel(kitName, KitPiece.LEGGINGS));
				updateAction.setInt("boots", kitPlayer.getPieceLevel(kitName, KitPiece.BOOTS));
				
				for (String itemName : kits.get(kitName).getItemNames())
					updateAction.setInt(itemName, kitPlayer.getItemLevel(kitName, itemName));
			}
			
			updateAction.executeUpdate();
		}
	}
	
	public KitPurchaseConfirmer getKitPurchaseConfirmer()
	{
		return kitPurchaseConfirmer;
	}
	
	public String getDefaultKitName()
	{
		return defaultKitName;
	}
}
