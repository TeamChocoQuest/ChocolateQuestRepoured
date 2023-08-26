package team.cqr.cqrepoured.event.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class StructureSelectorEventHandler {

	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			if (!player.level().isClientSide()) {
				((ItemStructureSelector) stack.getItem()).setFirstPos(stack, player.isCrouching() ? player.blockPosition() : event.getPos(), player);
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector && player.isCrouching()) {
			CQRMain.NETWORK.sendToServer(new CPacketStructureSelector(event.getHand()));
		}
	}

}
