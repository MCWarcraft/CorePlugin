 package core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
public class CoreAPI {
	
	/*
	 * EXITING GAME MODES
	 */
	public static void BroadcastKillstreak(Player player, int killstreak, String gamemode){
		String message = "-";
		if(killstreak == 5){
			message = ChatColor.RED + player.getName() + ChatColor.GOLD + " is on a killstreak of 5 at " + ChatColor.RED + gamemode + ChatColor.GOLD + "!";
		} else if(killstreak == 10){
			message = ChatColor.RED + player.getName() + ChatColor.GOLD + " is unstoppable with a killstreak of 10 at " + ChatColor.RED + gamemode + ChatColor.GOLD + "!";
		} else if(killstreak == 15){
			message = ChatColor.RED + player.getName() + ChatColor.GOLD + " is godlike with a killstreak of 15 at " + ChatColor.RED + gamemode + ChatColor.GOLD + "!";
		} else if(killstreak == 20){
			message = ChatColor.RED + player.getName() + ChatColor.GOLD + " is ruining other people's fun with a killstreak of 20 at " + ChatColor.RED + gamemode + ChatColor.GOLD + "!";
		} else if(killstreak == 25){
			message = ChatColor.RED + player.getName() + ChatColor.GOLD + ", could you stop? 25 is more than enough at " + ChatColor.RED + gamemode + ChatColor.GOLD + "!";
		} else if(killstreak == 30){
			message = ChatColor.GOLD + "Someone please stop " + ChatColor.RED + player.getName() + ChatColor.GOLD + " at " + ChatColor.RED + gamemode + ChatColor.GOLD + " with a 30 killstreak! This is getting out of hand!";
		} else if(killstreak == 35){
			message = ChatColor.GOLD + "Can " + ChatColor.RED + player.getName() + ChatColor.GOLD + " not be stopped at " + ChatColor.RED + gamemode + ChatColor.GOLD + "..?!  He has a 35 killstreak..!";
		} else if(killstreak == 40){
			message = ChatColor.GOLD + "Everybody stop playing. It's no use. " + ChatColor.RED + player.getName() + ChatColor.GOLD + " at " + ChatColor.RED + gamemode + ChatColor.GOLD + " with a 40 killstreak...";
		}
		if(!message.equals("-")){
			for(Player p : Bukkit.getOnlinePlayers()){
				p.sendMessage(message);
			}
		}
	}
}
