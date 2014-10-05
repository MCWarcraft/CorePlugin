package core.Kits;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import core.HonorPoints.CurrencyOperations;
import core.Scoreboard.CoreScoreboardManager;

public class KitCommandExecutor implements CommandExecutor
{
	private KitManager kitManager;

	private static DateFormat dateFormatter = new SimpleDateFormat("MMMMMMMMM d 'at' HH:mm");
	
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
		KitPlayer kitPlayer = kitManager.getKitPlayer(player.getUniqueId());

		if (cmd.getName().equalsIgnoreCase("kit"))
		{
			//If no args are supplied
			if (args.length == 0)
			{
				if (!KitLockManager.canEquip(player.getUniqueId()))
					player.sendMessage(ChatColor.RED + "You can't equip a kit right now.");
				else
				{
					//If the kit is on cooldown
					if (kitPlayer.isOnCooldown(kitPlayer.getSelectedKit()))
					{
						player.sendMessage(ChatColor.RED + kitPlayer.getSelectedKit() + " is on cooldown.");
						player.sendMessage(ChatColor.RED + "It will be available on "
						+ ChatColor.GOLD + dateFormatter.format(new Date(kitPlayer.getAvailableAtTime(kitPlayer.getSelectedKit()))) + " EST");
					}
					else
					{
						//Equip kit
						String equippedKitName = kitPlayer.getSelectedKit();
						kitManager.getEquippableKit(player).equip();
						player.sendMessage(ChatColor.GOLD + "You have equipped the " + ChatColor.GREEN + equippedKitName + ChatColor.GOLD + " kit");
					}
				}
			}
			//If args are supplied
			else
			{
				if (args[0].equalsIgnoreCase("confirm"))
				{
					if (args.length == 1)
					{
						if (!kitManager.getKitPurchaseConfirmer().finishPurchase(player.getUniqueId()))
							player.sendMessage(ChatColor.RED + "You don't have an open purchase to confirm.");
					}
					else
						player.sendMessage(ChatColor.RED + "/kit confirm");
				}

				//If the player is trying to buy a new kit
				else if (args[0].equalsIgnoreCase("buy"))
				{
					//If the right number of args is supplied
					if (args.length == 2)
					{
						Kit kit = kitManager.getKit(args[1]);

						//If the kit being attempted exists
						if (kit != null)
						{
							int cost = kit.getCost();

							//If the player doesn't already own the kit
							if (!kitPlayer.isKitUnlocked(args[1]))
							{
								//If the player can afford the kit
								if (CurrencyOperations.getCurrency(player) >= cost)
								{
									kitManager.getKitPurchaseConfirmer().openPurchase(new KitPurchase(kitPlayer, args[1], cost));
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
							player.sendMessage(kitPlayer.isKitUnlocked(args[1]) ? ChatColor.GREEN + "However, you already own it!" : ((kit.getCost() > CurrencyOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!"));
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
				else if (args[0].equalsIgnoreCase("upgrade"))
				{
					//If the right number of args is supplied
					if (args.length == 3)
					{
						Kit kit = kitManager.getKit(args[1]);

						//If the kit being attempted exists
						if (kit != null)
						{
							KitPiece piece = kitManager.kitPieceMap.get(args[2].toLowerCase());

							//If the kit piece is real
							if (piece != null)
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingUpgrades(args[1], piece))
								{
									int cost = kit.getPieceUpgradeCost(piece, kitPlayer.getPieceLevel(args[1], piece) + 1);

									//If the player can afford the upgrade
									if (CurrencyOperations.getCurrency(player) >= cost)
									{
										CurrencyOperations.setCurrency(player, CurrencyOperations.getCurrency(player) - cost);
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
									if (CurrencyOperations.getCurrency(player) >= cost)
									{
										CurrencyOperations.setCurrency(player, CurrencyOperations.getCurrency(player) - cost);
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
							//Else if it's another kit piece
							else if (kit.getItemNames().contains(args[2]))
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingItemUpgrades(args[1], args[2]))
								{
									int cost = kit.getItemUpgradeCost(args[2], kitPlayer.getItemLevel(args[1], args[2]) + 1);

									//If the player can afford the upgrade
									if (CurrencyOperations.getCurrency(player) >= cost)
									{
										CurrencyOperations.setCurrency(player, CurrencyOperations.getCurrency(player) - cost);
										kitPlayer.upgradeItem(args[1], args[2]);

										player.sendMessage(ChatColor.GREEN + "You have successfully purchased an upgrade for the kit '" + args[1] + "'.");
									}
									//If the player can't afford the upgrade
									else
										player.sendMessage(ChatColor.RED + "The next upgrade costs " + cost + " Honor.");										
								}
								//If the kit being upgraded has no available upgrades
								else
									player.sendMessage(ChatColor.RED + "There are no remaining upgrades for the piece '" + args[2] + "'");
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
				else if (args[0].equalsIgnoreCase("upgradecost"))
				{
					//If the right number of args is supplied
					if (args.length == 3)
					{
						Kit kit = kitManager.getKit(args[1]);

						//If the kit being attempted exists
						if (kit != null)
						{

							KitPiece piece = kitManager.kitPieceMap.get(args[2].toLowerCase());

							//If the kit piece is real
							if (piece != null)
							{
								//If the kit being upgraded has available upgrades
								if (kitPlayer.hasRemainingUpgrades(args[1], piece))
								{
									int cost = kit.getPieceUpgradeCost(piece, kitPlayer.getPieceLevel(args[1], piece) + 1);
									player.sendMessage(ChatColor.GREEN + "The next upgrade costs " + cost + " Honor.");
									player.sendMessage((cost > CurrencyOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!");
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
									player.sendMessage((cost > CurrencyOperations.getCurrency(player)) ? ChatColor.DARK_RED + "You can't afford it." : ChatColor.GREEN + "You can afford it!");

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
						//If the player owns the kit in question
						if (kitPlayer.setSelectedKit(args[1]))
						{
							player.sendMessage("" + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.GOLD + "You have selected the " + ChatColor.GREEN + args[1] + ChatColor.GOLD + " kit");
							CoreScoreboardManager.getDisplayBoard(player.getUniqueId()).update(false);
						}
						else if (kitPlayer.isOnCooldown(args[1]))
						{
							player.sendMessage(ChatColor.RED + args[1] + " is on cooldown.");
							player.sendMessage(ChatColor.RED + "It will be available on "
							+ ChatColor.GOLD + dateFormatter.format(new Date(kitPlayer.getAvailableAtTime(args[1]))) + " EST");
						}
						//If the player doesn't own the kit in question
						else
							player.sendMessage(ChatColor.RED + "You don't own " + args[1]);

					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit select <kitname>");
				}
				//If it's a direct equip
				else if (args[0].equalsIgnoreCase("equip"))
				{
					//If the right number of args is supplied
					if (args.length == 2)
					{
						//If the player owns the kit in question
						if (kitPlayer.setSelectedKit(args[1]))
							Bukkit.dispatchCommand(player, "kit");
						//If the player doesn't own the kit in question
						else
							player.sendMessage(ChatColor.RED + "You don't own " + args[1]);
					}
					//If the wrong number of args is supplied
					else
						player.sendMessage(ChatColor.RED + "/kit equip <kitname>");
				}
			}

			return true;
		}
		return false;
	}

}
