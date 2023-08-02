package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRMinotaurGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMinotaur;

public class RenderCQRMinotaur extends RenderCQRBipedBaseGeo<EntityCQRMinotaur> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/minotaur.png");
	
	public RenderCQRMinotaur(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMinotaurGeo(CQRMain.prefix("geo/entity/biped_minotaur.geo.json"), TEXTURE, "mob/minotaur"));
	}

}
