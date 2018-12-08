package com.tiviacz.chocolatequestrepoured.objects.items;

import com.google.common.collect.Multimap;
import com.tiviacz.chocolatequestrepoured.init.base.SwordBase;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ItemSwordTurtle extends SwordBase
{
	public ItemSwordTurtle(String name, ToolMaterial material) 
	{
		super(name, material);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier("KnockbackModifier", 1.0D, 0));
        }
        return multimap;
    }
}