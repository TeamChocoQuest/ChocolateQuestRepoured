package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGoblinGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class RenderCQRGoblin extends RenderCQRBipedBaseGeo<EntityCQRGoblin> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/goblin.png");

	public RenderCQRGoblin(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGoblinGeo(CQRMain.prefix("geo/entity/biped_goblin.geo.json"), TEXTURE, "mob/goblin"));
	}

}
