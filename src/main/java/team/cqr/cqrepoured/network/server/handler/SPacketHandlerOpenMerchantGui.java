package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.CQRNetworkHooks;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;

public class SPacketHandlerOpenMerchantGui extends AbstractPacketHandler<CPacketOpenMerchantGui> {

	@Override
	protected void execHandlePacket(CPacketOpenMerchantGui packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());

		if (entity instanceof AbstractEntityCQR && player instanceof ServerPlayerEntity) {
			ServerPlayerEntity spe = (ServerPlayerEntity)player;
			//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, world, packet.getEntityId(), 0, 0);
			CQRNetworkHooks.openGUI(spe, entity.getDisplayName(), buf -> buf.writeInt(entity.getId()), CQRContainerTypes.MERCHANT.get());
		}		
	}

}
