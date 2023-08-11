package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

public class SPacketHandlerStructureSelector extends AbstractPacketHandler<CPacketStructureSelector> {

	@Override
	protected void execHandlePacket(CPacketStructureSelector packet, Supplier<Context> context, Level world, Player player) {
		ItemStack stack = player.getItemInHand(packet.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			((ItemStructureSelector) stack.getItem()).setFirstPos(stack, player.blockPosition(), player);
		}
	}

}
