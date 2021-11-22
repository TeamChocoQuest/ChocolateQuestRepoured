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
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				if (world == null) {
					return;
				}

				Entity entity = world.getEntityByID(message.getEntityId());
				if (entity == null) {
					return;
				}

				CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
				if (cap == null) {
					return;
				}

				// int charge = message.getElectroCharge();
				int charge = message.getIsElectrocutionActive() ? 20 : 0;
				cap.setRemainingTicks(charge);
				if (message.hasTarget()) {
					Entity target = world.getEntityByID(message.getEntityIdTarget());
					cap.setTarget(target);
				} else {
					cap.setTarget(null);
				}
			});
		}
		return null;
	}

}
