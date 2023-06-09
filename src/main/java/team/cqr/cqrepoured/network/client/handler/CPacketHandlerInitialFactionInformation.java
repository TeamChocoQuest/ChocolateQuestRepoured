package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketInitialFactionInformation;

import java.util.function.Supplier;

public class CPacketHandlerInitialFactionInformation extends AbstractPacketHandler<SPacketInitialFactionInformation> {

	@Override
	protected void execHandlePacket(SPacketInitialFactionInformation message, Supplier<Context> context, Level world, Player player) {
		FactionRegistry FAC_REG = FactionRegistry.instance(world);
		for (Faction faction : message.getFactions()) {
			FAC_REG.addFaction(faction);
		}
		for (Tuple<Faction, Integer> rd : message.getReputations()) {
			FAC_REG.setReputation(message.getPlayerId(), rd.getB(), rd.getA());
		}
	}

}
