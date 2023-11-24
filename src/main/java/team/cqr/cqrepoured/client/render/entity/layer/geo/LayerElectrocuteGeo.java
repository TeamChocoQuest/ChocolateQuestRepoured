package team.cqr.cqrepoured.client.render.entity.layer.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.client.render.entity.layer.IElectrocuteLayerRenderLogic;

public class LayerElectrocuteGeo<T extends LivingEntity & GeoEntity> extends GeoRenderLayer<T> implements IElectrocuteLayerRenderLogic<T> {

	public LayerElectrocuteGeo(GeoEntityRenderer<T> renderer) {
		super(renderer);
	}

	@Override
	public void performPreLineRenderPreparation(PoseStack matrix) {
		//Fixes rotations on glib entities
	}

	@Override
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
		
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
		
		this.renderLayerLogic(animatable, poseStack, bufferSource, limbSwing, limbSwingAmount, partialTick, animatable.tickCount, netHeadYaw, headPitch);
	}

}
