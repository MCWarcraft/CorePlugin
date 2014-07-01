package randy.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;

import randy.core.tools.CoreSQL;

public class Database {
	
	private static HashMap<String, Boolean> loadedPlayers = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> kitLocked = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> kitPurchased = new HashMap<String, Boolean>();
	private static HashMap<String, Integer> kitUpgradeLevel = new HashMap<String, Integer>();
	private static HashMap<String, Boolean> kitUpgradeLocked = new HashMap<String, Boolean>();
	private static ArrayList<String> changedProperties = new ArrayList<String>();
	
	public static int GetUpgradeLevel(String player, CustomKit kit, String tool){
		return kitUpgradeLevel.get(player+":"+kit.kitname+":"+tool);
	}
	
	public static boolean GetUpgradeLocked(String player, CustomKit kit, String tool){
		return kitUpgradeLocked.get(player+":"+kit.kitname+":"+tool);
	}
	
	public static boolean GetKitLocked(String player, CustomKit kit){
		return kitLocked.get(player+":"+kit.gameMode+":"+kit.kitname);
	}
	
	public static boolean GetKitPurchased(String player, CustomKit kit){
		return kitPurchased.get(player+":"+kit.kitname);
	}
	
	public static void SetUpgradeLevel(String player, CustomKit kit, String tool, int level, boolean nextLevelLocked){
		kitUpgradeLevel.put(player+":"+kit.kitname+":"+tool, level);
		changedProperties.add("upgradelevel:"+player+":"+kit.kitname+":"+tool+"="+level+":"+nextLevelLocked);
	}
	
	public static void SetUpgradeLocked(String player, CustomKit kit, String tool, boolean locked){
		kitUpgradeLocked.put(player+":"+kit.kitname+":"+tool, locked);
		changedProperties.add("upgradelocked:"+player+":"+kit.kitname+":"+tool+"="+locked);
	}
	
	public static void SetKitLocked(String player, CustomKit kit, boolean locked){
		kitLocked.put(player+":"+kit.gameMode+":"+kit.kitname, locked);
		changedProperties.add("kitlocked:"+player+":"+kit.gameMode+":"+kit.kitname+"="+locked);
	}
	
	public static void SetKitPurchased(String player, CustomKit kit, boolean purchased){
		kitPurchased.put(player+":"+kit.kitname, purchased);
		changedProperties.add("kitpurchased:"+player+":"+kit.kitname+"="+purchased);
	}
	
	public static void PushChanges(){
		
		System.out.print("[Kits] Pushing changes to SQL...");
		for(String propery : changedProperties){
			String[] propertySplit = propery.split("=");
			String[] propertyInfo = propertySplit[0].split(":");
			if(propertyInfo[0].equals("upgradelevel")){
				String[] resultSplit = propertySplit[1].split(":");
				CoreSQL.SetKitUpgradeLevel(propertyInfo[1], Kits.getKitByName(propertyInfo[2], "ffa"), propertyInfo[3], Integer.parseInt(resultSplit[0]), Boolean.parseBoolean(resultSplit[1]));
			} else if(propertyInfo[0].equals("upgradelocked")){
				CoreSQL.SetKitUpgradeLocked(propertyInfo[1], Kits.getKitByName(propertyInfo[2], "ffa"), propertyInfo[3], Boolean.parseBoolean(propertySplit[1]));
			} else if(propertyInfo[0].equals("kitlocked")){
				CoreSQL.SetKitLocked(propertyInfo[1], Kits.getKitByName(propertyInfo[3], propertyInfo[2]), Boolean.parseBoolean(propertySplit[1]));
			} else if(propertyInfo[0].equals("kitpurchased")){
				CoreSQL.SetKitPurchased(propertyInfo[1], Kits.getKitByName(propertyInfo[2], "ffa"), Boolean.parseBoolean(propertySplit[1]));
			}
		}
		
		System.out.print("[Kits] Changes pushed to SQL!");
		
		changedProperties.clear();
	}
	
	/*public static void LoadAllPlayers(){
		
		//For every kit
		List<CustomKit> kits = Kits.customKits;
		for(CustomKit kit : kits){
			String kitname = kit.kitname;
			System.out.print("Loading kit " + kitname + "...");
			List<String> players = SQL.GetPlayers(kit);
			for(String player : players){
				LoadPlayerKit(player, kit);
			}
		}
	}*/
	
	public static void LoadPlayerKit(final String player, final CustomKit kit){
		if(!loadedPlayers.containsKey(kit.kitname+":"+player)){
			loadedPlayers.put(kit.kitname+":"+player, false);
		}
		if(loadedPlayers.get(kit.kitname+":"+player) == false){
			Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Kits"), new Runnable() {
				@Override
				public void run() {
					String kitname = kit.kitname;
					
					kitLocked.put(player+":"+kit.gameMode+":"+kitname, CoreSQL.GetKitLocked(player, kit));
					kitPurchased.put(player+":"+kitname, CoreSQL.GetKitPurchased(player, kit));
					
					kitUpgradeLevel.put(player+":"+kitname+":sword", CoreSQL.GetKitUpgradeLevel(player, kit, "sword"));
					kitUpgradeLevel.put(player+":"+kitname+":axe", CoreSQL.GetKitUpgradeLevel(player, kit, "axe"));
					kitUpgradeLevel.put(player+":"+kitname+":pickaxe", CoreSQL.GetKitUpgradeLevel(player, kit, "pickaxe"));
					kitUpgradeLevel.put(player+":"+kitname+":bow", CoreSQL.GetKitUpgradeLevel(player, kit, "bow"));
					kitUpgradeLevel.put(player+":"+kitname+":leggings", CoreSQL.GetKitUpgradeLevel(player, kit, "leggings"));
					kitUpgradeLevel.put(player+":"+kitname+":helmet", CoreSQL.GetKitUpgradeLevel(player, kit, "helmet"));
					kitUpgradeLevel.put(player+":"+kitname+":chestplate", CoreSQL.GetKitUpgradeLevel(player, kit, "chestplate"));
					kitUpgradeLevel.put(player+":"+kitname+":boots", CoreSQL.GetKitUpgradeLevel(player, kit, "boots"));
					kitUpgradeLevel.put(player+":"+kitname+":potion", CoreSQL.GetKitUpgradeLevel(player, kit, "potion"));
					
					kitUpgradeLocked.put(player+":"+kitname+":sword", CoreSQL.GetKitUpgradeLocked(player, kit, "sword"));
					kitUpgradeLocked.put(player+":"+kitname+":axe", CoreSQL.GetKitUpgradeLocked(player, kit, "axe"));
					kitUpgradeLocked.put(player+":"+kitname+":pickaxe", CoreSQL.GetKitUpgradeLocked(player, kit, "pickaxe"));
					kitUpgradeLocked.put(player+":"+kitname+":bow", CoreSQL.GetKitUpgradeLocked(player, kit, "bow"));
					kitUpgradeLocked.put(player+":"+kitname+":leggings", CoreSQL.GetKitUpgradeLocked(player, kit, "leggings"));
					kitUpgradeLocked.put(player+":"+kitname+":helmet", CoreSQL.GetKitUpgradeLocked(player, kit, "helmet"));
					kitUpgradeLocked.put(player+":"+kitname+":chestplate", CoreSQL.GetKitUpgradeLocked(player, kit, "chestplate"));
					kitUpgradeLocked.put(player+":"+kitname+":boots", CoreSQL.GetKitUpgradeLocked(player, kit, "boots"));
					kitUpgradeLocked.put(player+":"+kitname+":potion", CoreSQL.GetKitUpgradeLocked(player, kit, "potion"));

					loadedPlayers.put(kit.kitname+":"+player, true);
				}
			});

		}
	}
	
	public static boolean GetPlayerLoaded(String player, CustomKit kit){
		return loadedPlayers.get(kit.kitname+":"+player);
	}
}
