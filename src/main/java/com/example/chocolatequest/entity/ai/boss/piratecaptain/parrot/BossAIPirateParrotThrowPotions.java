package com.example.chocolatequest.entity.ai.boss.piratecaptain.parrot;

import com.example.chocolatequest.entity.boss.PirateParrotEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.util.Mth;

public class BossAIPirateParrotThrowPotions extends Goal {

    private final PirateParrotEntity entity;

    private static final double SPEED = 2;
    private static final double MIN_DISTANCE_SQ = 4 * 4;
    private int cd = 0;
    private static final int COOLDOWN = 40;

    public BossAIPirateParrotThrowPotions(PirateParrotEntity entity) {
        super();
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        this.cd--;
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.cd <= 0;
    }

    @Override
    public void start() {
        super.start();
        this.equipPotion();
    }

    private void equipPotion() {
        Holder<Potion> type = Potions.HARMING;
        switch (this.entity.getRandom().nextInt(3)) {
            case 0 -> type = Potions.HARMING;
            case 1 -> type = Potions.STRONG_HARMING;
            case 2 -> type = Potions.STRONG_POISON;
        }
        
        if (this.entity.getTarget() != null && this.entity.getTarget().isInvertedHealAndHarm()) {
            if (type == Potions.STRONG_HARMING) {
                type = Potions.STRONG_HEALING;
            }
            if (type == Potions.HARMING) {
                type = Potions.HEALING;
            }
        }
        
        ItemStack potion = new ItemStack(Items.SPLASH_POTION);
        potion.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS, new PotionContents(type));
        this.entity.setItemSlot(EquipmentSlot.MAINHAND, potion);
    }

    public void tick() {
        super.tick();

        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.entity.getLookControl().setLookAt(target, 30, 30);
            if (this.entity.distanceToSqr(target) <= MIN_DISTANCE_SQ) {
                this.throwPotion(this.entity, target);
                this.cd = COOLDOWN;
            } else {
                this.entity.getNavigation().moveTo(target, SPEED);
            }
        }
    }

    @SuppressWarnings("resource")
    private void throwPotion(PirateParrotEntity thrower, LivingEntity target) {
        double d0 = target.getY() + target.getEyeHeight() - 1.1D;
        double d1 = target.getX() + target.getDeltaMovement().x() - thrower.getX();
        double d2 = d0 - thrower.getY();
        double d3 = target.getZ() + target.getDeltaMovement().z() - thrower.getZ();
        float f = Mth.sqrt((float)(d1 * d1 + d3 * d3));
        
        ItemStack potionItem = thrower.getMainHandItem();
        if (potionItem.isEmpty() || potionItem.getItem() != Items.SPLASH_POTION) return;
        
        ThrownPotion potion = new ThrownPotion(thrower.level(), thrower);
        potion.setItem(potionItem);
        potion.setXRot(potion.getXRot() + 20F);
        potion.shoot(d1, d2 + f * 0.2F, d3, 0.75F, 8.0F);
        
        thrower.level().playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(), SoundEvents.WITCH_THROW, thrower.getSoundSource(), 1.0F, 0.8F + thrower.getRandom().nextFloat() * 0.4F);
        thrower.level().addFreshEntity(potion);

        this.entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }
}
