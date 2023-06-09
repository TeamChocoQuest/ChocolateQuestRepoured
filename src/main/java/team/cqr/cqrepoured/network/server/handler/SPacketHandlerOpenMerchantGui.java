package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.CQRNetworkHooks;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;

public class SPacketHandlerOpenMerchantGui extends AbstractPacketHandler<CPacketOpenMerchantGui> {

	@Override
	protected void execHandlePacket(CPacketOpenMerchantGui packet, Supplier<Context> context, Level world, Player player) {
		Entity entity = world.getEntity(packet.getEntityId());

		if (entity instanceof AbstractEntityCQR && player instanceof ServerPlayer) {
			ServerPlayer spe = (ServerPlayer)player;
			//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, world, packet.getEntityId(), 0, 0);
			CQRNetworkHooks.openGUI(spe, entity.getDisplayName(), buf -> buf.writeInt(entity.getId()), CQRContainerTypes.MERCHANT.get());
		}		
	}

}
