package net.livecar.nuttyworks.bettershop_npcs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Listeners.Opener;
import max.hubbard.bettershops.Shops.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

public class BettershopNPC_Trait extends Trait 
{
	public BettershopNPC_Trait() 
    {
		super("bettershopsnpc");
	}

	@Persist public String shopName;

	@EventHandler
	public void onRightClick(NPCRightClickEvent event)
	{
		if (this.npc != event.getNPC()) {
			return;
		}
		final Player player = event.getClicker();
		if (!player.hasPermission("bettershopnpc.interact")) 
		{
			player.sendMessage("You do not have permission");
			return;
		} else {
			Shop shop = ShopManager.fromString(this.shopName);
			if (shop == null)
				player.sendMessage("This shop does not exist");
			if (!shop.isOpen())
				player.sendMessage("This shop is closed");
			if (!shop.isOpen())
				player.sendMessage("This shop is closed");
			if (!shop.getBlacklist().contains(player)) 
			{
				if (shop.getOwner() != null) {
					Opener.open(player, shop);		
				}
			}
		}
	}
}
