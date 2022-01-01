package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;

public class RenderCQROrc extends RenderCQREntity<EntityCQROrc> {

	public RenderCQROrc(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/orc", true);
	}

}
