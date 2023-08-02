package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRDummyGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;

public class RenderCQRDummy extends RenderCQRBipedBaseGeo<EntityCQRDummy> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/dummy.png");

	public RenderCQRDummy(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRDummyGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/dummy"));
	}

}
