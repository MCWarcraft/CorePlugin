package core.EngagementTracker;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import core.CorePlugin;

public class PlayerEngageListener implements Listener
{
	private static HashMap<UUID, BukkitTask> disengageTasks = new HashMap<UUID, BukkitTask>();
	private static HashMap<UUID, String> messages = new HashMap<UUID, String>();
	
	private static CorePlugin plugin;
	
	private static int engageTicks = 100;
	
	public PlayerEngageListener(CorePlugin plugin, int time)
	{
		PlayerEngageListener.plugin = plugin;
		engageTicks = time * 20;
		checkDatabase();
	}
	
	private void checkDatabase()
	{
		//Configure main player data table
		PreparedStatement openEngageStatement;
		try
		{
			openEngageStatement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS engagement_violations " +
					"( id int NOT NULL AUTO_INCREMENT," +
						"uuid varchar(36)," + 
						"message varchar(255)," +
						"timestamp varchar(255)," +
						"PRIMARY KEY (id)" +
					")");
			
		openEngageStatement.execute();
		openEngageStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void engagePlayer(UUID playerUUID, String message, int engageTicks)
	{
		if (disengageTasks.containsKey(playerUUID))
			disengageTasks.get(playerUUID).cancel();
		
		messages.put(playerUUID, message);
		
		disengageTasks.put(playerUUID, new PlayerDisengageRunnable(playerUUID).runTaskLater(plugin, engageTicks));
	}
	
	public static void disengagePlayer(UUID playerUUID)
	{
		if (disengageTasks.containsKey(playerUUID))
			disengageTasks.remove(playerUUID).cancel();
		
		messages.remove(playerUUID);
	}
	
	protected synchronized static boolean logViolation(UUID playerUUID)
	{
		if (!messages.containsKey(playerUUID)) return false;
		
		try
		{
			PreparedStatement logStatement = plugin.getConnection().prepareStatement("INSERT INTO engagement_violations SET uuid = ?, message = ?, timestamp = ?");
			logStatement.setString(1, playerUUID.toString());
			logStatement.setString(2, messages.get(playerUUID));
			logStatement.setString(3, new Timestamp(new Date().getTime()).toString());
			
			logStatement.execute();
			logStatement.close();
			
			disengagePlayer(playerUUID);
			
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void entityDamageEvent(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Player p = (Player) event.getEntity();
			engagePlayer(p.getUniqueId(), event.getCause().name(), engageTicks);
		}
	}
	
	@EventHandler
	public void onLogoff(PlayerQuitEvent event)
	{
		logViolation(event.getPlayer().getUniqueId());
	}
}