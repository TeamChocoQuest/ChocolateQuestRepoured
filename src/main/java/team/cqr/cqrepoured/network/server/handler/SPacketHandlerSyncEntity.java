package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;

public class SPacketHandlerSyncEntity implements IMessageHandler<CPacketSyncEntity, IMessage> {

	@Override
	public IMessage onMessage(CPacketSyncEntity message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				if (CQRMain.proxy.getPlayer(ctx).isCreative()) {
					World world = CQRMain.proxy.getWorld(ctx);
					Entity entity = world.getEntityByID(message.getEntityId());
					if (entity instanceof AbstractEntityCQR) {
						AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
						cqrentity.setHealthScale(message.getHealthScaling() / 100.0D);
						cqrentity.setDropChance(EntityEquipmentSlot.HEAD, message.getDropChanceHelm() / 100.0F);
						cqrentity.setDropChance(EntityEquipmentSlot.CHEST, message.getDropChanceChest() / 100.0F);
						cqrentity.setDropChance(EntityEquipmentSlot.LEGS, message.getDropChanceLegs() / 100.0F);
						cqrentity.setDropChance(EntityEquipmentSlot.FEET, message.getDropChanceFeet() / 100.0F);
						cqrentity.setDropChance(EntityEquipmentSlot.MAINHAND, message.getDropChanceMainhand() / 100.0F);
						cqrentity.setDropChance(EntityEquipmentSlot.OFFHAND, message.getDropChanceOffhand() / 100.0F);
						cqrentity.setSizeVariation(message.getSizeScaling() / 100.0F);
					}
				}
			});
		}
		return null;
	}

}
