package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.AbstractLayerGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class LayerMagicArmorGeo<T extends AbstractEntityCQR & IAnimatable> extends AbstractLayerGeo<T>{

	public LayerMagicArmorGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
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

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entity.isMagicArmorActive()) {
			float f = (float)entity.tickCount + partialTicks;
			float xOffset = f * 0.01F;
			
			this.reRenderCurrentModelInRenderer(entity, partialTicks, matrixStackIn, bufferIn, packedLightIn, RenderType.energySwirl(RenderCQREntityGeo.TEXTURES_ARMOR, xOffset, xOffset));
		}
	}

}
