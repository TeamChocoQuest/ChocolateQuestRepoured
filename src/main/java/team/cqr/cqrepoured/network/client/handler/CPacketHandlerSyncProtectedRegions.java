package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

import java.util.function.Supplier;

public class CPacketHandlerSyncProtectedRegions extends AbstractPacketHandler<SPacketSyncProtectedRegions> {

	@Override
	protected void execHandlePacket(SPacketSyncProtectedRegions message, Supplier<Context> context, World world, PlayerEntity player) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
		PacketBuffer buf = message.getBuffer();

		if (buf.readBoolean()) {
			protectedRegionManager.clearProtectedRegions();
		}

		int protectedRegionsCount = buf.readShort();
		for (int i = 0; i < protectedRegionsCount; i++) {
			protectedRegionManager.addProtectedRegion(new ProtectedRegion(world, buf));
		}
	}

}
