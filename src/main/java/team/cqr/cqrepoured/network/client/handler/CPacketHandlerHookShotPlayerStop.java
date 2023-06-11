package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import org.joml.Vector3d;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;

public class CPacketHandlerHookShotPlayerStop extends AbstractPacketHandler<SPacketHookShotPlayerStop> {

	@Override
	protected void execHandlePacket(SPacketHookShotPlayerStop packet, Supplier<Context> context, World world, PlayerEntity player) {
		player.setDeltaMovement(Vector3d.ZERO);
		player.hasImpulse = true;
	}

}
