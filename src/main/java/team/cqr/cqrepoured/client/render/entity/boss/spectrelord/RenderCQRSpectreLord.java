package team.cqr.cqrepoured.client.render.entity.boss.spectrelord;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.util.Reference;

public class RenderCQRSpectreLord extends RenderCQREntity<EntityCQRSpectreLord> {

	private static final ResourceLocation TEXTURE_INVISIBLE = new ResourceLocation(Reference.MODID, "textures/entity/boss/spectre_lord_invisible.png");

	public RenderCQRSpectreLord(RenderManager renderManager) {
		super(renderManager, "boss/spectre_lord", true);
	}

	@Override
	protected void renderModel(EntityCQRSpectreLord entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = entitylivingbaseIn.getInvisibility() > 0.0F;
		if (flag) {
			GlStateManager.alphaFunc(GL11.GL_GREATER, entitylivingbaseIn.getInvisibility());
			this.bindTexture(TEXTURE_INVISIBLE);
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.depthFunc(GL11.GL_EQUAL);
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		if (flag) {
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
		}
	}

}
