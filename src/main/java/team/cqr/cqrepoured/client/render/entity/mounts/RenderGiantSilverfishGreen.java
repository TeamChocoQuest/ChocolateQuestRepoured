package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishGreen;

public class RenderGiantSilverfishGreen extends AbstractRenderGiantSilverfish<EntityGiantSilverfishGreen> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish_green.png");
	
	public RenderGiantSilverfishGreen(EntityRendererManager p_i50961_1_) {
		super(p_i50961_1_);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityGiantSilverfishGreen pEntity) {
		return TEXTURE;
	}

}
