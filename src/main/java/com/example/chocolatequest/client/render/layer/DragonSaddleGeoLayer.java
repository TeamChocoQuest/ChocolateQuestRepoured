package com.example.chocolatequest.client.render.layer;

import com.example.chocolatequest.entity.boss.EntityCQRDragon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/** Renders the dragon's saddle using Minecraft's built-in pig saddle texture. */
public class DragonSaddleGeoLayer extends GeoRenderLayer<EntityCQRDragon> {
    private static final ResourceLocation SADDLE_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/pig/pig_saddle.png");

    public DragonSaddleGeoLayer(GeoRenderer<EntityCQRDragon> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, EntityCQRDragon dragon, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
                       float partialTick, int packedLight, int packedOverlay) {
        if (!dragon.isSaddled()) return;

        GeoBone saddleBone = this.getRenderer().getGeoModel().getBone("dragonSaddle").orElse(null);
        if (saddleBone == null) return;

        saddleBone.setHidden(false);
        RenderType saddleRenderType = RenderType.entityCutoutNoCull(SADDLE_TEXTURE);
        VertexConsumer saddleBuffer = bufferSource.getBuffer(saddleRenderType);
        this.getRenderer().renderRecursively(poseStack, dragon, saddleBone, saddleRenderType,
                bufferSource, saddleBuffer, true, partialTick, packedLight, packedOverlay, -1);
        saddleBone.setHidden(true);
    }
}
