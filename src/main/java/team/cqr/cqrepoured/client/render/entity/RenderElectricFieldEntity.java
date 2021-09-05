package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;
import team.cqr.cqrepoured.objects.entity.misc.EntityElectricField;

public class RenderElectricFieldEntity extends Render<EntityElectricField> {

	public RenderElectricFieldEntity(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(EntityElectricField entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, x, y, z);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityElectricField entity) {
		return null;
	}

}
