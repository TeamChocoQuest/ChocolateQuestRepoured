package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.entity.misc.EntityElectricField;

public class RenderElectricFieldEntity extends EntityRenderer<EntityElectricField> {

	public RenderElectricFieldEntity(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityElectricField entity, double x, double y, double z, float entityYaw, float partialTicks) {
		long seed = (entity.getEntityId() * 255L) ^ (entity.ticksExisted >> 1 << 1);
		ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, x, y, z, 5, seed);
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		// This entity is more like a effect, it doesn't render fire and does not have a shadow
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElectricField entity) {
		return null;
	}

}
