package team.cqr.cqrepoured.client.render.entity;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer.EModelRenderCycle;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.ISizable;

public class RenderEntityGeo<T extends Entity & IAnimatable> extends GeoProjectilesRenderer<T> {
	
	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRMain.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;
	
	protected T currentEntityBeingRendered;
	
	protected float widthScale;
	protected float heightScale;
	
	protected EModelRenderCycle currentModelRenderCycle = EModelRenderCycle.INITIAL;
	
	public RenderEntityGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
	}
	
	protected RenderEntityGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
		super(renderManager, modelProvider);

		this.MODEL_ID_GETTER = modelProvider::getModelLocation;
		this.TEXTURE_GETTER = modelProvider::getTextureLocation;

		this.shadowRadius = shadowSize;
		this.widthScale = widthScale;
		this.heightScale = heightScale;
	}
	
	protected float getWidthScale(T entity) {
		if(entity instanceof ISizable) {
			return this.widthScale * ((ISizable)entity).getSizeVariation();
		}
		return this.widthScale;
	}

	protected float getHeightScale(T entity) {
		if(entity instanceof ISizable) {
			return this.heightScale * ((ISizable)entity).getSizeVariation();
		}
		return this.heightScale;
	}

	@Override
	public void renderLate(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		this.currentEntityBeingRendered = animatable;
	}
	
	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.currentModelRenderCycle = EModelRenderCycle.INITIAL;
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	@Override
	public void render(GeoModel model, T animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green,
			float blue, float alpha) {
		super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.currentModelRenderCycle = EModelRenderCycle.REPEATED;
	}
	
	@Override
	public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		if(this.currentModelRenderCycle == EModelRenderCycle.INITIAL) {
			float width = this.getWidthScale(animatable);
			float height = this.getHeightScale(animatable);
			stackIn.scale(width, height, width);
		}
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}
	
}
