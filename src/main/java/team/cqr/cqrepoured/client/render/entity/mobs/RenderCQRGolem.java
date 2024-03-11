package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRGolemGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;

public class RenderCQRGolem extends RenderCQRBipedBaseGeo<EntityCQRGolem> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/golem.png");
	
	public RenderCQRGolem(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolemGeo(CQRMain.prefix("geo/entity/biped_golem.geo.json"), TEXTURE, "mob/golem"));

		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
