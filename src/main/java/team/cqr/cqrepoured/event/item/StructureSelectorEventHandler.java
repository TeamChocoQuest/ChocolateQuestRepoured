package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class StructureSelectorEventHandler {

	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event) {
		PlayerEntity player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItem(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector) {
			if (!player.world.isRemote) {
				ItemStructureSelector structureSelector = (ItemStructureSelector) stack.getItem();

				if (player.isSneaking()) {
					BlockPos pos = new BlockPos(player);
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new StringTextComponent("First position set to " + pos));
				} else {
					BlockPos pos = event.getPos();
					structureSelector.setFirstPos(stack, pos);
					player.sendMessage(new StringTextComponent("First position set to " + pos));
				}
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLeftClickEmptyEvent(PlayerInteractEvent.LeftClickEmpty event) {
		PlayerEntity player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItem(event.getHand());

		if (stack.getItem() instanceof ItemStructureSelector && player.isSneaking()) {
			CQRMain.NETWORK.sendToServer(new CPacketStructureSelector(event.getHand()));
		}
	}

}
