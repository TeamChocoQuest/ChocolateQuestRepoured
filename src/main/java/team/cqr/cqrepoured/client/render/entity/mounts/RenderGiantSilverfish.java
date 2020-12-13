package team.cqr.cqrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.mounts.EntityGiantSilverfishNormal;
import team.cqr.cqrepoured.util.Reference;

public class RenderGiantSilverfish extends RenderLiving<EntityGiantSilverfishNormal> {

	static ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/mounts/giant_silverfish.png");

	public RenderGiantSilverfish(RenderManager rendermanagerIn) {
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
