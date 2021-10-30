package team.cqr.cqrepoured.client.render.entity.layers.geo;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class LayerElectrocuteGeo<T extends EntityLivingBase & IAnimatable> extends GeoLayerRenderer<T> {

	public LayerElectrocuteGeo(IGeoRenderer<T> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		if(entity instanceof AbstractEntityCQR && ((AbstractEntityCQR)entity).canPlayDeathAnimation()) {
			return;
		}
		if (entity.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
			if (cap.getRemainingTicks() < 0) {
				return;
			}
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, 0, 0, 0);
			if (cap.getTarget() != null) {
				Entity target = cap.getTarget();

				float yaw = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;

				double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
				double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
				// y1 += entity.getEyeHeight() / 2;
				double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

				double x2 = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
				double y2 = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
				// y2 += target.getEyeHeight() / 2;
				double z2 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;

				final Vec3d start = new Vec3d(0, 0, 0);
				final Vec3d end = new Vec3d(x2 - x1, y2 - y1, z2 - z1).scale(1.1D);

				GlStateManager.pushMatrix();

				GlStateManager.scale(-1, -1, 1);
				GlStateManager.rotate(yaw - 180, 0, 1, 0);

				ElectricFieldRenderUtil.renderElectricLineBetween(start, end, entity.getRNG(), 0.5, 0, 0, 0, 5);

				GlStateManager.popMatrix();
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	@Override
	public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.render(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
	}

}
