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
	
	protected GameBoard()
	{
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		o = board.registerNewObjective("test", "dummy");
		o.setDisplayName("");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void setTitle(String title)
	{
		o.setDisplayName(title);
	}
	
	public void setScore(OfflinePlayer player, int score)
	{
		o.getScore(player.getName()).setScore(score);
	}
	
	public void assign(Player player)
	{
		player.setScoreboard(board);
	}
}