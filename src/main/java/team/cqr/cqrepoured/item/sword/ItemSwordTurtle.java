package team.cqr.cqrepoured.item.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ItemSwordTurtle extends SwordItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifier;
	//private AttributeModifier knockBack;

	public ItemSwordTurtle(IItemTier material, int attackDamage, Item.Properties props) {
		super(material, attackDamage, material.getSpeed(), props);

		//this.knockBack = new AttributeModifier("KnockbackModifier", 1.0D, Operation.ADDITION);

		Multimap<Attribute, AttributeModifier> attributeMap = getDefaultAttributeModifiers(EquipmentSlotType.MAINHAND);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> modifierBuilder = ImmutableMultimap.builder();
		modifierBuilder.putAll(attributeMap);
		modifierBuilder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("KnockbackModifier", 1.0D, Operation.ADDITION));
		this.attributeModifier = modifierBuilder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack)
	{
		return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifier : super.getAttributeModifiers(equipmentSlot, stack);
	}
	
/*	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
		Multimap<Attribute, AttributeModifier> multimap =  super.getDefaultAttributeModifiers(pEquipmentSlot);
		
		if (pEquipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.KNOCKBACK_RESISTANCE, this.knockBack);
		}
		
		return multimap;
	} */

/*	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> multimap =  super.getDefaultAttributeModifiers(slot);

		if (slot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.KNOCKBACK_RESISTANCE, this.knockBack);
		}

		return multimap; */
		//return getItem().getDefaultAttributeModifiers(slot);
	//}

}
