package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;

import java.util.function.Supplier;

public class CPacketHandlerUpdateEntityPrevPos extends AbstractPacketHandler<SPacketUpdateEntityPrevPos> {

	@Override
	protected void execHandlePacket(SPacketUpdateEntityPrevPos message, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(message.getEntityId());

		entity.setPos(message.getX(), message.getY(), message.getZ());
		entity.xOld = entity.getX();
		entity.yOld = entity.getY();
		entity.zOld = entity.getZ();
		
		entity.yRot = message.getYaw();
		entity.yRotO = entity.yRot;
	}

}
