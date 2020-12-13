package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRMummy;

public class RenderCQRMummy extends RenderCQREntity<EntityCQRMummy> {

	public RenderCQRMummy(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/mummy", true);
	}

}
