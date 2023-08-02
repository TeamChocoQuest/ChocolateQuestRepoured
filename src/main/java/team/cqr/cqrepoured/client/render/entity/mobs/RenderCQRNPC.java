package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRNPCGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRNPC;

public class RenderCQRNPC extends RenderCQRBipedBaseGeo<EntityCQRNPC> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/lunk.png");
	
	public RenderCQRNPC(Context renderManager) {
		super(renderManager, new ModelCQRNPCGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/lunk"));
	}

}
