package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdatePlayerReputation;

public class CPacketHandlerUpdateReputation implements IMessageHandler<SPacketUpdatePlayerReputation, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdatePlayerReputation message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
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
			});
		}
		return null;
	}

}
