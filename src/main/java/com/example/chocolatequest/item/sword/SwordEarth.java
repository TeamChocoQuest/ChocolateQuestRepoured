package com.example.chocolatequest.item.sword;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class SwordEarth extends CQSwordItem {
    public SwordEarth(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (!pLevel.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) pLevel;
            
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.5F, 0.8F);

            double radius = 6.0D;
            AABB area = pPlayer.getBoundingBox().inflate(radius, 2.0D, radius);
            List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, area);

            for (LivingEntity target : entities) {
                if (target != pPlayer && target.isAlive() && !pPlayer.isAlliedTo(target)) {
                    // Deal damage
                    target.hurt(pLevel.damageSources().playerAttack(pPlayer), 8.0F);
                    
                    // Knockback
                    Vec3 knockbackDir = target.position().subtract(pPlayer.position()).normalize();
                    target.knockback(1.5D, -knockbackDir.x, -knockbackDir.z);
                }
            }

            // Spawn particles in a circle
            for (int i = 0; i < 36; i++) {
                double angle = i * Math.PI / 18.0;
                double xOffset = Math.cos(angle) * 3.0;
                double zOffset = Math.sin(angle) * 3.0;
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, 
                    pPlayer.getX() + xOffset, pPlayer.getY() + 0.2, pPlayer.getZ() + zOffset, 
                    3, 0.5, 0.2, 0.5, 0.05);
            }

            // Put item on cooldown for 10 seconds (200 ticks)
            pPlayer.getCooldowns().addCooldown(this, 200);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, java.util.List<net.minecraft.network.chat.Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(net.minecraft.network.chat.Component.translatable(this.getDescriptionId() + ".desc").withStyle(net.minecraft.ChatFormatting.GREEN));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
