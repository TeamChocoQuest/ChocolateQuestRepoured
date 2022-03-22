package team.cqr.cqrepoured.network.client.handler.endercalamity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;

import java.util.function.Supplier;

public class CPacketHandlerCalamityHandUpdateHand extends AbstractPacketHandler<SPacketCalamityUpdateHand> {

	@Override
	protected void execHandlePacket(SPacketCalamityUpdateHand packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());
		
		if(entity instanceof EntityCQREnderCalamity) {
			((EntityCQREnderCalamity)entity).processHandUpdates(packet.getHandStates());
		}
	}

}
