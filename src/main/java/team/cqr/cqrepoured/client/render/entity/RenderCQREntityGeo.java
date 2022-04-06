package team.cqr.cqrepoured.client.render.entity;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerElectrocuteGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerMagicArmorGeo;
import team.cqr.cqrepoured.client.util.BlockRenderUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

@OnlyIn(Dist.CLIENT)
public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & IAnimatable> extends ExtendedGeoEntityRenderer<T> {

	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRMain.MODID, "textures/entity/magic_armor/mages.png");

	protected float widthScale;
	protected float heightScale;

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;

	protected RenderCQREntityGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
	}
	
	@SuppressWarnings("resource")
	protected void bindTexture(ResourceLocation textureLocation) {
		Minecraft.getInstance().textureManager.bind(textureLocation);
	}

	protected RenderCQREntityGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
		super(renderManager, modelProvider);

		this.MODEL_ID_GETTER = modelProvider::getModelLocation;
		this.TEXTURE_GETTER = modelProvider::getTextureLocation;

		this.shadowRadius = shadowSize;
		this.widthScale = widthScale;
		this.heightScale = heightScale;

		// layers
		this.addLayer(new LayerElectrocuteGeo<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
		this.addLayer(new LayerMagicArmorGeo<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	/*
	 * 0 => Normal model
	 * 1 => Magical armor overlay
	 */
	private int currentModelRenderCycle = 0;

	// Entrypoint for rendering, calls everything else
	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.currentModelRenderCycle = 0;
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
	
	// Rendercall to render the model itself
	@Override
	public void render(GeoModel model, T animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.currentModelRenderCycle++;
	}

	protected float getWidthScale(T entity) {
		return this.widthScale * entity.getSizeVariation();
	}

	protected float getHeightScale(T entity) {
		return this.heightScale * entity.getSizeVariation();
	}

	@Override
	public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
			float partialTicks) {
		this.rtb = renderTypeBuffer;
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		if (this.currentModelRenderCycle == 0 /* Pre-Layers */) {
			float width = this.getWidthScale(animatable);
			float height = this.getHeightScale(animatable);
			stackIn.scale(width, height, width);
		}
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}

	private T currentEntityBeingRendered;
	private IRenderTypeBuffer rtb;

	@Override
	public void renderLate(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
			float partialTicks) {
		super.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		this.currentEntityBeingRendered = animatable;
	}
	
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		boolean customTextureMarker = this.currentModelRenderCycle == 0 && this.getTextureForBone(bone.getName(), this.currentEntityBeingRendered) != null;
		if (customTextureMarker) {
			this.bindTexture(this.getTextureForBone(bone.getName(), this.currentEntityBeingRendered));
		}
		if (this.currentModelRenderCycle == 0) {
			ItemStack boneItem = this.getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
			BlockState boneBlock = this.getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);
			if (boneItem != null || boneBlock != null) {
				stack.pushPose();
				
				if (boneItem != null) {
					this.preRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);

					Minecraft.getInstance().getItemRenderer().renderStatic(boneItem, this.getCameraTransformForItemAtBone(boneItem, bone.getName()), packedLightIn, packedOverlayIn, stack, this.rtb);

					this.postRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);
				}
				if (boneBlock != null) {
					this.preRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);

					this.renderBlock(stack, this.rtb, packedLightIn, boneBlock);

					this.postRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);
				}
				
				stack.popPose();
			}
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		
		if (customTextureMarker) {
			this.bindTexture(this.getTextureLocation(this.currentEntityBeingRendered));
		}
	}
	

	@Override
	protected void renderBlock(MatrixStack matrixStack, IRenderTypeBuffer rtb, int packedLightIn, BlockState iBlockState) {
		BlockRenderUtil.renderBlockAtEntity(matrixStack, rtb, packedLightIn, iBlockState, this.currentEntityBeingRendered, this);
	}

	/*
	 * Return null if there is no item
	 */
	@Nullable
	protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);

	protected abstract TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName);

	/*
	 * Return null if there is no held block
	 */
	@Nullable
	protected abstract BlockState getHeldBlockForBone(String boneName, T currentEntity);

	protected abstract void preRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void preRenderBlock(BlockState block, String boneName, T currentEntity);

	protected abstract void postRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void postRenderBlock(BlockState block, String boneName, T currentEntity);

	/*
	 * Return null, if the entity's texture is used
	 */
	@Nullable
	protected abstract ResourceLocation getTextureForBone(String boneName, T currentEntity);

}
