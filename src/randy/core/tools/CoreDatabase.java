package randy.core.tools;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;

import bourg.austin.HonorPoints.DatabaseOperations;
import randy.core.CoreScoreboard;

public class CoreDatabase {
	
	/*
	 * All hashmaps have follow a format:
	 * 
	 * propertytype:player:gamemode=property
	 */
	
	//Games
	private static HashMap<String, Integer> killMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> deathMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> towerKitGrabMap = new HashMap<String, Integer>();
	
	//Currency & ranking
	private static HashMap<String, Integer> currencyMap = new HashMap<String, Integer>();
	private static HashMap<String, Double> currencyMultiplierMap = new HashMap<String, Double>();
	private static HashMap<String, Integer> arenaRankingMap = new HashMap<String, Integer>();
	
	//Management
	private static ArrayList<String> changedProperties = new ArrayList<String>();
	private static ArrayList<String> loadedPlayers = new ArrayList<String>();
	
	/*
	 * GAMES
	 */
	public static void ModifyKills(String player, String gamemode, int amount){
		int kills = 0;
		if(killMap.containsKey(player+":"+gamemode)){
			kills = killMap.get(player+":"+gamemode);
		}
		kills += amount;
		killMap.put(player+":"+gamemode, kills);
		changedProperties.add("kills:"+player+":"+gamemode+"="+kills);
	}
	
	public static void ModifyDeaths(String player, String gamemode, int amount){
		int deaths = 0;
		if(deathMap.containsKey(player+":"+gamemode)){
			deaths = deathMap.get(player+":"+gamemode);
		}
		deaths += amount;
		deathMap.put(player+":"+gamemode, deaths);
		changedProperties.add("deaths:"+player+":"+gamemode+"="+deaths);
	}
	
	public static void ModifyTowerKitGrabs(String player, int amount){
		int grabs = 0;
		if(towerKitGrabMap.containsKey(player)){
			grabs = towerKitGrabMap.get(player);
		}
		grabs += amount;
		towerKitGrabMap.put(player, grabs);
		changedProperties.add("towergrabs:"+player+"="+grabs);
	}
	
	public static int GetDeaths(String player, String gamemode){
		return deathMap.get(player+":"+gamemode);
	}
	
	public static int GetKills(String player, String gamemode){
		return killMap.get(player+":"+gamemode);
	}
	
	public static int GetTowerKitGrabs(String player){
		return towerKitGrabMap.get(player);
	}
	
	public static int GetTotalDeaths(String player){
		if(deathMap.containsKey(player+":ffa") && deathMap.containsKey(player+":tower") && deathMap.containsKey(player+":minigame"))
			return deathMap.get(player+":ffa") + deathMap.get(player+":tower") + deathMap.get(player+":minigame");
		else
			return 0;
	}
	
	public static int GetTotalKills(String player){
		if(killMap.containsKey(player+":ffa") && killMap.containsKey(player+":tower") && killMap.containsKey(player+":minigame"))
			return killMap.get(player+":ffa") + killMap.get(player+":tower") + killMap.get(player+":minigame");
		else
			return 0;
	}
	
	/*
	 * CURRENCY & RANKING
	 */
	public static int GetCurrency(String player){
		if(currencyMap.containsKey(player)) return currencyMap.get(player);
		return 0;
	}
	
	public static void SetCurrency(String player, int amount){
		currencyMap.put(player, amount);
		changedProperties.add("currency:"+player+":none="+amount);
	}
	
	public static void ModifyCurrency(String player, int amount){
		SetCurrency(player, (int)(currencyMap.get(player) + (amount * currencyMultiplierMap.get(player))));
	}
	
	public static double GetCurrencyMultiplier(String player){
		if(currencyMultiplierMap.containsKey(player)) return currencyMultiplierMap.get(player);
		return 1;
	}
	
	public static void SetCurrencyMultiplier(String player, double multiplier){
		currencyMultiplierMap.put(player, multiplier);
		changedProperties.add("currencymultiplier:"+player+":none="+multiplier);
	}
	
	public static int GetArenaRanking(String player, int mode){
		if(mode == 1 || mode == 2 || mode == 3){
			if(arenaRankingMap.containsKey(player+":"+mode)) return arenaRankingMap.get(player+":"+mode);
		}
		return 0;
	}
	
	public static void SetArenaRanking(String player, int mode, int amount){
		if(mode == 1 || mode == 2 || mode == 3){
			arenaRankingMap.put(player+":"+mode, amount);
			changedProperties.add("arena"+mode+":"+player+":arena="+amount);
		}
	}
	
	public static void ModifyArenaRanking(String player, int mode, int amount){
		if(mode == 1 || mode == 2 || mode == 3){
			SetArenaRanking(player, mode, arenaRankingMap.get(player+":"+mode) + amount);
		}
	}
	
	/*
	 * LOADING AND SAVING
	 * 
	 * propertytype:player:gamemode=property
	 */
	public static void PushChanges(){
		
		System.out.print("[CorePlugin] Pushing changes to SQL...");
		for(String propery : changedProperties){
			String[] propertySplit = propery.split("=");
			String[] propertyInfo = propertySplit[0].split(":");
			
			String propertyType = propertyInfo[0];
			String player = propertyInfo[1];
			String gamemode = propertyInfo[2];
			String property = propertySplit[1];
			
			if(propertyType.equals("deaths")){
				CoreSQL.SetDeaths(player, gamemode, Integer.parseInt(property));
			} else if(propertyType.equals("kills")){
				CoreSQL.SetKills(player, gamemode, Integer.parseInt(property));
			} else if(propertyType.equals("towergrabs")){
				CoreSQL.SetTowerKitGrabs(player, Integer.parseInt(property));
			} else if(propertyType.equals("currency")){
				DatabaseOperations.setCurrency(Bukkit.getOfflinePlayer(player), Integer.parseInt(property));
			} else if(propertyType.equals("currencymultiplier")){
				DatabaseOperations.setMultiplier(Bukkit.getOfflinePlayer(player), Integer.parseInt(property));
			} else if(propertyType.equals("arena1")){
				CoreSQL.SetArenaRanking(player, 1, Integer.parseInt(property));
			} else if(propertyType.equals("arena2")){
				CoreSQL.SetArenaRanking(player, 2, Integer.parseInt(property));
			} else if(propertyType.equals("arena3")){
				CoreSQL.SetArenaRanking(player, 3, Integer.parseInt(property));
			}
		}
		
		System.out.print("[CorePlugin] Changes pushed to SQL!");
		
		changedProperties.clear();
	}
	
	public static void LoadPlayerData(final String player){
		if(!loadedPlayers.contains(player)){
			Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("CorePlugin"), new Runnable() {
				@Override
				public void run() {
					
					//player:gamemode
					killMap.put(player+":ffa", CoreSQL.GetKills(player, "ffa"));
					killMap.put(player+":tower", CoreSQL.GetKills(player, "tower"));
					killMap.put(player+":minigame", CoreSQL.GetKills(player, "minigame"));
					
					deathMap.put(player+":ffa", CoreSQL.GetDeaths(player, "ffa"));
					deathMap.put(player+":tower", CoreSQL.GetDeaths(player, "tower"));
					deathMap.put(player+":minigame", CoreSQL.GetDeaths(player, "minigame"));
					
					currencyMap.put(player, DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(player)));
					currencyMultiplierMap.put(player, DatabaseOperations.getMultiplier(Bukkit.getOfflinePlayer(player)));
					
					arenaRankingMap.put(player+":arena1", CoreSQL.GetArenaRanking(player, 1));
					arenaRankingMap.put(player+":arena2", CoreSQL.GetArenaRanking(player, 2));
					arenaRankingMap.put(player+":arena3", CoreSQL.GetArenaRanking(player, 3));
					
					towerKitGrabMap.put(player, CoreSQL.GetTowerKitGrabs(player));

					loadedPlayers.add(player);
					
					CoreScoreboard.UpdateScoreboard(player);
				}
			});

		}
	}
}
