package com.teamcqr.chocolatequestrepoured.network.server.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketExtendedReachAttack;
import com.teamcqr.chocolatequestrepoured.objects.items.spears.ItemSpearBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketHandlerExtendedReachAttack implements IMessageHandler<CPacketExtendedReachAttack, IMessage> {
	@Override
	public IMessage onMessage(final CPacketExtendedReachAttack message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			if (ctx.side.isServer()) {
				EntityPlayer attackingPlayer = CQRMain.proxy.getPlayer(ctx);
				World world = CQRMain.proxy.getPlayer(ctx).world;
				Entity attackTarget = world.getEntityByID(message.getEntityId());

				if (attackTarget == null) {
					return;
				}
				if (!(attackingPlayer.getHeldItemMainhand().getItem() instanceof ItemSpearBase)) {
					return;
				}
				if (attackingPlayer.getDistanceSq(attackTarget) < 144.0D) {
					attackingPlayer.attackTargetEntityWithCurrentItem(attackTarget);
				}
			}
		});
		return null;
	}
}
