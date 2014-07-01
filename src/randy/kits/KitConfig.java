package randy.kits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitConfig {
	
	//Options
	public static ArrayList<String> databaseOptions = new ArrayList<String>();
	
	//File
	static File configfile = new File("plugins" + File.separator + "FFA" + File.separator + "config.yml");
	static FileConfiguration configuration = YamlConfiguration.loadConfiguration(configfile);
	
	static File HPConfigfile = new File("plugins" + File.separator + "HonorPoints" + File.separator + "config.yml");
	static FileConfiguration HPConfiguration = YamlConfiguration.loadConfiguration(HPConfigfile);
	
	static File kitsfile = new File("plugins" + File.separator + "Kits" + File.separator + "kits.yml");
	static FileConfiguration kits = YamlConfiguration.loadConfiguration(kitsfile);
	
	static File lmskitsfile = new File("plugins" + File.separator + "Kits" + File.separator + "lmskits.yml");
	static FileConfiguration lmskits = YamlConfiguration.loadConfiguration(lmskitsfile);
	
	static File kothkitsfile = new File("plugins" + File.separator + "Kits" + File.separator + "kothkits.yml");
	static FileConfiguration kothkits = YamlConfiguration.loadConfiguration(kothkitsfile);
	
	static File towerkitsfile = new File("plugins" + File.separator + "Kits" + File.separator + "towerkits.yml");
	static FileConfiguration towerkits = YamlConfiguration.loadConfiguration(towerkitsfile);
	
	
	//Load config
	public static void LoadConfig(){
		
		File directory = new File("plugins" + File.separator + "Kits");
		if(!directory.exists()){
			directory.mkdir();
		}
		
		//Create file if it does not exist
		if(!configfile.exists()) {
			try {
				configfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The config file could not be created.");
			}
		}
		
		if(!kitsfile.exists()) {
			try {
				kitsfile.createNewFile();
			} catch (IOException e) {
				System.out.print("The kits file could not be created.");
			}
		}
		
		//Retrieve database stuff
		if(configuration.contains("Database")){
			databaseOptions = new ArrayList<String>();
			databaseOptions.add(HPConfiguration.getString("sql.ip"));
			databaseOptions.add(HPConfiguration.getString("sql.port"));
			databaseOptions.add(configuration.getString("Database.Database"));
			databaseOptions.add(HPConfiguration.getString("sql.username"));
			databaseOptions.add(HPConfiguration.getString("sql.password"));
		}
		
		//Load kits
		LoadKitFile("ffa");
		LoadKitFile("lms");
		LoadKitFile("koth");
		LoadKitFile("tower");
	}
	
	private static void LoadKitFile(String gamemode){
		FileConfiguration file = null;
		if(gamemode.equals("ffa")){
			file = kits;
		}else if(gamemode.equals("lms")){
			file = lmskits;
		}else if(gamemode.equals("koth")){
			file = kothkits;
		} else if(gamemode.equals("tower")){
			file = towerkits;
		}
		
		Object[] kitnames = file.getConfigurationSection("Kits").getKeys(false).toArray();
		for(int i = 0; i < kitnames.length; i++){
			String kitname = kitnames[i].toString();
			CustomKit kit = new CustomKit(kitname, gamemode);
			
			kit.kitdisplayname = file.getString("Kits."+kitname+".DisplayName");
			kit.defaultlocked = file.getBoolean("Kits."+kitname+".DefaultLocked");
			kit.kitprice = file.getInt("Kits."+kitname+".Price", 0);
			kit.multiplierRequired = file.getInt("Kits."+kitname+".MultiplierRequired", 1);
			kit.defaultpurchased = file.getBoolean("Kits."+kitname+".DefaultPurchased", true);
			
			/*
			 * EQUIPMENT
			 */
			ArrayList<String> equipment = new ArrayList<String>();
			
			//Equipment items
			equipment.add(file.getString("Kits."+kitname+".Armor.Helmet"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Chestplate"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Leggings"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Boots"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Sword"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Bow"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Pickaxe"));
			equipment.add(file.getString("Kits."+kitname+".Armor.Axe"));
			
			//Equipment enchantments per level
			if(file.get("Kits."+kitname+".Upgrades.Helmet") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Helmet").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Helmet."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Helmet."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Helmet."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Helmet."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Helmet."+lvl+".DisplayName");
					
					kit.helmetUpgrades.put(lvl, enchantment);
					kit.helmetUpgradesLevel.put(lvl, enchantmentNumber);
					kit.helmetUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Helmet."+lvl+".Price"));
					kit.helmetRequirements.put(lvl, requirements);
					kit.helmetUpgradesName.put(lvl, displayname);
					kit.helmetUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Helmet."+lvl+".DefaultLocked", true));
					kit.helmetUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Helmet."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Chestplate") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Chestplate").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Chestplate."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Chestplate."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Chestplate."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Chestplate."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Chestplate."+lvl+".DisplayName");
					
					kit.chestplateUpgrades.put(lvl, enchantment);
					kit.chestplateUpgradesLevel.put(lvl, enchantmentNumber);
					kit.chestplateUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Chestplate."+lvl+".Price"));
					kit.chestplateRequirements.put(lvl, requirements);
					kit.chestplateUpgradesName.put(lvl, displayname);
					kit.chestplateUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Chestplate."+lvl+".DefaultLocked", true));
					kit.chestplateUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Chestplate."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Leggings") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Leggings").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Leggings."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Leggings."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Leggings."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Leggings."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Leggings."+lvl+".DisplayName");
					
					kit.leggingsUpgrades.put(lvl, enchantment);
					kit.leggingsUpgradesLevel.put(lvl, enchantmentNumber);
					kit.leggingsUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Leggings."+lvl+".Price"));
					kit.leggingsRequirements.put(lvl, requirements);
					kit.leggingsUpgradesName.put(lvl, displayname);
					kit.leggingsUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Leggings."+lvl+".DefaultLocked", true));
					kit.leggingsUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Leggings."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Boots") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Boots").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Boots."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Boots."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Boots."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Boots."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Boots."+lvl+".DisplayName");
					
					kit.bootsUpgrades.put(lvl, enchantment);
					kit.bootsUpgradesLevel.put(lvl, enchantmentNumber);
					kit.bootsUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Boots."+lvl+".Price"));
					kit.bootsRequirements.put(lvl, requirements);
					kit.bootsUpgradesName.put(lvl, displayname);
					kit.bootsUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Boots."+lvl+".DefaultLocked", true));
					kit.bootsUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Boots."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Sword") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Sword").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Sword."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Sword."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Sword."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Sword."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Sword."+lvl+".DisplayName");
					
					kit.swordUpgrades.put(lvl, enchantment);
					kit.swordUpgradesLevel.put(lvl, enchantmentNumber);
					kit.swordUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Sword."+lvl+".Price"));
					kit.swordRequirements.put(lvl, requirements);
					kit.swordUpgradesName.put(lvl, displayname);
					kit.swordUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Sword."+lvl+".DefaultLocked", true));
					kit.swordUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Sword."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Pickaxe") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Pickaxe").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".DisplayName");
					
					kit.pickaxeUpgrades.put(lvl, enchantment);
					kit.pickaxeUpgradesLevel.put(lvl, enchantmentNumber);
					kit.pickaxeUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".Price"));
					kit.pickaxeRequirements.put(lvl, requirements);
					kit.pickaxeUpgradesName.put(lvl, displayname);
					kit.pickaxeUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".DefaultLocked", true));
					kit.pickaxeUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Pickaxe."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Axe") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Axe").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Axe."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Axe."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Axe."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Axe."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Axe."+lvl+".DisplayName");
					
					kit.axeUpgrades.put(lvl, enchantment);
					kit.axeUpgradesLevel.put(lvl, enchantmentNumber);
					kit.axeUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Axe."+lvl+".Price"));
					kit.axeRequirements.put(lvl, requirements);
					kit.axeUpgradesName.put(lvl, displayname);
					kit.axeUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Axe."+lvl+".DefaultLocked", true));
					kit.axeUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Axe."+lvl+".DefaultPurchased", false));
				}
			}
			
			if(file.get("Kits."+kitname+".Upgrades.Bow") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Bow").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					ArrayList<Enchantment> enchantment = new ArrayList<Enchantment>();
					ArrayList<Integer> enchantmentNumber = new ArrayList<Integer>();
					String enchantmentString = file.getString("Kits."+kitname+".Upgrades.Bow."+lvl+".Enchantments");
					ParseEnchantment(enchantment, enchantmentNumber, enchantmentString);
					
					ArrayList<Integer> requirements = new ArrayList<Integer>();
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Bow."+lvl+".Requirements.1v1", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Bow."+lvl+".Requirements.2v2", 0));
					requirements.add(file.getInt("Kits."+kitname+".Upgrades.Bow."+lvl+".Requirements.3v3", 0));
					
					String displayname = file.getString("Kits."+kitname+".Upgrades.Bow."+lvl+".DisplayName");
					
					kit.bowUpgrades.put(lvl, enchantment);
					kit.bowUpgradesLevel.put(lvl, enchantmentNumber);
					kit.bowUpgradesPrice.put(lvl, file.getInt("Kits."+kitname+".Upgrades.Bow."+lvl+".Price"));
					kit.bowRequirements.put(lvl, requirements);
					kit.bowUpgradesName.put(lvl, displayname);
					kit.bowUpgradesDefaultLocked.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Bow."+lvl+".DefaultLocked", true));
					kit.bowUpgradesDefaultPurchased.put(lvl, file.getBoolean("Kits."+kitname+".Upgrades.Bow."+lvl+".DefaultPurchased", false));
				}
			}
			
			//Fill default locked if they don't exist
			if(!kit.swordUpgradesDefaultLocked.containsKey(0)) kit.swordUpgradesDefaultLocked.put(0, true);
			if(!kit.axeUpgradesDefaultLocked.containsKey(0)) kit.axeUpgradesDefaultLocked.put(0, true);
			if(!kit.pickaxeUpgradesDefaultLocked.containsKey(0)) kit.pickaxeUpgradesDefaultLocked.put(0, true);
			if(!kit.bowUpgradesDefaultLocked.containsKey(0)) kit.bowUpgradesDefaultLocked.put(0, true);
			if(!kit.leggingsUpgradesDefaultLocked.containsKey(0)) kit.leggingsUpgradesDefaultLocked.put(0, true);
			if(!kit.helmetUpgradesDefaultLocked.containsKey(0)) kit.helmetUpgradesDefaultLocked.put(0, true);
			if(!kit.chestplateUpgradesDefaultLocked.containsKey(0)) kit.chestplateUpgradesDefaultLocked.put(0, true);
			if(!kit.bootsUpgradesDefaultLocked.containsKey(0)) kit.bootsUpgradesDefaultLocked.put(0, true);
			
			if(!kit.swordUpgradesDefaultLocked.containsKey(1)) kit.swordUpgradesDefaultLocked.put(1, true);
			if(!kit.axeUpgradesDefaultLocked.containsKey(1)) kit.axeUpgradesDefaultLocked.put(1, true);
			if(!kit.pickaxeUpgradesDefaultLocked.containsKey(1)) kit.pickaxeUpgradesDefaultLocked.put(1, true);
			if(!kit.bowUpgradesDefaultLocked.containsKey(1)) kit.bowUpgradesDefaultLocked.put(1, true);
			if(!kit.leggingsUpgradesDefaultLocked.containsKey(1)) kit.leggingsUpgradesDefaultLocked.put(1, true);
			if(!kit.helmetUpgradesDefaultLocked.containsKey(1)) kit.helmetUpgradesDefaultLocked.put(1, true);
			if(!kit.chestplateUpgradesDefaultLocked.containsKey(1)) kit.chestplateUpgradesDefaultLocked.put(1, true);
			if(!kit.bootsUpgradesDefaultLocked.containsKey(1)) kit.bootsUpgradesDefaultLocked.put(1, true);
			
			kit.equipment = equipment;
			
			/*
			 * HOTBAR
			 */
			String hotbarString = file.getString("Kits."+kitname+".Hotbar");
			ArrayList<String> hotbarList = new ArrayList<String>();
			if(hotbarString != null){
				String[] hotbarArray = hotbarString.split(",");
				for(int e = 0; e < hotbarArray.length; e++){
					hotbarList.add(hotbarArray[e]);
				}
			}else{
				hotbarList.add("AIR");
			}
			kit.hotbar = hotbarList;
			
			/*
			 * EXPANDED INVENTORY
			 */
			kit.inventoryItem = file.getString("Kits."+kitname+".Inventory");
			
			/*
			 * POTION EFFECTS
			 */
			if(file.get("Kits."+kitname+".Upgrades.Potion") != null){
				Object[] upgradeLevels = file.getConfigurationSection("Kits."+kitname+".Upgrades.Potion").getKeys(false).toArray();
				for(int e = 0; e < upgradeLevels.length; e++){
					ArrayList<PotionEffect> potionEffects = new ArrayList<PotionEffect>();
					int lvl = Integer.parseInt((String)upgradeLevels[e]);
					String effects = file.getString("Kits."+kitname+".Upgrades.Potion."+lvl+"Effects");
					if(effects != null){
						if(effects.contains(",")){
							String[] effectArray = effects.split(",");
							for(int effectNumber = 0; effectNumber < effectArray.length; e++){
								String[] effect = effectArray[effectNumber].split("=");
								potionEffects.add(new PotionEffect(PotionEffectType.getByName(effect[0]), 100000000, Integer.parseInt(effect[1])));
							}
						}else{
							String[] effect = effects.split("=");
							potionEffects.add(new PotionEffect(PotionEffectType.getByName(effect[0]), 100000000, Integer.parseInt(effect[1])));
						}
					}
					kit.potionUpgrades.put(lvl, potionEffects);
				}
			}
			
			Kits.customKits.add(kit);
		}
	}
	
	private static void ParseEnchantment(ArrayList<Enchantment> enchantmentList, ArrayList<Integer> levelList, String enchantmentstring){
		if(enchantmentstring.contains(",")){
			String[] enchantmentArray = enchantmentstring.split(",");
			for(int e = 0; e < enchantmentArray.length; e++){
				String[] enchsplit = enchantmentArray[e].split("=");
				enchantmentList.add(Enchantment.getByName(enchsplit[0]));
				levelList.add(Integer.parseInt(enchsplit[1]));
				//System.out.print("Enchsplit: " + enchsplit[0] + "=" + enchsplit[1]);
			}
		}else{
			String[] enchsplit = enchantmentstring.split("=");
			enchantmentList.add(Enchantment.getByName(enchsplit[0]));
			levelList.add(Integer.parseInt(enchsplit[1]));
		}
	}
}
