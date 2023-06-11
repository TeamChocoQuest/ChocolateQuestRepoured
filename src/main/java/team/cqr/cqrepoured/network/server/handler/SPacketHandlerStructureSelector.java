package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

public class SPacketHandlerStructureSelector extends AbstractPacketHandler<CPacketStructureSelector> {

	@Override
	protected void execHandlePacket(CPacketStructureSelector packet, Supplier<Context> context, World world, PlayerEntity player) {
		ItemStack stack = player.getItemInHand(packet.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			((ItemStructureSelector) stack.getItem()).setFirstPos(stack, player.blockPosition(), player);
		}
	}

}
