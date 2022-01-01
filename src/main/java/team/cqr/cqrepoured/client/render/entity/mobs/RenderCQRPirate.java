package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;

public class RenderCQRPirate extends RenderCQREntity<EntityCQRPirate> {

	public RenderCQRPirate(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/pirate", true);
	}

}
