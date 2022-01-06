package team.cqr.cqrepoured.client.render.entity.layer.geo;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public abstract class AbstractCQRLayerGeo<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
	
	protected final Function<T, ResourceLocation> funcGetCurrentTexture;
	protected final Function<T, ResourceLocation> funcGetCurrentModel;

	protected GeoEntityRenderer<T> geoRendererInstance;
	
	public AbstractCQRLayerGeo(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
		super(renderer);
		this.geoRendererInstance = renderer;
		this.funcGetCurrentTexture = funcGetCurrentTexture;
		this.funcGetCurrentModel = funcGetCurrentModel;
	}
	
	protected void reRenderCurrentModelInRenderer(T entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, RenderType cameo) {
		matrixStackIn.pushPose();

		this.getRenderer().render(
				this.getEntityModel().getModel(this.funcGetCurrentModel.apply(entity)), 
				entity, 
				partialTicks, 
				cameo, 
				matrixStackIn, 
				bufferIn, 
				bufferIn.getBuffer(cameo), 
				packedLightIn, 
				OverlayTexture.NO_OVERLAY, 
				1F, 1F, 1F, 1F
		);
		
		matrixStackIn.popPose();
		//1.12.2
		/*this.getRenderer().render(
			this.getEntityModel().getModel(this.funcGetCurrentModel.apply(entity)), 
			entity, 
			partialTicks, 
			(float) renderColor.getRed() / 255f, 
			(float) renderColor.getBlue() / 255f,
			(float) renderColor.getGreen() / 255f, 
			(float) renderColor.getAlpha() / 255
		);*/
	}

}
