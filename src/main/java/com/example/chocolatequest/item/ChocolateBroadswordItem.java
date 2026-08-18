package com.example.chocolatequest.item;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ChocolateBroadswordItem extends SwordItem {

    public ChocolateBroadswordItem(Tier tier, Item.Properties properties) {
        super(tier, properties.attributes(createBroadswordAttributes(tier)));
    }

    public static ItemAttributeModifiers createBroadswordAttributes(Tier tier) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        // Base damage & speed
        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus() + 4.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND); 
        
        // Custom Interaction Range (+1.5 blocks reach)
        builder.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "broadsword_range"), 1.5D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        
        return builder.build();
    }
}
