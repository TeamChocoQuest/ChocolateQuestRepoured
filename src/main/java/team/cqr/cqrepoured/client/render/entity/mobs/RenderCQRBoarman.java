package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRBoarmanGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;

public class RenderCQRBoarman extends RenderCQRBipedBaseGeo<EntityCQRBoarman> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/boarman_0.png");

	public RenderCQRBoarman(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarmanGeo(CQRMain.prefix("geo/entity/biped_boarman.geo.json"), TEXTURE, "mob/boarman"));
	}

}
