package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGremlinGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;

public class RenderCQRGremlin extends RenderCQRBipedBaseGeo<EntityCQRGremlin> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/gremlin.png");

	public RenderCQRGremlin(EntityRendererProvider.Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGremlinGeo(CQRMain.prefix("geo/entity/biped_gremlin.geo.json"), TEXTURE, "mob/gremlin"));
	}

}
