package team.cqr.cqrepoured.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class ItemHelmetDragon extends ArmorItem {

	//private AttributeModifier attackDamage;
	//private AttributeModifier health;
	private final Multimap<Attribute, AttributeModifier> attributeModifier;

	public ItemHelmetDragon(IArmorMaterial materialIn, EquipmentSlot equipmentSlotIn, Properties props) {
		super(materialIn, equipmentSlotIn, props);

		//this.health = new AttributeModifier("DragonHelmetHealthModifier", 10D, Operation.ADDITION);
		//this.attackDamage = new AttributeModifier("DragonHelmetDamageModifier", 1D, Operation.MULTIPLY_TOTAL);
		
		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.MAX_HEALTH, new AttributeModifier("DragonHelmetHealthModifier", 10D, Operation.ADDITION));
		modifierBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier("DragonHelmetDamageModifier", 1D, Operation.MULTIPLY_TOTAL));
		this.attributeModifier = modifierBuilder.build();
	}

/*	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == MobEntity.getEquipmentSlotForItem(stack)) {
			multimap.put(Attributes.MAX_HEALTH, this.health);
			multimap.put(Attributes.ATTACK_DAMAGE, this.attackDamage);
		}

		return multimap;
	} */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == MobEntity.getEquipmentSlotForItem(stack) ? this.attributeModifier : super.getAttributeModifiers(slot, stack);
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
