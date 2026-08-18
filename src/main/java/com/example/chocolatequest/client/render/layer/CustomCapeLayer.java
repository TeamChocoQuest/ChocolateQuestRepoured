package com.example.chocolatequest.client.render.layer;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.CapeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class CustomCapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final net.minecraft.client.model.geom.ModelPart customCloak;

    public CustomCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
        net.minecraft.client.model.geom.builders.CubeListBuilder builder = net.minecraft.client.model.geom.builders.CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F);
        
        net.minecraft.client.model.geom.builders.MeshDefinition mesh = new net.minecraft.client.model.geom.builders.MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cloak", builder, net.minecraft.client.model.geom.PartPose.offset(0.0F, 0.0F, 0.0F));
        this.customCloak = net.minecraft.client.model.geom.builders.LayerDefinition.create(mesh, 64, 64).bakeRoot().getChild("cloak");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (player.isInvisible() || !player.isModelPartShown(net.minecraft.world.entity.player.PlayerModelPart.CAPE)) {
            return;
        }

        ItemStack capeStack = ItemStack.EMPTY;
        
        // Try to get from accessories
        io.wispforest.accessories.api.AccessoriesCapability capability = io.wispforest.accessories.api.AccessoriesCapability.get(player);
        if (capability != null) {
            for (var container : capability.getContainers().values()) {
                for (int i = 0; i < container.getAccessories().getContainerSize(); i++) {
                    ItemStack stack = container.getAccessories().getItem(i);
                    if (stack.getItem() instanceof com.example.chocolatequest.item.CapeItem) {
                        capeStack = stack;
                        break;
                    }
                }
                if (!capeStack.isEmpty()) {
                    break;
                }
            }
        }
        
        if (capeStack.isEmpty()) {
            return;
        }

        // Determine texture based on item
        String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(capeStack.getItem()).getPath();
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/cape/" + itemName + ".png");

        poseStack.pushPose();
        float zOffset = player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.125F : 0.20F;
        poseStack.translate(0.0F, 0.0F, zOffset);
        
        double d0 = Mth.lerp((double)partialTicks, player.xCloakO, player.xCloak) - Mth.lerp((double)partialTicks, player.xo, player.getX());
        double d1 = Mth.lerp((double)partialTicks, player.yCloakO, player.yCloak) - Mth.lerp((double)partialTicks, player.yo, player.getY());
        double d2 = Mth.lerp((double)partialTicks, player.zCloakO, player.zCloak) - Mth.lerp((double)partialTicks, player.zo, player.getZ());
        
        float f = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
        double d3 = (double)Mth.sin(f * ((float)Math.PI / 180F));
        double d4 = (double)(-Mth.cos(f * ((float)Math.PI / 180F)));
        float f1 = (float)d1 * 10.0F;
        f1 = Mth.clamp(f1, -6.0F, 32.0F);
        float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
        f2 = Mth.clamp(f2, 0.0F, 150.0F);
        float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
        f3 = Mth.clamp(f3, -20.0F, 20.0F);
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        float f4 = Mth.lerp(partialTicks, player.oBob, player.bob);
        f1 += Mth.sin(Mth.lerp(partialTicks, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * f4;
        
        if (player.isCrouching()) {
            f1 += 25.0F;
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
        
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.customCloak.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        
        poseStack.popPose();
    }
}
