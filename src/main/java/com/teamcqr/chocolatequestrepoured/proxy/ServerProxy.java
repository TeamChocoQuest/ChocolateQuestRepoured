package com.teamcqr.chocolatequestrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy {

	@Override
	public void preInit() {

	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		return context.getServerHandler().player;
	}

	@Override
	public World getWorld(MessageContext context) {
		return context.getServerHandler().player.world;
	}

	@Override
	public boolean hasAdvancement(EntityPlayer player, ResourceLocation id) {
		if (player instanceof EntityPlayerMP) {
			Advancement advancement = ((EntityPlayerMP) player).getServerWorld().getAdvancementManager().getAdvancement(id);
			if (advancement != null) {
				return ((EntityPlayerMP) player).getAdvancements().getProgress(advancement).isDone();
			}
		}
		return false;
	}

	@Override
	public void updateGui() {

	}

}
