package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.mob.CQGremlinEntity;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.entity.mob.enums.MobTier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Items;

public class EntityCQRGremlinShaman extends CQGremlinEntity {
    protected final ServerBossEvent bossEvent;

    public EntityCQRGremlinShaman(EntityType<? extends CQGremlinEntity> type, Level level) {
        super(type, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return CQGremlinEntity.createCQRAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new com.example.chocolatequest.entity.ai.goal.GremlinShamanHookGoal(this));
    }

    @Override
    protected void equipBasedOnTier(MobTier tier) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.WIND_SWORD.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.SPIDER_HOOK.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.CLOUD_BOOTS.get()));
        
        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive() && !this.level().isClientSide) {
            // Repelling Shield: Push players away within 4 blocks
            java.util.List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(4.0));
            for (Player player : players) {
                if (!player.isCreative() && !player.isSpectator()) {
                    Vec3 dir = player.position().subtract(this.position()).normalize();
                    // Softer push away, no damage
                    player.push(dir.x * 0.15, 0.05, dir.z * 0.15);
                    player.hurtMarked = true;
                }
            }

            // Enchanting particles flying around
            if (this.tickCount % 2 == 0) {
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.ENCHANT, 
                    this.getX(), this.getY() + 1.0, this.getZ(), 
                    15, // count
                    2.0, 1.0, 2.0, // dx, dy, dz (spread)
                    0.2); // speed
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.SPIDER_HOOK.get());
        this.spawnAtLocation(ModItems.CLOUD_BOOTS.get());
        this.spawnAtLocation(ModItems.WIND_SWORD.get());

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.CAPE_GOBLINSHAMAN.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.BANNER_GOBLIN.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.SHIELD_GOBLIN.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.MAGIC_BELL.get());
        }

        this.spawnAtLocation(new ItemStack(Items.WIND_CHARGE, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.EMERALD, 6 + this.random.nextInt(7)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 4 + this.random.nextInt(5)));
    }
}
