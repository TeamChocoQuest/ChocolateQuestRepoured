package com.example.chocolatequest.client.render;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT)
public class ChatBubbleRenderer {

    public static final ResourceLocation[] BUBBLES = new ResourceLocation[] {
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_block_bed.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_block_castle.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_block_flower.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_cloudy.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_maze.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_o.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_rage.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_rainy.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_smile.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_smirk.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_emote_thunder.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_alex.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_cat.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_dragon.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_ender_dragon.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_parrot.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_skeleton.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_steve.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_villager.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_walker.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_entity_wolf.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_chocolate.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_cocoa.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_emerald.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_map.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_nugget.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_pizza.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_potion.png"),
ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/gui/bubble_item_sword.png")
    };

    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof AbstractEntityCQR cqr) {
            int bubbleIndex = cqr.getChatBubbleIndex();
            if (bubbleIndex >= 0 && bubbleIndex < BUBBLES.length) {
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                // The event's PoseStack is at the entity's FEET (0, 0, 0).
                // We translate up by the entity's bounding box height plus a small, consistent margin (0.35 blocks).
                float heightOffset = cqr.getBbHeight() + 0.35F;
                
                // If the mob is sitting, their visual model drops significantly. We must lower the bubble!
                if (cqr.isSitting()) {
                    heightOffset -= 0.6F;
                }
                
                poseStack.translate(0.0F, heightOffset, 0.0F);

                // Rotate to face the camera
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                
                // Scale it. Let's make it a tiny bit smaller (from 0.035 down to 0.03).
                float scale = -0.03F;
                poseStack.scale(scale, scale, scale);
                
                Matrix4f matrix4f = poseStack.last().pose();
                VertexConsumer vertexconsumer = event.getMultiBufferSource().getBuffer(RenderType.entityCutoutNoCull(BUBBLES[bubbleIndex]));
                
                // Draw a 32x32 quad. Since we scaled by a negative amount, Y goes up as it decreases, or down as it decreases?
                // Scale is -0.05F, so positive Y goes DOWN in this local space. 
                // We want the top of the bubble at -32, bottom at 0.
                float halfWidth = 16.0f;
                float height = 32.0f;
                int light = event.getPackedLight();
                
                vertexconsumer.addVertex(matrix4f, -halfWidth, 0, 0).setColor(-1).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
                vertexconsumer.addVertex(matrix4f, halfWidth, 0, 0).setColor(-1).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
                vertexconsumer.addVertex(matrix4f, halfWidth, -height, 0).setColor(-1).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
                vertexconsumer.addVertex(matrix4f, -halfWidth, -height, 0).setColor(-1).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
                
                poseStack.popPose();
            }
        }
    }
}
