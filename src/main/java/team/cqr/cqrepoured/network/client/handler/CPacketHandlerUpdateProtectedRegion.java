package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

import java.util.function.Supplier;

public class CPacketHandlerUpdateProtectedRegion extends AbstractPacketHandler<SPacketUpdateProtectedRegion> {

	@Override
	protected void execHandlePacket(SPacketUpdateProtectedRegion message, Supplier<Context> context, Level world, Player player) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

		if (protectedRegionManager != null) {
			ProtectedRegion protectedRegion = new ProtectedRegion(world, message.getBuffer());
			protectedRegionManager.removeProtectedRegion(protectedRegion.getUuid());
			protectedRegionManager.addProtectedRegion(protectedRegion);
		}		
	}

}
