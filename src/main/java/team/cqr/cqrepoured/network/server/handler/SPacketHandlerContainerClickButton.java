package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.inventory.IInteractable;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;

public class SPacketHandlerContainerClickButton extends AbstractPacketHandler<CPacketContainerClickButton> {

	@Override
	public void handlePacket(CPacketContainerClickButton packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			PlayerEntity player = context.get().getSender();
			if (player.containerMenu instanceof IInteractable) {
				((IInteractable) player.containerMenu).onClickButton(player, packet.getButton(), packet.getExtraData());
			}
		});
		context.get().setPacketHandled(true);
	}

}
