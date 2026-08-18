package com.example.chocolatequest.client.render;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.ItemMusket;
import com.example.chocolatequest.item.ItemRevolver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;

/**
 * Renders only the firearm model. Hand posing was intentionally removed until
 * the weapon models have proper animated grip bones.
 */
@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT)
public final class FirearmFirstPersonRenderer {

    private FirearmFirstPersonRenderer() {
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        AbstractClientPlayer player = minecraft.player;
        if (player == null || event.getHand() != InteractionHand.MAIN_HAND ||
                !(event.getItemStack().getItem() instanceof ItemRevolver firearm)) {
            return;
        }

        event.setCanceled(true);
        HumanoidArm arm = player.getMainArm();
        float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        float recoil = Mth.sin(Mth.sqrt(event.getSwingProgress()) * Mth.PI);
        boolean musket = firearm instanceof ItemMusket;

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(side * 0.56F, -0.52F - event.getEquipProgress() * 0.6F, -0.72F);

        if (musket) {
            // Slightly right of the previous centred pose.
            poseStack.translate(-side * 0.18F, 0.035F, -0.08F + recoil * 0.13F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-2.0F - recoil * 20.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * (-2.0F + recoil * 2.0F)));
            poseStack.scale(1.12F, 1.12F, 1.12F);
        } else {
            // Revolver is closer to the camera and level with the screen. Only
            // recoil rotates it; there is no permanent leftward cant.
            poseStack.translate(-side * 0.14F, -0.025F, 0.20F + recoil * 0.18F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-recoil * 29.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(side * recoil * 2.0F));
            poseStack.scale(1.42F, 1.42F, 1.42F);
        }

        minecraft.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(
                player,
                event.getItemStack(),
                arm == HumanoidArm.RIGHT ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                arm == HumanoidArm.LEFT,
                poseStack,
                event.getMultiBufferSource(),
                event.getPackedLight()
        );
        poseStack.popPose();
    }
}
