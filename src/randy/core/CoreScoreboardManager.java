package randy.core;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class CoreScoreboardManager
{
	private static HashMap<String, DisplayBoard> boards = new HashMap<String, DisplayBoard>();
	
	public static DisplayBoard getDisplayBoard(Player player)
	{
		if (boards.get(player.getName()) == null)
			boards.put(player.getName(), new DisplayBoard(player));
		return boards.get(player.getName());
	}
	
	public static void removeBoard(String playerName)
	{
		boards.get(playerName).resetBoard();
		boards.remove(boards);
	}
}
