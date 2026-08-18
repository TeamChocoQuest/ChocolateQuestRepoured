package com.example.chocolatequest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BigswordItem extends SwordItem {
    public BigswordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int duration = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (duration >= 10) { // Minimalny czas ladowania (0.5 sek)
                player.awardStat(Stats.ITEM_USED.get(this));
                
                float f = player.getYRot();
                float f1 = player.getXRot();
                float f2 = -Mth.sin(f * ((float)Math.PI / 180F)) * Mth.cos(f1 * ((float)Math.PI / 180F));
                float f3 = -Mth.sin(f1 * ((float)Math.PI / 180F));
                float f4 = Mth.cos(f * ((float)Math.PI / 180F)) * Mth.cos(f1 * ((float)Math.PI / 180F));
                float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                
                // Sila wyrzutu (Szarza)
                float boost = 2.5F;
                f2 *= boost / f5;
                f3 *= boost / f5;
                f4 *= boost / f5;
                
                player.push(f2, f3, f4);
                
                if (player.onGround()) {
                    player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
                }
                
                level.playSound(null, player, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
                
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
            }
        }
    }
}
