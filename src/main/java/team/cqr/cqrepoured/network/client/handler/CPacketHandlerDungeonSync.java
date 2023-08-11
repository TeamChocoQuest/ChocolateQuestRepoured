package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;

public class CPacketHandlerDungeonSync extends AbstractPacketHandler<SPacketDungeonSync> {

	@Override
	protected void execHandlePacket(SPacketDungeonSync message, Supplier<Context> context, Level world, Player player) {
		//ItemDungeonPlacer.updateClientDungeonList(message.getFakeDungeonList());
	}

}
