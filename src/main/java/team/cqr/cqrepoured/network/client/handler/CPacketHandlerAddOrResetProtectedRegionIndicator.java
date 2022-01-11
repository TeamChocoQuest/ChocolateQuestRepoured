package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.client.world.structure.protection.ProtectionIndicatorHelper;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketAddOrResetProtectedRegionIndicator;

public class CPacketHandlerAddOrResetProtectedRegionIndicator extends AbstractPacketHandler<SPacketAddOrResetProtectedRegionIndicator> {

	@Override
	protected void execHandlePacket(SPacketAddOrResetProtectedRegionIndicator message, Supplier<Context> context, World world, PlayerEntity player) {
		ProtectionIndicatorHelper.addOrResetProtectedRegionIndicator(world, message.getUuid(), message.getStart(), message.getEnd(), message.getPos(), null);
	}

}
