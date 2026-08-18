package com.example.chocolatequest.client.render.boss;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.client.util.SimpleElectricRenderUtil;
import com.example.chocolatequest.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;
import net.minecraft.util.Mth;

public class RenderExterminatorBackpackPart<T extends SubEntityExterminatorFieldEmitter> extends EntityRenderer<T> {

    public RenderExterminatorBackpackPart(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return ResourceLocation.withDefaultNamespace("textures/entity/enderman/enderman.png"); // not used, it's just sparks
    }
}
