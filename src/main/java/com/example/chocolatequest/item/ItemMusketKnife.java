package com.example.chocolatequest.item;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ItemMusketKnife extends ItemMusket {

    private final float attackDamage;

    public ItemMusketKnife(Tier tier, Properties properties) {
        super(properties.attributes(createAttributes(tier)));
        this.attackDamage = 3.0F + tier.getAttackDamageBonus();
    }

    private static ItemAttributeModifiers createAttributes(Tier tier) {
        float attackDamage = 3.0F + tier.getAttackDamageBonus();
        float attackSpeed = -3.2F;
        
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean supportsEnchantment(net.minecraft.world.item.ItemStack stack, net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment) {
        return enchantment.value().matchingSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND) || super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(net.minecraft.world.item.ItemStack stack, net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment) {
        return this.supportsEnchantment(stack, enchantment);
    }
}
