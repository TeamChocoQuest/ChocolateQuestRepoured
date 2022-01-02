package team.cqr.cqrepoured.client.render.entity.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public interface IElectrocuteLayerRenderLogic<T extends LivingEntity> {
	
	public default void renderLayerLogic(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity instanceof AbstractEntityCQR && ((AbstractEntityCQR) entity).canPlayDeathAnimation()) {
			return;
		}
		if (entity.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
			if (!cap.isElectrocutionActive()) {
				return;
			}
			long seed = (entity.getEntityId() * 255L) ^ (entity.ticksExisted >> 1 << 1);
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, 0, 0, 0, 5, seed);
			if (cap.getTarget() != null) {
				Entity target = cap.getTarget();

				float yaw = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;

				double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
				double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
				double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

				double x2 = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
				double y2 = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
				double z2 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;

				final Vector3d start = new Vector3d(0, entity.height * 0.5, 0);
				final Vector3d end = new Vector3d(x2 - x1, target.height * 0.5 + y2 - y1, z2 - z1);

				GlStateManager.pushMatrix();

				GlStateManager.translate(0, 1.501, 0);
				GlStateManager.scale(-1, -1, 1);
				GlStateManager.rotate(yaw - 180, 0, 1, 0);

				ElectricFieldRenderUtil.renderElectricLineBetween(start, end, 0.5, 0, 0, 0, 5, seed);

				GlStateManager.popMatrix();
			}
		}
	}

}
