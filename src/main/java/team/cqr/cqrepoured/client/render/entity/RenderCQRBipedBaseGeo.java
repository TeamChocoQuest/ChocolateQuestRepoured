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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

public abstract class RenderCQRBipedBaseGeo<T extends AbstractEntityCQR & IAnimatable> extends RenderCQREntityGeo<T> {
	
	protected final static ResourceLocation STANDARD_BIPED_GEO_MODEL = CQRMain.prefix("geo/entity/biped_base.geo.json");

	public RenderCQRBipedBaseGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
		this(renderManager, modelProvider, 1.0F, 1.0F);
	}
	
	protected RenderCQRBipedBaseGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider, final float widthScale, final float heightScale) {
		super(renderManager, modelProvider, widthScale, heightScale, widthScale / 2);
	}

	protected ItemStack currentItemAtBone = null;
	protected TransformType currentTransformTypeAtBone = null;
	
	protected ItemStack currentArmorAtBone = null;
	protected EquipmentSlotType currentSlotAtBone = null;
	protected Function<BipedModel<?>, ModelRenderer> currentArmorPartAtBone = null;
	
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_HEAD = (armorModel) -> armorModel.head;
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_BODY = (armorModel) -> armorModel.body;
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_ARM_LEFT = (armorModel) -> armorModel.leftArm;
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_ARM_RIGHT = (armorModel) -> armorModel.rightArm;
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_LEG_LEFT = (armorModel) -> armorModel.leftLeg;
	protected final Function<BipedModel<?>, ModelRenderer> GET_ARMOR_LEG_RIGHT = (armorModel) -> armorModel.rightLeg;
	
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
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
				this.currentSlotAtBone = EquipmentSlotType.CHEST;
				break;
			case ARMOR_HEAD:
				this.currentArmorAtBone = helmet;
				this.currentArmorPartAtBone = GET_ARMOR_HEAD;
				this.currentSlotAtBone = EquipmentSlotType.HEAD;
				break;
			case ARMOR_LEFT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_LEFT;
				this.currentSlotAtBone = EquipmentSlotType.CHEST;
				break;
			case ARMOR_RIGHT_ARM:
				this.currentArmorAtBone = chestplate;
				this.currentArmorPartAtBone = GET_ARMOR_ARM_RIGHT;
				this.currentSlotAtBone = EquipmentSlotType.CHEST;
				break;
			case ARMOR_RIGHT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlotType.LEGS;
				break;
			case ARMOR_LEFT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlotType.LEGS;
				break;
			case ARMOR_RIGHT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlotType.FEET;
				break;
			case ARMOR_LEFT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlotType.FEET;
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
				this.currentItemAtBone = HEALING_POTION_FOR_DISPLAY;
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
	protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
		return this.currentSlotAtBone;
	}

	@Override
	@Nullable
	protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
		if(this.currentArmorPartAtBone != null) {
			return this.currentArmorPartAtBone.apply(armorModel);
		}
		return null;
	}
	
	@Override
	public void renderEarly(T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
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
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		return this.currentTransformTypeAtBone;
	}

}
