package core.HonorPoints;

import org.bukkit.Bukkit;

import core.Scoreboard.ScoreboardValue;

public class HonorConnector implements ScoreboardValue
{
	public String getScoreboardValue(String key)
	{
		return "" + DatabaseOperations.getCurrency(Bukkit.getOfflinePlayer(key));
	}
}
