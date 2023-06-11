package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateAnimationOfEntity;

import java.util.function.Supplier;

public class CPacketHandlerAnimationUpdateOfEntity extends AbstractPacketHandler<SPacketUpdateAnimationOfEntity> {

	@Override
	protected void execHandlePacket(SPacketUpdateAnimationOfEntity message, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity instanceof IServerAnimationReceiver) {
			IServerAnimationReceiver animationReceiver = (IServerAnimationReceiver) entity;

			animationReceiver.processAnimationUpdate(message.getAnimationID());
		}
	}

}
