package team.cqr.cqrepoured.client.render.entity.mounts;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishNormal;

public class RenderGiantSilverfish extends MobRenderer<EntityGiantSilverfishNormal> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish.png");

	public RenderGiantSilverfish(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSilverfish(), 1.5F);
	}

	@Override
	protected void preRenderCallback(EntityGiantSilverfishNormal entitylivingbaseIn, float partialTickTime) {
		double width = 4D;
		double height = 4D;
		GL11.glScaled(width, height, width);

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGiantSilverfishNormal entity) {
		return TEXTURE;
	}

}
