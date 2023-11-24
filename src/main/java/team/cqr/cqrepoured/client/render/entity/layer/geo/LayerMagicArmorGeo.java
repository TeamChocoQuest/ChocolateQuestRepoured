package team.cqr.cqrepoured.client.render.entity.layer.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerMagicArmorGeo<T extends AbstractEntityCQR & GeoEntity> extends GeoRenderLayer<T>{

	public LayerMagicArmorGeo(GeoEntityRenderer<T> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		if(animatable.isMagicArmorActive()) {
			float f = (float)animatable.tickCount + partialTick;
			float xOffset = f * 0.01F;
			
			this.renderer.reRender(bakedModel, poseStack, bufferSource, animatable, RenderType.energySwirl(RenderCQREntityGeo.TEXTURES_ARMOR, xOffset, xOffset), buffer, partialTick, packedLight, packedOverlay, 1, 1, 1, 0);
		}
	}
	
	/*@Override
	public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
		if(entity.isMagicArmorActive()) {
			//TODO: Fix weird bug where the entity inflates when it is being looked at and the game gets paused!
			this.geoRendererInstance.bindTexture(RenderCQREntityGeo.TEXTURES_ARMOR);
			
			GlStateManager.pushMatrix();

			GlStateManager.depthMask(!entity.isInvisible());
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = entity.ticksExisted + partialTicks;
			float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
			float f2 = f * 0.01F;
			GlStateManager.translate(f1, f2, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

			this.reRenderCurrentModelInRenderer(entity, partialTicks, renderColor);

			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();

			GlStateManager.popMatrix();
		}
	}*/
}
