package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;

//@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonPlacerEventHandler {

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			CQRMain.NETWORK.sendTo(new SPacketDungeonSync(DungeonRegistry.getInstance().getDungeons()), (ServerPlayerEntity) event.player);
		}
	}

}
