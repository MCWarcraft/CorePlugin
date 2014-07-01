package randy.core.tools;

import core.Scoreboard.ScoreboardValue;

public class CoreDatabaseConnector implements ScoreboardValue
{
	@Override
	public String getScoreboardValue(String key)
	{
		String[] splitkey = key.split("\\|");
		if (splitkey.length != 2) return "err1";
		
		switch (splitkey[1])
		{
		case "kills":
			return "" + CoreDatabase.GetTotalKills(splitkey[0]);
		case "deaths":
			return "" + CoreDatabase.GetTotalDeaths(splitkey[0]);
		case "honor":
			return "" + CoreDatabase.GetCurrency(splitkey[0]);
		default:
			return "err2";
		}
	}
	
}
