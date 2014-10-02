package core.Utilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class LocationSelector implements Listener
{
	private static HashMap<UUID, Location> selectedLocations = new HashMap<UUID, Location>();
	
	private HashMap<UUID, Integer> selectStage;
	
	public LocationSelector()
	{
		selectStage = new HashMap<UUID, Integer>();
	}
	
	private static void setSelectedLocation(UUID playerUUID, Location clickLocation)
	{
		selectedLocations.put(playerUUID, clickLocation);
	}
	
	public static Location getSelectedLocation(UUID playerUUID)
	{
		return selectedLocations.get(playerUUID);
	}
	
	@EventHandler
	public void playerInteractListener(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
	
		if (selectStage.get(player.getUniqueId()) == null)
			selectStage.put(player.getUniqueId(), 0);
		
		//If player is holding a stick
		if ((player.getItemInHand().getType() == Material.STICK || player.getItemInHand().getType() == Material.BONE) && player.hasPermission("core.utility.select") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (selectStage.get(player.getUniqueId()) == 0)
			{
				//Store the click
				if (player.getItemInHand().getType() == Material.BONE)
					LocationSelector.setSelectedLocation(player.getUniqueId(), event.getClickedBlock().getLocation().add(new Vector(0, 1, 0)));
				else
					LocationSelector.setSelectedLocation(player.getUniqueId(), event.getClickedBlock().getLocation());
				
				player.sendMessage(ChatColor.YELLOW + "Location Selected");
				selectStage.put(player.getUniqueId(), 1);
			}
			else
			{
				Location originalLoc = LocationSelector.getSelectedLocation(player.getUniqueId()), facingLoc = event.getClickedBlock().getLocation();
				Vector dir = new Vector().setX(facingLoc.getBlockX() - originalLoc.getBlockX()).setZ(facingLoc.getBlockZ() - originalLoc.getBlockZ());
				
				//Store the click
				LocationSelector.setSelectedLocation(player.getUniqueId(), LocationSelector.getSelectedLocation(player.getUniqueId()).setDirection(dir));
				player.sendMessage(ChatColor.YELLOW + "Facing set");
				selectStage.put(player.getUniqueId(), 0);
			}
		}
	}
}
