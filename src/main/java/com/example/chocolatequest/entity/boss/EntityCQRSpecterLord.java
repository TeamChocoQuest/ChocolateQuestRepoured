package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.*;

public class EntityCQRSpecterLord extends AbstractEntityCQR {

    private static final EntityDataAccessor<Boolean> IS_CLONE = SynchedEntityData.defineId(EntityCQRSpecterLord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_VULNERABLE = SynchedEntityData.defineId(EntityCQRSpecterLord.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (!this.isClone()) {
            this.bossEvent.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.isClone()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    private int vulnerableTicks = 0;
    private int cloneCooldown = 100;

    public EntityCQRSpecterLord(EntityType<? extends AbstractEntityCQR> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CLONE, false);
        builder.define(IS_VULNERABLE, false);
    }

    public boolean isClone() {
        return this.entityData.get(IS_CLONE);
    }

    public void setClone(boolean clone) {
        this.entityData.set(IS_CLONE, clone);
        if (clone) {
            this.bossEvent.removeAllPlayers();
        }
    }

    public boolean isVulnerable() {
        return this.entityData.get(IS_VULNERABLE);
    }

    public void setVulnerable(boolean vulnerable) {
        this.entityData.set(IS_VULNERABLE, vulnerable);
    }

    @Override
    public double getBaseHealth() {
        return this.isClone() ? 1.0D : 300.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.UNDEAD;
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MOONLIGHT_SWORD.get()));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpecterTeleportAttackGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (!this.isClone()) {
                if (vulnerableTicks > 0) {
                    vulnerableTicks--;
                    if (vulnerableTicks == 0) {
                        this.setVulnerable(false);
                        this.cloneCooldown = 60; // 3 seconds before next clone phase
                    }
                } else {
                    if (cloneCooldown > 0) {
                        cloneCooldown--;
                    } else if (this.getTarget() != null) {
                        spawnClonesAndTeleport();
                    }
                }
            }
        } else {
            if (this.isClone() && this.tickCount % 5 == 0) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, 0.05, 0);
            }
        }
    }

    private void spawnClonesAndTeleport() {
        if (this.getTarget() == null) return;
        this.cloneCooldown = 200; // Reset cooldown
        LivingEntity target = this.getTarget();
        
        Vec3 bossPos = findSafeTeleportPosition(target, 4.0, 7.0);
        if (bossPos != null) {
            this.teleportTo(bossPos.x, bossPos.y, bossPos.z);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        // Spawn 3 clones
        if (this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 3; i++) {
                Vec3 clonePos = findSafeTeleportPosition(target, 4.0, 7.0);
                if (clonePos != null) {
                    EntityCQRSpecterLord clone = ModEntities.SPECTER_LORD.get().create(serverLevel);
                    if (clone != null) {
                        clone.setClone(true);
                        clone.moveTo(clonePos.x, clonePos.y, clonePos.z, this.getYRot(), this.getXRot());
                        clone.setTarget(target);
                        clone.equipBasedOnTier(this.getMobTier());
                        serverLevel.addFreshEntity(clone);
                        clone.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    private Vec3 findSafeTeleportPosition(LivingEntity target, double minRadius, double maxRadius) {
        for (int i = 0; i < 10; i++) {
            double angle = this.random.nextDouble() * 2 * Math.PI;
            double dist = minRadius + this.random.nextDouble() * (maxRadius - minRadius);
            double dx = Math.cos(angle) * dist;
            double dz = Math.sin(angle) * dist;
            
            BlockPos pos = target.blockPosition().offset((int)dx, 0, (int)dz);
            // Search up and down for a valid floor
            for (int yOffset = -3; yOffset <= 3; yOffset++) {
                BlockPos checkPos = pos.above(yOffset);
                if (this.level().getBlockState(checkPos).isAir() && this.level().getBlockState(checkPos.above()).isAir() && this.level().getBlockState(checkPos.below()).isSolidRender(this.level(), checkPos.below())) {
                    return new Vec3(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5);
                }
            }
        }
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide()) {
            return super.hurt(source, amount);
        }

        if (this.isClone()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY(0.5), this.getZ(), 20, 0.5D, 0.5D, 0.5D, 0.05D);
            }
            this.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.0F);
            this.discard();
            return false;
        }

        // Real Boss
        boolean wasHurt = false;
        if (source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) || this.vulnerableTicks > 0) {
            wasHurt = super.hurt(source, amount);
        } else if (source.getEntity() instanceof LivingEntity) {
            this.vulnerableTicks = 200; // 10 seconds of vulnerability
            this.setVulnerable(true);
            this.playSound(SoundEvents.WITHER_HURT, 1.0F, 1.5F);
            
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.getEntitiesOfClass(EntityCQRSpecterLord.class, this.getBoundingBox().inflate(30.0D), c -> c.isClone()).forEach(c -> {
                    serverLevel.sendParticles(ParticleTypes.POOF, c.getX(), c.getY(0.5), c.getZ(), 20, 0.5D, 0.5D, 0.5D, 0.05D);
                    c.discard();
                });
            }
            
            wasHurt = super.hurt(source, amount);
        }
        
        if (wasHurt && source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)) {
            if (!this.level().isClientSide() && this.getRandom().nextFloat() < 0.5f) {
                LivingEntity target = this.getTarget();
                if (target != null) {
                    double dx = this.getX() - target.getX();
                    double dz = this.getZ() - target.getZ();
                    
                    double rx = dz;
                    double rz = -dx;
                    if (this.getRandom().nextBoolean()) {
                        rx = -rx;
                        rz = -rz;
                    }
                    
                    double length = Math.sqrt(rx * rx + rz * rz);
                    if (length > 0) {
                        this.setDeltaMovement((rx / length) * 1.5, 0.4, (rz / length) * 1.5);
                        
                        if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
                            sl.sendParticles(net.minecraft.core.particles.ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 10, 0.5, 0.5, 0.5, 0.05);
                        }
                    }
                }
            }
        }
        
        return wasHurt;
    }
    
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level().isClientSide() && !this.isClone()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.getEntitiesOfClass(EntityCQRSpecterLord.class, this.getBoundingBox().inflate(50.0D), c -> c.isClone()).forEach(c -> c.discard());
            }
        }
    }

    @Override
    public boolean shouldDropExperience() {
        return !this.isClone() && super.shouldDropExperience();
    }

    class SpecterTeleportAttackGoal extends Goal {
        private final EntityCQRSpecterLord mob;
        private int cooldown = 100;

        public SpecterTeleportAttackGoal(EntityCQRSpecterLord mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (this.mob.getTarget() == null || !this.mob.isVulnerable() || this.mob.isClone()) {
                return false;
            }
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            return true;
        }

        @Override
        public void start() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                Vec3 look = target.getLookAngle();
                Vec3 behind = target.position().subtract(look.x * 2.0, 0, look.z * 2.0);
                
                BlockPos pos = BlockPos.containing(behind);
                boolean foundSafe = false;
                for (int yOffset = -2; yOffset <= 2; yOffset++) {
                    BlockPos checkPos = pos.above(yOffset);
                    if (this.mob.level().getBlockState(checkPos).isAir() && this.mob.level().getBlockState(checkPos.above()).isAir() && this.mob.level().getBlockState(checkPos.below()).isSolidRender(this.mob.level(), checkPos.below())) {
                        this.mob.teleportTo(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5);
                        this.mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                        foundSafe = true;
                        break;
                    }
                }
            }
            this.cooldown = 100 + this.mob.getRandom().nextInt(40); // 5-7 seconds
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        if (this.isClone()) {
            return;
        }
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.MOONLIGHT_SWORD.get());
        this.spawnAtLocation(ModItems.CAPE_SPECTERLORD.get());
        this.spawnAtLocation(new ItemStack(ModItems.SOUL_BOTTLE.get(), 2 + this.random.nextInt(3)));

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.MAGIC_BELL.get());
        }

        this.spawnAtLocation(new ItemStack(Items.ECHO_SHARD, 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.GHAST_TEAR, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.IRON_INGOT, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 2 + this.random.nextInt(3)));
    }
}
