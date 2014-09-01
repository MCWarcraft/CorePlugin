package core.Kits;


public class KitPurchase
{
	private KitPlayer kitPlayer;
	private String kitName;
	private int cost;
	
	public KitPurchase(KitPlayer kitPlayer, String kitName, int cost)
	{
		this.kitPlayer = kitPlayer;
		this.kitName = kitName;
		this.cost = cost;
	}
	
	public KitPlayer getKitPlayer()
	{
		return kitPlayer;
	}
	
	public String getKitName()
	{
		return kitName;
	}
	
	public int getCost()
	{
		return cost;
	}
}
