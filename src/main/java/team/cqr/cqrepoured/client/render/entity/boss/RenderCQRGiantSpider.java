package team.cqr.cqrepoured.client.render.entity.boss;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelGiantSpider;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.ITextureVariants;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;

public class RenderCQRGiantSpider extends RenderLiving<EntityCQRGiantSpider> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/giant_spider.png");

	public RenderCQRGiantSpider(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantSpider(), 0.0F);
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
	}
	
	protected double getWidthScale(EntityCQRGiantSpider entity) {
		return entity.getSizeVariation();
	}

	protected double getHeightScale(EntityCQRGiantSpider entity) {
		return entity.getSizeVariation();
	}

	@Override
	protected void preRenderCallback(EntityCQRGiantSpider entitylivingbaseIn, float partialTickTime) {
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);

		double width = this.getWidthScale(entitylivingbaseIn);
		double height = this.getHeightScale(entitylivingbaseIn);
		GL11.glScaled(width, height, width);

		if (this.mainModel.isRiding) {
			GL11.glTranslatef(0, 0.6F, 0);
		}
	}

	protected ResourceLocation[] textureVariantCache = null;

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantSpider entity) {
		if (entity instanceof IHasTextureOverride) {
			// Custom texture start
			if (((IHasTextureOverride) entity).hasTextureOverride()) {
				return ((IHasTextureOverride) entity).getTextureOverride();
			}
		}
		// Custom texture end
		if (entity instanceof ITextureVariants) {
			if (((ITextureVariants) entity).getTextureCount() > 1) {
				if (this.textureVariantCache == null) {
					this.textureVariantCache = new ResourceLocation[((ITextureVariants) entity).getTextureCount()];
				}
				final int index = ((ITextureVariants) entity).getTextureIndex();
				if (this.textureVariantCache[index] == null) {
					this.textureVariantCache[index] = new ResourceLocation(CQRMain.MODID, "textures/entity/" + "boss/giant_spider" + "_" + index + ".png");
				}
				return this.textureVariantCache[index];
			}
		}
		return TEXTURE;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRGiantSpider entityLivingBaseIn) {
		return 180F;
	}

}
