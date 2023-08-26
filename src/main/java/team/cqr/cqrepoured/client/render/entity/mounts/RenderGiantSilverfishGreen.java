package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishGreen;

public class RenderGiantSilverfishGreen extends AbstractRenderGiantSilverfish<EntityGiantSilverfishGreen> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/entity/mounts/giant_silverfish_green.png");
	
	public RenderGiantSilverfishGreen(Context p_i50961_1_) {
		super(p_i50961_1_);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityGiantSilverfishGreen pEntity) {
		return TEXTURE;
	}

}
