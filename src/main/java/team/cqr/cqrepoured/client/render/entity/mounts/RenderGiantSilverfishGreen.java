package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishGreen;

public class RenderGiantSilverfishGreen extends MobRenderer<EntityGiantSilverfishGreen> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish_green.png");

	public RenderGiantSilverfishGreen(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSilverfish(), 1.5F);
	}

	@Override
	protected void preRenderCallback(EntityGiantSilverfishGreen entitylivingbaseIn, float partialTickTime) {
		double width = 4D;
		double height = 4D;
		GL11.glScaled(width, height, width);

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGiantSilverfishGreen entity) {
		return TEXTURE;
	}
}
