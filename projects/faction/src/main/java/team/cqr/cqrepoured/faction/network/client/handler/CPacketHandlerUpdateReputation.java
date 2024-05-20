package team.cqr.cqrepoured.faction.network.client.handler;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.faction.capability.IFactionRelationCapability;
import team.cqr.cqrepoured.faction.init.FactionCapabilities;
import team.cqr.cqrepoured.faction.network.server.packet.SPacketUpdatePlayerReputation;

public class CPacketHandlerUpdateReputation extends AbstractPacketHandler<SPacketUpdatePlayerReputation> {

	@Override
	protected void execHandlePacket(SPacketUpdatePlayerReputation message, Supplier<Context> context, Level world, Player player) {
		if (player == null) 
			return;
		
		// First: Grab capability for relations
		@NotNull LazyOptional<IFactionRelationCapability> lOpCap = player.getCapability(FactionCapabilities.FACTION_RELATION);
		if (lOpCap.isPresent()) {
			Optional<IFactionRelationCapability> opCap = lOpCap.resolve();
			if (opCap.isEmpty())
				return;
			
			// We do have data!
			opCap.ifPresent(cap -> {
				cap.setReputationTowards(message.getFaction(), message.getScore());
			});
		}
	}

}
