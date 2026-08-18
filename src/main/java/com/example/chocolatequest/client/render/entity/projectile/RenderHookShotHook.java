package com.example.chocolatequest.client.render.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.example.chocolatequest.entity.projectile.ProjectileHookShotHook;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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

public class RenderHookShotHook<T extends ProjectileHookShotHook> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("cqrepoured", "textures/entity/hook.png");
    private final ModelPart root;
    private final ModelPart chainRoot;

    public RenderHookShotHook(EntityRendererProvider.Context context) {
        super(context);
        this.root = createBodyLayer().bakeRoot();
        this.chainRoot = createChainLayer().bakeRoot();
    }

        public static LayerDefinition createChainLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition chain = partdefinition.addOrReplaceChild("chain", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-1.5F, -0.5F, -1.0F, 1.0F, 1.0F, 8.0F)
            .texOffs(0, 0).addBox(0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 8.0F)
            .texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F)
            .texOffs(0, 0).addBox(-0.5F, -0.5F, 6.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stem = partdefinition.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(0, 8).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.ZERO);
        stem.addOrReplaceChild("clawRight", CubeListBuilder.create().texOffs(0, 6).addBox(-3.0F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        stem.addOrReplaceChild("clawLeft", CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        stem.addOrReplaceChild("clawUp", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, -0.7854F, 0.0F, 0.0F));
        stem.addOrReplaceChild("clawDown", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw - 180.0F));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        this.root.render(poseStack, vertexconsumer, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
        
        poseStack.popPose();

        if (entity.getOwner() instanceof Player player) {
            float f = player.getAttackAnim(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float)Math.PI);
            
            Vec3 vec3 = new Vec3(-0.5D, 0.03D, 0.8D); // Offset from player hand
            // Simplified leash rendering logic here
            // We just draw a line from entity to player
            
            poseStack.pushPose();
            
            Vec3 playerPos;
            boolean isOffhand = player.getOffhandItem().getItem() instanceof com.example.chocolatequest.item.ItemHookshotBase;
            int armDir = isOffhand ? -1 : 1;
            if (player.getMainArm() == net.minecraft.world.entity.HumanoidArm.LEFT) armDir = -armDir;
            
            if (net.minecraft.client.Minecraft.getInstance().options.getCameraType().isFirstPerson() && player == net.minecraft.client.Minecraft.getInstance().player) {
                Vec3 view = player.getViewVector(partialTicks);
                Vec3 right = view.cross(new Vec3(0, 1, 0)).normalize();
                Vec3 up = right.cross(view).normalize();
                playerPos = player.getEyePosition(partialTicks).add(view.scale(0.5)).add(right.scale(0.35 * armDir)).add(up.scale(-0.4));
            } else {
                float bodyYaw = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
                double offsetX = -Math.cos(bodyYaw) * 0.35 * armDir;
                double offsetZ = -Math.sin(bodyYaw) * 0.35 * armDir;
                double offsetY = player.getEyeHeight() - 0.4;
                playerPos = new Vec3(Mth.lerp(partialTicks, player.xo, player.getX()) + offsetX, Mth.lerp(partialTicks, player.yo, player.getY()) + offsetY, Mth.lerp(partialTicks, player.zo, player.getZ()) + offsetZ);
            }

            Vec3 entityPos = entity.getPosition(partialTicks);
            Vec3 diff = playerPos.subtract(entityPos);
            
            double dist = diff.length();
            if (dist > 0) {
                Vec3 dir = diff.normalize();
                double segmentLength = 7.0D / 16.0D;
                int segmentCount = (int) Math.ceil(dist / segmentLength);
                
                float rotYaw = (float) Math.toDegrees(Math.atan2(dir.x, dir.z));
                float rotPitch = (float) -Math.toDegrees(Math.atan2(dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z)));
                
                VertexConsumer chainBuffer = buffer.getBuffer(RenderType.entitySolid(net.minecraft.resources.ResourceLocation.withDefaultNamespace("textures/block/iron_block.png")));
                for (int i = 0; i < segmentCount; i++) {
                    poseStack.pushPose();
                    double dx = dir.x * i * segmentLength;
                    double dy = dir.y * i * segmentLength;
                    double dz = dir.z * i * segmentLength;
                    poseStack.translate(dx, dy, dz);
                    poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotYaw));
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotPitch));
                    poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(40.0F + (i % 2 == 0 ? 90.0F : 0)));
                    this.chainRoot.render(poseStack, chainBuffer, 15728880, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 0xFF808080);
                    poseStack.popPose();
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
