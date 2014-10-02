package core.HonorPoints;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import core.CorePlugin;
import core.Utilities.UUIDCache;

public class CurrencyOperations
{
	private static CorePlugin plugin;
	
	public static void initialize(CorePlugin plugin)
	{
		CurrencyOperations.plugin = plugin;
	}
	
	public static synchronized void addPlayer(OfflinePlayer p)
	{
		try
		{
			if (!isPlayerInTable(p))
			{
				PreparedStatement addPlayerStatement = plugin.getConnection().prepareStatement("INSERT INTO player_currency_data SET uuid = ?");
				addPlayerStatement.setString(1, p.getUniqueId().toString());
				addPlayerStatement.execute();
				addPlayerStatement.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized static boolean isPlayerInTable(OfflinePlayer player)
	{
		try
		{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM player_currency_data WHERE uuid = '" + player.getUniqueId() +"'");
			ResultSet result = statement.executeQuery();
			boolean isInRow = result.next();
			
			statement.close();
			result.close();
			
			return isInRow;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static synchronized int getCurrency(OfflinePlayer player)
	{
		try
		{
			PreparedStatement getPlayerDataStatement = plugin.getConnection().prepareStatement("SELECT currency FROM player_currency_data WHERE uuid = ?");
			getPlayerDataStatement.setString(1, player.getUniqueId().toString());
			
			ResultSet result = getPlayerDataStatement.executeQuery();
			
			if (result.next())
				return result.getInt("currency");
			return -1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static synchronized void setCurrency(OfflinePlayer player, int currency)
	{
		try
		{
			PreparedStatement setPlayerDataStatement = plugin.getConnection().prepareStatement("UPDATE player_currency_data SET currency = ? WHERE uuid = ?");
			setPlayerDataStatement.setInt(1, currency);
			setPlayerDataStatement.setString(2, player.getUniqueId().toString());

			setPlayerDataStatement.executeUpdate();
			setPlayerDataStatement.close();
			
			if (UUIDCache.getPlayerUUID(player.getName()) != null)
				Bukkit.getServer().getPluginManager().callEvent(new OnlinePlayerCurrencyUpdateEvent(Bukkit.getServer().getPlayer(UUIDCache.getPlayerUUID(player.getName()))));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static synchronized void setMultiplier(OfflinePlayer player, double multiplier)
	{
		try
		{
			PreparedStatement setPlayerDataStatement = plugin.getConnection().prepareStatement("UPDATE player_currency_data SET multiplier = ? WHERE uuid = ?");
			setPlayerDataStatement.setDouble(1, multiplier);
			setPlayerDataStatement.setString(2, player.getUniqueId().toString());

			setPlayerDataStatement.executeUpdate();
			setPlayerDataStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static synchronized double getMultiplier(OfflinePlayer player)
	{
		try
		{
			PreparedStatement getPlayerDataStatement = plugin.getConnection().prepareStatement("SELECT multiplier FROM player_currency_data WHERE uuid = ?");
			getPlayerDataStatement.setString(1, player.getUniqueId().toString());
			
			ResultSet result = getPlayerDataStatement.executeQuery();
			
			if (result.next())
				return result.getInt("multiplier");
			return -1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static synchronized void giveCurrency(OfflinePlayer player, int currency, boolean multiplier)
	{
		if (multiplier){
			setCurrency(player, getCurrency(player) + (int)(currency * getMultiplier(player)));
		}else{
			setCurrency(player, getCurrency(player) + currency);
		}
	}
}
