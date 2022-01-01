package team.cqr.cqrepoured.client.render.entity.boss.exterminator;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.render.entity.RenderMultiPartPart;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.boss.exterminator.SubEntityExterminatorFieldEmitter;

public class RenderExterminatorBackpackPart<T extends SubEntityExterminatorFieldEmitter> extends RenderMultiPartPart<T> {

	public RenderExterminatorBackpackPart(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return this.superShouldRender(livingEntity, camera, camX, camY, camZ);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.isActive()) {
			long seed = (entity.getEntityId() * 255L) ^ (entity.ticksExisted >> 1 << 1);
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, x, y, z, 5, seed);
			if (entity.getTargetedEntity() != null) {
				Entity target = entity.getTargetedEntity();

				double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
				double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
				y1 += entity.getHeight() * 0.5;
				double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

				double x2 = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
				double y2 = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
				y2 += target.getEyeHeight();
				double z2 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;

				final Vec3d start = new Vec3d(0, entity.getHeight() * 0.5, 0);
				final Vec3d end = new Vec3d(x2 - x1, y2 - y1, z2 - z1);

				GlStateManager.pushMatrix();

				ElectricFieldRenderUtil.renderElectricLineBetween(start, end, 0.5, x, y, z, 5, seed);

				GlStateManager.popMatrix();
			}
		}
	}

}
