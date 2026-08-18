package com.example.chocolatequest.item.staff;

import com.example.chocolatequest.entity.projectile.WindProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.example.chocolatequest.item.IRangedWeapon;
public class WindStaffItem extends Item implements IRangedWeapon {

    public WindStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getFoodData().getFoodLevel() >= 2 || player.getAbilities().instabuild) {
            
            if (!level.isClientSide()) {
                WindProjectileEntity projectile = new WindProjectileEntity(level, player);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(projectile);
                
                level.playSound(null, player.getX(), player.getY(), player.getZ(), com.example.chocolatequest.registry.ModSounds.WIND.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            }

            if (!player.getAbilities().instabuild) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 2);
            }

            // 1 second cooldown
            com.example.chocolatequest.registry.ModItems.applySharedCooldown(player, this, 20);
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void shoot(net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity shooter, net.minecraft.world.entity.Entity target, net.minecraft.world.InteractionHand hand) {

        if (!level.isClientSide()) {
            com.example.chocolatequest.entity.projectile.WindProjectileEntity projectile = new com.example.chocolatequest.entity.projectile.WindProjectileEntity(level, shooter);
            double dx = target.getX() - shooter.getX();
            double dy = target.getY(0.3333333333333333D) - projectile.getY();
            double dz = target.getZ() - shooter.getZ();
            double d = Math.sqrt(dx * dx + dz * dz);
            projectile.shoot(dx, dy + d * 0.2D, dz, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
            level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), getShootSound(), net.minecraft.sounds.SoundSource.HOSTILE, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }

    }

    @Override
    public net.minecraft.sounds.SoundEvent getShootSound() {
        return com.example.chocolatequest.registry.ModSounds.WIND.get();
    }

    @Override
    public double getRange() { return 15.0; }

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
