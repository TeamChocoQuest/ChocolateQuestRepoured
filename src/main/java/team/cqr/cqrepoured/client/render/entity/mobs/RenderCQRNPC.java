package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRNPC;

public class RenderCQRNPC extends RenderCQREntity<EntityCQRNPC> {

	public RenderCQRNPC(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/lunk", true);
	}

}
