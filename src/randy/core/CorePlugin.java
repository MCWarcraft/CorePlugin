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
import randy.kits.KitManager;
import randy.kits.PlayerJoin;
import randy.kits.PlayerLeave;
import bourg.austin.HonorPoints.HonorCommandExecutor;
import bourg.austin.HonorPoints.HonorListener;
import bourg.austin.HonorPoints.HonorPoints;
import core.Custody.CustodyLogoffListener;

public class CorePlugin extends JavaPlugin implements Listener {
	
	private final CoreListener coreListener = new CoreListener();
	private final CustodyLogoffListener custodyListener = new CustodyLogoffListener();
	private final PlayerLeave playerLeaveListener = new PlayerLeave();
	private final PlayerJoin playerJoinListener = new PlayerJoin();
	
	private KitManager kitManager;
	
	private HonorPoints honorPoints;
	
	@Override
	public void onDisable() {
		//TODO: Fix database
		CoreDatabase.PushChanges();
		honorPoints.closeConnection();
		kitManager.disable();
		System.out.print("[Core Plugin] succesfully disabled.");
	}
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		
		 honorPoints = new HonorPoints(this.getConfig().getString("sql.ip"), this.getConfig().getString("sql.port"), this.getConfig().getString("sql.database"), this.getConfig().getString("sql.username"), this.getConfig().getString("sql.password"));
			
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(coreListener, this);
		getServer().getPluginManager().registerEvents(custodyListener, this);
		getServer().getPluginManager().registerEvents(playerJoinListener, this);
		getServer().getPluginManager().registerEvents(playerLeaveListener, this);
		this.getServer().getPluginManager().registerEvents(new HonorListener(), this);
		
		//Set command executors
		this.getCommand("honor").setExecutor(new HonorCommandExecutor());
		
		kitManager = new KitManager(this);
		
		try {
			CoreSQL.ConnectToSQL();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for(Player player : Bukkit.getOnlinePlayers()){
			CoreDatabase.LoadPlayerData(player.getName());
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
