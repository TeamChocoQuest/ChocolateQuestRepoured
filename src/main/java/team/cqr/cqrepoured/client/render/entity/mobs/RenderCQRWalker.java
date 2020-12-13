package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRWalker;

public class RenderCQRWalker extends RenderCQREntity<EntityCQRWalker> {

	public RenderCQRWalker(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/walker", true);
	}

}
