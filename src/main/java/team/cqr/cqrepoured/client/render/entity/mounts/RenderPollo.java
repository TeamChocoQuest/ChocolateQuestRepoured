package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPollo;
import team.cqr.cqrepoured.entity.mount.EntityPollo;

public class RenderPollo extends MobRenderer<EntityPollo> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/pollo.png");

	public RenderPollo(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRPollo(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPollo entity) {
		return TEXTURE;
	}

}
