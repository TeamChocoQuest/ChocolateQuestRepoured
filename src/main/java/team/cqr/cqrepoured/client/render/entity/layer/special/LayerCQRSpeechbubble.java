package team.cqr.cqrepoured.client.render.entity.layer.special;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractCQRLayerGeo;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerCQRSpeechbubble<T extends AbstractEntityCQR & IAnimatable>  extends AbstractCQRLayerGeo<T> {

	public static final int CHANGE_BUBBLE_INTERVAL = 80;

	//private final ExtendedGeoEntityRenderer<T> extendedRenderer;
	
	public LayerCQRSpeechbubble(ExtendedGeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
		//this.extendedRenderer = renderer;
	}

	@Override
	public void render(MatrixStack matrixstack, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (CQRConfig.general.enableSpeechBubbles && entity.isChatting()) {
			//Tessellator tessellator = Tessellator.getInstance();
			//Minecraft minecraft = Minecraft.getInstance();

			matrixstack.pushPose();
			matrixstack.mulPose(Vector3f.YP.rotation(netHeadYaw));
			//Potentially unneeded
			// DONE : This does not really line up with scaling mob size
			/*if (this.extendedRenderer.heightScale != 1.0D || this.extendedRenderer.widthScale != 1.0D) {
				matrixstack.translate(0.0D, 1.5D, 0.0D);
				matrixstack.scale(1.0D / this.extendedRenderer.widthScale, 1.0D / this.extendedRenderer.heightScale, 1.0D / this.extendedRenderer.widthScale);
				matrixstack.translate(0.0D, -1.5D, 0.0D);
			}*/
			matrixstack.translate(-0.5D, (-1.0D * entity.getBbHeight()) / entity.getSizeVariation(), 0.0D);

			/*minecraft.getTextureManager().bindTexture(entity.getCurrentSpeechBubble().getResourceLocation());

			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(0, 1, 0).tex(0, 1).endVertex();
			buffer.pos(1, 1, 0).tex(1, 1).endVertex();
			buffer.pos(1, 0, 0).tex(1, 0).endVertex();
			buffer.pos(0, 0, 0).tex(0, 0).endVertex();

			tessellator.draw();*/
			
			IVertexBuilder builder = bufferIn.getBuffer(CQRRenderTypes.speechbubble(entity.getCurrentSpeechBubble().getResourceLocation()));
			builder.vertex(0, 1, 0).uv(0, 1).endVertex();
			builder.vertex(1, 1, 0).uv(1, 1).endVertex();
			builder.vertex(1, 0, 0).uv(1, 0).endVertex();
			builder.vertex(0, 0, 0).uv(0, 0).endVertex();
			
			matrixstack.popPose();

		}
	}

}
