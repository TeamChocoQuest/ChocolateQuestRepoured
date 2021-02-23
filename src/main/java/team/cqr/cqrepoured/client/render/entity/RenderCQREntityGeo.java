package team.cqr.cqrepoured.client.render.entity;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.MatrixStack;
import team.cqr.cqrepoured.client.util.MatrixUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.Reference;

public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & IAnimatable> extends GeoEntityRenderer<T> {

	private String entityName;
	private ResourceLocation texture;
	
	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(Reference.MODID, "textures/entity/magic_armor/mages.png");

	protected double widthScale;
	protected double heightScale;

	protected RenderCQREntityGeo(RenderManager renderManager, AnimatedGeoModel<T> modelProvider, String entityName) {
		this(renderManager, modelProvider, entityName, 1D, 1D, 0);
	}

	protected RenderCQREntityGeo(RenderManager renderManager, AnimatedGeoModel<T> modelProvider, String entityName, double widthScale, double heightScale, float shadowSize) {
		super(renderManager, modelProvider);
		this.shadowSize = shadowSize;
		this.entityName = entityName;
		this.widthScale = widthScale;
		this.heightScale = heightScale;

		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + this.entityName + ".png");
	}
	
	private int renderPass = 0;
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.renderPass = 0;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		//Magic armor rendering, this is how you render a overlay using geckolib
		if(entity.isMagicArmorActive()) {
			GlStateManager.pushMatrix();
			
			this.renderPass = 1;
			//GlStateManager.scale(1.1, 1.1, 1.1);
			
			GlStateManager.depthMask(!entity.isInvisible());
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = (float) entity.ticksExisted + partialTicks;
			float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
			float f2 = f * 0.01F;
			GlStateManager.translate(f1, f2, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
			
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void renderMultipass(T entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
		super.renderMultipass(entityIn, x, y, z, entityYaw, partialTicks);
	}

	protected double getWidthScale(T entity) {
		return this.widthScale * entity.getSizeVariation();
	}

	protected double getHeightScale(T entity) {
		return this.heightScale * entity.getSizeVariation();
	}

	@Override
	public void renderEarly(T animatable, float ticks, float red, float green, float blue, float partialTicks) {
		double width = this.getWidthScale(animatable);
		double height = this.getHeightScale(animatable);
		GlStateManager.scale(width, height, width);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if(this.renderPass != 0) {
			return TEXTURES_ARMOR;
		}
		
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
		if(this.renderPass == 0) {
			ItemStack boneItem = this.getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
			IBlockState boneBlock = this.getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);
			if (boneItem != null || boneBlock != null) {
				// Huge thanks to McHorse and Gecko to get this to work!!
				Tessellator.getInstance().draw();

				GlStateManager.pushMatrix();
				multiplyMatrix(IGeoRenderer.MATRIX_STACK, bone);

				if (boneItem != null) {
					preRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);

					Minecraft.getMinecraft().getItemRenderer().renderItem(this.currentEntityBeingRendered, boneItem, ItemCameraTransforms.TransformType.NONE);

					postRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);
				}
				if (boneBlock != null) {
					preRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);

					renderBlock(boneBlock, this.currentEntityBeingRendered);

					postRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);
				}

				GlStateManager.popMatrix();
				this.bindTexture(this.getEntityTexture(this.currentEntityBeingRendered));

				builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			}
		}
		super.renderRecursively(builder, bone, red, green, blue, alpha);
	}

	private void renderBlock(IBlockState iBlockState, Entity currentEntity) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.25F, -0.25F, 0.25F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		int i = currentEntity.getBrightnessForRender();
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		blockrendererdispatcher.renderBlockBrightness(iBlockState, 1.0F);

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
	}

	// Code by McHorse
	private static Matrix4f matrix = new Matrix4f();

	/**
	 * Multiply given matrix stack onto OpenGL's matrix stack
	 */
	public static void multiplyMatrix(MatrixStack stack, GeoBone bone) {
		matrix.set(stack.getModelMatrix());
		matrix.transpose();

		MatrixUtil.matrixToFloat(MatrixUtil.floats, matrix);
		MatrixUtil.buffer.clear();
		MatrixUtil.buffer.put(MatrixUtil.floats);
		MatrixUtil.buffer.flip();

		GlStateManager.multMatrix(MatrixUtil.buffer);
		GlStateManager.translate(bone.rotationPointX / 16, bone.rotationPointY / 16, bone.rotationPointZ / 16);
	}

	/*
	 * Return null if there is no item
	 */
	@Nullable
	protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);

	/*
	 * Return null if there is no held block
	 */
	@Nullable
	protected abstract IBlockState getHeldBlockForBone(String boneName, T currentEntity);

	protected abstract void preRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void preRenderBlock(IBlockState block, String boneName, T currentEntity);

	protected abstract void postRenderItem(ItemStack item, String boneName, T currentEntity);

	protected abstract void postRenderBlock(IBlockState block, String boneName, T currentEntity);

}
