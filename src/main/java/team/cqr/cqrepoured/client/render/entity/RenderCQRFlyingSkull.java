package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.ModelCQRFlyingSkullGeo;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;

public class RenderCQRFlyingSkull extends GeoEntityRenderer<EntityFlyingSkullMinion> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/flying_skull.png");

	public RenderCQRFlyingSkull(Context rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRFlyingSkullGeo(CQRMain.prefix("geo/flying_skull.geo.json"), TEXTURE, "flying_skull"));
	}
}
