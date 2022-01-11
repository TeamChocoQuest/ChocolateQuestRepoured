package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;

public class CPacketHandlerUpdateEntityPrevPos extends AbstractPacketHandler<SPacketUpdateEntityPrevPos> {

	@Override
	protected void execHandlePacket(SPacketUpdateEntityPrevPos message, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(message.getEntityId());

		entity.setPos(message.getX(), message.getY(), message.getZ());
		entity.xOld = entity.getX();
		entity.yOld = entity.getY();
		entity.zOld = entity.getZ();
		entity.yRot = message.getYaw();
		entity.yRotO = entity.yRot;
	}

}
