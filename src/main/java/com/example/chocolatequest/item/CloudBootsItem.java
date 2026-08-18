package com.example.chocolatequest.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

public class CloudBootsItem extends CQArmorItem {

    public CloudBootsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.world.level.Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getItemBySlot(EquipmentSlot.FEET) == stack) {
                // Potion effects (Jump Boost and Speed) were moved to AttributeModifiers in ModItems.java!
                // This ensures they do not show up as potion icons in the inventory GUI.
                // Spawn cloud particles when falling
                if (livingEntity.fallDistance > 0 && !livingEntity.onGround()) {
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1, 0.2D, 0.1D, 0.2D, 0.0D);
                    } else {
                        level.addParticle(ParticleTypes.CLOUD, livingEntity.getRandomX(0.5D), livingEntity.getY(), livingEntity.getRandomZ(0.5D), 0.0D, -0.1D, 0.0D);
                    }
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
