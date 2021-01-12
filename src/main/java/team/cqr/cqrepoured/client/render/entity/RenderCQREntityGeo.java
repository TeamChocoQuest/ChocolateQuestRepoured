package team.cqr.cqrepoured.client.render.entity;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.Reference;

public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & IAnimatable> extends GeoEntityRenderer<T> {

	private String entityName;
	private ResourceLocation texture;
	
	protected double widthScale;
	protected double heightScale;
	
	protected RenderCQREntityGeo(RenderManager renderManager, AnimatedGeoModel<T> modelProvider, String entityName) {
		this(renderManager, modelProvider, entityName, 1D, 1D);
	}
	
	protected RenderCQREntityGeo(RenderManager renderManager, AnimatedGeoModel<T> modelProvider, String entityName, double widthScale, double heightScale) {
		super(renderManager, modelProvider);
		this.shadowSize = 0;
		this.entityName = entityName;
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		
		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + ".png");
	}

	@Override
	public void renderEarly(T animatable, float ticks, float red, float green, float blue, float partialTicks) {
		//super.renderEarly(animatable, ticks, red, green, blue, partialTicks);
		double width = this.widthScale * animatable.getSizeVariation();
		double height = this.heightScale * animatable.getSizeVariation();
		GL11.glScaled(width, height, width);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + "_" + entity.getTextureIndex() + ".png") : this.texture;
	}

	private T currentEntityBeingRendered;
	
	@Override
	public void renderLate(T animatable, float ticks, float red, float green, float blue, float partialTicks) {
		super.renderLate(animatable, ticks, red, green, blue, partialTicks);
		this.currentEntityBeingRendered = animatable;
	}
	
	@Override
	public void renderRecursively(BufferBuilder builder, GeoBone bone, float red, float green, float blue, float alpha) {
		super.renderRecursively(builder, bone, red, green, blue, alpha);
		
		ItemStack boneItem = this.getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
		if(boneItem != null) {
			preRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);
			
			Minecraft.getMinecraft().getItemRenderer().renderItem(this.currentEntityBeingRendered, boneItem, ItemCameraTransforms.TransformType.NONE);
			
			postRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);
		}
	}
	
	/*
	 * Return null if there is no item
	 */
	@Nullable
	protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);
	
	protected abstract void preRenderItem(ItemStack item, String boneName, T currentEntity);
	protected abstract void postRenderItem(ItemStack item, String boneName, T currentEntity);

}
