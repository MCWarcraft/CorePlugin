package core.Kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class EquippableKit
{
	private Player player;
	private KitPlayer kitPlayer;
	private Kit kit;
	
	public EquippableKit(Player player, KitPlayer kitPlayer, Kit kit)
	{
		this.player = player;
		this.kitPlayer = kitPlayer;
		this.kit = kit;
	}
	
	@SuppressWarnings("deprecation")
	public void equip()
	{
		player.getInventory().clear();

		player.getInventory().addItem(kit.getPiece(KitPiece.WEAPON, kitPlayer.getPieceLevel(kit.getName(), KitPiece.WEAPON)));

		player.getInventory().setHelmet(kit.getPiece(KitPiece.HELMET, kitPlayer.getPieceLevel(kit.getName(), KitPiece.HELMET)));
		player.getInventory().setChestplate(kit.getPiece(KitPiece.CHESTPLATE, kitPlayer.getPieceLevel(kit.getName(), KitPiece.CHESTPLATE)));
		player.getInventory().setLeggings(kit.getPiece(KitPiece.LEGGINGS, kitPlayer.getPieceLevel(kit.getName(), KitPiece.LEGGINGS)));
		player.getInventory().setBoots(kit.getPiece(KitPiece.BOOTS, kitPlayer.getPieceLevel(kit.getName(), KitPiece.BOOTS)));
		
		for (ItemStack item : kit.getHotbarItems())
			if (item != null)
				player.getInventory().addItem(item);
		for (ItemStack item : kit.getInventoryItems())
			if (item != null)
				player.getInventory().addItem(item);
		
		player.updateInventory();
		
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		
		for (PotionEffect effect : kit.getPotionEffectSet(kitPlayer.getPotionLevel(kit.getName())))
			player.addPotionEffect(effect);
	}
}