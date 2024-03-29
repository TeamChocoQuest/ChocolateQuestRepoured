package team.cqr.cqrepoured.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.init.CQRPotions;
import team.cqr.cqrepoured.potion.ElectrocutionMobEffect.SpreadTargetData;

public interface IElectrocuteLayerRenderLogic<T extends LivingEntity> {
	
	public default void renderLayerLogic(T entity, PoseStack matrix, MultiBufferSource buffer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isAlive() || entity.deathTime > 0) {
			return;
		}
		if (!entity.hasEffect(CQRPotions.ELECTROCUTION.get())) {
			return;
		}
		// Make sure the instance is extended => we need that extra data!
		MobEffectInstance effectInstance = entity.getEffect(CQRPotions.ELECTROCUTION.get());
		if (!(effectInstance instanceof ExtendedMobEffectHolder)) {
			return;
		}
		// Render normal effect
		long seed = (entity.getId() * 255L) ^ (entity.tickCount >> 1 << 1);
		ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(matrix, buffer, entity, 5, seed);
		
		// Access extra data
		ExtendedMobEffectHolder holder = (ExtendedMobEffectHolder)effectInstance;
		if (holder.getExtendedMobEffectData() == null || !(holder.getExtendedMobEffectData() instanceof SpreadTargetData)) {
			return;
		}
		SpreadTargetData targetData = (SpreadTargetData) holder.getExtendedMobEffectData();
		Entity target = targetData.getTarget(entity.level());
		if (target != null) {
			float yaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;

			//Are those the correct replacements?! => Probably...
			double x1 = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
			double y1 = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
			double z1 = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;

			double x2 = target.xOld + (target.getX() - target.xOld) * partialTicks;
			double y2 = target.yOld + (target.getY() - target.yOld) * partialTicks;
			double z2 = target.zOld + (target.getZ() - target.zOld) * partialTicks;

			final Vec3 start = new Vec3(0, entity.getBbHeight() * 0.5, 0);
			final Vec3 end = new Vec3(x2 - x1, target.getBbHeight() * 0.5 + y2 - y1, z2 - z1);

			matrix.pushPose();

			this.performPreLineRenderPreparation(matrix);
			matrix.mulPose(Axis.YP.rotation(yaw -180));

			ElectricFieldRenderUtil.renderElectricLineBetween(matrix, buffer, start, end, 0.5, 5, seed);

			matrix.popPose();
		}
	}
	
	public default void performPreLineRenderPreparation(PoseStack matrix) {
		matrix.translate(0, 1.501, 0);
		matrix.scale(-1, -1, 1);
	}

}
