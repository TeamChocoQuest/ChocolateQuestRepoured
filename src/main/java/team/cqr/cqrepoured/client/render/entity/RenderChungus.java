package team.cqr.cqrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.Reference;

public class RenderChungus<T extends AbstractEntityCQR> extends Render<T> {

public double widthScale = 1;
	public double heightScale = 1;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/chungus.png");

	public RenderChungus(RenderManager rendermanagerIn) {
		super(rendermanagerIn);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate((float) x, (float) y + 0.02, (float) z);
		GlStateManager.scale(-1,-1,-1);
		double w = entity.getSizeVariation();
		double h = entity.getSizeVariation();
		GlStateManager.scale(w, h, w);
		GlStateManager.rotate(-entity.rotationYawHead, 0.0F, 1.0F, 0.0F);

		GlStateManager.translate(-0.5D,(-1 * entity.height) / entity.getSizeVariation(), 0.0D);
		Tessellator tessellator = Tessellator.getInstance();
		Minecraft minecraft = Minecraft.getMinecraft();

		minecraft.getTextureManager().bindTexture(this.getEntityTexture(entity));

		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(0, 1.82, 0).tex(0, 1).endVertex();
		buffer.pos(1, 1.82, 0).tex(1, 1).endVertex();
		buffer.pos(1, 0, 0).tex(1, 0).endVertex();
		buffer.pos(0, 0, 0).tex(0, 0).endVertex();

		tessellator.draw();

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return TEXTURE;
	}

}
