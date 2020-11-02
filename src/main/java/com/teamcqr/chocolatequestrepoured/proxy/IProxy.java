package com.teamcqr.chocolatequestrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {

	void preInit();

	void init();

	void postInit();

	EntityPlayer getPlayer(MessageContext ctx);

	World getWorld(MessageContext ctx);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	Advancement getAdvancement(EntityPlayer player, ResourceLocation id);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	boolean hasAdvancement(EntityPlayer player, ResourceLocation id);
	
	public boolean isOwnerOfIntegratedServer(EntityPlayer player);

	void updateGui();

}
