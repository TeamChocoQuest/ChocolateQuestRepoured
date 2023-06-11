package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import org.joml.Vector3d;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;

public class RenderExterminatorBackpackPart<T extends SubEntityExterminatorFieldEmitter> extends EntityRenderer<T> {

	public RenderExterminatorBackpackPart(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(T pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
		if (pEntity.isActive()) {
			long seed = (pEntity.getId() * 255L) ^ (pEntity.tickCount >> 1 << 1);

			pMatrixStack.pushPose();
			pMatrixStack.translate(0.0D, pEntity.getBbHeight() * 0.5D, 0.0D);
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(pMatrixStack, pBuffer, pEntity, 5, seed);
			pMatrixStack.popPose();

			if (pEntity.getTargetedEntity() != null) {
				Entity target = pEntity.getTargetedEntity();

				double x1 = pEntity.xOld + (pEntity.getX() - pEntity.xOld) * pPartialTicks;
				double y1 = pEntity.yOld + (pEntity.getY() - pEntity.yOld) * pPartialTicks;
				y1 += pEntity.getBbHeight() * 0.5;
				double z1 = pEntity.zOld + (pEntity.getZ() - pEntity.zOld) * pPartialTicks;

				double x2 = target.xOld + (target.getX() - target.xOld) * pPartialTicks;
				double y2 = target.yOld + (target.getY() - target.yOld) * pPartialTicks;
				y2 += target.getEyeHeight();
				double z2 = target.zOld + (target.getZ() - target.zOld) * pPartialTicks;

				final Vector3d start = new Vector3d(0, pEntity.getBbHeight() * 0.5, 0);
				final Vector3d end = new Vector3d(x2 - x1, y2 - y1, z2 - z1);

				ElectricFieldRenderUtil.renderElectricLineBetween(pMatrixStack, pBuffer, start, end, 0.5, 5, seed);
			}
		}
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
