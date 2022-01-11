package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.inventory.IInteractable;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;

public class SPacketHandlerContainerClickButton extends AbstractPacketHandler<CPacketContainerClickButton> {

	@Override
	protected void execHandlePacket(CPacketContainerClickButton packet, Supplier<Context> context, World world, PlayerEntity player) {
		if (player.containerMenu instanceof IInteractable) {
			((IInteractable) player.containerMenu).onClickButton(player, packet.getButton(), packet.getExtraData());
		}
	}

}
