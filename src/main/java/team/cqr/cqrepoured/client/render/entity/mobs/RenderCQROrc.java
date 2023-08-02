package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQROrcGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;

public class RenderCQROrc extends RenderCQRBipedBaseGeo<EntityCQROrc> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/orc_0.png");

	public RenderCQROrc(Context renderManager) {
		super(renderManager, new ModelCQROrcGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/orc"));
	}

}
