package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRZombie;

public class RenderCQRZombie extends RenderCQREntity<EntityCQRZombie> {

	public RenderCQRZombie(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/zombie", true);
	}

}
