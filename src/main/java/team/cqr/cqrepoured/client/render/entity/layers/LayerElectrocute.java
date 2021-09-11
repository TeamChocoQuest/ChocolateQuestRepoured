package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;

public class LayerElectrocute implements LayerRenderer<EntityLivingBase> {

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
			if(cap.getRemainingTicks() < 0) {
				return;
			}
			double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
			double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
			double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, x,-entity.getEyeHeight() / 2,z);
			if(cap.getTarget() != null) {
				Entity target = cap.getTarget();
				
				double xTarget = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
				double yTarget = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
				double zTarget = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;
				
				Vec3d vector = target.getPositionVector().subtract(entity.getPositionVector());
				ElectricFieldRenderUtil.renderElectricLineBetween(Vec3d.ZERO.add(0, entity.getEyeHeight(), 0), vector, entity.getRNG(), 0.5D, x,y,z, 5);
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
