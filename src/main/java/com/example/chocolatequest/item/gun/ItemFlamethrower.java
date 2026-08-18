package com.example.chocolatequest.item.gun;

import com.example.chocolatequest.item.ItemMagazineBased;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ItemFlamethrower extends ItemMagazineBased {

    public ItemFlamethrower(Properties properties) {
        super(properties.stacksTo(1), t -> t != null && t.getItem() == Items.SLIME_BALL);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return this.getAmmoInItem(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public void shootFlames(LivingEntity entity) {
        Level world = entity.level();
        float rotationYaw = Mth.wrapDegrees(entity.yHeadRot);
        double armDist = 1.0D;
        double offY = entity.getBbHeight() * 0.75D;
        double posX = entity.position().x - Math.sin(Math.toRadians(rotationYaw)) * armDist;
        double posY = entity.position().y + offY;
        double posZ = entity.position().z + Math.cos(Math.toRadians(rotationYaw)) * armDist;

        float vx = (float) -Math.sin(Math.toRadians(rotationYaw));
        float vz = (float) Math.cos(Math.toRadians(rotationYaw));
        double vy = -Math.sin(Math.toRadians(entity.getXRot()));
        vx = (float) (vx * (1.0D - Math.abs(vy)));
        vz = (float) (vz * (1.0D - Math.abs(vy)));

        if (world.isClientSide()) {
            for (int i = 0; i < 8; i++) {
                world.addParticle(ParticleTypes.FLAME, posX, posY, posZ, 
                    (vx + world.random.nextFloat() - 0.5D) / 3.0D, 
                    (vy + world.random.nextFloat() - 0.5D) / 8.0D, 
                    (vz + world.random.nextFloat() - 0.5D) / 3.0D);
            }
        } else {
            int dist = 10;
            AABB box = entity.getBoundingBox()
                .inflate(entity.getLookAngle().x * dist, entity.getLookAngle().y * dist, entity.getLookAngle().z * dist)
                .inflate(1.0D);
            
            List<Entity> list = world.getEntities(entity, box);

            for (Entity e : list) {
                if (e instanceof LivingEntity && !e.isInWaterRainOrBubble() && !e.isVehicle()) {
                    double d = posX - e.position().x;
                    double d2 = posZ - e.position().z;
                    double rotDiff = Math.atan2(d, d2);
                    rotDiff = rotDiff * 180.0D / 3.141592D;
                    rotDiff = -Mth.wrapDegrees(rotDiff - 180.0D);
                    rotDiff -= rotationYaw;

                    if (Math.abs(rotDiff) < 30.0D) {
                        e.igniteForSeconds(2);
                        e.hurt(world.damageSources().inFire(), 2.5F);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (this.getAmmoInItem(stack) <= 0) {
            return InteractionResultHolder.fail(stack);
        }
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity user) {
            if (user.getUseItem() == stack) {
                if (user.isUsingItem()) {
                    this.shootFlames(user);
                    this.removeAmmoFromItem(stack, 1);
                }
            } else if ((user.getMainHandItem() == stack || user.getOffhandItem() == stack) 
                       && entityIn.tickCount % 5 == 0 && this.getAmmoInItem(stack) < this.getMaxAmmo()) {
                if (entityIn instanceof Player player) {
                    this.reloadFromInventory(player.getInventory(), stack, !player.isCreative());
                }
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxAmmo() {
        return 2000;
    }

    @Override
    protected int getAmmoForSingleAmmoItem(ItemStack fuelItem) {
        return 20;
    }

    @Override
    protected int getMaxProcessedItemsPerReloadCycle() {
        return 1;
    }
}
