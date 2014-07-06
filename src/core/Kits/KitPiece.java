package core.Kits;

public enum KitPiece
{
	HELMET("helmet"), CHESTPLATE("chestplate"), LEGGINGS("leggings"), BOOTS("boots"), WEAPON("weapon");
	
	private String value;
	
	private KitPiece(String value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		return value;
	}
}