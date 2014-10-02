package core.Kits;

import java.util.UUID;

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
		return manager.getKitPlayer(UUID.fromString(key)).getSelectedKit().toUpperCase();
	}
}
