package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRMummyGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMummy;

public class RenderCQRMummy extends RenderCQRBipedBaseGeo<EntityCQRMummy> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/mummy.png");

	public RenderCQRMummy(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMummyGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/mummy"));
	}

}
