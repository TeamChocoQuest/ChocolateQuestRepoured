package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class CPacketHandlerSyncProtectedRegions extends AbstractPacketHandler<SPacketSyncProtectedRegions> {

	@Override
	protected void execHandlePacket(SPacketSyncProtectedRegions message, Supplier<Context> context, Level world, Player player) {
		IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
		FriendlyByteBuf buf = message.getBuffer();

		if (buf.readBoolean()) {
			protectedRegionManager.clearProtectedRegions();
		}

		int protectedRegionsCount = buf.readShort();
		for (int i = 0; i < protectedRegionsCount; i++) {
			//protectedRegionManager.addProtectedRegion(new ProtectedRegion(world, buf));
			protectedRegionManager.addProtectedRegion(buf.readJsonWithCodec(ProtectedRegion.CODEC));
		}
	}

}
