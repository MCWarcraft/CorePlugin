package core.Utilities;

import java.util.HashMap;

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
	private static HashMap<String, Location> selectedLocations = new HashMap<String, Location>();
	
	private HashMap<String, Integer> selectStage;
	
	public LocationSelector()
	{
		selectStage = new HashMap<String, Integer>();
	}
	
	private static void setSelectedLocation(String playerName, Location clickLocation)
	{
		selectedLocations.put(playerName, clickLocation);
	}
	
	public static Location getSelectedLocation(String name)
	{
		return selectedLocations.get(name);
	}
	
	@EventHandler
	public void playerInteractListener(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
	
		if (selectStage.get(player.getName()) == null)
			selectStage.put(player.getName(), 0);
		
		//If player is holding a stick
		if ((player.getItemInHand().getType() == Material.STICK || player.getItemInHand().getType() == Material.BONE) && player.hasPermission("core.utility.select") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (selectStage.get(player.getName()) == 0)
			{
				//Store the click
				if (player.getItemInHand().getType() == Material.BONE)
					LocationSelector.setSelectedLocation(player.getName(), event.getClickedBlock().getLocation().add(new Vector(0, 1, 0)));
				else
					LocationSelector.setSelectedLocation(player.getName(), event.getClickedBlock().getLocation());
				
				player.sendMessage(ChatColor.YELLOW + "Location Selected");
				selectStage.put(player.getName(), 1);
			}
			else
			{
				Location originalLoc = LocationSelector.getSelectedLocation(player.getName()), facingLoc = event.getClickedBlock().getLocation();
				Vector dir = new Vector().setX(facingLoc.getBlockX() - originalLoc.getBlockX()).setZ(facingLoc.getBlockZ() - originalLoc.getBlockZ());
				
				//Store the click
				LocationSelector.setSelectedLocation(player.getName(), LocationSelector.getSelectedLocation(player.getName()).setDirection(dir));
				player.sendMessage(ChatColor.YELLOW + "Facing set");
				selectStage.put(player.getName(), 0);
			}
		}
	}
}
