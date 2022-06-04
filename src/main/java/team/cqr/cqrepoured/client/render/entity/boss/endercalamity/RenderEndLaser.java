package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.RenderLaser;
import team.cqr.cqrepoured.client.util.PentagramUtil;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;

public class RenderEndLaser<T extends AbstractEntityLaser> extends RenderLaser<T> {

	public RenderEndLaser(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void renderModel(T entity, float yaw, float pitch, float partialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, float laserLength) {
		pMatrixStack.pushPose();
		pMatrixStack.translate(0.0D, 0.0D, -1.0D);
		for (int i = 0; i < Math.min(Math.ceil(laserLength / 4.0D), 3); i++) {
			pMatrixStack.translate(0.0D, 0.0D, -4.0D);
			float colorMultiplier = 0.75F + 0.25F * MathHelper.sin((entity.tickCount + partialTicks) * 0.25F + (float) Math.PI * 0.5F * i);
			this.renderRing(pMatrixStack, 9 - i * 2, entity, pitch, yaw, 2.0F - i * 0.5F, partialTicks, colorMultiplier);
		}
		pMatrixStack.popPose();

		super.renderModel(entity, yaw, pitch, partialTicks, pMatrixStack, pBuffer, pPackedLight, laserLength);
	}

	private void renderRing(MatrixStack matrixStack, int corners, T entity, float pitch, float yaw, float scale, float partialTicks, float colorMultiplier) {
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
		matrixStack.scale(scale, scale, scale);
		PentagramUtil.renderPentagram(matrixStack, CQRRenderTypes.emissiveSolid(), entity.getColorR() * colorMultiplier, entity.getColorG() * colorMultiplier, entity.getColorB() * colorMultiplier, corners);
		matrixStack.popPose();
	}

}
