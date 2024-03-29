package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRZombieGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;

public class RenderCQRZombie extends RenderCQRBipedBaseGeo<EntityCQRZombie> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/zombie_0.png");
	
	public RenderCQRZombie(Context renderManager) {
		super(renderManager, new ModelCQRZombieGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/zombie"));
	}

}
