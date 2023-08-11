package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.client.world.structure.protection.ProtectionIndicatorHelper;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

import java.util.function.Supplier;

public class CPacketHandlerAddOrResetProtectedRegionIndicator extends AbstractPacketHandler<SPacketAddOrResetProtectedRegionIndicator> {

	@Override
	protected void execHandlePacket(SPacketAddOrResetProtectedRegionIndicator message, Supplier<Context> context, Level world, Player player) {
		ProtectionIndicatorHelper.addOrResetProtectedRegionIndicator(world, message.getUuid(), message.getStart(), message.getEnd(), message.getPos(), null);
	}

}
