package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

public class SPacketHandlerStructureSelector extends AbstractPacketHandler<CPacketStructureSelector> {

	@Override
	public void handlePacket(CPacketStructureSelector packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			ItemStack stack = player.getItemInHand(packet.getHand());

			if (stack.getItem() instanceof ItemStructureSelector) {
				((ItemStructureSelector) stack.getItem()).setFirstPos(stack, player.blockPosition());
			}
		});
		context.get().setPacketHandled(true);
	}

}
