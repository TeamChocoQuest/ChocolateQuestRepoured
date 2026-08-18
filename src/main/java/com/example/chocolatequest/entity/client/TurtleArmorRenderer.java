package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.item.TurtleArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import net.minecraft.world.entity.EquipmentSlot;

public class TurtleArmorRenderer extends GeoArmorRenderer<TurtleArmorItem> {
    public TurtleArmorRenderer() {
        super(new TurtleArmorModel());
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
    }

    @Override
    public net.minecraft.client.renderer.RenderType getRenderType(TurtleArmorItem animatable, net.minecraft.resources.ResourceLocation texture, net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick) {
        return net.minecraft.client.renderer.RenderType.entityCutoutNoCull(texture);
    }
}

