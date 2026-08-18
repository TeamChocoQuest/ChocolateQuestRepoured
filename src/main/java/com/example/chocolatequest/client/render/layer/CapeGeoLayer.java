package com.example.chocolatequest.client.render.layer;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CapeGeoLayer<T extends AbstractEntityCQR> extends GeoRenderLayer<T> {
    private final ResourceLocation capeTexture;

    public CapeGeoLayer(GeoRenderer<T> entityRendererIn, ResourceLocation capeTexture) {
        super(entityRendererIn);
        this.capeTexture = capeTexture;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        GeoBone capeBone = this.getRenderer().getGeoModel().getBone("bipedCape").orElse(null);
        if (capeBone != null) {
            capeBone.setHidden(false); // Unhide just for this layer
            RenderType capeRenderType = RenderType.entityCutoutNoCull(this.capeTexture);
            VertexConsumer capeBuffer = bufferSource.getBuffer(capeRenderType);
            this.getRenderer().renderRecursively(poseStack, animatable, capeBone, capeRenderType, bufferSource, capeBuffer, true, partialTick, packedLight, packedOverlay, -1);
            capeBone.setHidden(true); // Hide again so base renderer doesn't render it with body texture
        }
    }
}
