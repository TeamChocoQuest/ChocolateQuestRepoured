package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.misc.AbstractEntityLaser;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;

import java.util.function.Supplier;

public class CPacketHandlerSyncLaserRotation extends AbstractPacketHandler<SPacketSyncLaserRotation> {

	@Override
	protected void execHandlePacket(SPacketSyncLaserRotation message, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity instanceof AbstractEntityLaser) {
			((AbstractEntityLaser) entity).serverRotationYawCQR = message.getYaw();
			((AbstractEntityLaser) entity).serverRotationPitchCQR = message.getPitch();
		}
	}

}
