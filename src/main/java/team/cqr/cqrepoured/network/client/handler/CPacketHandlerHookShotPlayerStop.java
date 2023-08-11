package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketHookShotPlayerStop;

import java.util.function.Supplier;

public class CPacketHandlerHookShotPlayerStop extends AbstractPacketHandler<SPacketHookShotPlayerStop> {

	@Override
	protected void execHandlePacket(SPacketHookShotPlayerStop packet, Supplier<Context> context, Level world, Player player) {
		player.setDeltaMovement(Vec3.ZERO);
		player.hasImpulse = true;
	}

}
