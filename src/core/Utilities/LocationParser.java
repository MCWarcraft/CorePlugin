package core.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LocationParser
{
	public static String locationToString(Location loc)
	{
		if (loc == null)
		{
			return "null";
		}
		return (loc.getWorld().getName()) + "|" +
				(loc.getBlockX()) + "|" +
				(loc.getBlockY()) + "|" +
				(loc.getBlockZ()) + "|" +
				(loc.getDirection().getX() + "|" +
				(loc.getDirection().getZ()));
	}
	
	public static Location parseLocation(String unparsed)
	{		
		if (unparsed == null || unparsed.equalsIgnoreCase("null"))
			return null;
		
		
		String[] coords = unparsed.split("\\|");
		double x, y, z, facingX, facingZ;
		
		World world = Bukkit.getServer().getWorld(coords[0]);
		if (world == null)
			return null;
		
		try
		{
			x = Double.parseDouble(coords[1]);
			y = Double.parseDouble(coords[2]);
			z = Double.parseDouble(coords[3]);
			facingX = Double.parseDouble(coords[4]);
			facingZ = Double.parseDouble(coords[5]);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		
		return new Location(world, x, y, z).setDirection(new Vector().setX(facingX).setZ(facingZ));
	}
	
	public static String vectorToString(Vector vec)
	{
		if (vec == null)
		{
			return "null";
		}
		
		return (vec.getBlockX() + "|" + vec.getBlockY() + "|" + vec.getBlockZ());
	}
	
	public static Vector parseVector(String unparsed)
	{		
		if (unparsed == null || unparsed.equalsIgnoreCase("null"))
			return null;
		
		
		String[] coords = unparsed.split("\\|");
		double x, y, z;
		
		try
		{
			x = Double.parseDouble(coords[0]);
			y = Double.parseDouble(coords[1]);
			z = Double.parseDouble(coords[2]);
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		
		return new Vector(x, y, z);
	}
}
