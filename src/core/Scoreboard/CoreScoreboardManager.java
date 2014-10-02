package core.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import core.CorePlugin;

public class CoreScoreboardManager
{
	private static CorePlugin plugin;
	private static HashMap<UUID, DisplayBoard> displayBoards = new HashMap<UUID, DisplayBoard>();
	
	public static void initialize(CorePlugin plugin)
	{
		CoreScoreboardManager.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(new CurrencyChangeListener(), plugin);
	}
	
	public static DisplayBoard getDisplayBoard(Player player)
	{
		if (displayBoards.get(player.getUniqueId()) == null)
			displayBoards.put(player.getUniqueId(), new DisplayBoard(player, plugin));
		return displayBoards.get(player.getUniqueId());
	}
	
	public static DisplayBoard getDisplayBoard(UUID playerUUID)
	{
		return displayBoards.get(playerUUID);
	}
	
	public static GameBoard getNewGameBoard()
	{
		return new GameBoard();
	}
	
	public static void removeDisplayBoard(UUID playerUUID)
	{
		displayBoards.remove(playerUUID);
	}
}