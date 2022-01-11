package team.cqr.cqrepoured.network.client.handler;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandler;
import team.cqr.cqrepoured.capability.armor.CapabilityCooldownHandlerProvider;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

public class CPacketHandlerArmorCooldownSync extends AbstractPacketHandler<SPacketArmorCooldownSync> {

	@Override
	protected void execHandlePacket(SPacketArmorCooldownSync message, Supplier<Context> context, World world, PlayerEntity player) {
		LazyOptional<CapabilityCooldownHandler> lOpCap = player.getCapability(CapabilityCooldownHandlerProvider.CAPABILITY_ITEM_COOLDOWN_CQR, null);

		if (lOpCap.isPresent()) {
			CapabilityCooldownHandler icapability = lOpCap.resolve().get();
			for (Map.Entry<Item, Integer> entry : message.getItemCooldownMap().entrySet()) {
				icapability.setCooldown(entry.getKey(), entry.getValue());
			}
		}
	}

}
