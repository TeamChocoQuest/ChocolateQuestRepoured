package team.cqr.cqrepoured.item.sword;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

public class ItemSwordTurtle extends SwordItem {

	private AttributeModifier knockBack;

	public ItemSwordTurtle(IItemTier material, int attackDamage, Item.Properties props) {
		super(material, attackDamage, material.getSpeed(), props);

		this.knockBack = new AttributeModifier("KnockbackModifier", 1.0D, Operation.ADDITION);
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
		Multimap<Attribute, AttributeModifier> multimap =  super.getDefaultAttributeModifiers(pEquipmentSlot);
		
		if (pEquipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.KNOCKBACK_RESISTANCE, this.knockBack);
		}
		
		return multimap;
	}

}
