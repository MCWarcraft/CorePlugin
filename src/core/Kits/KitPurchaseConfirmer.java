package core.Kits;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import core.HonorPoints.CurrencyOperations;
import core.HonorPoints.OnlinePlayerCurrencyUpdateEvent;

public class KitPurchaseConfirmer implements Listener
{
	private HashMap<String, KitPurchase> openPurchases;
	
	public KitPurchaseConfirmer()
	{
		openPurchases = new HashMap<String, KitPurchase>();
	}
	
    public void openPurchase(KitPurchase purchase)
    {
    	openPurchases.put(purchase.getKitPlayer().getPlayerName(), purchase);
    	Bukkit.getPlayer(purchase.getKitPlayer().getPlayerName()).sendMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/kit confirm " + ChatColor.GOLD + "to buy " + ChatColor.BLUE + purchase.getKitName());
    	Bukkit.getPlayer(purchase.getKitPlayer().getPlayerName()).sendMessage(ChatColor.GOLD + "If this was a mistake, do nothing.");
    }
    
    public boolean finishPurchase(String playerName)
    {
    	KitPurchase purchase = openPurchases.get(playerName);
    	openPurchases.remove(playerName);
    	
    	if (purchase == null)
    		return false;
    	
    	//Give them the kit
		CurrencyOperations.setCurrency(Bukkit.getOfflinePlayer(playerName), CurrencyOperations.getCurrency(Bukkit.getOfflinePlayer(playerName)) - purchase.getCost());
		purchase.getKitPlayer().unlockKit(purchase.getKitName());
		
		Bukkit.getPlayer(playerName).sendMessage(ChatColor.GREEN + "You have successfully purchased the kit '" + purchase.getKitName() + "'.");
		
		return true;
    }
	
    @EventHandler
    public void onLogoffEvent(PlayerQuitEvent event)
    {
    	openPurchases.remove(event.getPlayer().getName());
    }
    
    @EventHandler
    public void onCurrencyChangeEvent(OnlinePlayerCurrencyUpdateEvent event)
    {
    	if (openPurchases.keySet().contains(event.getPlayer().getName()))
    		event.getPlayer().sendMessage(ChatColor.RED + "Your honor balance has changed. Your purchase has been cancelled.");
    }
}