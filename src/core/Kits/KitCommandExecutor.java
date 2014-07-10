package core.Kits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import core.HonorPoints.DatabaseOperations;

public class KitCommandExecutor implements CommandExecutor
{
	private KitManager kitManager;
	
	public KitCommandExecutor(KitManager kitManager)
	{
		this.kitManager = kitManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can use kit commands.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equals("kit"))
		{
			if (!KitLockManager.canEquip(player.getName())) return true;
			
			//If no args are supplied
			if (args.length == 0)
			{
				//Equip kit
				kitManager.getEquippableKit(player).equip();
				player.sendMessage(ChatColor.GREEN + "You have equipped the kit '" + kitManager.getKitPlayer(player.getName()).getSelectedKit() + "'");
			}
			//If args are supplied
			else
			{
				//If the player is trying to buy a new kit
				if (args[0].equalsIgnoreCase("buy"))
				{
					//If the right number of args is supplied
					if (args.length == 2)
					{
						Kit kit = kitManager.getKit(args[1]);
						
						//If the kit being attempted exists
						if (kit != null)
						{
							int cost = kit.getCost();
							KitPlayer kitPlayer = kitManager.getKitPlayer(player.getName());
							
							//If the player doesn't already own the kit
							if (!kitPlayer.isKitUnlocked(args[1]))
							{
								//If the player can afford the kit
								if (DatabaseOperations.getCurrency(player) >= cost)
								{
									//Give them the kit
									DatabaseOperations.setCurrency(player, DatabaseOperations.getCurrency(player) - cost);
									kitPlayer.unlockKit(args[1]);
									
									player.sendMessage(ChatColor.GREEN + "You have successfully purchased the kit '" + args[1] + "'.");
								}
								else
									player.sendMessage(ChatColor.RED + args[1] + " costs " + cost + " Honor.");
								
							}
							//If the player already owns the kit
							else
								player.sendMessage(ChatColor.RED + "You already own " + args[1] + ".");
						}
						//If the kit doesn't exist
						else
							player.sendMessage(ChatColor.RED + "There is no kit called " + args[1] + ".");
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit buy <kitname>");
				}
				else if (args[0].equalsIgnoreCase("cost"))
				{
					//If the right number of args is supplied
					if (args.length == 2)
					{
						Kit kit = kitManager.getKit(args[1]);
						
						//If the kit being attempted exists
						if (kit != null)
						{
							player.sendMessage(ChatColor.GREEN + args[1] + " costs " + kit.getCost() + " honor.");
							player.sendMessage(kitManager.getKitPlayer(player.getName()).isKitUnlocked(args[1]) ? ChatColor.GREEN + "However, you already own it!" : ((kit.getCost() > DatabaseOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!"));
						}
						//If the kit doesn't exist
						else
							player.sendMessage(ChatColor.RED + "There is no kit called " + args[1] + ".");
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit cost <kitname>");
				}
				
				//If the player is trying to buy an upgrade
				if (args[0].equalsIgnoreCase("upgrade"))
				{
					//If the right number of args is supplied
					if (args.length == 3)
					{
						Kit kit = kitManager.getKit(args[1]);
						
						//If the kit being attempted exists
						if (kit != null)
						{
							KitPlayer kitPlayer = kitManager.getKitPlayer(player.getName());
							
							KitPiece piece = kitManager.kitPieceMap.get(args[2].toLowerCase());
							
							//If the kit piece is real
							if (piece != null)
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingUpgrades(args[1], piece))
								{
									int cost = kit.getPieceUpgradeCost(piece, kitPlayer.getPieceLevel(args[1], piece) + 1);
									
									//If the player can afford the upgrade
									if (DatabaseOperations.getCurrency(player) >= cost)
									{
										DatabaseOperations.setCurrency(player, DatabaseOperations.getCurrency(player) - cost);
										kitPlayer.upgradePiece(args[1], piece);

										player.sendMessage(ChatColor.GREEN + "You have successfully purchased an upgrade for the kit '" + args[1] + "'.");
									}
									//If the player can't afford the upgrade
									else
										player.sendMessage(ChatColor.RED + "The next upgrade costs " + cost + " Honor.");										
								}
								//If the kit being upgraded has no available upgrades
								else
									player.sendMessage(ChatColor.RED + "There are no remaining upgrades for " + piece.toString());
							}
							//If the piece is potion
							else if (args[2].equalsIgnoreCase("potion") || args[2].equalsIgnoreCase("potions"))
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingPotionUpgrades(args[1]))
								{
									int cost = kit.getPotionUpgradeCost(kitPlayer.getPotionLevel(args[1]) + 1);
									
									//If the player can afford the upgrade
									if (DatabaseOperations.getCurrency(player) >= cost)
									{
										DatabaseOperations.setCurrency(player, DatabaseOperations.getCurrency(player) - cost);
										kitPlayer.upgradePotionSet(args[1]);

										player.sendMessage(ChatColor.GREEN + "You have successfully purchased an upgrade for the kit '" + args[1] + "'.");
									}
									//If the player can't afford the upgrade
									else
										player.sendMessage(ChatColor.RED + "The next upgrade costs " + cost + " Honor.");										
								}
								//If the kit being upgraded has no available upgrades
								else
									player.sendMessage(ChatColor.RED + "There are no remaining potion upgrades");
							}
							//If the kit piece isn't real
							else
								player.sendMessage(ChatColor.RED + args[2] + " isn't a valid piece.");
						}
						//If the kit doesn't exist
						else
							player.sendMessage(ChatColor.RED + "There is no kit called " + args[1] + ".");
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit upgrade <kitname> <piece>");
				}
				
				//If the player is trying to check the cost of an upgrade
				if (args[0].equalsIgnoreCase("upgradecost"))
				{
					//If the right number of args is supplied
					if (args.length == 3)
					{
						Kit kit = kitManager.getKit(args[1]);
						
						//If the kit being attempted exists
						if (kit != null)
						{
							KitPlayer kitPlayer = kitManager.getKitPlayer(player.getName());
							
							KitPiece piece = kitManager.kitPieceMap.get(args[2].toLowerCase());
							
							//If the kit piece is real
							if (piece != null)
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingUpgrades(args[1], piece))
								{
									int cost = kit.getPieceUpgradeCost(piece, kitPlayer.getPieceLevel(args[1], piece) + 1);
									player.sendMessage(ChatColor.GREEN + "The next upgrade costs " + cost + " Honor.");
									player.sendMessage((cost > DatabaseOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!");
								}
								//If the kit being upgraded has no available upgrades
								else
									player.sendMessage(ChatColor.RED + "There are no remaining upgrades for " + piece.toString());
							}
							//If the piece is potion
							else if (args[2].equalsIgnoreCase("potion") || args[2].equalsIgnoreCase("potions"))
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingPotionUpgrades(args[1]))
								{
									int cost = kit.getPotionUpgradeCost(kitPlayer.getPotionLevel(args[1]) + 1);
									player.sendMessage(ChatColor.GREEN + "The next upgrade costs " + cost + " Honor.");
									player.sendMessage((cost > DatabaseOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!");
									
								}
								//If the kit being upgraded has no available upgrades
								else
									player.sendMessage(ChatColor.RED + "There are no remaining potion upgrades");
							}
							//If the kit piece isn't real
							else
								player.sendMessage(ChatColor.RED + args[2] + " isn't a valid piece.");
						}
						//If the kit doesn't exist
						else
							player.sendMessage(ChatColor.RED + "There is no kit called " + args[1] + ".");
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit upgradecost <kitname> <piece>");
				}
				//If the player is trying to select a kit
				else if (args[0].equalsIgnoreCase("select"))
				{
					//If the right number of args is supplied
					if (args.length == 2)
					{
						Kit kit = kitManager.getKit(args[1]);
						
						//If the kit being attempted exists
						if (kit != null)
						{
							KitPlayer kitPlayer = kitManager.getKitPlayer(player.getName());
							
							//If the player owns the kit in question
							if (kitPlayer.isKitUnlocked(args[1]))
							{
								kitPlayer.setSelectedKit(args[1]);
								player.sendMessage(ChatColor.GREEN + "You have equipped " + args[1]);
							}
							//If the player doesn't own the kit in question
							else
								player.sendMessage(ChatColor.RED + "You don't own " + args[1]);
						}
						//If the kit doesn't exist
						else
							player.sendMessage(ChatColor.RED + "There is no kit called " + args[1] + ".");
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit buy <kitname>");
				}
			}
			
			return true;
		}
		return false;
	}

}
