package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.model.entity.mobs.ModelCQRGolem;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;

public class RenderCQRGolem extends RenderCQREntity<EntityCQRGolem> {

	public RenderCQRGolem(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolem(), 0.5F, "mob/golem", 1.0D, 1.0D);
	}

	@Override
	public void doRender(EntityCQRGolem entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
