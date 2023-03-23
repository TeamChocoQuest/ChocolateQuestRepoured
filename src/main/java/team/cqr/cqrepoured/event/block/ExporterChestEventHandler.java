package team.cqr.cqrepoured.event.block;

import net.minecraft.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.BlockExporterChest;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class ExporterChestEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
		if (event.getPlacedBlock().getBlock() == Blocks.CHEST && !BlockExporterChest.canSupportRigidBlock(event.getWorld(), event.getPos())) {
			event.setCanceled(true);
		}
	}

}
