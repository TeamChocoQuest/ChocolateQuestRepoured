package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRSpectreGeo;
import com.example.chocolatequest.entity.mob.CQSpecterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.util.Color;

public class RenderCQRSpectre extends RenderCQRBipedBaseGeo<CQSpecterEntity> {
    public RenderCQRSpectre(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQRSpectreGeo());
    }

    @Override
    public Color getRenderColor(CQSpecterEntity animatable, float partialTick, int packedLight) {
        // Semi-transparent ghost effect (50% alpha)
        return Color.ofRGBA(255, 255, 255, 128);
    }

    @Override
    public net.minecraft.client.renderer.RenderType getRenderType(CQSpecterEntity animatable, net.minecraft.resources.ResourceLocation texture, net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick) {
        return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
    }
}
