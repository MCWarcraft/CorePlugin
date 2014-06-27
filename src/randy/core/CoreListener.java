package randy.core;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import randy.core.tools.CoreDatabase;
import randy.kits.Kits;
import randy.minigames.GameManager;
import randy.minigames.GameManager.GameState;

public class CoreListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		CoreDatabase.LoadPlayerData(event.getPlayer().getName());
		CoreScoreboard.CreateScoreboard(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event){
		if(event.getEntity().getType() == EntityType.PLAYER){
			Player player = (Player)event.getEntity();
			if(CoreAPI.GetCurrentGameMode(player).equals("minigame")){
				if(GameManager.currentState == GameState.PreGame){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		
		String gameMode = CoreAPI.GetCurrentGameMode(player);
		
		if(gameMode.equals("ffa")){
			if(!Kits.hasKit.get(player)){
				event.setCancelled(true);
			}else{
				ItemStack droppedItem = event.getItemDrop().getItemStack();
				if(droppedItem.getType() == Material.MUSHROOM_SOUP ||
						droppedItem.getType() == Material.BOWL){
					event.getItemDrop().remove();
				}else if(droppedItem.getType().toString().contains("SWORD") ||
						droppedItem.getType().toString().contains("AXE") ||
						droppedItem.getType().toString().contains("BOW") ||
						droppedItem.getType().toString().contains("ARROW") ||
						droppedItem.getType().toString().contains("COMPASS") ||
						droppedItem.getType().toString().contains("WATCH") ||
						droppedItem.getType().toString().contains("NETHER_STAR")){
					event.setCancelled(true);
				}
			}
		} else if(gameMode.equals("tower") || gameMode.equals("minigame")){
			ItemStack droppedItem = event.getItemDrop().getItemStack();
			if(droppedItem.getType() == Material.MUSHROOM_SOUP ||
					droppedItem.getType() == Material.BOWL){
				event.getItemDrop().remove();
			}else if(droppedItem.getType().toString().contains("SWORD") ||
					droppedItem.getType().toString().contains("AXE") ||
					droppedItem.getType().toString().contains("BOW") ||
					droppedItem.getType().toString().contains("ARROW") ||
					droppedItem.getType().toString().contains("COMPASS") ||
					droppedItem.getType().toString().contains("WATCH") ||
					droppedItem.getType().toString().contains("NETHER_STAR")){
				event.setCancelled(true);
			}
		} else if(gameMode.equals("spawn")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		event.getDrops().clear();
		event.setDroppedExp(0);
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event){
		event.getBrokenItem().setDurability((short)0);
	}
}
