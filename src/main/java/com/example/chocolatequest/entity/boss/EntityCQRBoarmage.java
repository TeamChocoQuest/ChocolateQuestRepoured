package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.boss.BossAIMageShoot;
import com.example.chocolatequest.entity.ai.boss.MageLifeDrainGoal;
import com.example.chocolatequest.entity.ai.boss.boarmage.BossAIBoarmageExplodeAreaAttack;
import com.example.chocolatequest.entity.ai.boss.boarmage.BossAIBoarmageTeleportSpell;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQRBoarmage extends AbstractEntityCQRMageBase implements GeoEntity {

    protected List<LivingEntity> summonedMinions = new ArrayList<>();

    public EntityCQRBoarmage(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
        super(type, worldIn);
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, net.minecraft.world.DifficultyInstance difficulty) {
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.FIRE_STAFF.get()));
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.FIRE_STAFF.get()));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.LEGS, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.isInLava() || this.isOnFire()) && this.tickCount % 5 == 0) {
            this.heal(1.0F);
        }
    }

    @Override
    public void die(DamageSource cause) {
        // Kill minions
        for (LivingEntity e : this.summonedMinions) {
            if (e != null && e.isAlive()) {
                e.die(cause);
                e.remove(RemovalReason.KILLED);
            }
        }
        this.summonedMinions.clear();
        super.die(cause);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MageLifeDrainGoal(this));
        this.goalSelector.addGoal(1, new BossAIBoarmageTeleportSpell(this));
        this.goalSelector.addGoal(2, new BossAIBoarmageExplodeAreaAttack(this));
        this.goalSelector.addGoal(3, new BossAIMageShoot(this, 10, 24.0f, true)); // Replaces standard spells
    }

    @Override
    public double getBaseHealth() {
        return 200.0D;
    }
    
    @Override
    protected net.minecraft.world.BossEvent.BossBarColor getBossBarColor() {
        return net.minecraft.world.BossEvent.BossBarColor.RED;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.UNDEAD;
    }

    @Override
    protected int getShieldHitThreshold() {
        return 6;
    }

    @Override
    protected int getShieldDurationTicks() {
        return 45;
    }

    @Override
    protected float getShieldDamageMultiplier() {
        return 0.35F;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.FIRE_STAFF.get());

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.BANNER_PIGMEN.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.MAGIC_BELL.get());
        }

        this.spawnAtLocation(new ItemStack(Items.MAGMA_CREAM, 3 + this.random.nextInt(4)));
        this.spawnAtLocation(new ItemStack(ModItems.FIRE_POTION.get(), 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(ModItems.FIRE_ARROW.get(), 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.BLAZE_POWDER, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.BLAZE_ROD, 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.COOKED_PORKCHOP, 4 + this.random.nextInt(5)));
    }
}
