package randy.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import randy.core.CorePlugin;
import randy.core.tools.CoreDatabase;
import core.Custody.Custody;

public class KitManager{
	
	CorePlugin plugin;
	
	public void disable() {
		Bukkit.getScheduler().cancelAllTasks();
		
		Database.PushChanges();
		
		System.out.print("[Kits] succesfully disabled.");
	}
	
	public KitManager(CorePlugin plugin) {
		this.plugin = plugin;
		
		KitConfig.LoadConfig();
		
		Player[] players = Bukkit.getOnlinePlayers();
		for(int i = 0; i < players.length; i++){
			Kits.hasKit.put(players[i], false);
		}
		
		System.out.print("[Kits] Succesfully enabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args){
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(commandName.equalsIgnoreCase("kit")){
				if(args.length == 1){
					String kitname = args[0];
					CustomKit kit = Kits.getKitByName(kitname, "ffa");
					//System.out.print("'"+args[0]+"'");
					if(kit != null){
						if(!Database.GetKitLocked(player.getName(), kit)){
							String gamemode = Custody.getCustody(player.getName());
							if(gamemode.equals("spawn") ||
									gamemode.equals("ffa")){
								kit.AssignKitToPlayer(player);							
							}
						}else{
							player.sendMessage(ChatColor.RED + "You do not own this kit! You can purchase it through the upgrade menu.");
						}
					}else{
						player.sendMessage(ChatColor.RED + "That kit doesn't exist.");
					}
					return true;
				}
				
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("purchase")){
						String kitname = args[1];
						if(Kits.getKitByName(kitname, "ffa") != null){	//Does kit exist?
							CustomKit kit = Kits.getKitByName(kitname, "ffa");
							AttemptPurchase(player, kit);
						}
					}
				}
				
				if(args.length == 4){ //Upgrade commands
					if(args[0].equalsIgnoreCase("upgrade")){
						String kitname = args[1];
						String tool = args[2];
						int upgradeLevel = Integer.parseInt(args[3]);
						
						if(Kits.getKitByName(kitname, "ffa") != null){	//Does kit exist?
							CustomKit kit = Kits.getKitByName(kitname, "ffa");
							AttemptUpgrade(player, kit, tool, upgradeLevel, "ffa");
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void AttemptPurchase(final Player player, CustomKit kit){
		final String playerName = player.getName();
		final String kitname = kit.kitname;
		final String gamemode = kit.gameMode;
		
		Database.LoadPlayerKit(playerName, kit);
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Kits"), new Runnable() {
			@Override
			public void run() {
				
				boolean donePurchasing = false;
				boolean messageSent = false;

				CustomKit kit = Kits.getKitByName(kitname, gamemode);

				while(!donePurchasing){
					if(Database.GetPlayerLoaded(playerName, kit) == true){
						boolean kitLocked = Database.GetKitLocked(playerName, kit);
						boolean kitPurchased = Database.GetKitPurchased(playerName, kit);
						
						if(!kitPurchased){
							player.sendMessage(ChatColor.GOLD + "You already have " + ChatColor.GREEN + kit.kitdisplayname + ChatColor.GOLD + "!");
							return;
						}
						
						if(!kit.defaultlocked && kitLocked){
							Database.SetKitLocked(playerName, kit, false);
							kitLocked = false;
						}
						
						if(kit.kitprice == 0 && !kitLocked){
							player.sendMessage(ChatColor.GOLD + "You have bought " + ChatColor.GREEN + kit.kitdisplayname + ChatColor.GOLD + "!");
							return;
						}
						
						int currency = CoreDatabase.GetCurrency(playerName);
						
						if(currency >= kit.kitprice){
							if(!kitLocked){
								CoreDatabase.ModifyCurrency(playerName, -kit.kitprice);
								Database.SetKitLocked(playerName, kit, false);
								player.sendMessage(ChatColor.GOLD + "You have bought " + ChatColor.GREEN + kit.kitdisplayname + ChatColor.GOLD + " for " + ChatColor.GREEN + kit.kitprice + ChatColor.GOLD + "!");
							}else{
								player.sendMessage(ChatColor.GRAY + "> You don't have enough honor points!");
								player.sendMessage(ChatColor.GRAY + "> Purchase more at " + ChatColor.GOLD + "mcwarcraft.com");
							}
						}else{
							player.sendMessage(ChatColor.RED + "You do not have enough Honor!");
						}
						donePurchasing = true;
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
	
	public static void AttemptUpgrade(final Player player, CustomKit kit, final String tool, final int upgradeLevel, final String gamemode){
		
		final String playerName = player.getName();
		final String kitname = kit.kitname;
		
		Database.LoadPlayerKit(playerName, kit);
		Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Kits"), new Runnable() {
			@Override
			public void run() {
				
				boolean doneUpgrading = false;
				boolean messageSent = false;

				CustomKit kit = Kits.getKitByName(kitname, gamemode);
				
				HashMap<Integer, ArrayList<Enchantment>> upgrades = null;
				HashMap<Integer, Integer> prices = null;
				HashMap<Integer, ArrayList<Integer>> requirements = null;
				HashMap<Integer, Boolean> locked = null;
				
				if(tool.equalsIgnoreCase("sword")){
					upgrades = kit.swordUpgrades;
					prices = kit.swordUpgradesPrice;
					requirements = kit.swordRequirements;
					locked = kit.swordUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("pickaxe")){
					upgrades = kit.pickaxeUpgrades;
					prices = kit.pickaxeUpgradesPrice;
					requirements = kit.pickaxeRequirements;
					locked = kit.pickaxeUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("axe")){
					upgrades = kit.axeUpgrades;
					prices = kit.axeUpgradesPrice;
					requirements = kit.axeRequirements;
					locked = kit.axeUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("bow")){
					upgrades = kit.bowUpgrades;
					prices = kit.bowUpgradesPrice;
					requirements = kit.bowRequirements;
					locked = kit.bowUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("boots")){
					upgrades = kit.bootsUpgrades;
					prices = kit.bootsUpgradesPrice;
					requirements = kit.bootsRequirements;
					locked = kit.bootsUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("leggings")){
					upgrades = kit.leggingsUpgrades;
					prices = kit.leggingsUpgradesPrice;
					requirements = kit.leggingsRequirements;
					locked = kit.leggingsUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("chestplate")){
					upgrades = kit.chestplateUpgrades;
					prices = kit.chestplateUpgradesPrice;
					requirements = kit.chestplateRequirements;
					locked = kit.chestplateUpgradesDefaultLocked;
				} else if (tool.equalsIgnoreCase("helmet")){
					upgrades = kit.helmetUpgrades;
					prices = kit.helmetUpgradesPrice;
					requirements = kit.helmetRequirements;
					locked = kit.helmetUpgradesDefaultLocked;
				}else{
					return;
				}

				while(!doneUpgrading){
					if(Database.GetPlayerLoaded(playerName, kit) == true){
						if(!Database.GetKitLocked(playerName, kit)){
							if(upgrades.get(upgradeLevel) != null){ //Does upgrade level exist?
								if(upgradeLevel == Database.GetUpgradeLevel(playerName, kit, tool) + 1){ //Is the upgrade the next one?
									if(upgradeLevel <= kit.GetMaxUpgradeLevel(tool)){ //Is not at max upgrade level yet
										if(!Database.GetUpgradeLocked(playerName, kit, tool)){ //Upgrade is not locked
											int upgradePrice = prices.get(upgradeLevel);
											int currency = CoreDatabase.GetCurrency(playerName);
											if(currency >= upgradePrice){ //Can purchase?
												int arenaRanking = CoreDatabase.GetArenaRanking(player.getName(), 1);
												int rankRequired = requirements.get(upgradeLevel).get(0);
												if(arenaRanking >= rankRequired){
													arenaRanking = CoreDatabase.GetArenaRanking(player.getName(), 2);
													rankRequired = requirements.get(upgradeLevel).get(1);
													if(arenaRanking >= rankRequired){
														arenaRanking = CoreDatabase.GetArenaRanking(player.getName(), 3);
														rankRequired = requirements.get(upgradeLevel).get(2);
														if(arenaRanking >= rankRequired){
															CoreDatabase.ModifyCurrency(playerName, -upgradePrice);
															boolean nextLevelLocked = locked.get(upgradeLevel + 1);
															Database.SetUpgradeLevel(playerName, kit, tool, upgradeLevel, nextLevelLocked);
															player.sendMessage(ChatColor.RED + "Upgrade successful. " + ChatColor.GOLD + upgradePrice + ChatColor.RED + " has been taken from your account.");
														}else{
															player.sendMessage(ChatColor.RED + "Upgrade unsuccessful. You must have at least " + ChatColor.GOLD + rankRequired + ChatColor.RED + " rating in " + ChatColor.GOLD + "3v3" + ChatColor.RED + "!");
														}
													}else{
														player.sendMessage(ChatColor.RED + "Upgrade unsuccessful. You must have at least " + ChatColor.GOLD + rankRequired + ChatColor.RED + " rating in " + ChatColor.GOLD + "2v2" + ChatColor.RED + "!");
													}
												}else{
													player.sendMessage(ChatColor.RED + "Upgrade unsuccessful. You must have at least " + ChatColor.GOLD + rankRequired + ChatColor.RED + " rating in " + ChatColor.GOLD + "1v1" + ChatColor.RED + "!");
												}
											}else{
												player.sendMessage(ChatColor.GRAY + "> You don't have enough honor points!");
												player.sendMessage(ChatColor.GRAY + "> Purchase more at " + ChatColor.GOLD + "mcwarcraft.com");
											}
										}else{
											player.sendMessage(ChatColor.RED + "This upgrade is locked!");
										}
									}
								}else{
									if(upgradeLevel > Database.GetUpgradeLevel(playerName, kit, tool) + 1){
										player.sendMessage(ChatColor.RED + "You must own ALL previous upgrades to purchase this!");
									}else{
										player.sendMessage(ChatColor.RED + "You already have that upgrade!");
									}
								}
							}else{
								player.sendMessage(ChatColor.RED + "That upgrade level does not exist!");
							}
						}else{
							player.sendMessage(ChatColor.RED + "This kit is locked!");
						}
						doneUpgrading = true;
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
}