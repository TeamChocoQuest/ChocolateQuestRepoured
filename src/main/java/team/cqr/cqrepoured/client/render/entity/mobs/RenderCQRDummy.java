package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRDummy;

public class RenderCQRDummy extends RenderCQREntity<EntityCQRDummy> {

	public RenderCQRDummy(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/dummy");
	}

}
