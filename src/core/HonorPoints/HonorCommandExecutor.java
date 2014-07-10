package core.HonorPoints;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HonorCommandExecutor implements CommandExecutor
{	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{		
		if (cmd.getName().equalsIgnoreCase("honor"))
		{
			//If there are 3 arguments
			if (args.length >= 1)
			{
				//If the player is trying to set and has permission to do so
				if (args[0].equalsIgnoreCase("set") && sender.hasPermission("honor.manipulate"))
				{
					if (args.length == 3)
					{
						//If the player being manipulated is in the database
						if (DatabaseOperations.isPlayerInTable(Bukkit.getOfflinePlayer(args[1])))
						{
							int amount = -1;
							//Try to format the value
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							//Catch a bad number
							catch (NumberFormatException e)
							{
								sender.sendMessage(ChatColor.RED + "The amount of currency to set must be a valid integer");
								return true;
							}
							//If the number is positive or 0
							if (amount >= 0)
							{
								DatabaseOperations.setCurrency(Bukkit.getOfflinePlayer(args[1]), amount);
								sender.sendMessage(ChatColor.BLUE + "" + args[1] + "" + ChatColor.GREEN + " now has " + ChatColor.BLUE + DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) + ChatColor.GREEN + " honor");
							}
							//If the number is negative
							else
								sender.sendMessage(ChatColor.RED + "The amount of currency must be positive");
						}
						//If there is no record of the player being requested
						else
							sender.sendMessage(ChatColor.RED + "There is no record of a player named " + args[1]);
					}
					else if (sender.hasPermission("honor.manipulate"))
						sender.sendMessage(ChatColor.RED + "/honor set <player> <amount>");
				}
				
				//If the player is trying to add and has permission to do so
				else if ((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")) && sender.hasPermission("honor.manipulate"))
				{
					if (args.length == 3)
					{
						//If the player being manipulated is in the database
						if (DatabaseOperations.isPlayerInTable(Bukkit.getOfflinePlayer(args[1])))
						{
							int amount = -1;
							//Try to format the value
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							//Catch a bad number
							catch (NumberFormatException e)
							{
								sender.sendMessage(ChatColor.RED + "The amount of currency to add must be a valid integer");
								return true;
							}
							//If the number is positive or 0
							if (amount >= 0)
							{
								DatabaseOperations.setCurrency(Bukkit.getOfflinePlayer(args[1]), DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) + amount);
								sender.sendMessage(ChatColor.BLUE + "" + args[1] + "" + ChatColor.GREEN + " now has " + ChatColor.BLUE + DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) + ChatColor.GREEN + " honor after addition");
							}
							//If the number is negative
							else
							{
								sender.sendMessage(ChatColor.RED + "The amount of currency must be positive");
							}
						}
						//If there is no record of the player being requested
						else
							sender.sendMessage(ChatColor.RED + "There is no record of a player named " + args[1]);
					}
					else if (sender.hasPermission("honor.manipulate"))
						sender.sendMessage(ChatColor.RED + "/honor (add/give) <player> <amount>");
				}
				
				//If the player is trying to deduct and has permission to do so
				else if ((args[0].equalsIgnoreCase("deduct") || args[0].equalsIgnoreCase("take")) && sender.hasPermission("honor.manipulate"))
				{
					if (args.length == 3)
					{
						//If the player being manipulated is in the database
						if (DatabaseOperations.isPlayerInTable(Bukkit.getOfflinePlayer(args[1])))
						{
							int amount = -1;
							//Try to format the value
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							//Catch a bad number
							catch (NumberFormatException e)
							{
								sender.sendMessage(ChatColor.RED + "The amount of currency to set must be a valid integer");
								return true;
							}
							//If the number is positive or 0
							if (amount >= 0)
							{
								//If the player can afford the deduction
								if (DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) - amount >= 0)
								{
									DatabaseOperations.setCurrency(Bukkit.getOfflinePlayer(args[1]), DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) - amount);
									sender.sendMessage(ChatColor.BLUE + "" + args[1] + "" + ChatColor.GREEN + " now has " + ChatColor.BLUE + DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) + ChatColor.GREEN + " honor after deduction");
								}
								//If the player can't afford the deduction
								else
									sender.sendMessage(ChatColor.RED + "The player can't afford this deduction");
							}
							//If the number is negative
							else
							{
								sender.sendMessage(ChatColor.RED + "The amount of currency must be positive");
							}
						}
						//If there is no record of the player being requested
						else
							sender.sendMessage(ChatColor.RED + "There is no record of a player named " + args[1]);
					}
					else if (sender.hasPermission("honor.manipulate"))
						sender.sendMessage(ChatColor.RED + "/honor (deduct/take) <player> <amount>");
				}
				else if ((args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("see")) && sender.hasPermission("honor.view.other"))
				{
					if (args.length == 2)
					{
						//If the player being manipulated is in the database
						if (DatabaseOperations.isPlayerInTable(Bukkit.getOfflinePlayer(args[1])))
							sender.sendMessage(ChatColor.BLUE + "" + args[1] + "" + ChatColor.GREEN + " has " + ChatColor.BLUE + DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(args[1])) + ChatColor.GREEN + " honor");
						//If there is no record of the player being requested
						else
							sender.sendMessage(ChatColor.RED + "There is no record of a player named " + args[1]);
					}
					else if (sender.hasPermission("honor.view.other"))
						sender.sendMessage(ChatColor.RED + "/honor (view/see) <player>");
				}
				//If the sender is trying to set the multipler
				else if (args[0].equalsIgnoreCase("setmultiplier"))
				{

						//If there are enough args
						if (args.length == 3)
						{
							//If the player being manipulated is in the database
							if (DatabaseOperations.isPlayerInTable(Bukkit.getOfflinePlayer(args[1])))
							{
								double multiplier = -1;
								try {multiplier = Double.parseDouble(args[2]);} catch (NumberFormatException e){sender.sendMessage(ChatColor.RED + "The multiplier must be a valid double."); return true;}
								//If the multiplier is bigger than 0
								if (multiplier > 0)
								{
									DatabaseOperations.setMultiplier(Bukkit.getOfflinePlayer(args[1]), multiplier);
									sender.sendMessage(args[1] + ChatColor.GREEN + "'s multiplier is now set to " + ChatColor.WHITE + args[2] + ".");
								}
								//If the multiplier is negative
								else
									sender.sendMessage(ChatColor.RED + "The multiplier must be positive.");
							}
							//If there is no record of the player being requested
							else
								sender.sendMessage(ChatColor.RED + "There is no record of a player named " + args[1]);
						}
						//If the number of args is wrong
						else
							sender.sendMessage(ChatColor.RED + "/honor setmultiplier <player> <multiplier>");
				}
				return true;
			}
			//If the root command has been called by a player
			else if (sender instanceof Player && sender.hasPermission("honor.view.self"))
			{
				sender.sendMessage(ChatColor.GREEN + "You have " + ChatColor.BLUE + DatabaseOperations.getCurrency((Player) sender) + ChatColor.GREEN + " honor");
				return true;
			}
			//If the root command has been called by the console
			else if (!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "Only players can use the root command for /honor");
				return true;
			}
		}
		return false;
	}
}
