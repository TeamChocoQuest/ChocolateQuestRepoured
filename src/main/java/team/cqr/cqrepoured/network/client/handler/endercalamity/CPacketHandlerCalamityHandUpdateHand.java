package team.cqr.cqrepoured.network.client.handler.endercalamity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;

import java.util.function.Supplier;

public class CPacketHandlerCalamityHandUpdateHand extends AbstractPacketHandler<SPacketCalamityUpdateHand> {

	@Override
	protected void execHandlePacket(SPacketCalamityUpdateHand packet, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(packet.getEntityId());
		
		if(entity instanceof EntityCQREnderCalamity) {
			((EntityCQREnderCalamity)entity).processHandUpdates(packet.getHandStates());
		}
	}

}
