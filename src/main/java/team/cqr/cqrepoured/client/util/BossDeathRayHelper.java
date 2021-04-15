package team.cqr.cqrepoured.client.util;

import java.util.Random;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;

public class BossDeathRayHelper {
	
	private final int red;
	private final int green;
	private final int blue;
	private final float raySize;
	private final int maxRays = 30;
	
	public BossDeathRayHelper(int red, int green, int blue, float raySize) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.raySize = raySize;
	}
	
	public void renderRays(int ticks, float partialTicks) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		float f = ((float) ticks + partialTicks) / AbstractEntityCQRBoss.MAX_DEATH_TICKS;
		//f = Math.min(f, 1F);
		float f1 = 0.0F;

		if (f > 0.8F) {
			f1 = (f - 0.8F) / 0.2F;
		}

		Random random = new Random(432L);
		
		GlStateManager.pushAttrib();
		
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		// GlStateManager.translate(0.0F, -entitylivingbaseIn.height / 2, 0.0F);

		float rays = (f + f * f) / 2.0F * 60.0F;
		rays = Math.min(maxRays, rays);
		for (int i = 0; (float) i < rays; ++i) {
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
			float f2 = random.nextFloat() * this.raySize + (this.raySize / 4) + f1 * (this.raySize / 2F);
			float f3 = random.nextFloat() * (this.raySize / 10F) + 1.0F + f1 * (this.raySize / 10F);
			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(this.red, this.green, this.blue, (int) (255.0F * (1.0F - f1))).endVertex();
			bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.pos(0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.pos(0.0D, (double) f2, (double) (1.0F * f3)).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(this.red, this.green, this.blue, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		
		GlStateManager.popAttrib();
		
		RenderHelper.enableStandardItemLighting();
	}

}
