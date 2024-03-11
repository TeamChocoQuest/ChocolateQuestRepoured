package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRWalkerGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;

public class RenderCQRWalker extends RenderCQRBipedBaseGeo<EntityCQRWalker> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/walker.png");
	
	public RenderCQRWalker(Context renderManager) {
		super(renderManager, new ModelCQRWalkerGeo(STANDARD_BIPED_GEO_MODEL, TEXTURE, "mob/walker"));
		
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
