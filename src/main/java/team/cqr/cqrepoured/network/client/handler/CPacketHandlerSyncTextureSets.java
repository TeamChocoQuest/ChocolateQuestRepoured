package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.customtextures.ClientPacketHandler;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketCustomTextures;

import java.util.function.Supplier;

public class CPacketHandlerSyncTextureSets extends AbstractPacketHandler<SPacketCustomTextures> {

	@Override
	protected void execHandlePacket(SPacketCustomTextures message, Supplier<Context> context, World world, PlayerEntity player) {
		ClientPacketHandler.handleCTPacketClientside(message);
	}

}
