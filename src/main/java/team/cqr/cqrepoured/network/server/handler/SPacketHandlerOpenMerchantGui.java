package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.util.GuiHandler;

public class SPacketHandlerOpenMerchantGui extends AbstractPacketHandler<CPacketOpenMerchantGui> {

	@Override
	protected void execHandlePacket(CPacketOpenMerchantGui packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());

		if (entity instanceof AbstractEntityCQR) {
			player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, world, packet.getEntityId(), 0, 0);
		}		
	}

}
