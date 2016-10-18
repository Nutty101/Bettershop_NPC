package net.livecar.nuttyworks.bettershop_npcs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class Bettershop_NPC extends org.bukkit.plugin.java.JavaPlugin implements org.bukkit.event.Listener 
{
	
	public Citizens getCitizensPlugin;
	
	public void onEnable() 
	{
		Bukkit.getPluginManager().registerEvents(this, this);
		getLogger().log(java.util.logging.Level.ALL, ChatColor.GREEN + "Bettershop_NPC's Registered");
		
		
		if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) 
		{
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {
			getCitizensPlugin = (Citizens) getServer().getPluginManager().getPlugin("Citizens");
		}
		
		net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(BettershopNPC_Trait.class).withName("bettershopsnpc"));
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] inargs) 
	{
		if (inargs.length == 0 || inargs[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.GOLD + "----- Bettershop_NPC Help ----- V 0.1");
			sender.sendMessage(ChatColor.GOLD + "/setshop {shopname}");
			return true;
		}
		
		int npcid = -1;
		List<String> sList = new ArrayList<String>();

		for (int nCnt = 0; nCnt < inargs.length; nCnt++) 
		{
			if (inargs[nCnt].equalsIgnoreCase("--npc"))
			{
				//Npc ID should be the next one
				if (inargs.length >= nCnt+2)
				{
					npcid = Integer.parseInt(inargs[nCnt+1]);
					nCnt++;
				}
			} else {
				sList.add(inargs[nCnt]);
			}
		}

		inargs = sList.toArray(new String[sList.size()]);;
		NPC npc = null;
		if (npcid == -1)
		{
			// Now lets find the NPC this should run on.
			npc = getCitizensPlugin.getNPCSelector().getSelected(sender);
			if (npc != null) {
				// Gets NPC Selected for this sender
				npcid = npc.getId();
			}
		} else {
			npc = CitizensAPI.getNPCRegistry().getById(npcid);
		}

		if (npc == null)
		{
			sender.sendMessage("You need to select an NPC first");
			return true;
		}
		
		if (inargs[0].equalsIgnoreCase("setshop")) 
		{
			// Not valid from console
			if (!sender.hasPermission("bettershopnpc.setshop") && !sender.isOp()) 
			{
				sender.sendMessage("Missing bettershopnpc.setshop permissions");
				return true;
			} else {
				BettershopNPC_Trait shopTrait = (BettershopNPC_Trait)npc.getTrait(BettershopNPC_Trait.class);
				String shopName = "";
		        for (int i = 1; i < inargs.length; i++) {
		        	shopName = shopName + " " + inargs[i];
		        }
				shopTrait.shopName = shopName.trim();
				sender.sendMessage("Shopname set to " + shopName);
			}
		}
		return true;
	}

}
