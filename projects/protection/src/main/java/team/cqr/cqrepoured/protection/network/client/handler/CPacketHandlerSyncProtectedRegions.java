package team.cqr.cqrepoured.protection.network.client.handler;

import java.util.function.Supplier;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.protection.ProtectedRegion;
import team.cqr.cqrepoured.protection.ProtectedRegionManager;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketSyncProtectedRegions;

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
