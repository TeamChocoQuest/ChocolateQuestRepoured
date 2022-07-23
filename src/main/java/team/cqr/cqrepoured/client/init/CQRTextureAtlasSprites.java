package team.cqr.cqrepoured.client.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;

@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class CQRTextureAtlasSprites {

	@SubscribeEvent
	public static void registerTextureAtlasSprites(TextureStitchEvent.Pre event) {
		event.addSprite(CQRMain.prefix("items/empty_slot_sword"));
		event.addSprite(CQRMain.prefix("items/empty_slot_potion"));
		event.addSprite(CQRMain.prefix("items/empty_slot_badge"));
		event.addSprite(CQRMain.prefix("items/empty_slot_arrow"));
	}

}
