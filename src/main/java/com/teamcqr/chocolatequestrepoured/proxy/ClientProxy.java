package com.teamcqr.chocolatequestrepoured.proxy;

import com.teamcqr.chocolatequestrepoured.client.init.ModEntityRenderers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements IProxy {

	@Override
	public void preInit() {
		ModEntityRenderers.registerRenderers();
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft().player;
		} else {
			return context.getServerHandler().player;
		}
	}

}
