package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.client.render.entity.RenderMultiPartPart;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;

public class RenderExterminatorBackpackPart<T extends SubEntityExterminatorFieldEmitter> extends RenderMultiPartPart<T> {

	public RenderExterminatorBackpackPart(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public boolean shouldRender(T livingEntity, ClippingHelper camera, double camX, double camY, double camZ) {
		return this.superShouldRender(livingEntity, camera, camX, camY, camZ);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(T pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		if (pEntity.isActive()) {
			long seed = (pEntity.getId() * 255L) ^ (pEntity.tickCount >> 1 << 1);

			pMatrixStack.pushPose();
			pMatrixStack.translate(0.0D, pEntity.getBbHeight() * 0.5D, 0.0D);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(pMatrixStack, pBuffer, pEntity, 5, seed);
			pMatrixStack.popPose();

			if (pEntity.getTargetedEntity() != null) {
				Entity target = pEntity.getTargetedEntity();

				double x1 = pEntity.xOld + (pEntity.getX() - pEntity.xOld) * pPartialTicks;
				double y1 = pEntity.yOld + (pEntity.getY() - pEntity.yOld) * pPartialTicks;
				y1 += pEntity.getHeight() * 0.5;
				double z1 = pEntity.zOld + (pEntity.getZ() - pEntity.zOld) * pPartialTicks;

				double x2 = target.xOld + (target.getX() - target.xOld) * pPartialTicks;
				double y2 = target.yOld + (target.getY() - target.yOld) * pPartialTicks;
				y2 += target.getEyeHeight();
				double z2 = target.zOld + (target.getZ() - target.zOld) * pPartialTicks;

				final Vector3d start = new Vector3d(0, pEntity.getHeight() * 0.5, 0);
				final Vector3d end = new Vector3d(x2 - x1, y2 - y1, z2 - z1);

				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				ElectricFieldRenderUtil.renderElectricLineBetween(pMatrixStack, pBuffer, start, end, 0.5, 5, seed);
			}
		}
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

}
