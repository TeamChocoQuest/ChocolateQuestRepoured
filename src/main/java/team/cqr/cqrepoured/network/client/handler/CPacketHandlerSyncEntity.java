package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;

public class CPacketHandlerSyncEntity extends AbstractPacketHandler<SPacketSyncEntity> {

	@Override
	protected void execHandlePacket(SPacketSyncEntity packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());
		if (entity instanceof AbstractEntityCQR) {
			AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
			cqrentity.setHealthScale(packet.getHealthScaling());
			cqrentity.setDropChance(EquipmentSlotType.HEAD, packet.getDropChanceHelm());
			cqrentity.setDropChance(EquipmentSlotType.CHEST, packet.getDropChanceChest());
			cqrentity.setDropChance(EquipmentSlotType.LEGS, packet.getDropChanceLegs());
			cqrentity.setDropChance(EquipmentSlotType.FEET, packet.getDropChanceFeet());
			cqrentity.setDropChance(EquipmentSlotType.MAINHAND, packet.getDropChanceMainhand());
			cqrentity.setDropChance(EquipmentSlotType.OFFHAND, packet.getDropChanceOffhand());
			cqrentity.setSizeVariation(packet.getSizeScaling());
		}
		CQRMain.PROXY.updateGui();
	}

}
