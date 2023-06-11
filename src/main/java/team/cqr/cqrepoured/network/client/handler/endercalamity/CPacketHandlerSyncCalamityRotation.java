package team.cqr.cqrepoured.network.client.handler.endercalamity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;

import java.util.function.Supplier;

public class CPacketHandlerSyncCalamityRotation extends AbstractPacketHandler<SPacketSyncCalamityRotation> {

	@Override
	protected void execHandlePacket(SPacketSyncCalamityRotation packet, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(packet.getEntityId());
		
		if(entity instanceof EntityCQREnderCalamity) {
			((EntityCQREnderCalamity)entity).serverRotationPitchCQR = packet.getPitch();
		}
	}

}
