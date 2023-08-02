package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.entity.humanoid.ModelCQRSkeletonGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQRBipedBaseGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSkeleton;

public class RenderCQRSkeleton extends RenderCQRBipedBaseGeo<EntityCQRSkeleton> {

	private static final ResourceLocation TEXTURE = CQRMain.prefix("textures/entity/mob/skeleton.png");
	
	public RenderCQRSkeleton(Context renderManager) {
		super(renderManager, new ModelCQRSkeletonGeo(CQRMain.prefix("geo/entity/biped_skeleton.geo.json"), TEXTURE, "mob/skeleton"));
		
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}

}
