package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishRed;

public class RenderGiantSilverfishRed extends AbstractRenderGiantSilverfish<EntityGiantSilverfishRed> {

	public RenderGiantSilverfishRed(Context p_i50961_1_) {
		super(p_i50961_1_);
	}

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish_red.png");

	@Override
	public ResourceLocation getTextureLocation(EntityGiantSilverfishRed pEntity) {
		return TEXTURE;
	}
}
