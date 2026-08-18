package com.example.chocolatequest.entity.mob;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class  CQDrinkPotionGoal<T extends PathfinderMob & ICQMob> extends Goal {
    private final T mob;
    private int useTimer;
    private int retreatTicks;
    private boolean isDrinking;
    private ItemStack previousMainhand;

    public CQDrinkPotionGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasDrunkPotion()) {
            return false;
        }
        if (this.mob.getHealth() > this.mob.getMaxHealth() * 0.4f) {
            return false;
        }
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void start() {
        this.useTimer = 0;
        this.retreatTicks = 0;
        this.isDrinking = false;
        this.previousMainhand = this.mob.getMainHandItem().copy();
        
        // Clear navigation so they stop pathfinding to the player
        this.mob.getNavigation().stop();
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

        if (!this.isDrinking) {
            this.retreatTicks++;
            double distance = target != null ? this.mob.distanceTo(target) : 10.0;
            
            // Back away smoothly
            if (target != null) {
                Vec3 look = this.mob.getLookAngle();
                Vec3 backwards = new Vec3(-look.x, 0.0, -look.z).normalize().scale(0.12);
                
                // Add backwards velocity if on ground
                if (this.mob.onGround()) {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(backwards.x, 0, backwards.z));
                }
            }

            // Block while retreating if they have a shield
            if (!this.mob.isUsingItem() && !this.mob.getOffhandItem().isEmpty() && this.mob.getOffhandItem().is(Items.SHIELD)) {
                this.mob.startUsingItem(InteractionHand.OFF_HAND);
            }

            // Stop retreating and start drinking if we hit a wall, retreated for 4 seconds, or reached 8 blocks distance
            if (this.mob.horizontalCollision || this.retreatTicks > 80 || distance > 8.0) {
                this.startDrinking();
            }
        } else {
            this.useTimer++;
            if (this.useTimer >= 40) { // Takes ~2 seconds to drink
                this.finishDrinking();
            }
        }
    }

    private void startDrinking() {
        this.isDrinking = true;
        this.mob.stopUsingItem(); // Stop blocking
        
        // Equip Healing Potion
        ItemStack potion = PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING);
        this.mob.setItemInHand(InteractionHand.MAIN_HAND, potion);
    }

    private void finishDrinking() {
        this.mob.heal(8.0f); // Heal 4 hearts
        this.mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
        this.mob.setDrunkPotion(true);
        
        // Restore weapon
        this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.previousMainhand);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.hasDrunkPotion() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public void stop() {
        if (!this.mob.hasDrunkPotion() && this.isDrinking) {
            // Interrupted before finishing, restore weapon
            this.mob.setItemInHand(InteractionHand.MAIN_HAND, this.previousMainhand);
        }
        this.mob.stopUsingItem(); // Stop whatever they are doing
        this.useTimer = 0;
        this.isDrinking = false;
    }
}
