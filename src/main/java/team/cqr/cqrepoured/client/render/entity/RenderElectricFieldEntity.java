package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil2;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricField;

public class RenderElectricFieldEntity extends Render<EntityElectricField> {

	public RenderElectricFieldEntity(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityElectricField entity, double x, double y, double z, float entityYaw, float partialTicks) {
		long seed = (entity.getEntityId() * 255L) ^ (entity.ticksExisted >> 1 << 1);
		ElectricFieldRenderUtil2.renderElectricFieldWithSizeOfEntityAt(entity, x, y, z, 5, seed);
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
