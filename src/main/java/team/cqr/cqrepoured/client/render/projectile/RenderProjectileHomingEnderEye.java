package team.cqr.cqrepoured.client.render.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.RenderSpriteBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHomingEnderEye;

public class RenderProjectileHomingEnderEye extends RenderSpriteBase<ProjectileHomingEnderEye> {

	public RenderProjectileHomingEnderEye(EntityRendererManager renderManager)
	{
		super(renderManager, CQRMain.prefix("textures/items/ender_eye.png"));
	}

	@Override
	public void render(ProjectileHomingEnderEye entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

}
