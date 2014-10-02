package core.EngagementTracker;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDisengageRunnable extends BukkitRunnable
{
	private UUID playerUUID;
	
	public PlayerDisengageRunnable(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}
	
	@Override
	public void run()
	{
		PlayerEngageListener.disengagePlayer(playerUUID);
	}
}
