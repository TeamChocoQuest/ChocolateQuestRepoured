package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.item.SlimeArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import net.minecraft.world.entity.EquipmentSlot;

public class SlimeArmorRenderer extends GeoArmorRenderer<SlimeArmorItem> {
    public SlimeArmorRenderer() {
        super(new SlimeArmorModel());
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
    }

    @Override
    public net.minecraft.client.renderer.RenderType getRenderType(SlimeArmorItem animatable, net.minecraft.resources.ResourceLocation texture, net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick) {
        // This makes the armor render as translucent (glass-like) instead of opaque/cutout!
        return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
    }
}
