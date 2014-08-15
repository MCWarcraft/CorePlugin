package core.EngagementTracker;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

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
	private static HashMap<String, BukkitTask> disengageTasks = new HashMap<String, BukkitTask>();
	private static HashMap<String, String> messages = new HashMap<String, String>();
	
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
						"player varchar(17)," + 
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
	
	public static void engagePlayer(String playerName, String message, int engageTicks)
	{
		if (disengageTasks.containsKey(playerName))
			disengageTasks.get(playerName).cancel();
		
		messages.put(playerName, message);
		
		disengageTasks.put(playerName, new PlayerDisengageRunnable(playerName).runTaskLater(plugin, engageTicks));
	}
	
	public static void disengagePlayer(String playerName)
	{
		if (disengageTasks.containsKey(playerName))
			disengageTasks.remove(playerName).cancel();
		
		messages.remove(playerName);
	}
	
	protected synchronized static boolean logViolation(String playerName)
	{
		if (!messages.containsKey(playerName)) return false;
		
		try
		{
			PreparedStatement logStatement = plugin.getConnection().prepareStatement("INSERT INTO engagement_violations SET player = ?, message = ?, timestamp = ?");
			logStatement.setString(1, playerName);
			logStatement.setString(2, messages.get(playerName));
			logStatement.setString(3, new Timestamp(new Date().getTime()).toString());
			
			logStatement.execute();
			logStatement.close();
			
			disengagePlayer(playerName);
			
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
			engagePlayer(p.getName(), event.getCause().name(), engageTicks);
		}
	}
	
	@EventHandler
	public void onLogoff(PlayerQuitEvent event)
	{
		logViolation(event.getPlayer().getName());
	}
}