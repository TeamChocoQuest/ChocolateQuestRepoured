package team.cqr.cqrepoured.client.render.entity.layer.geo;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerCQRSpeechbubble<T extends AbstractEntityCQR & GeoEntity> extends GeoRenderLayer<T> {

	public static final int CHANGE_BUBBLE_INTERVAL = 80;

	public LayerCQRSpeechbubble(RenderCQREntityGeo<T> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		
		if (CQRConfig.SERVER_CONFIG.general.enableSpeechBubbles.get() && animatable.isChatting()) {
			poseStack.pushPose();
			
			float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
			float lerpHeadRot = Mth.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
			float netHeadYaw = lerpHeadRot - lerpBodyRot;
			
			poseStack.mulPose(Axis.YP.rotation(netHeadYaw));
			poseStack.translate(-0.5D, animatable.getBbHeight() / animatable.getSizeVariation() + 0.25D, 0.0D);

			VertexConsumer builder = bufferSource.getBuffer(CQRRenderTypes.speechbubble(animatable.getCurrentSpeechBubble().getResourceLocation()));
			Matrix4f matrix = poseStack.last().pose();

			builder.vertex(matrix, 0, 1, 0).uv(0, 0).uv2(packedLight).endVertex();
			builder.vertex(matrix, 1, 1, 0).uv(1, 0).uv2(packedLight).endVertex();
			builder.vertex(matrix, 1, 0, 0).uv(1, 1).uv2(packedLight).endVertex();
			builder.vertex(matrix, 0, 0, 0).uv(0, 1).uv2(packedLight).endVertex();

			poseStack.popPose();
		}
	}

}
