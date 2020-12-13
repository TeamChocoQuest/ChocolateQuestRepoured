package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.models.entities.mobs.ModelCQRMinotaur;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.mobs.EntityCQRMinotaur;

public class RenderCQRMinotaur extends RenderCQREntity<EntityCQRMinotaur> {

	public RenderCQRMinotaur(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMinotaur(), 0.5F, "mob/minotaur", 1.0D, 1.0D);
	}

}
