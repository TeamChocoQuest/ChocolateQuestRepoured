package com.example.chocolatequest.client.render.boss;

import com.example.chocolatequest.client.model.boss.CQExterminatorModel;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CQExterminatorRenderer extends GeoEntityRenderer<EntityCQRExterminator> {
    public CQExterminatorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CQExterminatorModel());
    }

    @Override
    public void render(EntityCQRExterminator entity, float entityYaw, float partialTick, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        // The supplied pose stack is already rooted at the Exterminator. All bolt
        // points therefore have to be relative to the boss, not to the camera.
        boolean renderedCoilBridge = false;
        java.util.Set<Integer> renderedTargetFields = new java.util.HashSet<>();
        for (net.neoforged.neoforge.entity.PartEntity<?> part : entity.getParts()) {
            if (part instanceof com.example.chocolatequest.entity.boss.exterminator.SubEntityExterminatorFieldEmitter emitter) {
                if (emitter.isActive()) {
                    long seed = (emitter.getId() * 255L) ^ (emitter.tickCount / 4);
                    if (emitter.getTargetedEntity() != null) {
                        net.minecraft.world.entity.Entity target = emitter.getTargetedEntity();

                        double x2 = target.xOld + (target.getX() - target.xOld) * partialTick;
                        double y2 = target.yOld + (target.getY() - target.yOld) * partialTick;
                        y2 += target.getEyeHeight() * 0.5;
                        double z2 = target.zOld + (target.getZ() - target.zOld) * partialTick;
                        
                        double bossX = entity.xOld + (entity.getX() - entity.xOld) * partialTick;
                        double bossY = entity.yOld + (entity.getY() - entity.yOld) * partialTick;
                        double bossZ = entity.zOld + (entity.getZ() - entity.zOld) * partialTick;
                        // Multipart children are positioned manually and their xOld/yOld/zOld
                        // values are not reliable. Interpolating those fields can blend a coil
                        // with the world origin, producing a huge vertical bolt (especially in
                        // superflat worlds whose ground is below Y=0). Keep the coil anchored to
                        // its current boss-local offset; only normal entities are interpolated.
                        net.minecraft.world.phys.Vec3 start = emitter.position()
                                .subtract(entity.position())
                                .add(0.0D, emitter.getBbHeight() * 0.5D, 0.0D);
                        net.minecraft.world.phys.Vec3 end = new net.minecraft.world.phys.Vec3(x2 - bossX, y2 - bossY, z2 - bossZ);

                        // Do not render a malformed multipart position during its first client
                        // frame. On the following frame alignParts() supplies the real offset.
                        if (start.lengthSqr() > 16.0D) {
                            continue;
                        }

                        seed = (emitter.getId() * 255L) ^ (entity.tickCount / 4);

                        // Two fine, independently flickering discharge paths look like a Tesla arc,
                        // instead of the previous thick lightning ribbon.
                        for(int i=0; i<2; i++) {
                            com.example.chocolatequest.client.util.SimpleElectricRenderUtil.renderElectricLineBetween(poseStack, bufferSource, start, end, (int)seed + i*100);
                        }
                        if (renderedTargetFields.add(target.getId())) {
                            net.minecraft.world.phys.Vec3 fieldCenter = new net.minecraft.world.phys.Vec3(
                                    x2 - bossX, target.getY() + target.getBbHeight() * 0.5D - bossY, z2 - bossZ);
                            com.example.chocolatequest.client.util.SimpleElectricRenderUtil.renderElectricField(
                                    poseStack, bufferSource, fieldCenter, target.getBbWidth(), target.getBbHeight(), (int)seed + 731);
                        }
                    }
                    
                    // Always draw the ambient halo to the other emitter!
                    net.minecraft.world.entity.Entity otherEmitter = emitter == entity.getEmitterLeft() ? entity.getEmitterRight() : entity.getEmitterLeft();
                    if (!renderedCoilBridge && otherEmitter != null && otherEmitter.isAlive()) {
                        renderedCoilBridge = true;
                        seed = (emitter.getId() * 255L) ^ (emitter.tickCount / 4);
                        net.minecraft.world.phys.Vec3 start = emitter.position()
                                .subtract(entity.position())
                                .add(0.0D, emitter.getBbHeight() * 0.5D, 0.0D);
                        net.minecraft.world.phys.Vec3 end = otherEmitter.position()
                                .subtract(entity.position())
                                .add(0.0D, otherEmitter.getBbHeight() * 0.5D, 0.0D);

                        if (start.lengthSqr() > 16.0D || end.lengthSqr() > 16.0D) {
                            continue;
                        }

                        // Small unstable bridge between both powered coils.
                        for(int i=0; i<2; i++) {
                            com.example.chocolatequest.client.util.SimpleElectricRenderUtil.renderElectricLineBetween(poseStack, bufferSource, start, end, (int)seed + i*50);
                        }
                    }
                }
            }
        }
    }
}
