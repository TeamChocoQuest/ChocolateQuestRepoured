package com.teamcqr.chocolatequestrepoured.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {

	public void preInit();

	public void init();

	public void postInit();

	public EntityPlayer getPlayer(MessageContext ctx);

	public World getWorld(MessageContext ctx);

	/**
	 * Only works when called on server side or when the passed player is the user.
	 */
	public boolean hasAdvancement(EntityPlayer player, ResourceLocation id);

	public void updateGui();

}
