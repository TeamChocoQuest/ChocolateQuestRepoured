package team.cqr.cqrepoured.client.render.entity.layer.special;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractCQRLayerGeo;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerCQRSpeechbubble<T extends AbstractEntityCQR & IAnimatable>  extends AbstractCQRLayerGeo<T> {

	public static final int CHANGE_BUBBLE_INTERVAL = 80;

	public LayerCQRSpeechbubble(RenderCQREntityGeo<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (CQRConfig.general.enableSpeechBubbles && entity.isChatting()) {
			matrixStack.pushPose();
			matrixStack.scale(1F, -1F, 1F);
			matrixStack.mulPose(Vector3f.YP.rotation(netHeadYaw));
			//Potentially unneeded
			// DONE : This does not really line up with scaling mob size
			matrixStack.translate(-0.5D, (-1.0D * entity.getBbHeight()) / entity.getSizeVariation(), 0.0D);
			matrixStack.translate(0, -1.25, 0);
			
			IVertexBuilder builder = bufferIn.getBuffer(CQRRenderTypes.speechbubble(entity.getCurrentSpeechBubble().getResourceLocation()));
			Matrix4f matrix = matrixStack.last().pose();

			builder.vertex(matrix, 0, 1, 0).uv(0, 1).endVertex();
			builder.vertex(matrix, 1, 1, 0).uv(1, 1).endVertex();
			builder.vertex(matrix, 1, 0, 0).uv(1, 0).endVertex();
			builder.vertex(matrix, 0, 0, 0).uv(0, 0).endVertex();
			
			matrixStack.popPose();
		}
	}

}
