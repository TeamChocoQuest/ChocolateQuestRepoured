package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.util.RenderUtils;
import team.cqr.cqrepoured.client.init.CQRModelLayers;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPirateParrot;
import team.cqr.cqrepoured.client.render.entity.RenderPirateParrot;
import team.cqr.cqrepoured.entity.IShoulderEntityProvider;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class LayerPirateCaptainParrot<T extends LivingEntity & GeoEntity & IShoulderEntityProvider> extends GeoRenderLayer<T> {

	private final ModelCQRPirateParrot parrotModel;
	
	public LayerPirateCaptainParrot(EntityRendererProvider.Context renderManager, GeoRenderer<T> entityRendererIn) {
		super((GeoRenderer<T>) entityRendererIn);
		
		this.parrotModel = new ModelCQRPirateParrot(renderManager.getModelSet().bakeLayer(CQRModelLayers.PIRATE_PARROT));
	}
	
	@Override
	public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		
		if (bone.getName().startsWith("shoulderEntity")) {
			Optional<CompoundTag> data = animatable.getShoulderEntityFor(bone.getName());
			if (data.isPresent()) {
				poseStack.pushPose();

				RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);

				EntityType.byString(data.get().getString("id")).filter((typeTmp) -> {
					return typeTmp == CQREntityTypes.PIRATE_PARROT.get();
				}).ifPresent((shoulderEntityType) -> {
					poseStack.pushPose();
					
					float limbSwing = 0;
					float limbSwingAmount = 0;
					float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
					float lerpHeadRot = Mth.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
					float netHeadYaw = lerpHeadRot - lerpBodyRot;
					float headPitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
					
					boolean shouldSit = animatable.isPassenger() && (animatable.getVehicle() != null && animatable.getVehicle().shouldRiderSit());
					
					if (!shouldSit && animatable.isAlive()) {
						limbSwingAmount = animatable.walkAnimation.speed(partialTick);
						limbSwing = animatable.walkAnimation.position(partialTick);

						if (animatable.isBaby())
							limbSwing *= 3f;

						if (limbSwingAmount > 1f)
							limbSwingAmount = 1f;
					}
					
					poseStack.translate(/*true ? (double) 0.4F : (double) -0.4F CQR entities only have a left shoulder entiy...*/0.4F, animatable.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
					VertexConsumer ivertexbuilder = bufferSource.getBuffer(this.parrotModel.renderType(RenderPirateParrot.TEXTURE));
					this.parrotModel.renderOnShoulder(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, limbSwing, limbSwingAmount, netHeadYaw, headPitch, animatable.tickCount);
					poseStack.popPose();
				});

				poseStack.popPose();
				
				buffer = bufferSource.getBuffer(renderType);
			}
		}
	}
	
}
