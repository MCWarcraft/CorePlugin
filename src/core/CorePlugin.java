package core;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import core.Custody.CustodyLogoffListener;
import core.HonorPoints.HonorCommandExecutor;
import core.HonorPoints.HonorListener;
import core.HonorPoints.HonorPoints;
import core.Kits.KitCommandExecutor;
import core.Kits.KitManager;
import core.Scoreboard.CoreScoreboardManager;
import core.Utilities.CoreItems;

public class CorePlugin extends JavaPlugin implements Listener
{
	private KitManager kitManager;
	private HonorPoints honorPoints;
	
	private String whoMessage;
	private HashMap<String, ChatColor> whoColors;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		
		//Initialize managers
		honorPoints = new HonorPoints(this.getConfig().getString("sql.ip"), this.getConfig().getString("sql.port"), this.getConfig().getString("sql.database"), this.getConfig().getString("sql.username"), this.getConfig().getString("sql.password"));
		kitManager = new KitManager(this);	
		
		loadData();
		
		//Register events
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CoreListener(), this);
		getServer().getPluginManager().registerEvents(new CustodyLogoffListener(), this);
		this.getServer().getPluginManager().registerEvents(new HonorListener(), this);
		
		//Set command executors
		this.getCommand("who").setExecutor(new CoreCommandExecutor(this));
		this.getCommand("honor").setExecutor(new HonorCommandExecutor());
		this.getCommand("kit").setExecutor(new KitCommandExecutor(kitManager));
		
		//Start CoreScoreboardManager
		CoreScoreboardManager.initialize(this);
		CoreItems.initialize();
		
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
	
	public void loadData()
	{
		System.out.println("LOADING DATA IN CORE");
		
		whoColors = new HashMap<String, ChatColor>();
		whoMessage = getConfig().getString("whomessage");
		
		ConfigurationSection whoSection = getConfig().getConfigurationSection("who");
		
		if (whoSection == null)
		{
			System.out.println("Null section");
			return;
		}
		
		System.out.println("Not null section");
		
		for (String color : whoSection.getKeys(false))
		{
			System.out.println(color);
			
			ChatColor chatColor = ChatColor.valueOf(color);
			if (chatColor == null) continue;
			
			List<String> players = whoSection.getStringList(color);
			System.out.println(players.size());
			
			for (String player : players)
			{
				whoColors.put(player.toLowerCase(), chatColor);
				getServer().getLogger().info(chatColor + player);
			}
		}
	}
	
	public ChatColor getWhoColor(String playerName)
	{		
		if (whoColors.get(playerName.toLowerCase()) == null) return ChatColor.WHITE;
		return whoColors.get(playerName.toLowerCase());
	}
	
	public String getWhoMessage()
	{
		return whoMessage;
	}
}