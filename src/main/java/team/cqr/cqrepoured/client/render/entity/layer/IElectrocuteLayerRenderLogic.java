package team.cqr.cqrepoured.client.render.entity.layer;

import org.joml.Vector3d;
import org.joml.Vector3f;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public interface IElectrocuteLayerRenderLogic<T extends LivingEntity> {
	
	public default void renderLayerLogic(T entity, MatrixStack matrix, IRenderTypeBuffer buffer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity instanceof AbstractEntityCQR && ((AbstractEntityCQR) entity).canPlayDeathAnimation()) {
			return;
		}
			LazyOptional<CapabilityElectricShock> lOpCap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
			if(lOpCap.isPresent()) {
				CapabilityElectricShock cap = lOpCap.resolve().get();
				if (!cap.isElectrocutionActive()) {
					return;
				}
				long seed = (entity.getId() * 255L) ^ (entity.tickCount >> 1 << 1);
				ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(matrix, buffer, entity, 5, seed);
				if (cap.getTarget() != null) {
					Entity target = cap.getTarget();

					float yaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;

					//Are those the correct replacements?!
					double x1 = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
					double y1 = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
					double z1 = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;

					double x2 = target.xOld + (target.getX() - target.xOld) * partialTicks;
					double y2 = target.yOld + (target.getY() - target.yOld) * partialTicks;
					double z2 = target.zOld + (target.getZ() - target.zOld) * partialTicks;

					final Vector3d start = new Vector3d(0, entity.getBbHeight() * 0.5, 0);
					final Vector3d end = new Vector3d(x2 - x1, target.getBbHeight() * 0.5 + y2 - y1, z2 - z1);

					matrix.pushPose();

					this.performPreLineRenderPreparation(matrix);
					matrix.mulPose(Vector3f.YP.rotation(yaw -180));

					ElectricFieldRenderUtil.renderElectricLineBetween(matrix, buffer, start, end, 0.5, 5, seed);

					matrix.popPose();
				}
			}
	}
	
	public default void performPreLineRenderPreparation(MatrixStack matrix) {
		matrix.translate(0, 1.501, 0);
		matrix.scale(-1, -1, 1);
	}

}
