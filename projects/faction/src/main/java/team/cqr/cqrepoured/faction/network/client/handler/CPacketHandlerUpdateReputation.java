package team.cqr.cqrepoured.faction.network.client.handler;

import java.util.function.Supplier;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.faction.network.server.packet.SPacketUpdatePlayerReputation;

public class CPacketHandlerUpdateReputation extends AbstractPacketHandler<SPacketUpdatePlayerReputation> {

	@Override
	protected void execHandlePacket(SPacketUpdatePlayerReputation message, Supplier<Context> context, Level world, Player player) {
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
