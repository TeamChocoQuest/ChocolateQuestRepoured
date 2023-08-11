package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.inventory.IInteractable;
import de.dertoaster.multihitboxlib.api.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;

import java.util.function.Supplier;

public class SPacketHandlerContainerClickButton extends AbstractPacketHandler<CPacketContainerClickButton> {

	@Override
	protected void execHandlePacket(CPacketContainerClickButton packet, Supplier<Context> context, Level world, Player player) {
		if (player.containerMenu instanceof IInteractable) {
			((IInteractable) player.containerMenu).onClickButton(player, packet.getButton(), packet.getExtraData());
		}
	}

}
