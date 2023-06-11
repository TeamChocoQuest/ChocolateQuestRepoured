package team.cqr.cqrepoured.network.client.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;

public class CPacketHandlerItemStackSync extends AbstractPacketHandler<SPacketItemStackSync> {

	@Override
	protected void execHandlePacket(SPacketItemStackSync message, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(message.getEntityId());

		if (entity != null) {
			LazyOptional<CapabilityExtraItemHandler> lOpCap = entity.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);

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
