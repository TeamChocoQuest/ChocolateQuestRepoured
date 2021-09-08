package team.cqr.cqrepoured.network.client.handler;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateElectrocuteCapability;

public class CPacketHandlerUpdateElectrocuteCapability implements IMessageHandler<SPacketUpdateElectrocuteCapability, IMessage> {

	@Override
	public IMessage onMessage(SPacketUpdateElectrocuteCapability message, MessageContext ctx) {
		if(ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				Entity entity = world.getEntityByID(message.getEntityId());

				if (entity != null) {
					CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
					if(cap != null) {
						int charge = message.getElectroCharge();
						cap.setRemainingTicks(charge);
						if(message.hasTarget()) {
							Entity target = world.getEntityByID(message.getEntityIdTarget());
							cap.setTarget(target);
						} else {
							cap.setTarget(null);
						}
					}
				}
			});
		}
		return null;
	}

}
