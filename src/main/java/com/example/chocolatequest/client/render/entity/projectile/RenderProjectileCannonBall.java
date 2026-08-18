package com.example.chocolatequest.client.render.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.client.model.entity.ModelCannonBall;
import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class RenderProjectileCannonBall extends EntityRenderer<ProjectileCannonBall> {

	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "textures/entity/ball_cannon.png");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "projectile_cannon_ball"), "main");

	private final ModelCannonBall<ProjectileCannonBall> model;

	public RenderProjectileCannonBall(Context renderManager)
	{
		super(renderManager);
		this.model = new ModelCannonBall<>(renderManager.bakeLayer(LAYER_LOCATION));
	}

	@Override
	public void render(ProjectileCannonBall cannonBall, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		matrixStack.scale(0.875F, 0.875F, 0.875F);
		matrixStack.scale(-1, -1, -1);

		VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
		this.model.renderToBuffer(matrixStack, builder, packedLight, OverlayTexture.NO_OVERLAY, -1);

		matrixStack.popPose();
		super.render(cannonBall, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(ProjectileCannonBall entity)
	{
		return TEXTURE;
	}
}
