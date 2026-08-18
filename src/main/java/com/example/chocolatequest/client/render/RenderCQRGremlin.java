package com.example.chocolatequest.client.render;

import com.example.chocolatequest.client.model.ModelCQRGremlinGeo;
import com.example.chocolatequest.entity.mob.CQGremlinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderCQRGremlin extends RenderCQRBipedBaseGeo<CQGremlinEntity> {
    public RenderCQRGremlin(EntityRendererProvider.Context context) {
        super(context, new ModelCQRGremlinGeo());
    }

    @Override
    protected boolean shouldRenderBuiltInCape(CQGremlinEntity entity) {
        // The regular gremlin and the boss share this renderer, but only the
        // Gremlin Shaman owns the goblin-shaman cape texture.
        return entity instanceof com.example.chocolatequest.entity.boss.EntityCQRGremlinShaman;
    }

    @Override
    public void renderRecursively(com.mojang.blaze3d.vertex.PoseStack poseStack, CQGremlinEntity animatable, software.bernie.geckolib.cache.object.GeoBone bone, net.minecraft.client.renderer.RenderType renderType, net.minecraft.client.renderer.MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("bipedCape") && !isReRender) {
            net.minecraft.resources.ResourceLocation capeTexture = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "textures/entity/cape/cape_goblinshaman.png");
            net.minecraft.client.renderer.RenderType capeRenderType = net.minecraft.client.renderer.RenderType.entityCutoutNoCull(capeTexture);
            com.mojang.blaze3d.vertex.VertexConsumer capeBuffer = bufferSource.getBuffer(capeRenderType);
            super.renderRecursively(poseStack, animatable, bone, capeRenderType, bufferSource, capeBuffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        }
    }
}
