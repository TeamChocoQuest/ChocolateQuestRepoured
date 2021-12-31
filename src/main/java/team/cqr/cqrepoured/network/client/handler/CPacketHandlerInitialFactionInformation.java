package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.network.server.packet.SPacketInitialFactionInformation;

public class CPacketHandlerInitialFactionInformation implements IMessageHandler<SPacketInitialFactionInformation, IMessage> {

	@Override
	public IMessage onMessage(SPacketInitialFactionInformation message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				FactionRegistry FAC_REG = FactionRegistry.instance(world);
				for (Faction faction : message.getFactions()) {
					FAC_REG.addFaction(faction);
				}
				for (Tuple<Faction, Integer> rd : message.getReputations()) {
					FAC_REG.setReputation(message.getPlayerId(), rd.getSecond(), rd.getFirst());
				}
			});
		}
		return null;
	}

}
