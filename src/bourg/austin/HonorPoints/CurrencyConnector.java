package bourg.austin.HonorPoints;

import org.bukkit.OfflinePlayer;

public class CurrencyConnector
{
	public CurrencyConnector()
	{
		
	}
	
	public int getPlayerCurrency(OfflinePlayer player)
	{
		return DatabaseOperations.getCurrency(player);
	}
	
	public boolean canAfford(OfflinePlayer player, int cost)
	{
		return DatabaseOperations.getCurrency(player) >= cost;
	}
	
	public boolean giveCurrency(OfflinePlayer player, int amount)
	{
		if (amount < 0)
			return false;
		DatabaseOperations.setCurrency(player, DatabaseOperations.getCurrency(player) + amount);
		return true;
	}
	
	public boolean deductCurrency(OfflinePlayer player, int amount)
	{
		if (!canAfford(player, amount))
			return false;
		DatabaseOperations.setCurrency(player, DatabaseOperations.getCurrency(player) - amount);
		return true;
	}
}
