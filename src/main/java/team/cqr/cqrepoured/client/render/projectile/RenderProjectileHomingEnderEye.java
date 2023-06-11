package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHomingEnderEye;

public class RenderProjectileHomingEnderEye extends RenderSpriteBase<ProjectileHomingEnderEye> {

	public RenderProjectileHomingEnderEye(Context renderManager)
	{
		super(renderManager, new ResourceLocation("textures/item/ender_eye.png"));
	}

	@Override
	public void render(ProjectileHomingEnderEye entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

}
