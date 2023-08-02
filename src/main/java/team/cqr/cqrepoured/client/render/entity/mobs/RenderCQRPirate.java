package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRPirateGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;

public class RenderCQRPirate extends RenderCQRBipedBaseGeo<EntityCQRPirate> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/pirate_0.png");

	public RenderCQRPirate(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ModelCQRPirateGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/pirate"));
	}

}
