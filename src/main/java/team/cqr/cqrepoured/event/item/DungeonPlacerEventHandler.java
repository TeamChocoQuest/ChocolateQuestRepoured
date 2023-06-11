package team.cqr.cqrepoured.event.item;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketDungeonSync;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;

@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonPlacerEventHandler {

	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide()) {
			CQRMain.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getPlayer()), new SPacketDungeonSync(DungeonRegistry.getInstance().getDungeons()));
			//CQRMain.NETWORK.sendTo(new SPacketDungeonSync(DungeonRegistry.getInstance().getDungeons()), (ServerPlayerEntity) event.player);
		}
	}

}
