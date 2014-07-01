package randy.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import randy.core.tools.CoreDatabase;

public class CustomKit {

	public String kitname;
	public String kitdisplayname;
	public String gameMode;
	public boolean defaultlocked;
	public boolean defaultpurchased;
	public int kitprice;
	public int multiplierRequired;

	//Main slots	
	public ArrayList<String> equipment = new ArrayList<String>();
	public ArrayList<String> hotbar = new ArrayList<String>();
	public String inventoryItem;

	//Upgrades
	public HashMap<Integer, ArrayList<Enchantment>> helmetUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> swordUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> chestplateUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> bootsUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> leggingsUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> bowUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> pickaxeUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<Enchantment>> axeUpgrades = new HashMap<Integer, ArrayList<Enchantment>>();
	public HashMap<Integer, ArrayList<PotionEffect>> potionUpgrades = new HashMap<Integer, ArrayList<PotionEffect>>();

	public HashMap<Integer, ArrayList<Integer>> helmetUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> swordUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> chestplateUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> bootsUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> leggingsUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> bowUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> pickaxeUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> axeUpgradesLevel = new HashMap<Integer, ArrayList<Integer>>();

	public HashMap<Integer, Integer> helmetUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> swordUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> chestplateUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> bootsUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> leggingsUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> bowUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> pickaxeUpgradesPrice = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> axeUpgradesPrice = new HashMap<Integer, Integer>();	
	public HashMap<Integer, Integer> potionsUpgradesPrice = new HashMap<Integer, Integer>();

	public HashMap<Integer, ArrayList<Integer>> helmetRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> swordRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> chestplateRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> bootsRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> leggingsRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> bowRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> pickaxeRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> axeRequirements = new HashMap<Integer, ArrayList<Integer>>();
	public HashMap<Integer, ArrayList<Integer>> potionsRequirements = new HashMap<Integer, ArrayList<Integer>>();

	public HashMap<Integer, String> helmetUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> swordUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> chestplateUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> bootsUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> leggingsUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> bowUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> pickaxeUpgradesName = new HashMap<Integer, String>();
	public HashMap<Integer, String> axeUpgradesName = new HashMap<Integer, String>();	
	public HashMap<Integer, String> potionsUpgradesName = new HashMap<Integer, String>();

	public HashMap<Integer, Boolean> helmetUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> swordUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> chestplateUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> bootsUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> leggingsUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> bowUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> pickaxeUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> axeUpgradesDefaultLocked = new HashMap<Integer, Boolean>();	
	public HashMap<Integer, Boolean> potionsUpgradesDefaultLocked = new HashMap<Integer, Boolean>();
	
	public HashMap<Integer, Boolean> helmetUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> swordUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> chestplateUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> bootsUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> leggingsUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> bowUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> pickaxeUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> axeUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();	
	public HashMap<Integer, Boolean> potionsUpgradesDefaultPurchased = new HashMap<Integer, Boolean>();

	public CustomKit(String kitname, String gameMode){
		this.kitname = kitname;
		this.gameMode = gameMode;
	}

	public void AssignKitToPlayer(final Player player){
		final String playerName = player.getName();
		final PlayerInventory inventory = player.getInventory();

		Kits.hasKit.put(player, true);

		Database.LoadPlayerKit(playerName, this);
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Kits"), new Runnable() {
			@Override
			public void run() {

				boolean applyUpgrades = true;
				boolean doneAssigning = false;
				boolean messageSent = false;

				//TODO: Find a new way to do this
				/*
				if(CoreAPI.GetCurrentGameMode(player).equals("minigame")){
					applyUpgrades = false;
				}
				*/

				CustomKit kit = Kits.getKitByName(kitname, gameMode);

				while(!doneAssigning){
					if(Database.GetPlayerLoaded(playerName, kit) == true){
						if(CoreDatabase.GetCurrencyMultiplier(playerName) >= kit.multiplierRequired){
							inventory.clear();

							//Equipment slots
							for(int i = 0; i < equipment.size(); i++){
								String equipmentName = equipment.get(i);

								if(equipmentName != null){
									ItemStack item;

									if(equipmentName.contains("HELMET")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "helmet");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && helmetUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, helmetUpgrades.get(lvl), helmetUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.setHelmet(item);

									} else if(equipmentName.contains("BOOTS")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "boots");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && bootsUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, bootsUpgrades.get(lvl), bootsUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.setBoots(item);

									} else if(equipmentName.contains("LEGGINGS")){
										int lvl = Database.GetUpgradeLevel(playerName, kit, "leggings");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && leggingsUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, leggingsUpgrades.get(lvl), leggingsUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.setLeggings(item);
									} else if(equipmentName.contains("CHESTPLATE")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "chestplate");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && chestplateUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, chestplateUpgrades.get(lvl), chestplateUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.setChestplate(item);
									} else if(equipmentName.contains("SWORD")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "sword");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && swordUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, swordUpgrades.get(lvl), swordUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.addItem(item);
									} else if(equipmentName.contains("PICKAXE")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "pickaxe");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && pickaxeUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, pickaxeUpgrades.get(lvl), pickaxeUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.addItem(item);
									} else if(equipmentName.contains("AXE")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "axe");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && axeUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, axeUpgrades.get(lvl), axeUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.addItem(item);
									} else if(equipmentName.contains("BOW")) {
										int lvl = Database.GetUpgradeLevel(playerName, kit, "bow");
										if(!applyUpgrades) lvl = 0;
										if((lvl == 0 && bowUpgrades.get(lvl) != null) || lvl != 0){
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, bowUpgrades.get(lvl), bowUpgradesLevel.get(lvl));
										}else{
											item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										}
										inventory.addItem(item);
									}else{
										item = Kits.getItem(Material.getMaterial(equipment.get(i)), 1, null, null, null, null);
										inventory.addItem(item);
									}
								}
							}


							for(int i = 0; i < hotbar.size(); i++){
								inventory.addItem(Kits.getItem(Material.getMaterial(hotbar.get(i)), 1, null, null, null, null));
							}

							Kits.FillInventory(inventory, Material.getMaterial(inventoryItem));

							//Set potion effects
							int lvl = Database.GetUpgradeLevel(playerName, kit, "potion");
							if((lvl == 0 && potionUpgrades.get(lvl) != null) || lvl != 0){
								for(int i = 0; i < potionUpgrades.get(lvl).size(); i++){
									player.addPotionEffect(potionUpgrades.get(lvl).get(i));
								}
							}

							player.sendMessage(ChatColor.GOLD + "You have received the " + ChatColor.RED + kit.kitdisplayname + ChatColor.GOLD + " kit!");
						}else{
							if(kit.multiplierRequired == 2){
								player.sendMessage(ChatColor.GRAY + "> This kit requires the " + ChatColor.AQUA + "LEGION" + ChatColor.GRAY + " rank!");
								player.sendMessage(ChatColor.GRAY + "> Purchase at " + ChatColor.GOLD + "mcwarcraft.com");
							}else if(kit.multiplierRequired == 3){
								player.sendMessage(ChatColor.GRAY + "> This kit requires the " + ChatColor.LIGHT_PURPLE + "HERO" + ChatColor.GRAY + " rank!");
								player.sendMessage(ChatColor.GRAY + "> Purchase at " + ChatColor.GOLD + "mcwarcraft.com");
							}
						}
						doneAssigning = true;
					}else{
						if(!messageSent){
							player.sendMessage(ChatColor.GOLD + "Your kit is loading, please wait...");
							messageSent = true;
						}
					}
				}
			}
		});
	}

	public int GetMaxUpgradeLevel(String tool){
		Object[] upgradeArray = null;
		if(tool.equalsIgnoreCase("helmet")) {
			upgradeArray = helmetUpgrades.keySet().toArray();
		} else if(tool.equalsIgnoreCase("boots")) {
			upgradeArray = bootsUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("leggings")) {
			upgradeArray = leggingsUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("chestplate")) {
			upgradeArray = chestplateUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("sword")) {
			upgradeArray = swordUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("pickaxe")) {
			upgradeArray = pickaxeUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("axe")) {
			upgradeArray = axeUpgrades.keySet().toArray();
		}  else if(tool.equalsIgnoreCase("bow")) {
			upgradeArray = bowUpgrades.keySet().toArray();
		}

		if(upgradeArray == null){
			return -1;
		}else{
			return (int)upgradeArray[upgradeArray.length - 1];
		}
	}

	public String GetUpgradeDisplayName(String tool, int level){
		if(tool.equalsIgnoreCase("helmet")) {
			return helmetUpgradesName.get(level);
		} else if(tool.equalsIgnoreCase("boots")) {
			return bootsUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("leggings")) {
			return leggingsUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("chestplate")) {
			return chestplateUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("sword")) {
			return swordUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("pickaxe")) {
			return pickaxeUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("axe")) {
			return axeUpgradesName.get(level);
		}  else if(tool.equalsIgnoreCase("bow")) {
			return bowUpgradesName.get(level);
		}
		return null;
	}

	public boolean GetDefaultLockedUpgrade(String tool, int level){
		HashMap<Integer, Boolean> locked = null;

		if(tool.equalsIgnoreCase("sword")){
			locked = swordUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("pickaxe")){
			locked = pickaxeUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("axe")){
			locked = axeUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("bow")){
			locked = bowUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("boots")){
			locked = bootsUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("leggings")){
			locked = leggingsUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("chestplate")){
			locked = chestplateUpgradesDefaultLocked;
		} else if (tool.equalsIgnoreCase("helmet")){
			locked = helmetUpgradesDefaultLocked;
		}else{
			return false;
		}

		return locked.get(level);
	}
}
