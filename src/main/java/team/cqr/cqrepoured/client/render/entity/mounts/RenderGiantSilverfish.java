package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishNormal;

public class RenderGiantSilverfish extends AbstractRenderGiantSilverfish<EntityGiantSilverfishNormal> {

	public RenderGiantSilverfish(EntityRendererManager p_i50961_1_) {
		super(p_i50961_1_);
	}
	
	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish.png");

	@Override
	public ResourceLocation getTextureLocation(EntityGiantSilverfishNormal pEntity) {
		return TEXTURE;
	}

}
