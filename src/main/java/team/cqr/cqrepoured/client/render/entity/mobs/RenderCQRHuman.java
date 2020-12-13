package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRHuman;

public class RenderCQRHuman extends RenderCQREntity<EntityCQRHuman> {

	public RenderCQRHuman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/human", true);
	}

}
