package team.cqr.cqrepoured.network.client.handler;

import java.util.Map;
import java.util.function.Supplier;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.capability.armor.CapabilityArmorCooldown;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.network.server.packet.SPacketArmorCooldownSync;

public class CPacketHandlerArmorCooldownSync extends AbstractPacketHandler<SPacketArmorCooldownSync> {

	@Override
	protected void execHandlePacket(SPacketArmorCooldownSync message, Supplier<Context> context, Level world, Player player) {
		LazyOptional<CapabilityArmorCooldown> lOpCap = player.getCapability(CQRCapabilities.CAPABILITY_ITEM_COOLDOWN_CQR, null);

		if (lOpCap.isPresent()) {
			CapabilityArmorCooldown icapability = lOpCap.resolve().get();
			for (Map.Entry<Item, Integer> entry : message.getItemCooldownMap().entrySet()) {
				icapability.setCooldown(entry.getKey(), entry.getValue());
			}
		}
	}

}
