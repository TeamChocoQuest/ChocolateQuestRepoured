package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQRBoarman;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRBoarman;

public class RenderCQRBoarman extends RenderCQREntity<EntityCQRBoarman> {

	public RenderCQRBoarman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarman(), 0.5F, "mob/boarman", 1.0D, 1.0D);
	}

}
