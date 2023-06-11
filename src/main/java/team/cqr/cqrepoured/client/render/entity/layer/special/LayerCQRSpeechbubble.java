package team.cqr.cqrepoured.client.render.entity.layer.special;

import java.util.function.Function;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.layer.AbstractLayerGeo;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerCQRSpeechbubble<T extends AbstractEntityCQR & IAnimatable> extends AbstractLayerGeo<T> {

	public static final int CHANGE_BUBBLE_INTERVAL = 80;

	public LayerCQRSpeechbubble(RenderCQREntityGeo<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (CQRConfig.SERVER_CONFIG.general.enableSpeechBubbles.get() && entity.isChatting()) {
			matrixStack.pushPose();
			matrixStack.mulPose(Vector3f.YP.rotation(netHeadYaw));
			matrixStack.translate(-0.5D, entity.getBbHeight() / entity.getSizeVariation() + 0.25D, 0.0D);

			IVertexBuilder builder = bufferIn.getBuffer(CQRRenderTypes.speechbubble(entity.getCurrentSpeechBubble().getResourceLocation()));
			Matrix4f matrix = matrixStack.last().pose();

			builder.vertex(matrix, 0, 1, 0).uv(0, 0).uv2(packedLightIn).endVertex();
			builder.vertex(matrix, 1, 1, 0).uv(1, 0).uv2(packedLightIn).endVertex();
			builder.vertex(matrix, 1, 0, 0).uv(1, 1).uv2(packedLightIn).endVertex();
			builder.vertex(matrix, 0, 0, 0).uv(0, 1).uv2(packedLightIn).endVertex();

			matrixStack.popPose();
		}
	}

}
