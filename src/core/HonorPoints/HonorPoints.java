package core.HonorPoints;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import core.CorePlugin;

public class HonorPoints implements Listener
{	
	private CorePlugin plugin;
	
	public HonorPoints(CorePlugin plugin)
	{
		this.plugin = plugin;
		
		checkDatabase();
	}
	
	private synchronized void checkDatabase()
	{
		try
		{
			//Configure tables for arenas
			PreparedStatement openSinglesArenaDataStatement = plugin.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_currency_data" +
					"( uuid varchar(36) not null," +
						"currency int(10) DEFAULT 0," +
						"multiplier double(4, 2) DEFAULT 1," +
						"PRIMARY KEY (uuid) " +
					")");
			openSinglesArenaDataStatement.execute();
			openSinglesArenaDataStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void playerLoginEvent(PlayerLoginEvent event)
	{
		CurrencyOperations.addPlayer(event.getPlayer());
	}
}