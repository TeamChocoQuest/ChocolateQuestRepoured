package team.cqr.cqrepoured.network.server.handler;

import java.util.function.Supplier;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.network.AbstractPacketHandler;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;

public class SPacketHandlerOpenMerchantGui extends AbstractPacketHandler<CPacketOpenMerchantGui> {

	@Override
	protected void execHandlePacket(CPacketOpenMerchantGui packet, Supplier<Context> context, World world, PlayerEntity player) {
		Entity entity = world.getEntity(packet.getEntityId());

		if (entity instanceof AbstractEntityCQR && player instanceof ServerPlayerEntity) {
			ServerPlayerEntity spe = (ServerPlayerEntity)player;
			//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, world, packet.getEntityId(), 0, 0);
			NetworkHooks.openGui(spe, new INamedContainerProvider() {
				
				@Override
				public Container createMenu(int windowId, PlayerInventory invPlayer, PlayerEntity lePlayer) {
					PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
					buf.writeInt(entity.getId());
					return CQRContainerTypes.MERCHANT.get().create(windowId, invPlayer, buf);
				}
				
				@Override
				public ITextComponent getDisplayName() {
					return entity.getDisplayName();
				}
			});
		}		
	}

}
