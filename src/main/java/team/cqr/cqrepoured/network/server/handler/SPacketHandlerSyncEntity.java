package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;

public class SPacketHandlerSyncEntity extends AbstractPacketHandler<CPacketSyncEntity> {

	@Override
	public void handlePacket(CPacketSyncEntity packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			if(player.isCreative()) {
				World world = player.level;
				Entity entity = world.getEntity(packet.getEntityId());
				if (entity instanceof AbstractEntityCQR) {
					AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
					cqrentity.setHealthScale(packet.getHealthScaling() / 100.0D);
					cqrentity.setDropChance(EquipmentSlotType.HEAD, packet.getDropChanceHelm() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.CHEST, packet.getDropChanceChest() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.LEGS, packet.getDropChanceLegs() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.FEET, packet.getDropChanceFeet() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.MAINHAND, packet.getDropChanceMainhand() / 100.0F);
					cqrentity.setDropChance(EquipmentSlotType.OFFHAND, packet.getDropChanceOffhand() / 100.0F);
					cqrentity.setSizeVariation(packet.getSizeScaling() / 100.0F);
				}
			}
		});
		context.get().setPacketHandled(true);
	}

}
