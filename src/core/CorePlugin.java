package core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import bourg.austin.HonorPoints.HonorCommandExecutor;
import bourg.austin.HonorPoints.HonorListener;
import bourg.austin.HonorPoints.HonorPoints;
import core.Custody.CustodyLogoffListener;
import core.Kits.KitCommandExecutor;
import core.Kits.KitManager;
import core.Scoreboard.CoreScoreboardManager;

public class CorePlugin extends JavaPlugin implements Listener
{
	private KitManager kitManager;
	private HonorPoints honorPoints;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		
		//Initialize managers
		honorPoints = new HonorPoints(this.getConfig().getString("sql.ip"), this.getConfig().getString("sql.port"), this.getConfig().getString("sql.database"), this.getConfig().getString("sql.username"), this.getConfig().getString("sql.password"));
		kitManager = new KitManager(this);	
		
		//Register events
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CoreListener(), this);
		getServer().getPluginManager().registerEvents(new CustodyLogoffListener(), this);
		//getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		//getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
		this.getServer().getPluginManager().registerEvents(new HonorListener(), this);
		
		//Set command executors
		this.getCommand("honor").setExecutor(new HonorCommandExecutor());
		this.getCommand("kit").setExecutor(new KitCommandExecutor(kitManager));
		
		//Start SQL
		//TODO Burn this
		/*
		try {CoreSQL.ConnectToSQL();} catch (SQLException e) {e.printStackTrace();}
		
		for(Player player : Bukkit.getOnlinePlayers())
			CoreDatabase.LoadPlayerData(player.getName());
		*/
		
		//Start CoreScoreboardManager
		CoreScoreboardManager.initialize(this);
		
		
		this.getServer().getLogger().info("[Core Plugin] Succesfully enabled.");
	}
	
	@Override
	public void onDisable()
	{
		//TODO: Fix database
		//CoreDatabase.PushChanges();
		honorPoints.closeConnection();
		kitManager.shutdown();
		//TODO Create shutdown protocol
		//kitManager.disable();
		this.getServer().getLogger().info("[Core Plugin] succesfully disabled.");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreprocess(PlayerCommandPreprocessEvent event)
	{
		String command = event.getMessage().substring(1);
		if(command.equalsIgnoreCase("help"))
		{
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
