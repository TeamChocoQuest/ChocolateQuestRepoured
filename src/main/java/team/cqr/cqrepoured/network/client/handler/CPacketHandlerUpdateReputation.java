package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;

import java.util.function.Supplier;

public class CPacketHandlerUpdateReputation extends AbstractPacketHandler<SPacketUpdatePlayerReputation> {

	@Override
	protected void execHandlePacket(SPacketUpdatePlayerReputation message, Supplier<Context> context, World world, PlayerEntity player) {
		FactionRegistry FAC_REG = FactionRegistry.instance(world);
		try {
			Faction faction = FAC_REG.getFactionInstance(message.getFaction());
			if (faction != null) {
				FAC_REG.setReputation(message.getPlayerId(), message.getScore(), faction);
			}
		} catch (Exception ex) {
			// IGNORE
			ex.printStackTrace();
		}
	}

}
