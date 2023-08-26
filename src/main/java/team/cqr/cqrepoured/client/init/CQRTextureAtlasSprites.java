package team.cqr.cqrepoured.client.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;

@EventBusSubscriber(bus = Bus.MOD, modid = CQRConstants.MODID, value = Dist.CLIENT)
public class CQRTextureAtlasSprites {

	@SubscribeEvent
	public static void registerTextureAtlasSprites(TextureStitchEvent.Pre event) {
		event.addSprite(ContainerCQREntity.EMPTY_SLOT_ARROW);
		event.addSprite(ContainerCQREntity.EMPTY_SLOT_BADGE);
		event.addSprite(ContainerCQREntity.EMPTY_SLOT_MAIN_HAND);
		event.addSprite(ContainerCQREntity.EMPTY_SLOT_OFF_HAND);
		event.addSprite(ContainerCQREntity.EMPTY_SLOT_POTION);
		
	}

}
