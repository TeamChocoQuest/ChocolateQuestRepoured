package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRTritonGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;

public class RenderCQRTriton extends RenderCQRBipedBaseGeo<EntityCQRTriton> {
	
	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/triton_0.png");

	public RenderCQRTriton(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTritonGeo(CQRMain.prefix("geo/entity/biped_triton.geo.json"), TEXTURE, "mob/triton"));
	}

}
