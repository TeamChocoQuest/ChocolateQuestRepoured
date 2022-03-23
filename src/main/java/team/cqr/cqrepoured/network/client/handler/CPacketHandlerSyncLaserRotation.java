package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncLaserRotation;

import java.util.function.Supplier;

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
