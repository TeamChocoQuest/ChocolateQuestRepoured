package team.cqr.cqrepoured.client.util;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Vector3f;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

public class BossDeathRayHelper {

	private final int red;
	private final int green;
	private final int blue;
	private final float raySize;
	private final int maxRays;

	public BossDeathRayHelper(int red, int green, int blue, float raySize) {
		this(red, green, blue, raySize, 60);
	}

	public BossDeathRayHelper(int red, int green, int blue, float raySize, int maxRays) {
		this.red = red;
		this.maxRays = maxRays;
		this.green = green;
		this.blue = blue;
		this.raySize = raySize;
	}

	public void renderRays(MatrixStack ms, int ticks, float partialTicks) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderHelper.disableStandardItemLighting();
		float f = (ticks + partialTicks) / AbstractEntityCQRBoss.MAX_DEATH_TICKS;
		float f1 = 0.0F;

		if (f > 0.8F) {
			f1 = (f - 0.8F) / 0.2F;
		}

		Random random = new Random(432L);

		GlStateManager.pushAttrib();

		GlStateManager._disableTexture();
		GlStateManager._shadeModel(7425);
		GlStateManager._enableBlend();
		GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
		GlStateManager._disableAlphaTest();
		GlStateManager._enableCull();
		GlStateManager._depthMask(false);
		//GlStateManager.pushMatrix();
		ms.pushPose();
		// GlStateManager.translate(0.0F, -entitylivingbaseIn.height / 2, 0.0F);

		float rays = (f + f * f) / 2.0F * 60.0F;
		rays = Math.min(this.maxRays, rays);
		for (int i = 0; i < rays; ++i) {
			ms.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F + f * 90.0F));
			//GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			//GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			//GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			//GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			//GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			//GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
			float f2 = random.nextFloat() * this.raySize + (this.raySize / 4) + f1 * (this.raySize / 2F);
			float f3 = random.nextFloat() * (this.raySize / 10F) + 1.0F + f1 * (this.raySize / 10F);
			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.vertex(0.0D, 0.0D, 0.0D).color(this.red, this.green, this.blue, (int) (this.raySize * (1.0F - f1))).endVertex();
			bufferbuilder.vertex(-0.866D * f3, f2, -0.5F * f3).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.vertex(0.866D * f3, f2, -0.5F * f3).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.vertex(0.0D, f2, 1.0F * f3).color(this.red, this.green, this.blue, 0).endVertex();
			bufferbuilder.vertex(-0.866D * f3, f2, -0.5F * f3).color(this.red, this.green, this.blue, 0).endVertex();
			tessellator.end();
		}

		ms.popPose();
		//GlStateManager.popMatrix();
		GlStateManager._depthMask(true);
		GlStateManager._disableCull();
		GlStateManager._disableBlend();
		GlStateManager._shadeModel(7424);
		GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager._enableTexture();
		GlStateManager._enableAlphaTest();

		GlStateManager.popAttrib();

		RenderHelper.enableStandardItemLighting();
	}

}
