package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRHumanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;

public class RenderCQRHuman extends RenderCQRBipedBaseGeo<EntityCQRHuman> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/human_0.png");
	
	public RenderCQRHuman(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRHumanGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/human"));
	}

}
