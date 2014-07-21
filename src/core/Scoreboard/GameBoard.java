package core.Scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GameBoard
{
	private Scoreboard board;
	
	private Objective o;
	
	protected GameBoard(String title)
	{
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		o = board.registerNewObjective("test", "dummy");
		o.setDisplayName(title);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		o.setDisplayName(title);
	}
	
	protected GameBoard()
	{
		this("");
	}
	
	public void setScore(OfflinePlayer player, int score)
	{
		o.getScore(player).setScore(score);
	}
	
	public void assign(Player player)
	{
		player.setScoreboard(board);
	}
}