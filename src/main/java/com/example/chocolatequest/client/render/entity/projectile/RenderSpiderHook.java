package com.example.chocolatequest.client.render.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.example.chocolatequest.entity.projectile.ProjectileHookShotHook;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderSpiderHook<T extends ProjectileHookShotHook> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/cobweb.png");

    public RenderSpiderHook(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);

        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw - 180.0F));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // Cross 1
        vertexconsumer.addVertex(matrix4f, -0.5f, -0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, -0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, 0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, -0.5f, 0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);

        // Cross 1 reversed
        vertexconsumer.addVertex(matrix4f, -0.5f, 0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, 0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, -0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, -0.5f, -0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);

        // Cross 2
        vertexconsumer.addVertex(matrix4f, -0.5f, -0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, -0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, 0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, -0.5f, 0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);

        // Cross 2 reversed
        vertexconsumer.addVertex(matrix4f, -0.5f, 0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, 0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, 0.5f, -0.5f, -0.5f).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);
        vertexconsumer.addVertex(matrix4f, -0.5f, -0.5f, 0.5f).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0.0f, 1.0f, 0.0f);

        poseStack.popPose();

        if (entity.getOwner() instanceof net.minecraft.world.entity.LivingEntity owner) {
            poseStack.pushPose();
            Vec3 ownerPos = owner.getPosition(partialTicks).add(0, owner.getEyeHeight() - 0.2, 0);
            Vec3 entityPos = entity.getPosition(partialTicks);
            Vec3 diff = ownerPos.subtract(entityPos);
            
            double dist = diff.length();
            if (dist > 0) {
                Vec3 dir = diff.normalize();
                double sagginess = -0.3D;
                double minSegmentLength = 8.0D / 16.0D;
                int segmentCount = (int) (dist / minSegmentLength);
                if (segmentCount == 0) segmentCount = 1;
                double segmentLength = dist / segmentCount;
                
                Matrix4f matrix4fLine = poseStack.last().pose();
                Matrix3f matrix3fLine = poseStack.last().normal();
                VertexConsumer lineBuffer = buffer.getBuffer(RenderType.lineStrip());
                
                for(int i = 0; i <= segmentCount; ++i) {
                    double dy = Math.sin((float) (i * Math.PI / segmentCount));
                    lineBuffer.addVertex(matrix4fLine, (float)(dir.x * i * segmentLength), (float)(dir.y * i * segmentLength + dy * sagginess), (float)(dir.z * i * segmentLength))
                              .setColor(230, 230, 230, 255)
                              .setNormal(poseStack.last(), 0.0F, 1.0F, 0.0F);
                }
            }
            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
