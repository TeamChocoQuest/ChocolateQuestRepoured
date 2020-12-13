package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;

public class RenderCQRGiantTortoisePart extends Render<EntityCQRGiantTortoisePart> {

	public RenderCQRGiantTortoisePart(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoisePart entity) {
		return null;
	}

	@Override
	public void doRender(EntityCQRGiantTortoisePart entity, double x, double y, double z, float entityYaw, float partialTicks) {
	}

}
