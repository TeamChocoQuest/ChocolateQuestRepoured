package team.cqr.cqrepoured.network.client.handler;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandler;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

public class CPacketHandlerArmorCooldownSync implements IMessageHandler<SPacketArmorCooldownSync, IMessage> {

	@Override
	public IMessage onMessage(SPacketArmorCooldownSync message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
				CapabilityCooldownHandler icapability = player.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);

				if (icapability != null) {
					for (Map.Entry<Item, Integer> entry : message.getItemCooldownMap().entrySet()) {
						icapability.setCooldown(entry.getKey(), entry.getValue());
					}
				}
			});
		}
		return null;
	}

}
