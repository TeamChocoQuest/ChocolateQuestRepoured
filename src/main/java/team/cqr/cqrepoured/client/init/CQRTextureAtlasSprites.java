package team.cqr.cqrepoured.client.init;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import team.cqr.cqrepoured.CQRMain;

@EventBusSubscriber(modid = CQRMain.MODID, value = Side.CLIENT)
public class CQRTextureAtlasSprites {

	@SubscribeEvent
	public static void registerTextureAtlasSprites(TextureStitchEvent.Pre event) {
		AtlasTexture map = event.getMap();

		map.registerSprite(new ResourceLocation(CQRMain.MODID, "items/empty_slot_sword"));
		map.registerSprite(new ResourceLocation(CQRMain.MODID, "items/empty_slot_potion"));
		map.registerSprite(new ResourceLocation(CQRMain.MODID, "items/empty_slot_badge"));
		map.registerSprite(new ResourceLocation(CQRMain.MODID, "items/empty_slot_arrow"));
	}

}
