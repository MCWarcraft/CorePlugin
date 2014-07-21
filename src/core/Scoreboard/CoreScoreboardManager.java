package core.Scoreboard;

import java.util.HashMap;

import org.bukkit.entity.Player;

import core.CorePlugin;

public class CoreScoreboardManager
{
	private static CorePlugin plugin;
	private static HashMap<String, DisplayBoard> displayBoards = new HashMap<String, DisplayBoard>();
	
	public static void initialize(CorePlugin plugin)
	{
		CoreScoreboardManager.plugin = plugin;
	}
	
	public static DisplayBoard getDisplayBoard(Player player)
	{
		if (displayBoards.get(player.getName()) == null)
			displayBoards.put(player.getName(), new DisplayBoard(player, plugin));
		return displayBoards.get(player.getName());
	}
	
	public static DisplayBoard getDisplayBoard(String player)
	{
		return displayBoards.get(player);
	}
	
	public static GameBoard getNewGameBoard()
	{
		return new GameBoard();
	}
	
	public static void removeDisplayBoard(String playerName)
	{
		displayBoards.remove(playerName);
	}
}