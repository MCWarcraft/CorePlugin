package core.Kits;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import core.HonorPoints.CurrencyOperations;
import core.HonorPoints.OnlinePlayerCurrencyUpdateEvent;

public class KitPurchaseConfirmer implements Listener
{
	private HashMap<UUID, KitPurchase> openPurchases;
	
	public KitPurchaseConfirmer()
	{
		openPurchases = new HashMap<UUID, KitPurchase>();
	}
	
    public void openPurchase(KitPurchase purchase)
    {
    	openPurchases.put(purchase.getKitPlayer().getPlayerUUID(), purchase);
    	Bukkit.getPlayer(purchase.getKitPlayer().getPlayerUUID()).sendMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/kit confirm " + ChatColor.GOLD + "to buy " + ChatColor.BLUE + purchase.getKitName());
    	Bukkit.getPlayer(purchase.getKitPlayer().getPlayerUUID()).sendMessage(ChatColor.GOLD + "If this was a mistake, do nothing.");
    }
    
    public boolean finishPurchase(UUID playerUUID)
    {
    	KitPurchase purchase = openPurchases.get(playerUUID);
    	openPurchases.remove(playerUUID);
    	
    	if (purchase == null)
    		return false;
    	
    	//Give them the kit
		CurrencyOperations.setCurrency(Bukkit.getOfflinePlayer(playerUUID), CurrencyOperations.getCurrency(Bukkit.getOfflinePlayer(playerUUID)) - purchase.getCost());
		purchase.getKitPlayer().unlockKit(purchase.getKitName());
		
		Bukkit.getPlayer(playerUUID).sendMessage(ChatColor.GREEN + "You have successfully purchased the kit '" + purchase.getKitName() + "'.");
		
		return true;
    }
	
    @EventHandler
    public void onLogoffEvent(PlayerQuitEvent event)
    {
    	openPurchases.remove(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onCurrencyChangeEvent(OnlinePlayerCurrencyUpdateEvent event)
    {
    	if (openPurchases.keySet().contains(event.getPlayer().getUniqueId()))
    		event.getPlayer().sendMessage(ChatColor.RED + "Your honor balance has changed. Your purchase has been cancelled.");
    }
}