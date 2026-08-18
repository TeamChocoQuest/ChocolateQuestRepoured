package com.example.chocolatequest.item.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.projectile.BubbleProjectileEntity;

import java.util.Random;

public class ItemBubblePistol extends Item {

    private final Random rng = new Random();

    public ItemBubblePistol(Properties properties) {
        super(properties.durability(200));
    }

    public double getInaccuracy() {
        return 0.5D;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 10;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player player) {
            player.getCooldowns().addCooldown(this, 80);
        }
        stack.hurtAndBreak(1, entityLiving, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
        stack.hurtAndBreak(1, entityLiving, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        if (entityLiving instanceof Player player) {
            player.getCooldowns().addCooldown(this, 80);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity living && living.isUsingItem() && living.getUseItem() == stack) {
            this.shootBubbles(living);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    private void shootBubbles(LivingEntity entity) {
        double x = -Math.sin(Math.toRadians(entity.getYRot()));
        double z = Math.cos(Math.toRadians(entity.getYRot()));
        double y = -Math.sin(Math.toRadians(entity.getXRot()));
        this.shootBubbles(new Vec3(x, y, z), entity);
    }

    private void shootBubbles(Vec3 velocity, LivingEntity shooter) {
        if (shooter.level().isClientSide) return;

        Vec3 v = new Vec3(
            -this.getInaccuracy() + velocity.x + (2 * this.getInaccuracy() * this.rng.nextDouble()), 
            -this.getInaccuracy() + velocity.y + (2 * this.getInaccuracy() * this.rng.nextDouble()), 
            -this.getInaccuracy() + velocity.z + (2 * this.getInaccuracy() * this.rng.nextDouble())
        );
        v = v.normalize().scale(1.4);

        shooter.playSound(SoundEvents.FISHING_BOBBER_THROW, 1, 0.75F + (0.5F * shooter.getRandom().nextFloat()));

        BubbleProjectileEntity bubble = new BubbleProjectileEntity(shooter.level(), shooter);
        bubble.setDeltaMovement(v);
        shooter.level().addFreshEntity(bubble);
    }
}
