package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;

public class RenderCQRDummy extends RenderCQREntity<EntityCQRDummy> {

	public RenderCQRDummy(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/dummy");
	}

}
