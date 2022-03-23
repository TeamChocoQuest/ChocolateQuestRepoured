package team.cqr.cqrepoured.item.armor;

import com.google.common.collect.Multimap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class ItemHelmetDragon extends ArmorItem {

	private AttributeModifier attackDamage;
	private AttributeModifier health;

	public ItemHelmetDragon(ArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Properties props) {
		super(materialIn, equipmentSlotIn, props);

		this.health = new AttributeModifier("DragonHelmetHealthModifier", 10D, Operation.ADDITION);
		this.attackDamage = new AttributeModifier("DragonHelmetDamageModifier", 1D, Operation.MULTIPLY_TOTAL);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.MAX_HEALTH, this.health);
			multimap.put(Attributes.ATTACK_DAMAGE, this.attackDamage);
		}

		return multimap;
	}

	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @Override public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot
	 * armorSlot, ModelBiped _default) { if
	 * (itemStack != null) { if (itemStack.getItem() instanceof ItemArmor) { ModelHelmetDragon
	 * model = new ModelHelmetDragon();
	 * 
	 * model.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD; model.bipedHeadwear.showModel = armorSlot ==
	 * EntityEquipmentSlot.HEAD;
	 * 
	 * model.isSneak = _default.isSneak; model.isRiding = _default.isRiding; model.isChild = _default.isChild;
	 * model.rightArmPose = _default.rightArmPose;
	 * model.leftArmPose = _default.leftArmPose;
	 * 
	 * return model; } } return null; }
	 */

}
