package com.example.chocolatequest.entity.mob;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.sounds.SoundEvent;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import com.example.chocolatequest.entity.mob.enums.MobTier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CQPirateEntity extends AbstractEntityCQR {

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(Component.translatable("entity.cqrepoured.pirate_captain"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10)).setDarkenScreen(true);

    public CQPirateEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    @Override
    public double getBaseHealth() {
        return 20.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.PIRATE;
    }

    @Override
    public int getTextureCount() {
        return 3;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.PIRATE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
        return ModSounds.PIRATE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.PIRATE_DEATH.get();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof net.minecraft.world.entity.animal.Parrot) return false;
        return super.canAttack(target);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (entity instanceof net.minecraft.world.entity.animal.Parrot) return false;
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof net.minecraft.world.entity.animal.Parrot) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void equipBasedOnTier(MobTier tier) {
        if (tier == MobTier.BOSS) {
            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SHADOW_DAGGER.get()));
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.REVOLVER.get()));
            this.setCustomName(Component.translatable("entity.cqrepoured.pirate_captain"));
            this.setCustomNameVisible(true);
        } else {
            super.equipBasedOnTier(tier);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        if (this.getMobTier() == MobTier.BOSS) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(150.0D); // Much more HP for the boss
            this.setHealth(this.getMaxHealth());
            this.setDropChance(EquipmentSlot.MAINHAND, 1.0F); // Drop Shadow Dagger
        }
        return data;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getMobTier() == MobTier.BOSS) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.getMobTier() == MobTier.BOSS) {
            this.bossEvent.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        if (this.getMobTier() == MobTier.BOSS) {
            this.spawnAtLocation(ModItems.REVOLVER.get());
            this.spawnAtLocation(ModItems.SHADOW_DAGGER.get());
            this.spawnAtLocation(new ItemStack(ModItems.BULLET_IRON.get(), 6 + this.random.nextInt(7)));
            this.spawnAtLocation(new ItemStack(net.minecraft.world.item.Items.GOLD_INGOT, 2 + this.random.nextInt(4)));
        }
    }
}
