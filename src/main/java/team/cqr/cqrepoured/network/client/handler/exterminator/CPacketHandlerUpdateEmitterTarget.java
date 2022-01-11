package team.cqr.cqrepoured.network.client.handler.exterminator;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

public class CPacketHandlerUpdateEmitterTarget extends AbstractPacketHandler<SPacketUpdateEmitterTarget> {

	@Override
	protected void execHandlePacket(SPacketUpdateEmitterTarget packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());

		if (entity != null && entity instanceof EntityCQRExterminator) {
			EntityCQRExterminator exterminator = (EntityCQRExterminator) entity;

			if (packet.isLeftEmitter()) {
				exterminator.updateEmitterTargetLeftClient(packet.isTargetSet() ? world.getEntity(packet.getTargetID()) : null);
			} else {
				exterminator.updateEmitterTargetRightClient(packet.isTargetSet() ? world.getEntity(packet.getTargetID()) : null);
			}
		}
	}

}
