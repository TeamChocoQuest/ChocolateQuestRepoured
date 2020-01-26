package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ArmorCQRBase extends ItemArmor {

	public ArmorCQRBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@SideOnly(Side.CLIENT)
	public abstract ModelBiped getBipedArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot);

	@Override
	@SideOnly(Side.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		ModelBiped armor = getBipedArmorModel(entityLiving, itemStack, armorSlot);

		armor.bipedBody.showModel = false;
		armor.bipedHead.showModel = false;
		armor.bipedHeadwear.showModel = false;
		armor.bipedLeftArm.showModel = false;
		armor.bipedLeftLeg.showModel = false;
		armor.bipedRightArm.showModel = false;
		armor.bipedRightLeg.showModel = false;
		
		switch(armorSlot) {
		case CHEST:
			armor.bipedBody.showModel = _default.bipedBody.showModel;
			armor.bipedLeftArm.showModel = _default.bipedLeftArm.showModel;
			armor.bipedRightArm.showModel = _default.bipedRightArm.showModel;
			break;
		case FEET:
			armor.bipedLeftLeg.showModel = _default.bipedLeftLeg.showModel;
			armor.bipedRightLeg.showModel = _default.bipedRightLeg.showModel;
			break;
		case HEAD:
			armor.bipedHead.showModel = _default.bipedHead.showModel;
			armor.bipedHeadwear.showModel = _default.bipedHeadwear.showModel;
			break;
		case LEGS:
			armor.bipedLeftLeg.showModel = _default.bipedLeftLeg.showModel;
			armor.bipedRightLeg.showModel = _default.bipedRightLeg.showModel;
			break;
		default:
			break;
		
		}

		armor.isRiding = _default.isRiding;
		armor.isSneak = _default.isSneak;
		armor.isChild = _default.isChild;
		armor.leftArmPose = _default.leftArmPose;
		armor.rightArmPose = _default.leftArmPose;
		
		copyAnglesAndPositions(_default, armor);
		return armor;
	}
	
	private void copyAnglesAndPositions(ModelBiped source, ModelBiped dest) {
		ModelBase.copyModelAngles(source.bipedBody, dest.bipedBody);
		copyAngles(source.bipedBody, dest.bipedBody);
		copyRotationPointAndOffsets(source.bipedBody, dest.bipedBody);

		ModelBase.copyModelAngles(source.bipedHead, dest.bipedHead);
		copyAngles(source.bipedHead, dest.bipedHead);
		copyRotationPointAndOffsets(source.bipedHead, dest.bipedHead);

		ModelBase.copyModelAngles(source.bipedRightArm, dest.bipedRightArm);
		copyAngles(source.bipedRightArm, dest.bipedRightArm);
		copyRotationPointAndOffsets(source.bipedRightArm, dest.bipedRightArm);

		ModelBase.copyModelAngles(source.bipedLeftArm, dest.bipedLeftArm);
		copyAngles(source.bipedLeftArm, dest.bipedLeftArm);
		copyRotationPointAndOffsets(source.bipedLeftArm, dest.bipedLeftArm);

		ModelBase.copyModelAngles(source.bipedRightLeg, dest.bipedRightLeg);
		copyAngles(source.bipedRightLeg, dest.bipedRightLeg);
		copyRotationPointAndOffsets(source.bipedRightLeg, dest.bipedRightLeg);

		ModelBase.copyModelAngles(source.bipedLeftLeg, dest.bipedLeftLeg);
		copyAngles(source.bipedLeftLeg, dest.bipedLeftLeg);
		copyRotationPointAndOffsets(source.bipedLeftLeg, dest.bipedLeftLeg);
	}

	private void copyRotationPointAndOffsets(ModelRenderer source, ModelRenderer dest) {
		dest.rotationPointX = source.rotationPointX;
		dest.rotationPointY = source.rotationPointY;
		dest.rotationPointZ = source.rotationPointZ;
		dest.offsetX = source.offsetX;
		dest.offsetY = source.offsetY;
		dest.offsetZ = source.offsetZ;
	}
	
	private void copyAngles(ModelRenderer source, ModelRenderer dest) {
		dest.rotateAngleX = source.rotateAngleX;
		dest.rotateAngleY = source.rotateAngleY;
		dest.rotateAngleZ = source.rotateAngleZ;
	}

}
