package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;

public class CPacketHandlerSyncEntity implements IMessageHandler<SPacketSyncEntity, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncEntity message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());
				if (entity instanceof AbstractEntityCQR) {
					AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
					cqrentity.setHealthScale(message.getHealthScaling());
					cqrentity.setDropChance(EntityEquipmentSlot.HEAD, message.getDropChanceHelm());
					cqrentity.setDropChance(EntityEquipmentSlot.CHEST, message.getDropChanceChest());
					cqrentity.setDropChance(EntityEquipmentSlot.LEGS, message.getDropChanceLegs());
					cqrentity.setDropChance(EntityEquipmentSlot.FEET, message.getDropChanceFeet());
					cqrentity.setDropChance(EntityEquipmentSlot.MAINHAND, message.getDropChanceMainhand());
					cqrentity.setDropChance(EntityEquipmentSlot.OFFHAND, message.getDropChanceOffhand());
					cqrentity.setSizeVariation(message.getSizeScaling());
				}
				CQRMain.proxy.updateGui();
			});
		}
		return null;
	}

}
