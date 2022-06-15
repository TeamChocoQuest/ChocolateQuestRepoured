package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

@EventBusSubscriber(modid = CQRMain.MODID)
public class StructureSelectorEventHandler {

	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		PlayerEntity player = event.getPlayer();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			if (!player.level.isClientSide()) {
				ItemStructureSelector structureSelector = (ItemStructureSelector) stack.getItem();

				if (player.isCrouching()) {
					BlockPos pos = player.blockPosition();
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new StringTextComponent("First position set to " + pos), null);
				} else {
					BlockPos pos = event.getPos();
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new StringTextComponent("First position set to " + pos), null);
				}
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty event) {
		PlayerEntity player = event.getPlayer();
		ItemStack stack = player.getItemInHand(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector && player.isCrouching()) {
			CQRMain.NETWORK.sendToServer(new CPacketStructureSelector(event.getHand()));
		}
	}

}
