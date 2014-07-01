package bourg.austin.HonorPoints;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HonorPoints
{	
	private Connection connection;
	
	String ip, port, database, username, password;
	
	public HonorPoints(String ip, String port, String database, String username, String password)
	{
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		
		openConnection();
		checkDatabase();
		
		DatabaseOperations.initialize(this);
	}
	
	public synchronized boolean openConnection()
	{
		String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + database;
		
		try
		{
			connection = DriverManager.getConnection(connectionString, username, password);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public synchronized boolean closeConnection()
	{		
		try
		{
			connection.close();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	private synchronized void checkDatabase()
	{
		try
		{
			//Configure tables for arenas
			PreparedStatement openSinglesArenaDataStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_currency_data" +
					"( player varchar(17) not null," +
						"currency int(10) DEFAULT 0," +
						"multiplier double(4, 2) DEFAULT 1," +
						"PRIMARY KEY (player) " +
					")");
			openSinglesArenaDataStatement.execute();
			openSinglesArenaDataStatement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection()
	{
		return connection;
	}
}