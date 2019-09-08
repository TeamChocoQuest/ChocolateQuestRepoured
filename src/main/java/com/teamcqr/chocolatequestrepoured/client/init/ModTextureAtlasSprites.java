package com.teamcqr.chocolatequestrepoured.client.init;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ModTextureAtlasSprites {

	@SubscribeEvent
	public static void registerTextureAtlasSprites(TextureStitchEvent.Pre event) {
		TextureMap map = event.getMap();

		map.registerSprite(new ResourceLocation(Reference.MODID, "items/empty_slot_sword"));
		map.registerSprite(new ResourceLocation(Reference.MODID, "items/empty_slot_potion"));
		map.registerSprite(new ResourceLocation(Reference.MODID, "items/empty_slot_badge"));
	}

}
