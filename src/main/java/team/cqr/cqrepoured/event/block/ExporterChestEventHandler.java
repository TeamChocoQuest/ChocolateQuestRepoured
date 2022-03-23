package team.cqr.cqrepoured.event.block;

import net.minecraft.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.block.BlockExporterChest;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class ExporterChestEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
		if (event.getPlacedBlock().getBlock() == Blocks.CHEST && !BlockExporterChest.canPlaceChestAt(event.getWorld(), event.getPos())) {
			event.setCanceled(true);
		}
	}

}
