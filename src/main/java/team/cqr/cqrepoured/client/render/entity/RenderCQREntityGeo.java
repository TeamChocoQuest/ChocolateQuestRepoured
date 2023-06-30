package team.cqr.cqrepoured.client.render.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.model.entity.ModelCQRPirateParrot;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerElectrocuteGeo;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerMagicArmorGeo;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQREntityTypes;

@OnlyIn(Dist.CLIENT)
public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & GeoEntity> extends DynamicGeoEntityRenderer<T> implements GeoRenderer<T> {

	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRMain.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;

	protected final Queue<Tuple<GeoBone, CompoundTag>> SHOULDER_ENTITY_QUEUE = new ArrayDeque<>();

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
		this.addLayer(new LayerElectrocuteGeo<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
		this.addLayer(new LayerMagicArmorGeo<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
		this.addLayer(new LayerCQRSpeechbubble<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
		
		this.addRenderLayer(new BlockAndItemGeoLayer<T>(this, this::getHeldItemForBone, this::getHeldBlockForBone) {
			@Override
			protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
				return RenderCQREntityGeo.this.getCameraTransformForItemAtBone(stack, bone.getName());
			}
		});
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

	@Override
	protected void handleArmorRenderingForBone(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) {
		super.handleArmorRenderingForBone(bone, stack, bufferIn, packedLightIn, packedOverlayIn, currentTexture);
		handleShoulderEntityBone(bone);
	}

	protected void handleShoulderEntityBone(GeoBone bone) {
		if (bone.getName().startsWith("shoulderEntity")) {
			CompoundTag data = this.getShoulderEntityDataFor(this.animatable, bone);
			if (data != null) {
				this.SHOULDER_ENTITY_QUEUE.add(new Tuple<>(bone, data));
			}
		}
	}

	protected CompoundTag getShoulderEntityDataFor(T currentEntityBeingRendered, GeoBone bone) {
		switch (bone.getName()) {
		case "shoulderEntityLeft":
			return currentEntityBeingRendered.getLeftShoulderEntity();
		default:
			return null;
		}
	}

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

		// Now, shoulder entities
		this.renderShoulderEntities(stack, bufferIn, packedLightIn, entity);
	}
	
	@Override
	protected void renderLayer(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float rotFloat, float netHeadYaw, float headPitch, MultiBufferSource bufferIn2,
                               GeoRenderLayer<T> layerRenderer) {
		super.renderLayer(stack, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, rotFloat, netHeadYaw, headPitch, bufferIn2, layerRenderer);
		
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.netHeadYaw = netHeadYaw;
		this.headPitch = headPitch;
	}
	
	private float limbSwing;
	private float limbSwingAmount;
	private float netHeadYaw;
	private float headPitch;
	
	private final ModelCQRPirateParrot parrotModel = new ModelCQRPirateParrot();

	protected void renderShoulderEntities(PoseStack stack, MultiBufferSource buffer, int packedLightIn, T entity) {
		while (!this.SHOULDER_ENTITY_QUEUE.isEmpty()) {
			Tuple<GeoBone, CompoundTag> entry = this.SHOULDER_ENTITY_QUEUE.poll();

			GeoBone bone = entry.getA();
			CompoundTag entityNBT = entry.getB();

			stack.pushPose();

			this.moveAndRotateMatrixToMatchBone(stack, bone);

			EntityType.byString(entityNBT.getString("id")).filter((typeTmp) -> {
				return typeTmp == CQREntityTypes.PIRATE_PARROT.get();
			}).ifPresent((shoulderEntityType) -> {
				stack.pushPose();
				stack.translate(/*true ? (double) 0.4F : (double) -0.4F CQR entities only have a left shoulder entiy...*/0.4F, entity.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
				VertexConsumer ivertexbuilder = buffer.getBuffer(this.parrotModel.renderType(RenderPirateParrot.TEXTURE));
				this.parrotModel.renderOnShoulder(stack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, this.limbSwing, this.limbSwingAmount, this.netHeadYaw, this.headPitch, entity.tickCount);
				stack.popPose();
			});

			stack.popPose();

		}
		;
	}
	
	protected HumanoidModel<?> currentArmorModel = null;
	
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
	}

	protected BlockState getHeldBlockForBone(GeoBone boneName, T currentEntity) {
		return null;
	}

	protected ItemStack getHeldItemForBone(GeoBone boneName, T currentEntity) {
		return null;
	}

}
