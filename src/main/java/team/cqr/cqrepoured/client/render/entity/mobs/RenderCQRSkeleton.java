package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.RenderManager;
import team.cqr.cqrepoured.client.model.entity.mobs.ModelCQRSkeleton;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSkeleton;

public class RenderCQRSkeleton extends RenderCQREntity<EntityCQRSkeleton> {

	public RenderCQRSkeleton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRSkeleton(), 0.5F, "mob/skeleton", 1.0D, 1.0D);

		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
	}

}
