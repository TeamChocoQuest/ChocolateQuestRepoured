package team.cqr.cqrepoured.network.client.handler.exterminator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;

import java.util.function.Supplier;

public class CPacketHandlerUpdateEmitterTarget extends AbstractPacketHandler<SPacketUpdateEmitterTarget> {

	@Override
	protected void execHandlePacket(SPacketUpdateEmitterTarget packet, Supplier<Context> context, Level world, Player player) {
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
