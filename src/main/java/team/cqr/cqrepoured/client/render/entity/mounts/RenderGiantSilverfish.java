package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishNormal;

public class RenderGiantSilverfish extends AbstractRenderGiantSilverfish<EntityGiantSilverfishNormal> {

	public RenderGiantSilverfish(Context p_i50961_1_) {
		super(p_i50961_1_);
	}
	
	static ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/entity/mounts/giant_silverfish.png");

	@Override
	public ResourceLocation getTextureLocation(EntityGiantSilverfishNormal pEntity) {
		return TEXTURE;
	}

}
