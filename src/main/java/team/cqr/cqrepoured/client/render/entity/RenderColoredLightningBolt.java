package team.cqr.cqrepoured.client.render.entity;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;

public class RenderColoredLightningBolt extends EntityRenderer<EntityColoredLightningBolt> {

	public RenderColoredLightningBolt(Context renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	public void render(EntityColoredLightningBolt pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		float[] afloat = new float[8];
	      float[] afloat1 = new float[8];
	      float f = 0.0F;
	      float f1 = 0.0F;
	      Random random = new Random(pEntity.seed);

	      for(int i = 7; i >= 0; --i) {
	         afloat[i] = f;
	         afloat1[i] = f1;
	         f += (float)(random.nextInt(11) - 5);
	         f1 += (float)(random.nextInt(11) - 5);
	      }

	      VertexConsumer ivertexbuilder = pBuffer.getBuffer(RenderType.lightning());
	      Matrix4f matrix4f = pMatrixStack.last().pose();

	      for(int j = 0; j < 4; ++j) {
	         Random random1 = new Random(pEntity.seed);

	         for(int k = 0; k < 3; ++k) {
	            int l = 7;
	            int i1 = 0;
	            if (k > 0) {
	               l = 7 - k;
	            }

	            if (k > 0) {
	               i1 = l - 2;
	            }

	            float f2 = afloat[l] - f;
	            float f3 = afloat1[l] - f1;

	            for(int j1 = l; j1 >= i1; --j1) {
	               float f4 = f2;
	               float f5 = f3;
	               if (k == 0) {
	                  f2 += (float)(random1.nextInt(11) - 5);
	                  f3 += (float)(random1.nextInt(11) - 5);
	               } else {
	                  f2 += (float)(random1.nextInt(31) - 15);
	                  f3 += (float)(random1.nextInt(31) - 15);
	               }

	               float f10 = 0.1F + (float)j * 0.2F;
	               if (k == 0) {
	                  f10 = (float)((double)f10 * ((double)j1 * 0.1D + 1.0D));
	               }

	               float f11 = 0.1F + (float)j * 0.2F;
	               if (k == 0) {
	                  f11 *= (float)(j1 - 1) * 0.1F + 1.0F;
	               }

	               quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, pEntity.red, pEntity.green, pEntity.blue, pEntity.alpha, f10, f11, false, false, true, false);
	               quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, pEntity.red, pEntity.green, pEntity.blue, pEntity.alpha, f10, f11, true, false, true, true);
	               quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, pEntity.red, pEntity.green, pEntity.blue, pEntity.alpha, f10, f11, true, true, false, true);
	               quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, pEntity.red, pEntity.green, pEntity.blue, pEntity.alpha, f10, f11, false, true, false, false);
	            }
	         }
	      }

	   }

	   private static void quad(Matrix4f matrix, VertexConsumer builder, float x, float z, int yOffset, float x2, float z2, float cRed, float cGreen, float cBlue, float cAlpha, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
	      builder.vertex(matrix, x + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float)(yOffset * 16), z + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(cRed, cGreen, cBlue, cAlpha).endVertex();
	      builder.vertex(matrix, x2 + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float)((yOffset + 1) * 16), z2 + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(cRed, cGreen, cBlue, cAlpha).endVertex();
	      builder.vertex(matrix, x2 + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float)((yOffset + 1) * 16), z2 + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(cRed, cGreen, cBlue, cAlpha).endVertex();
	      builder.vertex(matrix, x + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float)(yOffset * 16), z + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(cRed, cGreen, cBlue, cAlpha).endVertex();
	   }
	@Override
	public ResourceLocation getTextureLocation(EntityColoredLightningBolt pEntity) {
		return null;
	}

}
