package com.example.chocolatequest.client.renderer.blockentity;

import com.example.chocolatequest.block.entity.BannerStandBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BannerStandBlockEntityRenderer implements BlockEntityRenderer<BannerStandBlockEntity> {

    public BannerStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BannerStandBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (true) {
            ItemStack bannerStack = blockEntity.hasBanner() ? blockEntity.getBanner() : new ItemStack(com.example.chocolatequest.registry.ModItems.BANNER_STAND.get());
            poseStack.pushPose();

            // Center the item
            poseStack.translate(0.5D, 0.1D, 0.5D);

            // Rotate based on player rotation when placed
            poseStack.mulPose(Axis.YP.rotationDegrees(-blockEntity.getRotation()));

            // Render the item large like a banner
            // We scale X and Y to make it look like a standard banner size if possible, or just scale uniformly.
            poseStack.scale(1.25F, 2.5F, 1.25F);

            // Move it up so it stands on the button
            poseStack.translate(0.0D, 0.5D, 0.0D);

            // Render the banner item
            Minecraft.getInstance().getItemRenderer().renderStatic(bannerStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);

            poseStack.popPose();
        }
    }
}

