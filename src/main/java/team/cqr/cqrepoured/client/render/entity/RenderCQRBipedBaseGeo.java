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
