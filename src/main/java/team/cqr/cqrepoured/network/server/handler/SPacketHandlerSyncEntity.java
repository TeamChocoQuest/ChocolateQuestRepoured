package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncEntity;

public class SPacketHandlerSyncEntity extends AbstractPacketHandler<CPacketSyncEntity> {

	@Override
	protected void execHandlePacket(CPacketSyncEntity packet, Supplier<Context> context, Level world, Player player) {
		if(player.isCreative()) {
			Entity entity = world.getEntity(packet.getEntityId());
			if (entity instanceof AbstractEntityCQR) {
				AbstractEntityCQR cqrentity = (AbstractEntityCQR) entity;
				cqrentity.setHealthScale(packet.getHealthScaling() / 100.0D);
				cqrentity.setDropChance(EquipmentSlot.HEAD, packet.getDropChanceHelm() / 100.0F);
				cqrentity.setDropChance(EquipmentSlot.CHEST, packet.getDropChanceChest() / 100.0F);
				cqrentity.setDropChance(EquipmentSlot.LEGS, packet.getDropChanceLegs() / 100.0F);
				cqrentity.setDropChance(EquipmentSlot.FEET, packet.getDropChanceFeet() / 100.0F);
				cqrentity.setDropChance(EquipmentSlot.MAINHAND, packet.getDropChanceMainhand() / 100.0F);
				cqrentity.setDropChance(EquipmentSlot.OFFHAND, packet.getDropChanceOffhand() / 100.0F);
				cqrentity.setSizeVariation(packet.getSizeScaling() / 100.0F);

				CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> cqrentity), new SPacketSyncEntity(cqrentity));
			}
		}
	}

}
