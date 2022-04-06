package team.cqr.cqrepoured.client.render.entity;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

import static team.cqr.cqrepoured.client.render.entity.StandardBipedBones.*;

public abstract class RenderCQRBipedBaseGeo<T extends AbstractEntityCQR & IAnimatable> extends RenderCQREntityGeo<T> {

	protected RenderCQRBipedBaseGeo(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
		super(renderManager, modelProvider);
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
			case ARMOR_LEFT_FOOT:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlotType.FEET;
				break;
			case ARMOR_RIGHT_LEG:
				this.currentArmorAtBone = boots;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlotType.FEET;
				break;
			case ARMOR_LEFT_LEG:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_LEFT;
				this.currentSlotAtBone = EquipmentSlotType.LEGS;
				break;
			case ARMOR_RIGHT_FOOT:
				this.currentArmorAtBone = leggings;
				this.currentArmorPartAtBone = GET_ARMOR_LEG_RIGHT;
				this.currentSlotAtBone = EquipmentSlotType.LEGS;
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
