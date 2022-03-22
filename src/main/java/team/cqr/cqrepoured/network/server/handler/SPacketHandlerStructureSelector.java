package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

import java.util.function.Supplier;

public class SPacketHandlerStructureSelector extends AbstractPacketHandler<CPacketStructureSelector> {

	@Override
	protected void execHandlePacket(CPacketStructureSelector packet, Supplier<Context> context, World world, PlayerEntity player) {
		ItemStack stack = player.getItemInHand(packet.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			((ItemStructureSelector) stack.getItem()).setFirstPos(stack, player.blockPosition());
		}
	}

}
