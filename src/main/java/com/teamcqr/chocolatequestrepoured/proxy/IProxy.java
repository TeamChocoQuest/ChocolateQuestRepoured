package com.teamcqr.chocolatequestrepoured.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {

	public void preInit();

	public void init();

	public void postInit();

	public EntityPlayer getPlayer(MessageContext context);

	public World getWorld(MessageContext context);

}
