package team.cqr.cqrepoured.client.render.entity;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.entity.ISizable;
import team.cqr.cqrepoured.mixin.AccessorEntityRenderDispatcher;

public class RenderEntityGeo<T extends Entity & GeoEntity> extends GeoEntityRenderer<T> {
	
	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRConstants.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;
	
	protected float widthScale;
	protected float heightScale;

	public final static EntityRendererProvider.Context generateContext(EntityRenderDispatcher dispatcher) {
		EntityRendererProvider.Context context = new Context(
				dispatcher, 
				((AccessorEntityRenderDispatcher)dispatcher).getItemRenderer(), 
				((AccessorEntityRenderDispatcher)dispatcher).getBlockRenderDispatcher(), 
				dispatcher.getItemInHandRenderer(), 
				Minecraft.getInstance().getResourceManager(), 
				((AccessorEntityRenderDispatcher)dispatcher).getEntityModels(), 
				((AccessorEntityRenderDispatcher)dispatcher).getFont()
		);
		
		return context;
	}
	
	
	public RenderEntityGeo(EntityRendererProvider.Context renderManager, GeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
	}
	
	protected RenderEntityGeo(EntityRendererProvider.Context renderManager, GeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
		super(renderManager, modelProvider);

		this.MODEL_ID_GETTER = modelProvider::getModelResource;
		this.TEXTURE_GETTER = modelProvider::getTextureResource;

		this.shadowRadius = shadowSize;
		this.widthScale = widthScale;
		this.heightScale = heightScale;
	}
	
	public float getWidthScale(T entity) {
		if(entity instanceof ISizable) {
			return this.widthScale * ((ISizable)entity).getSizeVariation();
		}
		return this.widthScale;
	}

	public float getHeightScale(T entity) {
		if(entity instanceof ISizable) {
			return this.heightScale * ((ISizable)entity).getSizeVariation();
		}
		return this.heightScale;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}
	
}
