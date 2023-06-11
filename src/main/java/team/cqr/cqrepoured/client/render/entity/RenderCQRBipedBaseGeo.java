package team.cqr.cqrepoured.client.render.entity;

import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_BODY;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_HEAD;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_LEFT_ARM;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_LEFT_FOOT;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_LEFT_LEG;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_RIGHT_ARM;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_RIGHT_FOOT;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.ARMOR_RIGHT_LEG;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.LEFT_HAND;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.POTION_BONE;
import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.RIGHT_HAND;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.EModelRenderCycle;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

public abstract class RenderCQRBipedBaseGeo<T extends AbstractEntityCQR & GeoEntity> extends RenderCQREntityGeo<T> {
	
	protected final ParrotModel PARROT_MODEL = new ParrotModel();
	
	protected final static ResourceLocation STANDARD_BIPED_GEO_MODEL = CQRMain.prefix("geo/entity/biped_base.geo.json");

	public RenderCQRBipedBaseGeo(Context renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1.0F, 1.0F);
	}
	
	protected RenderCQRBipedBaseGeo(Context renderManager, AnimatedGeoModel<T> modelProvider, final float widthScale, final float heightScale) {
		super(renderManager, modelProvider, widthScale, heightScale, widthScale / 2);
	}

	protected ItemStack currentItemAtBone = null;
	protected TransformType currentTransformTypeAtBone = null;
	
	protected ItemStack currentArmorAtBone = null;
	protected EquipmentSlot currentSlotAtBone = null;
	protected Function<HumanoidModel<?>, ModelRenderer> currentArmorPartAtBone = null;
	
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_HEAD = (armorModel) -> armorModel.head;
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_BODY = (armorModel) -> armorModel.body;
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_ARM_LEFT = (armorModel) -> armorModel.leftArm;
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_ARM_RIGHT = (armorModel) -> armorModel.rightArm;
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_LEG_LEFT = (armorModel) -> armorModel.leftLeg;
	protected final Function<HumanoidModel<?>, ModelRenderer> GET_ARMOR_LEG_RIGHT = (armorModel) -> armorModel.rightLeg;
	
	//Call order (armor): 
	/**
	 * 1) getArmorForBone
	 * 2) getEquipmentSlotForArmorBone
	 * 3) getArmorPartForBone
	 */
	
	//Call order (items):
	/**
	 * 1) getHeldItemForBone
	 * 2) getCameraTransformForItemAtBone
	 */
	
	@Override
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if(bone.getName().equalsIgnoreCase(StandardBipedBones.CAPE_BONE)) {
			bone.setHidden(this.getCurrentModelRenderCycle() != EModelRenderCycle.INITIAL || !this.currentEntityBeingRendered.hasCape(), false);
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	@Override
	protected ResourceLocation getTextureForBone(String boneName, T currentEntity) {
		if(boneName.equalsIgnoreCase(StandardBipedBones.CAPE_BONE) && currentEntity.hasCape()) {
			return currentEntity.getResourceLocationOfCape();
		}
		return null;
	}
	
	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return bone.getName().startsWith("armor");
	}
	
	protected abstract void calculateArmorStuffForBone(String boneName, T currentEntity);
	protected abstract void calculateItemStuffForBone(String boneName, T currentEntity);
	
	protected void standardArmorCalculationForBone(String boneName, T currentEntity) {
		switch(boneName) {
			case ARMOR_BODY:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_BODY;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_HEAD:
				this.currentArmorAtBone = helmet;
				this.currentArmorPartAtBone = GET_ARMOR_HEAD;
				this.currentSlotAtBone = EquipmentSlot.HEAD;
				break;
			case ARMOR_LEFT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_LEFT;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_RIGHT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.CHEST;
				break;
			case ARMOR_RIGHT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.LEGS;
				break;
			case ARMOR_LEFT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlot.LEGS;
				break;
			case ARMOR_RIGHT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlot.FEET;
				break;
			case ARMOR_LEFT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlot.FEET;
				break;
			default:
				break;
		}
	}
	
	public static final ItemStack HEALING_POTION_FOR_DISPLAY = new ItemStack(CQRItems.POTION_HEALING.get());
	
	protected void standardItemCalculationForBone(String boneName, T currentEntity) {
		switch(boneName) {
			case LEFT_HAND:
				this.currentItemAtBone = currentEntity.isLeftHanded() ? this.mainHand : this.offHand;
				this.currentTransformTypeAtBone = TransformType.THIRD_PERSON_RIGHT_HAND;
				break;
			case RIGHT_HAND:
				this.currentItemAtBone = !currentEntity.isLeftHanded() ? this.mainHand : this.offHand;
				this.currentTransformTypeAtBone = TransformType.THIRD_PERSON_RIGHT_HAND;
				break;
			case POTION_BONE:
				this.currentItemAtBone = currentEntity.getHealingPotions() > 0 && !currentEntity.isHoldingPotion() ? currentEntity.getHeldItemPotion() : null;
				this.currentTransformTypeAtBone = TransformType.NONE;
				break;
			default: break;
		}
	}
	
	@Override
	protected ItemStack getArmorForBone(String boneName, T currentEntity) {
		this.currentArmorAtBone = null;
		this.currentSlotAtBone = null;
		this.currentArmorPartAtBone = null;
		
		this.calculateArmorStuffForBone(boneName, currentEntity);
		
		return this.currentArmorAtBone;
	}
	
	@Override
	protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
		return this.currentSlotAtBone;
	}

	@Override
	@Nullable
	protected ModelRenderer getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
		if(this.currentArmorPartAtBone != null) {
			return this.currentArmorPartAtBone.apply(armorModel);
		}
		return null;
	}
	
	@Override
	public void renderEarly(T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
	
	@Override
	protected ItemStack getHeldItemForBone(String boneName, T currentEntity) {
		this.currentItemAtBone = null;
		this.currentTransformTypeAtBone = null;
		
		this.calculateItemStuffForBone(boneName, currentEntity);
		
		return this.currentItemAtBone;
	}
	
	@Override
	protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, T currentEntity, IBone bone) {
		if(boneName.equals(POTION_BONE)) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
		}
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return this.currentTransformTypeAtBone;
	}
	
	@Override
	public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(textureLocation);
	}

}
