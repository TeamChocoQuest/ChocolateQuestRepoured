package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.customtextures.ClientPacketHandler;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketCustomTextures;

import java.util.function.Supplier;

public class CPacketHandlerSyncTextureSets extends AbstractPacketHandler<SPacketCustomTextures> {

	@Override
	protected void execHandlePacket(SPacketCustomTextures message, Supplier<Context> context, Level world, Player player) {
		ClientPacketHandler.handleCTPacketClientside(message);
	}

}
