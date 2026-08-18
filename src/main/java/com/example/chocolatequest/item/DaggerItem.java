package com.example.chocolatequest.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class DaggerItem extends SwordItem {
    private static final java.util.Map<java.util.UUID, Long> invisCooldowns = new java.util.HashMap<>();

    public DaggerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.LivingEntity target, net.minecraft.world.entity.LivingEntity attacker) {
        net.minecraft.world.phys.Vec3 targetLook = target.getLookAngle();
        net.minecraft.world.phys.Vec3 attackerView = attacker.position().subtract(target.position()).normalize();
        
        // If dotProduct < -0.3, attacker is roughly behind the target
        if (targetLook.dot(attackerView) < -0.3) {
            target.invulnerableTime = 0; // Bypass i-frames for backstab bonus
            net.minecraft.world.damagesource.DamageSource source = attacker instanceof net.minecraft.world.entity.player.Player player 
                ? attacker.damageSources().playerAttack(player) 
                : attacker.damageSources().mobAttack(attacker);
            target.hurt(source, 6.0F); // Extra backstab damage
            
            if (target.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, target.getX(), target.getY() + target.getBbHeight()/2, target.getZ(), 15, 0.3, 0.3, 0.3, 0.1);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> use(net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
        net.minecraft.world.item.ItemStack itemstack = player.getItemInHand(hand);
        
        if (player.isShiftKeyDown() && this == com.example.chocolatequest.registry.ModItems.SHADOW_DAGGER.get()) {
            long currentTime = System.currentTimeMillis();
            long lastUseTime = invisCooldowns.getOrDefault(player.getUUID(), 0L);
            if (currentTime - lastUseTime >= 10000) { // 10 seconds cooldown
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.INVISIBILITY, 100, 0, false, false, true));
                invisCooldowns.put(player.getUUID(), currentTime);
                player.playSound(net.minecraft.sounds.SoundEvents.ILLUSIONER_PREPARE_BLINDNESS, 1.0F, 1.0F);
                return net.minecraft.world.InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
            } else {
                if (!level.isClientSide()) {
                    long remaining = 10 - (currentTime - lastUseTime) / 1000;
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal("Invisibility cooldown: " + remaining + "s").withStyle(net.minecraft.ChatFormatting.RED), true);
                }
                return net.minecraft.world.InteractionResultHolder.pass(itemstack);
            }
        }
        
        if (!player.getCooldowns().isOnCooldown(this) && !player.hasEffect(net.minecraft.world.effect.MobEffects.INVISIBILITY)) {
            net.minecraft.world.phys.Vec3 look = player.getLookAngle();
            // Short horizontal dash
            player.setDeltaMovement(player.getDeltaMovement().add(look.x * 0.8, 0.2, look.z * 0.8));
            player.hurtMarked = true;
            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 30); // 1.5s cooldown
            player.playSound(net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 2.0F);
            return net.minecraft.world.InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return net.minecraft.world.InteractionResultHolder.pass(itemstack);
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
