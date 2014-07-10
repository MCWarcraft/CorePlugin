package core.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import core.CorePlugin;

public class DisplayBoard
{
	private Player player;
	
	private Scoreboard board;
	private Objective o;
	private HashMap<OfflinePlayer, Score> values;
	private ArrayList<String> titles, fixedValues;
	private ArrayList<ScoreboardValue> dynamicValues;
	private ArrayList<String> dynamicKeys;
	private ChatColor scoreColor;
	private String showTitle, steadyTitle, scrollableTitle;
	private boolean showing;
	private CorePlugin plugin;
	
	//Scrolling
	private int stringSize = 16;
	private String scrolled = "";
	private int currentIndex = 0;
	
	private BukkitTask scrollTask;
	
	
	protected DisplayBoard(Player player, String title, ChatColor scoreColor, CorePlugin plugin)
	{
		this.player = player;	
		this.scoreColor = scoreColor;
		this.plugin = plugin;
		
		showTitle = "";
		resetBoard();
		
		setTitle(title, "");
		
		values = new HashMap<OfflinePlayer, Score>();
		titles = new ArrayList<String>();
		fixedValues = new ArrayList<String>();
		dynamicValues = new ArrayList<ScoreboardValue>();
		dynamicKeys = new ArrayList<String>();
		showing = false;
		
	}
	
	protected DisplayBoard(Player player, CorePlugin plugin)
	{
		this(player, "", ChatColor.RESET, plugin);
	}
	
	public void putField(String title, String value)
	{
		titles.add(title);
		fixedValues.add(value);
		dynamicValues.add(null);
		dynamicKeys.add(null);
	}
	
	public void putField(String title, int value)
	{
		putField(title, "" + value);
	}
	
	public void putField(String title, ScoreboardValue value, String key)
	{
		titles.add(title);
		fixedValues.add(null);
		dynamicValues.add(value);
		dynamicKeys.add(key);
	}
	
	public void putHeader(String text)
	{
		titles.add(text);
		fixedValues.add(null);
		dynamicValues.add(null);
		dynamicKeys.add(null);
	}
	
	public void putSpace()
	{
		titles.add(" ");
		fixedValues.add(null);
		dynamicValues.add(null);
		dynamicKeys.add(null);
	}
	
	public void update(boolean forceshow)
	{
		resetBoard();
		ArrayList<String> finalStrings = padDuplicates(constructLines());
		values.clear();
		
		for (int i = 0; i < finalStrings.size(); i++)
		{
			values.put(Bukkit.getOfflinePlayer(finalStrings.get(i)), o.getScore(Bukkit.getOfflinePlayer(finalStrings.get(i))));
			values.get(Bukkit.getOfflinePlayer(finalStrings.get(i))).setScore(finalStrings.size() - i);
		}
		if (forceshow || showing)
			show();
	}
	
	public void hide()
	{
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		showing = false;
	}
	
	public void show()
	{
		player.setScoreboard(board);
		showing = true;
	}
	
	private String constructLine(int line)
	{		
		//If dynamic value
		if (fixedValues.get(line) == null && dynamicValues.get(line) != null)
		{
			return titles.get(line) + scoreColor + dynamicValues.get(line).getScoreboardValue(dynamicKeys.get(line));
		}
		//If static value
		else if (dynamicValues.get(line) == null && fixedValues.get(line) != null)
			return titles.get(line) + scoreColor + fixedValues.get(line);
		//If header
		else if (dynamicValues.get(line) == null && fixedValues.get(line) == null && titles.get(line) != null)
			return titles.get(line);
		//Else if it's just a space
		else
			return " ";
	}
	
	private ArrayList<String> constructLines()
	{
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 0; i < titles.size(); i++)
			lines.add(constructLine(i));
		return lines;
	}
	
	private ArrayList<String> padDuplicates(ArrayList<String> original)
	{
		ArrayList<String> padded = new ArrayList<String>();
		for (int i = 0; i < original.size(); i++)
		{
			/*
			padded.add(original.get(i) + StringUtils.repeat(" ", Collections.frequency(original.subList(0, i), original.get(i))));
			if (padded.get(padded.size() - 1).length() > 16)
				padded.remove(padded.size() - 1);		
			*/
			padded.add((original.get(i) + StringUtils.repeat(" ", 16 - original.get(i).length())).substring(0, 16 - Collections.frequency(original.subList(0, i), original.get(i))));
		}
		return padded;		
	}
	
	public void setScoreColor(ChatColor color)
	{
		scoreColor = color;
	}
	
	public void setTitle(String scrollable, String steady)
	{
		try {scrollTask.cancel();} catch (NullPointerException e){}
		
		if (scrollable.length() + steady.length() <= 16)
			showTitle = steady + scrollable;	
		else
		{
			steadyTitle = steady;
			scrollableTitle = scrollable + "     ";
			
			stringSize = 16 - steadyTitle.length();
			scroll();
			
			scrollTask = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					scroll();
				}
			}.runTaskTimer(plugin, 18, 4);
		}
	}

	private void resetBoard()
	{
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		o = board.registerNewObjective("test", "dummy");
		o.setDisplayName(showTitle);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void resetFormat()
	{		
		values.clear();
		titles.clear();
		fixedValues.clear();
		dynamicValues.clear();
		dynamicKeys.clear();
		scoreColor = ChatColor.RESET;
		stringSize = 16;
		scrolled = "";
		currentIndex = 0;
		hide();
	}

	private void scroll()
	{
		if (currentIndex == scrollableTitle.length()) currentIndex = 0;
		if (currentIndex + stringSize <= scrollableTitle.length())
			scrolled = scrollableTitle.substring(currentIndex, currentIndex + stringSize);
		else
			scrolled = scrollableTitle.substring(currentIndex, scrollableTitle.length()) + scrollableTitle.substring(0, (currentIndex + stringSize) - scrollableTitle.length());
		currentIndex++;
		
		showTitle = steadyTitle + scrolled;
		o.setDisplayName(showTitle);
	}
}