package randy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class CoreScoreboard {

	public static ScoreboardManager scoreboardManager;
	public static HashMap<String, Scoreboard> scoreboards = new HashMap<String,Scoreboard>();
	public static ArrayList<String> existingScores = new ArrayList<String>();
	public static HashMap<String, Integer> changedScores = new HashMap<String, Integer>();
	public static HashMap<String, String> titles = new HashMap<String, String>();
	public static HashMap<String, String> gameModePlayers = new HashMap<String, String>();

	public static void CreateScoreboard(String player){
		if(!scoreboards.containsKey(player)){
			Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
			String shortName = player;
			if(player.length() > 16) shortName = player.substring(0,16);
			Objective objective = scoreboard.registerNewObjective(shortName, "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			scoreboards.put(player, scoreboard);
		}
	}

	public static void RemoveScoreboard(String player){
		if(scoreboards.containsKey(player)){
			//Bukkit.getPlayer(player).getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			ResetScores(player);

			//Don't actually remove the scoreboard to keep the objective to avoid crashes
			//scoreboards.remove(player);
		}
	}
	
	public static Scoreboard GetScoreboard(String player){
		CreateScoreboard(player);
		return scoreboards.get(player);
	}
	
	public static Objective GetObjective(String player){
		Scoreboard board = GetScoreboard(player);
		String shortName = player;
		if(player.length() > 16) shortName = player.substring(0,16);
		return board.getObjective(shortName);
	}

	public static void UpdateScoreboard(final String player){

		//System.out.print("Updating " + player + "'s scoreboard.");

		if(Bukkit.getPlayer(player) != null){
			
			Player plyr = Bukkit.getPlayer(player);
			//Set all scores first
			String gamemode = CoreAPI.GetCurrentGameMode(Bukkit.getPlayer(player));
			if(gamemode.equals("ffa")) randy.ffa.main.UpdateScoreboard(player);
			else if(gamemode.equals("minigame")) randy.minigames.MGScoreboardManager.UpdateScoreboard(player);
			else if(gamemode.equals("tower")) randy.ffaextra.main.UpdateScoreboard(player);
			else if(gamemode.equals("spawn")) randy.spawnutility.main.UpdateScoreboard(player);
	
			Scoreboard board = scoreboards.get(player);
			Objective objective = GetObjective(player);
			objective.setDisplayName(titles.get(player));
			if(plyr.getScoreboard() != null){
				if(plyr.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null){
					objective.setDisplaySlot(DisplaySlot.SIDEBAR);
					plyr.setScoreboard(board);
				}
			}
		}
	}

	public static void UpdateAllScoreboards(){
		for(String player : scoreboards.keySet()){
			UpdateScoreboard(player);
		}
	}

	public static void SetScore(String player, String text, String gamemode, int score){
		if(!existingScores.contains(gamemode+":"+player+"~"+text)){
			existingScores.add(gamemode+":"+player+"~"+text);
		}
		
		String scoreText = text;
		String shortText = scoreText;
		if(scoreText.length() > 16) shortText = shortText.substring(0, 16);
		GetObjective(player).getScore(Bukkit.getOfflinePlayer(shortText)).setScore(score);
		
		gameModePlayers.put(player, gamemode);
	}

	public static void RemoveScore(String player, String text, String gamemode){	
		if(existingScores.contains(gamemode+":"+player+"~"+text)){
			String shortText = text;
			if(text.length() > 16) shortText = text.substring(0, 16);
			GetScoreboard(player).resetScores(Bukkit.getOfflinePlayer(shortText));
			existingScores.remove(gamemode+":"+player+"~"+text);
		}
	}

	public static void SetTitle(String player, String title){
		String shortTitle = title;
		if(title.length() > 16) shortTitle = shortTitle.substring(0, 16);
		titles.put(player, shortTitle);

		//System.out.print("Setting title: " + shortTitle);
	}

	public static void ResetScores(final String player){
		
		if(!existingScores.isEmpty()){
			Bukkit.getServer().getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("CorePlugin"), new Runnable() {
				@Override
				public void run() {
					int loopCount = 0;
					int count = 0;
					while(loopCount < 5){
						if(count < existingScores.size() && existingScores.size() > 0){
							String score = existingScores.get(count);
							if(score != null && score.contains(":"+player+"~")){				
								RemoveScore(score.split("~")[0]
										.split(":")[1], 
										score.split("~")[1], score.split("~")[0]
												.split(":")[0]);
							}
						}
						
						count++;
						if(count >= existingScores.size()){
							count = 0;
							loopCount++;
						}
					}
					
					/*for(String score : existingScores){
						RemoveScore(score.split("~")[0]
								.split(":")[1], 
								score.split("~")[1], score.split("~")[0]
										.split(":")[0]);
					}*/
					//UpdateScoreboard(player);
				}
			});
		}
	}

	public static List<String> GetGamemodePlayers(String gamemode){
		List<String> players = new ArrayList<String>();
		for(String player : gameModePlayers.keySet()){
			if(gameModePlayers.get(player).equals(gamemode)){
				if(!players.contains(player))
					players.add(player);
			}
		}

		//if(players.isEmpty()) return null;
		return players;
	}
}
