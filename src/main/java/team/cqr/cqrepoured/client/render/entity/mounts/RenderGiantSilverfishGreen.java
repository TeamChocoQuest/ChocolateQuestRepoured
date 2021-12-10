package team.cqr.cqrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantSilverfishGreen;

public class RenderGiantSilverfishGreen extends RenderLiving<EntityGiantSilverfishGreen> {

	static ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_silverfish_green.png");

	public RenderGiantSilverfishGreen(RenderManager rendermanagerIn) {
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
