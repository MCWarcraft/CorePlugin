package core.Kits;

import core.Scoreboard.ScoreboardValue;

public class KitScoreboardConnector implements ScoreboardValue
{
	private static KitManager manager;
	
	public static void initialize(KitManager manager)
	{
		KitScoreboardConnector.manager = manager;
	}
	
	@Override
	public String getScoreboardValue(String key)
	{
		return manager.getKitPlayer(key).getSelectedKit().toUpperCase();
	}
}
