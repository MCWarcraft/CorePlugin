package randy.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import core.Custody.CustodyLogoffListener;

public class main extends JavaPlugin implements Listener {
	
	private final CoreListener coreListener = new CoreListener();
	private final CustodyLogoffListener custodyListener = new CustodyLogoffListener();
	
	@Override
	public void onDisable() {
		//TODO: Fix database
		//CoreDatabase.PushChanges();
		System.out.print("[Core Plugin] succesfully disabled.");
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(coreListener, this);
		getServer().getPluginManager().registerEvents(custodyListener, this);
		
		//TODO: Fix database
		/*
		try {
			CoreSQL.ConnectToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for(Player player : Bukkit.getOnlinePlayers()){
			CoreDatabase.LoadPlayerData(player.getName());
		}
		*/
		
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
