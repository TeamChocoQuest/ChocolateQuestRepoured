package com.example.chocolatequest.entity.ai.boss;

import com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase;
import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;

import java.util.EnumSet;

public class BossAIMageSummon extends Goal {
    private final AbstractEntityCQRMageBase mage;
    private int summonCooldown = 200;
    private final int summonInterval;

    public BossAIMageSummon(AbstractEntityCQRMageBase mage, int summonInterval) {
        this.mage = mage;
        this.summonInterval = summonInterval;
        this.summonCooldown = summonInterval;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.summonCooldown > 0) {
            this.summonCooldown--;
        }
        LivingEntity target = this.mage.getTarget();
        if (target != null && target.isAlive()) {
            return this.summonCooldown <= 0;
        }
        return false;
    }

    @Override
    public void start() {
        this.mage.startCastingSpell(40);
        this.summonMinions();
        this.summonCooldown = this.summonInterval;
    }

    @Override
    public void tick() {
        if (this.mage.spellCastTicks <= 0) {
            // Wait for spell to finish
        }
    }

    private void summonMinions() {
        if (!this.mage.level().isClientSide) {
            this.mage.playSound(net.minecraft.sounds.SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
            int count = 2 + this.mage.getRandom().nextInt(2); // 2 to 3 minions
            for (int i = 0; i < count; i++) {
                double offsetX = (this.mage.getRandom().nextDouble() - 0.5D) * 6.0D;
                double offsetZ = (this.mage.getRandom().nextDouble() - 0.5D) * 6.0D;
                BlockPos pos = BlockPos.containing(this.mage.getX() + offsetX, this.mage.getY(), this.mage.getZ() + offsetZ);

                LivingEntity minion;
                if (this.mage.getRandom().nextBoolean()) {
                    minion = ModEntities.CQ_ZOMBIE.get().create(this.mage.level());
                } else {
                    minion = ModEntities.CQ_SKELETON.get().create(this.mage.level());
                }

                if (minion != null) {
                    minion.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    
                    // Equip with stone sword
                    minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    // Clear armor
                    minion.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    minion.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    minion.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                    minion.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);

                    this.mage.level().addFreshEntity(minion);
                }
            }
        }
    }
}
