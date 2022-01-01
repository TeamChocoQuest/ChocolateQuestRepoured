package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRNPC;

public class RenderCQRNPC extends RenderCQREntity<EntityCQRNPC> {

	public RenderCQRNPC(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/lunk", true);
	}

}
