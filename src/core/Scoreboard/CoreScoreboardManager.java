package core.Scoreboard;

import java.util.HashMap;

import org.bukkit.entity.Player;

import core.CorePlugin;

public class CoreScoreboardManager
{
	private static CorePlugin plugin;
	private static HashMap<String, DisplayBoard> boards = new HashMap<String, DisplayBoard>();
	
	public static void initialize(CorePlugin plugin)
	{
		CoreScoreboardManager.plugin = plugin;
	}
	
	public static DisplayBoard getDisplayBoard(Player player)
	{
		if (boards.get(player.getName()) == null)
			boards.put(player.getName(), new DisplayBoard(player, plugin));
		return boards.get(player.getName());
	}
	
	public static void removeBoard(String playerName)
	{
		boards.remove(playerName);
	}
	
	public static DisplayBoard getDisplayBoard(String player)
	{
		return boards.get(player);
	}
}