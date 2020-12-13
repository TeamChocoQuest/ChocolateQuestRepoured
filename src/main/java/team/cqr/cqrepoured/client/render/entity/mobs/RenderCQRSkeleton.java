package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQRSkeleton;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRSkeleton;

public class RenderCQRSkeleton extends RenderCQREntity<EntityCQRSkeleton> {

	public RenderCQRSkeleton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRSkeleton(), 0.5F, "mob/skeleton", 1.0D, 1.0D);
	}

}
