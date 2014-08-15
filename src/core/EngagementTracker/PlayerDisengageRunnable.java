package core.EngagementTracker;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDisengageRunnable extends BukkitRunnable
{
	private String playerName;
	
	public PlayerDisengageRunnable(String playerName)
	{
		this.playerName = playerName;
	}
	
	@Override
	public void run()
	{
		PlayerEngageListener.disengagePlayer(playerName);
	}
}
