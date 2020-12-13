package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRPirate;

public class RenderCQRPirate extends RenderCQREntity<EntityCQRPirate> {

	public RenderCQRPirate(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/pirate", true);
	}

}
