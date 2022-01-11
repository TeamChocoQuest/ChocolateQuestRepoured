package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

public class CPacketHandlerAnimationUpdateOfEntity extends AbstractPacketHandler<SPacketUpdateAnimationOfEntity> {

	@Override
	protected void execHandlePacket(SPacketUpdateAnimationOfEntity message, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity instanceof IServerAnimationReceiver) {
			IServerAnimationReceiver animationReceiver = (IServerAnimationReceiver) entity;

			animationReceiver.processAnimationUpdate(message.getAnimationID());
		}
	}

}
