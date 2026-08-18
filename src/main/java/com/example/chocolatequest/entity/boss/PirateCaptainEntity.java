package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.entity.ai.boss.piratecaptain.BossAIPirateDodgeAndShoot;
import com.example.chocolatequest.entity.ai.boss.piratecaptain.BossAIPirateSummonParrot;
import com.example.chocolatequest.entity.ai.boss.piratecaptain.BossAIPirateTeleportBehindEnemy;
import com.example.chocolatequest.entity.ai.boss.piratecaptain.BossAIPirateTurnInvisible;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Items;

import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class PirateCaptainEntity extends AbstractEntityCQR {

    public static final EntityDataAccessor<Boolean> IS_DISINTEGRATING = SynchedEntityData.defineId(PirateCaptainEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_REINTEGRATING = SynchedEntityData.defineId(PirateCaptainEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_DODGING = SynchedEntityData.defineId(PirateCaptainEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        boolean wasHurt = super.hurt(source, amount);
        if (wasHurt && source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)) {
            if (!this.level().isClientSide() && this.getRandom().nextFloat() < 0.5f) {
                // Dash sideways
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
    public static final EntityDataAccessor<Boolean> IS_SUMMONING = SynchedEntityData.defineId(PirateCaptainEntity.class, EntityDataSerializers.BOOLEAN);

    public static final RawAnimation DODGE_ANIM = RawAnimation.begin().thenPlay("animation.pirate_captain.dodge_shoot");
    public static final RawAnimation SUMMON_ANIM = RawAnimation.begin().thenPlay("animation.pirate_captain.summon_parrot");

    public static final int TURN_INVISIBLE_ANIMATION_TIME = 15;
    private boolean spawnedParrot = false;

    protected final ServerBossEvent bossEvent;

    public PirateCaptainEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
        this.bossEvent.setDarkenScreen(true);
    }

    @Override
    public double getBaseHealth() {
        return 720.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.PIRATE;
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CAPTAIN_REVOLVER.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.SHADOW_DAGGER.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createCQRAttributes()
                .add(Attributes.MAX_HEALTH, 720.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DISINTEGRATING, false);
        builder.define(IS_REINTEGRATING, false);
        builder.define(IS_DODGING, false);
        builder.define(IS_SUMMONING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("spawnedParrot", this.spawnedParrot);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spawnedParrot = compound.getBoolean("spawnedParrot");
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Spells
        this.goalSelector.addGoal(0, new BossAIPirateDodgeAndShoot(this));
        this.goalSelector.addGoal(1, new BossAIPirateSummonParrot(this));

        // Goals
        this.goalSelector.addGoal(2, new BossAIPirateTeleportBehindEnemy(this));
        this.goalSelector.addGoal(3, new BossAIPirateTurnInvisible(this));
    }

    @Override
    public boolean canAttack(net.minecraft.world.entity.LivingEntity target) {
        if (target instanceof PirateParrotEntity) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        if (!super.canBeAffected(potioneffectIn)) {
            return false;
        }
        if (potioneffectIn.getEffect().value().isBeneficial()) {
            return true;
        }
        return potioneffectIn.getEffect().value() == MobEffects.GLOWING.value();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.hasPassenger(e -> e instanceof PirateParrotEntity)) {
            this.ejectPassengers();
        }
        return super.doHurtTarget(entityIn);
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        
        this.equipBasedOnTier(this.getMobTier());
        
        return data;
    }

    public boolean hasSpawnedParrot() {
        return this.spawnedParrot;
    }

    public void setSpawnedParrot(boolean spawnedParrot) {
        this.spawnedParrot = spawnedParrot;
    }

    public void setIsDisintegrating(boolean value) {
        this.entityData.set(IS_DISINTEGRATING, value);
    }

    public void setIsReintegrating(boolean value) {
        this.entityData.set(IS_REINTEGRATING, value);
    }

    public boolean isDisintegrating() {
        return this.entityData.get(IS_DISINTEGRATING);
    }

    public boolean isReintegrating() {
        return this.entityData.get(IS_REINTEGRATING);
    }

    public void setIsDodging(boolean value) {
        this.entityData.set(IS_DODGING, value);
    }

    public boolean isDodging() {
        return this.entityData.get(IS_DODGING);
    }

    public void setIsSummoning(boolean value) {
        this.entityData.set(IS_SUMMONING, value);
    }

    public boolean isSummoning() {
        return this.entityData.get(IS_SUMMONING);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        AnimationController<PirateCaptainEntity> bossActionController = new AnimationController<>(this, "boss_action_controller", 0, this::predicateBossAction);
        bossActionController.setCustomInstructionKeyframeHandler(this::handleInstructionKeyframe);
        controllers.add(bossActionController);
    }

    protected PlayState predicateBossAction(AnimationState<PirateCaptainEntity> event) {
        if (this.isDodging()) {
            event.getController().setAnimation(DODGE_ANIM);
            return PlayState.CONTINUE;
        } else if (this.isSummoning()) {
            event.getController().setAnimation(SUMMON_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    protected void handleInstructionKeyframe(CustomInstructionKeyframeEvent<PirateCaptainEntity> event) {
        // Only run client-side particles/sounds if needed here, server logic is in AI ticks now
    }

    public void shootSingleBullet() {
        if (this.level().isClientSide()) return;
        LivingEntity target = this.getTarget();
        if (target != null) {
            com.example.chocolatequest.item.ItemRevolver revolver = (com.example.chocolatequest.item.ItemRevolver) ModItems.CAPTAIN_REVOLVER.get();
            revolver.shoot(this.level(), this, target, net.minecraft.world.InteractionHand.MAIN_HAND);
        }
    }

    public void spawnParrot() {
        if (this.level().isClientSide() || this.hasSpawnedParrot()) return;
        Vec3 look = this.getLookAngle().normalize().scale(3);
        Vec3 v = new Vec3(-look.z, look.y, look.x); // rotate 90 degrees
        net.minecraft.core.BlockPos spawnPos = net.minecraft.core.BlockPos.containing(this.position().add(v).add(0, 1, 0));
        
        if (!this.level().getBlockState(spawnPos).isAir()) {
            v = new Vec3(0, 1, 0);
        }

        PirateParrotEntity parrot = com.example.chocolatequest.registry.ModEntities.CQ_PIRATE_PARROT.get().create(this.level());
        if (parrot != null) {
            parrot.setOwnerUUID(this.getUUID());
            parrot.setTame(true, true);
            Vec3 pos = this.position().add(v);
            parrot.setPos(pos.x, pos.y, pos.z);
            this.level().addFreshEntity(parrot);
            this.setSpawnedParrot(true);
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.CAPTAIN_REVOLVER.get());
        this.spawnAtLocation(ModItems.SHADOW_DAGGER.get());
        this.spawnAtLocation(ModItems.HOOKSHOT.get());
        this.spawnAtLocation(ModItems.BANNER_PIRATE.get());

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(Items.SPYGLASS);
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.CAPE_PIRATE.get());
        }

        this.spawnAtLocation(new ItemStack(ModItems.BULLET_IRON.get(), 12 + this.random.nextInt(13)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_BLOCK, 1 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 6 + this.random.nextInt(7)));
        this.spawnAtLocation(new ItemStack(Items.EMERALD, 2 + this.random.nextInt(4)));
    }
}
