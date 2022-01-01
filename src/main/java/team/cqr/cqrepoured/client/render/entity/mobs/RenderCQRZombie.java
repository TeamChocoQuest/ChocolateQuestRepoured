package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;

public class RenderCQRZombie extends RenderCQREntity<EntityCQRZombie> {

	public RenderCQRZombie(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/zombie", true);
	}

}
