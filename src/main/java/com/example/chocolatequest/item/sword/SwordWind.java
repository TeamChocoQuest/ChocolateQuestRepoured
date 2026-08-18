package com.example.chocolatequest.item.sword;

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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import java.util.List;

public class SwordWind extends CQSwordItem {
    public SwordWind(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        
        if (!pLevel.isClientSide()) {
            // Push nearby entities
            AABB bounds = pPlayer.getBoundingBox().inflate(6.0D);
            List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, bounds, e -> e != pPlayer);
            
            for (LivingEntity entity : entities) {
                Vec3 diff = entity.position().subtract(pPlayer.position());
                if (diff.lengthSqr() < 0.01) {
                    diff = new Vec3(1, 0, 0);
                }
                diff = diff.normalize().scale(1.8D).add(0, 0.4D, 0); // Horizontal push + slight uplift
                entity.setDeltaMovement(entity.getDeltaMovement().add(diff));
                entity.hurtMarked = true;
            }
            
            if (pLevel instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.CLOUD, pPlayer.getX(), pPlayer.getY() + 1.0D, pPlayer.getZ(), 50, 2.0D, 0.5D, 2.0D, 0.1D);
                sl.playSound(null, pPlayer.blockPosition(), net.minecraft.sounds.SoundEvents.WIND_CHARGE_BURST.value(), net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            
            pPlayer.getCooldowns().addCooldown(this, 100); // 5 seconds cooldown
            itemstack.hurtAndBreak(1, pPlayer, LivingEntity.getSlotForHand(pUsedHand));
        }
        
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, net.minecraft.world.item.Item.TooltipContext pContext, java.util.List<net.minecraft.network.chat.Component> pTooltipComponents, net.minecraft.world.item.TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(net.minecraft.network.chat.Component.translatable(this.getDescriptionId() + ".desc").withStyle(net.minecraft.ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
