package team.cqr.cqrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.mount.EntityGiantEndermite;

public class RenderGiantEndermite extends MobRenderer<EntityGiantEndermite> {

	private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation(CQRMain.MODID, "textures/entity/mounts/giant_endermite.png");

	public RenderGiantEndermite(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderMite(), 1.5F);
	}

	@Override
	protected void preRenderCallback(EntityGiantEndermite entitylivingbaseIn, float partialTickTime) {
		double width = 2.5D;
		double height = 2.5D;
		GL11.glScaled(width, height, width);

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected float getDeathMaxRotation(EntityGiantEndermite entityLivingBaseIn) {
		return 180.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGiantEndermite entity) {
		return ENDERMITE_TEXTURES;
	}

}
