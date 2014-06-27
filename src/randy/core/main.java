package randy.core;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import randy.core.tools.CoreDatabase;
import randy.core.tools.CoreSQL;

public class main extends JavaPlugin implements Listener {
	
	private final CoreListener coreListener = new CoreListener();
	
	@Override
	public void onDisable() {
		CoreDatabase.PushChanges();
		System.out.print("[Core Plugin] succesfully disabled.");
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(coreListener, this);
		
		try {
			CoreSQL.ConnectToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CoreScoreboard.scoreboardManager = Bukkit.getScoreboardManager();
		
		for(Player player : Bukkit.getOnlinePlayers()){
			CoreDatabase.LoadPlayerData(player.getName());
			CoreScoreboard.CreateScoreboard(player.getName());
		}
		System.out.print("[Core Plugin] Succesfully enabled.");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreprocess(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().substring(1);
		if(command.equalsIgnoreCase("help")) {
			Player player = event.getPlayer();
			player.sendMessage(ChatColor.GOLD + "Website: mcwarcraft.com");
			player.sendMessage(ChatColor.GOLD + "Shop: mcwarcraft.com/shop");
			player.sendMessage(ChatColor.GOLD + "Commands:");
			player.sendMessage(ChatColor.GOLD + "/msg <player> - Sends the specified player a message");
			player.sendMessage(ChatColor.GOLD + "/r - Respond to the last player that messaged you");
			player.sendMessage(ChatColor.GOLD + "/who - Displays who is online");
			event.setCancelled(true);
			return;
		}
	 }
}
