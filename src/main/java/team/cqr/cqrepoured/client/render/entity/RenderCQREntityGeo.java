package team.cqr.cqrepoured.client.render.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.model.ParrotModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
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
public abstract class RenderCQREntityGeo<T extends AbstractEntityCQR & IAnimatable> extends ExtendedGeoEntityRenderer<T> implements IGeoRenderer<T> {

	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRMain.MODID, "textures/entity/magic_armor/mages.png");

	public final Function<T, ResourceLocation> TEXTURE_GETTER;
	public final Function<T, ResourceLocation> MODEL_ID_GETTER;

	protected final Queue<Tuple<GeoBone, CompoundNBT>> SHOULDER_ENTITY_QUEUE = new ArrayDeque<>();

	public RenderCQREntityGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1F, 1F, 0);
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
		this.addLayer(new LayerCQRSpeechbubble<T>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
	}

	@Override
	public float getWidthScale(T entity) {
		return this.widthScale * entity.getSizeVariation();
	}

	@Override
	public float getHeightScale(T entity) {
		return this.heightScale * entity.getSizeVariation();
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return this.TEXTURE_GETTER.apply(entity);
	}

	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (this.isArmorBone(bone)) {
			bone.setCubesHidden(true);
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	protected void handleArmorRenderingForBone(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) {
		super.handleArmorRenderingForBone(bone, stack, bufferIn, packedLightIn, packedOverlayIn, currentTexture);
		handleShoulderEntityBone(bone);
	}

	protected void handleShoulderEntityBone(GeoBone bone) {
		if (bone.getName().startsWith("shoulderEntity")) {
			CompoundNBT data = this.getShoulderEntityDataFor(this.currentEntityBeingRendered, bone);
			if (data != null) {
				this.SHOULDER_ENTITY_QUEUE.add(new Tuple<>(bone, data));
			}
		}
	}

	protected CompoundNBT getShoulderEntityDataFor(T currentEntityBeingRendered, GeoBone bone) {
		switch (bone.name) {
		case "shoulderEntityLeft":
			return currentEntityBeingRendered.getLeftShoulderEntity();
		default:
			return null;
		}
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
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

					float f = MathHelper.lerp(partialTicks, cpe.yRotO, cpe.yRot);

					stack.pushPose();

					Vector3d translate = cpe.position().subtract(entity.position());
					stack.translate(translate.x(), translate.y(), translate.z());

					((EntityRenderer<CQRPartEntity>) renderer).render(cpe, f, partialTicks, stack, bufferIn, packedLightIn);

					stack.popPose();
				}
			}
		}

		// Now, shoulder entities
		this.renderShoulderEntities(stack, bufferIn, packedLightIn, entity);
	}
	
	@Override
	protected void renderLayer(MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float rotFloat, float netHeadYaw, float headPitch, IRenderTypeBuffer bufferIn2,
			GeoLayerRenderer<T> layerRenderer) {
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

	protected void renderShoulderEntities(MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, T entity) {
		while (!this.SHOULDER_ENTITY_QUEUE.isEmpty()) {
			Tuple<GeoBone, CompoundNBT> entry = this.SHOULDER_ENTITY_QUEUE.poll();

			GeoBone bone = entry.getA();
			CompoundNBT entityNBT = entry.getB();

			stack.pushPose();

			this.moveAndRotateMatrixToMatchBone(stack, bone);

			EntityType.byString(entityNBT.getString("id")).filter((typeTmp) -> {
				return typeTmp == CQREntityTypes.PIRATE_PARROT.get();
			}).ifPresent((shoulderEntityType) -> {
				stack.pushPose();
				stack.translate(/*true ? (double) 0.4F : (double) -0.4F CQR entities only have a left shoulder entiy...*/0.4F, entity.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
				IVertexBuilder ivertexbuilder = buffer.getBuffer(this.parrotModel.renderType(RenderPirateParrot.TEXTURE));
				this.parrotModel.renderOnShoulder(stack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, this.limbSwing, this.limbSwingAmount, this.netHeadYaw, this.headPitch, entity.tickCount);
				stack.popPose();
			});

			stack.popPose();

		}
		;
	}

}
