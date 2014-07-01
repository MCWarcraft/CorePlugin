package randy.core.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import randy.kits.CustomKit;
import randy.kits.Kits;
import austin.CavemanSQL.DatabaseConnection;
import austin.CavemanSQL.DatabaseQueryAction;
import austin.CavemanSQL.DatabaseUpdateAction;

public class CoreSQL {
	
	private static MySQL database;
	private static DatabaseConnection connection;
	//private static Statement statement;
	
	public static void ConnectToSQL() throws SQLException{
		if(connection == null || database == null){
			//ArrayList<String> databaseOptions = config.databaseOptions;
			database = new MySQL(Bukkit.getPluginManager().getPlugin("CorePlugin"), "66.85.144.162",  "3306",  "mcph152586",  "mcph152586",  "edcaaba01e");
			connection = database.openConnection();
		}
	}
	
	public static void BreakConnection(){
		/*database.closeConnection();
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
	
	private static String GetTableName(String gamemode){
			if(gamemode.equals("ffa")) return "ffa_player_data";
			if(gamemode.equals("tower")) return "ffaextra_player_data";
			if(gamemode.equals("minigame")) return "minigame_player_data";
		return null;
	}
	
	public static void SetKills(String player, String gamemode, int kills){
		String tablename = GetTableName(gamemode);
		CheckTable(tablename);
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction(tablename);
		action.setPrimaryValue(player);
		action.setInt("kills", kills);
		action.executeUpdate();
	}
	
	public static void SetDeaths(String player, String gamemode, int deaths){
		String tablename = GetTableName(gamemode);
		CheckTable(tablename);
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction(tablename);
		action.setPrimaryValue(player);
		action.setInt("deaths", deaths);
		action.executeUpdate();
	}
	
	public static int GetKills(String player, String gamemode){
		String tablename = GetTableName(gamemode);
		CheckTable(tablename);
		DatabaseQueryAction action = connection.getDatabaseQueryAction(tablename);
		action.setFields("kills");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static int GetDeaths(String player, String gamemode){
		String tablename = GetTableName(gamemode);
		CheckTable(tablename);
		DatabaseQueryAction action = connection.getDatabaseQueryAction(tablename);
		action.setFields("deaths");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static int GetArenaRanking(String player, int playeramount){
		DatabaseQueryAction action = connection.getDatabaseQueryAction("player_data");
		action.setFields("rating"+playeramount);
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static void SetArenaRanking(String player, int playeramount, int ranking){
		CheckTable("player_data");
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction("player_data");
		action.setPrimaryValue("rating"+playeramount);
		action.setInt(player+"playeramount", ranking);
		action.executeUpdate();
	}
	
	public static void SetTowerKitGrabs(String player, int amount){
		CheckTable("ffaextra_player_data");
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction("ffaextra_player_data");
		action.setPrimaryValue(player);
		action.setInt("towerkitgrabs", amount);
		action.executeUpdate();
	}
	
	public static int GetTowerKitGrabs(String player){
		CheckTable("ffaextra_player_data");
		DatabaseQueryAction action = connection.getDatabaseQueryAction("ffaextra_player_data");
		action.setFields("towerkitgrabs");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static Integer GetKitUpgradeLevel(String player, CustomKit kit, String tool){
		
		CheckTable("ffa_kit_" + kit.kitname.toLowerCase());
		
		DatabaseQueryAction action = connection.getDatabaseQueryAction("ffa_kit_" + kit.kitname.toLowerCase());
		action.setFields(tool.toLowerCase()+"level");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return Integer.parseInt(result.getString(1).split("-")[0]);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		
		return 0;
	}
	
	public static void SetKitUpgradeLevel(String player, CustomKit kit, String tool, int level, boolean nextLevelLocked){		
		CheckTable("ffa_kit_" + kit.kitname.toLowerCase());
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction("ffa_kit_" + kit.kitname.toLowerCase());
		action.setPrimaryValue(player);
		action.setString(tool.toLowerCase() + "level", level+"-"+nextLevelLocked);
		action.executeUpdate();
		
		
	}
	
	public static boolean GetKitUpgradeLocked(String player, CustomKit kit, String tool){
		CheckTable("ffa_kit_" + kit.kitname.toLowerCase());
		
		DatabaseQueryAction action = connection.getDatabaseQueryAction("ffa_kit_" + kit.kitname.toLowerCase());
		action.setFields(tool.toLowerCase()+"level");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return Boolean.parseBoolean(result.getString(1).split("-")[1]);
		} catch (SQLException e) {
			return kit.GetDefaultLockedUpgrade(tool, 1);
			//e.printStackTrace();
		}
	}
	
	public static void SetKitUpgradeLocked(String player, CustomKit kit, String tool, boolean locked){
		CheckTable("ffa_kit_" + kit.kitname.toLowerCase());
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction("ffa_kit_" + kit.kitname.toLowerCase());
		action.setPrimaryValue(player);
		action.setString(tool.toLowerCase() + "level", GetKitUpgradeLevel(player,kit,tool)+"-"+locked);
		action.executeUpdate();
	}
	
	public static boolean GetKitLocked(String player, CustomKit kit){
		String tableName = null;
		if(kit.gameMode.equals("ffa")){
			tableName = "ffa_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("lms")){
			tableName = "lms_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("koth")){
			tableName = "koth_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("tower")){
			tableName = "tower_kit_" + kit.kitname.toLowerCase();
		}
		CheckTable(tableName);
		
		DatabaseQueryAction action = connection.getDatabaseQueryAction(tableName);
		action.setFields("unlockedkit");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return Boolean.parseBoolean(result.getString(1).split("-")[1]);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public static void SetKitLocked(String player, CustomKit kit, boolean locked){
		String tableName = null;
		if(kit.gameMode.equals("ffa")){
			tableName = "ffa_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("lms")){
			tableName = "lms_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("koth")){
			tableName = "koth_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("tower")){
			tableName = "tower_kit_" + kit.kitname.toLowerCase();
		}
		CheckTable(tableName);
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction(tableName);
		action.setPrimaryValue(player);
		action.setString("unlockedkit", GetKitLocked(player,kit)+"-"+locked);
		action.executeUpdate();
	}
	
	public static boolean GetKitPurchased(String player, CustomKit kit){
		String tableName = null;
		if(kit.gameMode.equals("ffa")){
			tableName = "ffa_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("lms")){
			tableName = "lms_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("koth")){
			tableName = "koth_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("tower")){
			tableName = "tower_kit_" + kit.kitname.toLowerCase();
		}
		CheckTable(tableName);
		
		DatabaseQueryAction action = connection.getDatabaseQueryAction(tableName);
		action.setFields("unlockedkit");
		action.addConstraint("player", player);
		ResultSet result = action.executeQuery();
		try {
			result.next();
			return Boolean.parseBoolean(result.getString(1).split("-")[0]);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public static void SetKitPurchased(String player, CustomKit kit, boolean purchased){
		String tableName = null;
		if(kit.gameMode.equals("ffa")){
			tableName = "ffa_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("lms")){
			tableName = "lms_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("koth")){
			tableName = "koth_kit_" + kit.kitname.toLowerCase();
		}else if(kit.gameMode.equals("tower")){
			tableName = "tower_kit_" + kit.kitname.toLowerCase();
		}
		CheckTable(tableName);
		
		DatabaseUpdateAction action = connection.getDatabaseUpdateAction(tableName);
		action.setPrimaryValue(player);
		action.setString("unlockedkit", purchased+"-"+GetKitPurchased(player,kit));
		action.executeUpdate();
	}
	
	public static void CheckTable(String tablename){
		try {
			ConnectToSQL();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement openPlayerDataStatement;
		try {
			if(tablename.equals("ffa_player_data") || tablename.equals("minigame_player_data")){
				openPlayerDataStatement = 
						database
						.connection
						.prepareStatement("CREATE TABLE IF NOT EXISTS " 
						+ tablename +
						"( player varchar(100) not null," +
							"kills int DEFAULT 0," +
							"deaths int DEFAULT 0," +
							"PRIMARY KEY (player) " +
						")");		
				openPlayerDataStatement.execute();
				openPlayerDataStatement.close();
			}
			
			if(tablename.equals("ffaextra_player_data")){
				openPlayerDataStatement = database.connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablename +
						"( player varchar(100) not null," +
							"kills int DEFAULT 0," +
							"deaths int DEFAULT 0," +
							"towerkitgrabs int DEFAULT 0," +
							"PRIMARY KEY (player) " +
						")");		
				openPlayerDataStatement.execute();
				openPlayerDataStatement.close();
			}
			
			if(tablename.contains("_kit_")){
				String gamemode = tablename.split("_")[0];
				CustomKit kit = Kits.getKitByName(tablename.split("_")[2], gamemode);
				if(gamemode.equals("ffa")){
					openPlayerDataStatement = database.connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablename +
							"( player varchar(100) not null," +
								"swordlevel varchar(100) DEFAULT '0-"+kit.swordUpgradesDefaultLocked.get(1)+"'," +
								"axelevel varchar(100) DEFAULT '0-"+kit.axeUpgradesDefaultLocked.get(1)+"'," +
								"pickaxelevel varchar(100) DEFAULT '0-"+kit.pickaxeUpgradesDefaultLocked.get(1)+"'," +
								"bowlevel varchar(100) DEFAULT '0-"+kit.bowUpgradesDefaultLocked.get(1)+"'," +
								"leggingslevel varchar(100) DEFAULT '0-"+kit.leggingsUpgradesDefaultLocked.get(1)+"'," +
								"helmetlevel varchar(100) DEFAULT '0-"+kit.helmetUpgradesDefaultLocked.get(1)+"'," +
								"chestplatelevel varchar(100) DEFAULT '0-"+kit.chestplateUpgradesDefaultLocked.get(1)+"'," +
								"bootslevel varchar(100) DEFAULT '0-"+kit.bootsUpgradesDefaultLocked.get(1)+"'," +
								"potionlevel varchar(100) DEFAULT '0-true'," +
								"unlockedkit varchar(100) DEFAULT '" + kit.defaultpurchased + "-" + kit.defaultlocked + "'," + // First = bought, second = locked
								"PRIMARY KEY (player) " +
							")");
				
					openPlayerDataStatement.execute();
					openPlayerDataStatement.close();
				}
				
				if(gamemode.equals("koth") || gamemode.equals("lms") || gamemode.equals("tower")){
					openPlayerDataStatement = database.connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablename +
							"( player varchar(100) not null," +
								"unlockedkit varchar(100) DEFAULT '" + kit.defaultpurchased + "-" + kit.defaultlocked + "'," + // First = bought, second = locked
								"PRIMARY KEY (player) " +
							")");
				
					openPlayerDataStatement.execute();
					openPlayerDataStatement.close();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
