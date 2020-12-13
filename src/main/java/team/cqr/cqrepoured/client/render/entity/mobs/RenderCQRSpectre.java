package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRSpectre;

public class RenderCQRSpectre extends RenderCQREntity<EntityCQRSpectre> {

	public RenderCQRSpectre(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/spectre", true);
	}

	@Override
	public void doRender(EntityCQRSpectre entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
