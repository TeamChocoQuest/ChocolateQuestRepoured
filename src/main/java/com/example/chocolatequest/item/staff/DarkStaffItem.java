package com.example.chocolatequest.item.staff;

import com.example.chocolatequest.entity.projectile.DarkProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.example.chocolatequest.item.IRangedWeapon;

public class DarkStaffItem extends Item implements IRangedWeapon {

    public DarkStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Needs half a drumstick
        if (player.getFoodData().getFoodLevel() >= 1 || player.getAbilities().instabuild) {
            
            if (!level.isClientSide()) {
                DarkProjectileEntity projectile = new DarkProjectileEntity(level, player);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(projectile);
                
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            }

            if (!player.getAbilities().instabuild) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
            }

            // Short cooldown
            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 10);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void shoot(net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity shooter, net.minecraft.world.entity.Entity target, net.minecraft.world.InteractionHand hand) {
        if (!level.isClientSide()) {
            DarkProjectileEntity projectile = new DarkProjectileEntity(level, shooter);
            double d0 = target.getX() - shooter.getX();
            double d1 = target.getY(0.5D) - projectile.getY();
            double d2 = target.getZ() - shooter.getZ();
            projectile.shoot(d0, d1, d2, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
            level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), getShootSound(), net.minecraft.sounds.SoundSource.HOSTILE, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }

    @Override
    public net.minecraft.sounds.SoundEvent getShootSound() {
        return SoundEvents.WITHER_SHOOT;
    }

    @Override
    public double getRange() { return 24.0; }

    @Override
    public int getCooldown() { return 20; }

    @Override
    public int getChargeTicks() { return 20; }

    @Override
    public int getUseDuration(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
        return 72000;
    }

    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(net.minecraft.world.item.ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }
}
