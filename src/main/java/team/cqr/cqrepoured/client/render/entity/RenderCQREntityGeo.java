package team.cqr.cqrepoured.client.render.entity;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.render.entity.layer.geo.CQRBlockAndItemGeoLayer;
import team.cqr.cqrepoured.client.render.entity.layer.geo.CQRItemArmorGeoLayer;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerElectrocuteGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerMagicArmorGeo;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

@OnlyIn(Dist.CLIENT)
public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & GeoEntity> extends DynamicGeoEntityRenderer<T> implements GeoRenderer<T> {

	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRConstants.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;

	public RenderCQREntityGeo(Context renderManager, GeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
	}

	protected RenderCQREntityGeo(Context renderManager, GeoModel<T> modelProvider, float widthScale, float heightScale, float shadowSize) {
		super(renderManager, modelProvider);

		this.MODEL_ID_GETTER = modelProvider::getModelResource;
		this.TEXTURE_GETTER = modelProvider::getTextureResource;

		this.shadowRadius = shadowSize;
		this.scaleWidth = widthScale;
		this.scaleHeight = heightScale;

		// layers
		this.addRenderLayer(new LayerElectrocuteGeo<T>(this));
		this.addRenderLayer(new LayerMagicArmorGeo<T>(this));
		this.addRenderLayer(new LayerCQRSpeechbubble<>(this));
		
		Optional<BlockAndItemGeoLayer<T>> optItemLayer = this.createBlockAndItemLayer(this);
		Optional<ItemArmorGeoLayer<T>> optArmorLayer = this.createArmorLayer(this);
		
		optItemLayer.ifPresent(this::addRenderLayer);
		optArmorLayer.ifPresent(this::addRenderLayer);
	}
	
	protected Optional<ItemArmorGeoLayer<T>> createArmorLayer(RenderCQREntityGeo<T> renderCQREntityGeo) {
		ItemArmorGeoLayer<T> layer = new CQRItemArmorGeoLayer<>(this, CQRItemArmorGeoLayer.<T>createStandardBipedCalcMap());
		return Optional.of(layer);
	}

	protected Optional<BlockAndItemGeoLayer<T>> createBlockAndItemLayer(RenderCQREntityGeo<T> renderCQREntityGeo) {
		BlockAndItemGeoLayer<T> layer = new CQRBlockAndItemGeoLayer<>(this, CQRBlockAndItemGeoLayer.<T>createStandardBipedCalcMap());
		return Optional.of(layer);
	}

	protected ItemDisplayContext getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return ItemDisplayContext.NONE;
	}

	public float getWidthScale(T entity) {
		return this.scaleWidth * entity.getSizeVariation();
	}

	public float getHeightScale(T entity) {
		return this.scaleHeight * entity.getSizeVariation();
	}
	
	@Override
	public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, T animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
		super.scaleModelForRender(this.getWidthScale(animatable), this.getHeightScale(animatable), poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}

	/*@Override
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (this.isArmorBone(bone)) {
			bone.setCubesHidden(true);
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}*/

	// TODO: Replaced by MHLib, otherwise, move to layer!
	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

		if (entity.getParts() != null && entity.getParts().length > 0) {
			for (PartEntity<?> part : entity.getParts()) {
				if (part instanceof CQRPartEntity<?>) {
					CQRPartEntity<?> cpe = (CQRPartEntity<?>) part;
					if (!cpe.hasCustomRenderer()) {
						continue;
					}
					EntityRenderer<? extends CQRPartEntity<? extends Entity>> renderer = CQREntityRenderers.getRendererFor(cpe, this.entityRenderDispatcher);
					if (renderer == null) {
						continue;
					}

					float f = Mth.lerp(partialTicks, cpe.yRotO, cpe.getYRot());

					stack.pushPose();

					Vec3 translate = cpe.position().subtract(entity.position());
					stack.translate(translate.x(), translate.y(), translate.z());

					((EntityRenderer<CQRPartEntity<?>>) renderer).render(cpe, f, partialTicks, stack, bufferIn, packedLightIn);

					stack.popPose();
				}
			}
		}
	}
	
	//TODO: Include in layer subclass
	/*protected HumanoidModel<?> currentArmorModel = null;
	
	protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
		this.currentArmorModel = armorModel;
		return super.getArmorPartForBone(name, armorModel);
	}
	
	@Override
	protected void renderArmorPart(PoseStack stack, ModelRenderer sourceLimb, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, ItemStack armorForBone, ResourceLocation armorResource) {
		if(this.currentArmorModel != null) {
			VertexConsumer ivb = ItemRenderer.getArmorFoilBuffer(this.getCurrentRTB(),
					this.currentArmorModel.renderType(armorResource), false, armorForBone.hasFoil());
			sourceLimb.render(stack, ivb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
		super.renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, red, green, blue, alpha, armorForBone, armorResource);
	}*/

}
