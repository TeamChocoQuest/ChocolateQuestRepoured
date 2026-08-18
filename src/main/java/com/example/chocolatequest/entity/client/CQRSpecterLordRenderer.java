package com.example.chocolatequest.entity.client;

import com.example.chocolatequest.client.model.ModelCQRSpecterLordGeo;
import com.example.chocolatequest.client.render.RenderCQRBipedBaseGeo;
import com.example.chocolatequest.entity.boss.EntityCQRSpecterLord;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.util.Color;

public class CQRSpecterLordRenderer extends RenderCQRBipedBaseGeo<EntityCQRSpecterLord> {

    public CQRSpecterLordRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCQRSpecterLordGeo());
    }

    @Override
    protected boolean shouldRenderBuiltInCape(EntityCQRSpecterLord entity) {
        return true;
    }

    @Override
    public Color getRenderColor(EntityCQRSpecterLord animatable, float partialTick, int packedLight) {
        if (animatable.isVulnerable()) {
            return Color.ofRGBA(255, 255, 255, 255); // Solid when vulnerable
        } else {
            return Color.ofRGBA(255, 255, 255, 128); // Translucent otherwise
        }
    }

    

    @Override
    public net.minecraft.client.renderer.RenderType getRenderType(EntityCQRSpecterLord animatable, net.minecraft.resources.ResourceLocation texture, net.minecraft.client.renderer.MultiBufferSource bufferSource, float partialTick) {
        if (animatable.isVulnerable()) {
            return net.minecraft.client.renderer.RenderType.entityCutoutNoCull(texture);
        } else {
            return net.minecraft.client.renderer.RenderType.entityTranslucent(texture);
        }
    }

    @Override
    public void renderRecursively(com.mojang.blaze3d.vertex.PoseStack poseStack, EntityCQRSpecterLord animatable, software.bernie.geckolib.cache.object.GeoBone bone, net.minecraft.client.renderer.RenderType renderType, net.minecraft.client.renderer.MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (bone.getName().equals("bipedCape") && !isReRender) {
            net.minecraft.resources.ResourceLocation capeTexture = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.example.chocolatequest.ChocolateQuestReDone.MODID, "textures/entity/cape/cape_specterlord.png");
            net.minecraft.client.renderer.RenderType capeRenderType = net.minecraft.client.renderer.RenderType.entityCutoutNoCull(capeTexture);
            com.mojang.blaze3d.vertex.VertexConsumer capeBuffer = bufferSource.getBuffer(capeRenderType);
            super.renderRecursively(poseStack, animatable, bone, capeRenderType, bufferSource, capeBuffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        }
    }

}
