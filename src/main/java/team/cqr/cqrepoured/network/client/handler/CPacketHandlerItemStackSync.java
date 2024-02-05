package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;

public class CPacketHandlerItemStackSync extends AbstractPacketHandler<SPacketItemStackSync> {

	@Override
	protected void execHandlePacket(SPacketItemStackSync message, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity != null) {
			LazyOptional<CapabilityExtraItemHandler> lOpCap = entity.getCapability(CQRCapabilities.EXTRA_ITEM_HANDLER, null);

			if (lOpCap.isPresent()) {
				CapabilityExtraItemHandler capability = lOpCap.resolve().get();
				int slot = message.getSlotIndex();

				if (slot >= 0 && slot < capability.getSlots()) {
					capability.setStackInSlot(slot, message.getStack());
				}
			}
		}
	}

}
