package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUnloadProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class CPacketHandlerUnloadProtectedRegion extends AbstractPacketHandler<SPacketUnloadProtectedRegion> {

	@Override
	protected void execHandlePacket(SPacketUnloadProtectedRegion message, Supplier<Context> context, World world, PlayerEntity player) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);

		if (protectedRegionManager != null) {
			protectedRegionManager.removeProtectedRegion(message.getUuid());
		}
	}

}
