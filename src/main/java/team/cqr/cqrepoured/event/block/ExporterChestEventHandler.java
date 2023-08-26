package team.cqr.cqrepoured.event.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.block.BlockExporterChest;

//@EventBusSubscriber(modid = CQRConstants.MODID)
public class ExporterChestEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
		if (event.getPlacedBlock().getBlock() == Blocks.CHEST && !BlockExporterChest.canSupportRigidBlock(event.getWorld(), event.getPos())) {
			event.setCanceled(true);
		}
	}

}
