package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.init.CQRParticleType;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSpawnParticles;

public class CPacketHandlerSpawnParticles extends AbstractPacketHandler<SPacketSpawnParticles> {

	@Override
	protected void execHandlePacket(SPacketSpawnParticles message, Supplier<Context> context, World world, PlayerEntity player) {
		int particleId = message.getParticleId();
		double xCoord = message.getxCoord();
		double yCoord = message.getyCoord();
		double zCoord = message.getzCoord();
		double xSpeed = message.getxSpeed();
		double ySpeed = message.getySpeed();
		double zSpeed = message.getzSpeed();
		int count = message.getCount();
		double xOffset = message.getxOffset();
		double yOffset = message.getyOffset();
		double zOffset = message.getzOffset();
		int[] optionalArguments = message.getOptionalArguments();
		CQRParticleType.spawnParticles(particleId, world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, count, xOffset, yOffset, zOffset, optionalArguments);
	}

}
