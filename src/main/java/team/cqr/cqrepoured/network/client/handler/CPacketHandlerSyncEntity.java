package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;

public class CPacketHandlerSyncEntity extends AbstractPacketHandler<SPacketSyncEntity> {

	@Override
	protected void execHandlePacket(SPacketSyncEntity packet, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(packet.getEntityId());
		if (entity instanceof AbstractEntityCQR) {
			AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
			cqrentity.setHealthScale(packet.getHealthScaling());
			cqrentity.setDropChance(EquipmentSlot.HEAD, packet.getDropChanceHelm());
			cqrentity.setDropChance(EquipmentSlot.CHEST, packet.getDropChanceChest());
			cqrentity.setDropChance(EquipmentSlot.LEGS, packet.getDropChanceLegs());
			cqrentity.setDropChance(EquipmentSlot.FEET, packet.getDropChanceFeet());
			cqrentity.setDropChance(EquipmentSlot.MAINHAND, packet.getDropChanceMainhand());
			cqrentity.setDropChance(EquipmentSlot.OFFHAND, packet.getDropChanceOffhand());
			cqrentity.setSizeVariation(packet.getSizeScaling());
		}
		CQRMain.PROXY.updateGui();
	}

}
