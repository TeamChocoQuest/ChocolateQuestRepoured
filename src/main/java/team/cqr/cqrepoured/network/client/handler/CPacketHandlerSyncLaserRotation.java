package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;

public class CPacketHandlerSyncLaserRotation extends AbstractPacketHandler<SPacketSyncLaserRotation> {

	@Override
	protected void execHandlePacket(SPacketSyncLaserRotation message, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity instanceof AbstractEntityLaser) {
			((AbstractEntityLaser) entity).serverRotationYawCQR = message.getYaw();
			((AbstractEntityLaser) entity).serverRotationPitchCQR = message.getPitch();
		}
	}

}
