package team.cqr.cqrepoured.protection.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.protection.ProtectionIndicatorHelper;
import team.cqr.cqrepoured.protection.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;

import java.util.function.Supplier;

public class CPacketHandlerAddOrResetProtectedRegionIndicator extends AbstractPacketHandler<SPacketAddOrResetProtectedRegionIndicator> {

	@Override
	protected void execHandlePacket(SPacketAddOrResetProtectedRegionIndicator message, Supplier<Context> context, Level world, Player player) {
		ProtectionIndicatorHelper.addOrResetProtectedRegionIndicator(world, message.getUuid(), message.getStart(), message.getEnd(), message.getPos(), null);
	}

}
