package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQROrc;

public class RenderCQROrc extends RenderCQREntity<EntityCQROrc> {

	public RenderCQROrc(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/orc", true);
	}

}
