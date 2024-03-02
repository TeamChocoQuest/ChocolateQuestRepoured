package team.cqr.cqrepoured.protection.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketUnloadProtectedRegion;

import java.util.function.Supplier;

public class CPacketHandlerUnloadProtectedRegion extends AbstractPacketHandler<SPacketUnloadProtectedRegion> {

	@Override
	protected void execHandlePacket(SPacketUnloadProtectedRegion message, Supplier<Context> context, Level world, Player player) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

		if (protectedRegionManager != null) {
			protectedRegionManager.removeProtectedRegion(message.getUuid());
		}
	}

}
