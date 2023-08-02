package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRMandrilGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;

public class RenderCQRMandril extends RenderCQRBipedBaseGeo<EntityCQRMandril> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/mandril.png");

	public RenderCQRMandril(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMandrilGeo(CQRMain.prefix("geo/entity/biped_mandril.geo.json"), TEXTURE, "mob/mandril"));
	}

}
