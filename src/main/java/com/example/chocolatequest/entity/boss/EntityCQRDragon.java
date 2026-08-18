package com.example.chocolatequest.entity.boss;

import net.minecraft.core.BlockPos;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

/**
 * Green Dragon boss and flying mount. Its combat is split into readable ground,
 * takeoff, aerial and landing phases instead of permanently hovering in place.
 */
public class EntityCQRDragon extends PathfinderMob implements GeoEntity {
    public static final int ACTION_NONE = 0;
    public static final int ACTION_GROUND_FIRE = 1;
    public static final int ACTION_AIR_FIRE = 2;
    public static final int ACTION_BITE = 3;

    public static final byte FLIGHT_GROUNDED = 0;
    public static final byte FLIGHT_TAKING_OFF = 1;
    public static final byte FLIGHT_FLYING = 2;
    public static final byte FLIGHT_APPROACHING = 3;
    public static final byte FLIGHT_LANDING = 4;
    public static final byte FLIGHT_STAGGERED = 5;
    public static final byte FLIGHT_FALLING = 6;
    public static final byte FLIGHT_CRASHING = 7;

    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLIGHT_STATE = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(
            EntityCQRDragon.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final RawAnimation TAKEOFF = RawAnimation.begin().thenPlayAndHold("takeoff");
    private static final RawAnimation FLYING = RawAnimation.begin().thenLoop("flying");
    private static final RawAnimation FLYING_IDLE = RawAnimation.begin().thenLoop("flyingIdle");
    private static final RawAnimation LANDING = RawAnimation.begin().thenPlayAndHold("landing");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("dog_sleep");
    private static final RawAnimation AIR_FIRE = RawAnimation.begin().thenLoop("air_fire_breath");
    private static final RawAnimation GROUND_FIRE = RawAnimation.begin().thenLoop("ground_fire_breath");
    private static final RawAnimation BITE = RawAnimation.begin().thenPlay("ground_bite");
    private static final RawAnimation SHOT_DOWN = RawAnimation.begin().thenPlayAndHold("flying_to_freefall");
    private static final RawAnimation FREEFALL = RawAnimation.begin().thenLoop("freefall");
    private static final RawAnimation CRASH = RawAnimation.begin().thenPlayAndHold("freefall_to_crash");

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.cqrepoured.cqr_dragon"),
            BossEvent.BossBarColor.GREEN,
            BossEvent.BossBarOverlay.NOTCHED_10);

    private int actionTicks;
    private int stateTicks;
    private int riderFireCooldown;
    private int breathAge;
    private int touchdownTicks;
    private int shotDownCooldown;
    private int crashRecoveryTicks;
    private int lastAnimationKey = Integer.MIN_VALUE;
    private final MoveControl groundMoveControl;
    private final FlyingMoveControl flightMoveControl;
    private final PathNavigation groundNavigation;
    private final FlyingPathNavigation flightNavigation;
    private int breathTargetEntityId = -1;
    private Vec3 breathTargetPosition = Vec3.ZERO;
    @Nullable
    private Vec3 landingTarget;

    public EntityCQRDragon(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.groundMoveControl = new SmoothDragonGroundMoveControl(this);
        this.flightMoveControl = new FlyingMoveControl(this, 8, true);
        this.groundNavigation = this.navigation;
        this.flightNavigation = this.createFlightNavigation(level);
        this.moveControl = this.groundMoveControl;
        this.navigation = this.groundNavigation;
        this.setNoGravity(false);
        this.xpReward = 80;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, ACTION_NONE);
        builder.define(FLIGHT_STATE, FLIGHT_GROUNDED);
        builder.define(TAMED, false);
        builder.define(SLEEPING, false);
        builder.define(SADDLED, false);
        builder.define(OWNER, Optional.empty());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level);
    }

    private FlyingPathNavigation createFlightNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    private void applyMovementMode(byte state) {
        if (this.groundNavigation == null || this.flightNavigation == null) return;
        boolean flyingMode = state != FLIGHT_GROUNDED;
        PathNavigation wantedNavigation = flyingMode ? this.flightNavigation : this.groundNavigation;
        MoveControl wantedControl = flyingMode ? this.flightMoveControl : this.groundMoveControl;
        if (this.navigation != wantedNavigation) {
            this.navigation.stop();
            this.navigation = wantedNavigation;
        }
        this.moveControl = wantedControl;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (FLIGHT_STATE.equals(key)) this.applyMovementMode(this.getFlightState());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.FLYING_SPEED, 0.76D)
                .add(Attributes.STEP_HEIGHT, 1.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.88D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GreenDragonCombatGoal(this));
        this.goalSelector.addGoal(3, new GreenDragonFollowOwnerGoal(this));
        this.goalSelector.addGoal(5, new GreenDragonWanderGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // A dragon often has leaves, a roof edge or its own large body between
        // its eyes and the player for a few ticks.  Requiring permanent line of
        // sight made it forget prey as soon as it climbed above a forest.
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        this.bossEvent.setVisible(!this.isTamed());
        if (this.riderFireCooldown > 0) this.riderFireCooldown--;
        if (this.shotDownCooldown > 0) this.shotDownCooldown--;

        if (this.tickCount == 2 && this.getFlightState() == FLIGHT_GROUNDED
                && !this.onGround() && this.getGroundDistance() > 3.0D) {
            this.setFlightState(FLIGHT_FLYING);
        }

        this.tickFlightState();
        if (this.isBreathingFire()) this.tickFireBreath();
        if (this.actionTicks > 0 && --this.actionTicks == 0) {
            this.entityData.set(ACTION, ACTION_NONE);
            this.breathTargetEntityId = -1;
        }

        if (this.isTamed()) {
            LivingEntity target = this.getTarget();
            if (target instanceof Player || target != null && this.isOwner(target)) this.setTarget(null);
        }

        LivingEntity rider = this.getControllingPassenger();
        if (rider instanceof Player player) {
            this.setSleeping(false);
            this.setTarget(null);
            if (player.swinging && this.riderFireCooldown == 0 && !this.isTransitioning()) {
                this.riderFireCooldown = 38;
                Vec3 aim = player.getEyePosition().add(player.getLookAngle().scale(48.0D));
                this.startFireBreath(null, aim);
            }
        }
    }

    private void tickFlightState() {
        byte state = this.getFlightState();
        this.stateTicks++;
        this.applyMovementMode(state);

        if (state == FLIGHT_STAGGERED) {
            this.tickShotDownStagger();
        } else if (state == FLIGHT_FALLING) {
            this.tickFreefall();
        } else if (state == FLIGHT_CRASHING) {
            this.tickCrash();
        } else if (state == FLIGHT_TAKING_OFF) {
            this.setNoGravity(true);
            this.getNavigation().stop();
            Vec3 forward = Vec3.directionFromRotation(0.0F, this.getYRot());
            Vec3 motion = this.getDeltaMovement().scale(0.72D)
                    .add(forward.x * 0.052D, 0.035D, forward.z * 0.052D);
            this.setDeltaMovement(motion.x, Math.min(0.12D, motion.y), motion.z);
            if (this.stateTicks == 5) {
                this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 2.2F, 0.82F);
            }
            // 26 ticks matches the 1.28 s Blockbench clip almost exactly.
            if (this.stateTicks >= 26) this.setFlightState(FLIGHT_FLYING);
        } else if (state == FLIGHT_APPROACHING || state == FLIGHT_LANDING) {
            boolean touchingGround = this.getDeltaMovement().y <= 0.04D
                    && (this.onGround() || this.verticalCollision || this.getGroundDistance() <= 0.65D);
            if (touchingGround) {
                this.tickTouchdown();
                return;
            }
            this.setNoGravity(true);
            this.tickLandingMovement();
        } else if (state == FLIGHT_FLYING) {
            this.setNoGravity(true);
            Vec3 motion = this.getDeltaMovement();
            double maximumRise = this.isVehicle() ? 0.38D : 0.16D;
            if (motion.y > maximumRise) {
                this.setDeltaMovement(motion.x, maximumRise, motion.z);
            }
            if (!this.isBreathingFire()) {
                this.setXRot(Mth.approachDegrees(this.getXRot(), 0.0F, 3.5F));
            }

            // Hard safety ceiling for AI flight.  It is relative both to the
            // prey and to terrain under the dragon, so a hill is safe but an
            // orbit can never drift dozens of blocks into the sky.
            if (!this.isVehicle()) {
                LivingEntity target = this.getTarget();
                double terrainY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        Mth.floor(this.getX()), Mth.floor(this.getZ()));
                double referenceY = target == null ? terrainY : Math.max(terrainY, target.getY());
                double ceilingY = referenceY + 7.0D;
                if (this.getY() > ceilingY) {
                    double correction = Mth.clamp((this.getY() - ceilingY) * 0.035D, 0.045D, 0.14D);
                    Vec3 corrected = this.getDeltaMovement();
                    this.setDeltaMovement(corrected.x, Math.min(corrected.y, -correction), corrected.z);
                }
            }
            // Even an unscheduled collision gets a visible touchdown sequence.
            if (this.getDeltaMovement().y <= 0.04D
                    && (this.onGround() || this.verticalCollision || this.getGroundDistance() <= 0.65D)) {
                this.setFlightState(FLIGHT_LANDING);
                this.tickTouchdown();
            }
        } else if (state == FLIGHT_GROUNDED) {
            this.setNoGravity(false);
            this.landingTarget = null;
            // Ground movement must never inherit a vertical impulse left by the
            // flying controller. Gravity may still pull the dragon down from an
            // edge, but ground navigation cannot make it hover or bounce.
            Vec3 motion = this.getDeltaMovement();
            if (motion.y > 0.0D) this.setDeltaMovement(motion.x, 0.0D, motion.z);
        }
    }

    /** First second after the arrow hit: wings fold and controlled flight is lost. */
    private void tickShotDownStagger() {
        this.setNoGravity(true);
        this.getNavigation().stop();
        Vec3 motion = this.getDeltaMovement();
        double downward = Mth.clamp(motion.y * 0.48D - 0.012D, -0.045D, 0.015D);
        this.setDeltaMovement(motion.x * 0.91D, downward, motion.z * 0.91D);
        this.setXRot(Mth.approachDegrees(this.getXRot(), 28.0F, 3.5F));
        if (this.stateTicks >= 20) {
            this.setFlightState(FLIGHT_FALLING);
            Vec3 fallingMotion = this.getDeltaMovement();
            this.setDeltaMovement(fallingMotion.x, Math.min(-0.12D, fallingMotion.y), fallingMotion.z);
        }
    }

    /** Uncontrolled, gravity-driven part whose duration naturally follows the current altitude. */
    private void tickFreefall() {
        this.setNoGravity(false);
        this.getNavigation().stop();
        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.985D, Math.max(-1.35D, motion.y), motion.z * 0.985D);
        this.setXRot(Mth.approachDegrees(this.getXRot(), 38.0F, 2.8F));

        double groundDistance = this.getGroundDistance();
        double predictedCrashDistance = Mth.clamp(Math.max(0.0D, -motion.y) * 9.0D, 2.3D, 5.2D);
        if (this.onGround() || this.verticalCollision || groundDistance <= 0.6D) {
            this.setFlightState(FLIGHT_CRASHING);
            this.triggerCrashImpact();
        } else if (groundDistance <= predictedCrashDistance) {
            // keyframes then coincide with the physical touchdown.
            this.setFlightState(FLIGHT_CRASHING);
        }
    }

    private void tickCrash() {
        this.getNavigation().stop();
        if (this.crashRecoveryTicks > 0) {
            this.setNoGravity(false);
            this.setDeltaMovement(Vec3.ZERO);
            if (--this.crashRecoveryTicks == 0) {
                this.setXRot(0.0F);
                this.setFlightState(FLIGHT_GROUNDED);
            }
            return;
        }

        this.setNoGravity(false);
        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.82D, Math.max(-1.4D, motion.y), motion.z * 0.82D);
        if (this.onGround() || this.verticalCollision || this.getGroundDistance() <= 0.6D) {
            this.triggerCrashImpact();
        }
    }

    private void triggerCrashImpact() {
        if (this.crashRecoveryTicks > 0) return;
        this.crashRecoveryTicks = 52;
        this.setNoGravity(false);
        this.setDeltaMovement(Vec3.ZERO);
        this.setXRot(0.0F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.HOSTILE, 2.5F, 0.62F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_HURT,
                SoundSource.HOSTILE, 2.1F, 0.7F);

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.25D, this.getZ(),
                    42, 2.7D, 0.35D, 2.7D, 0.12D);
            serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 0.5D, this.getZ(),
                    34, 2.2D, 0.6D, 2.2D, 0.15D);
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.7D, this.getZ(),
                    18, 1.8D, 0.55D, 1.8D, 0.06D);
        }

        AABB impact = this.getBoundingBox().inflate(5.5D, 1.5D, 5.5D);
        for (LivingEntity victim : this.level().getEntitiesOfClass(LivingEntity.class, impact,
                living -> living != this && !this.hasPassenger(living) && this.canAttack(living))) {
            victim.hurt(this.damageSources().mobAttack(this), 10.0F);
            victim.knockback(1.65D, this.getX() - victim.getX(), this.getZ() - victim.getZ());
        }
    }

    private void tickLandingMovement() {
        if (this.landingTarget == null) {
            this.setFlightState(FLIGHT_FLYING);
            return;
        }
        Vec3 difference = this.landingTarget.subtract(this.position());
        double horizontal = Math.sqrt(difference.x * difference.x + difference.z * difference.z);
        if (this.getFlightState() == FLIGHT_APPROACHING
                && horizontal < 5.5D && this.getGroundDistance() < 3.6D) {
            this.setFlightState(FLIGHT_LANDING);
        }
        if (horizontal > 0.25D) {
            float wantedYaw = (float) (Mth.atan2(-difference.x, difference.z) * Mth.RAD_TO_DEG);
            this.setYRot(Mth.approachDegrees(this.getYRot(), wantedYaw, 8.0F));
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }

        boolean finalApproach = this.getFlightState() == FLIGHT_LANDING;
        double horizontalSpeed = horizontal > 2.5D ? (finalApproach ? 0.19D : 0.27D) : horizontal * 0.075D;
        double xSpeed = horizontal > 0.01D ? difference.x / horizontal * horizontalSpeed : 0.0D;
        double zSpeed = horizontal > 0.01D ? difference.z / horizontal * horizontalSpeed : 0.0D;
        double downSpeed = finalApproach ? -0.19D : (horizontal > 4.0D ? -0.095D : -0.13D);
        Vec3 wanted = new Vec3(xSpeed, downSpeed, zSpeed);
        this.setDeltaMovement(this.getDeltaMovement().scale(0.52D).add(wanted.scale(0.48D)));

        if (this.onGround() || this.verticalCollision || this.getGroundDistance() <= 0.65D
                || this.getY() <= this.landingTarget.y + 0.42D) {
            this.tickTouchdown();
        } else if (this.stateTicks > (finalApproach ? 80 : 180)) {
            // Retry from slightly above instead of silently returning to an
            // endless flight pose while touching an unsuitable landing edge.
            this.landingTarget = this.findLandingSpot(this.position());
            this.stateTicks = 0;
            if (this.landingTarget == null) this.setFlightState(FLIGHT_FLYING);
        }
    }

    private void tickTouchdown() {
        if (this.getFlightState() != FLIGHT_LANDING) this.setFlightState(FLIGHT_LANDING);
        this.touchdownTicks++;
        this.setNoGravity(false);
        this.setXRot(Mth.approachDegrees(this.getXRot(), 0.0F, 5.0F));
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.18D, 0.0D, 0.18D));
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
        // Keep LANDING synchronized long enough for the final wing-fold and
        // body-settle keyframes to be visible on clients.
        // On an emergency touchdown LANDING may only just have begun.  Keep it
        // long enough to show the complete wing fold instead of cutting to idle.
        if (this.touchdownTicks >= 10 && this.stateTicks >= 26) this.finishLanding();
    }

    private void finishLanding() {
        boolean wasLanding = this.getFlightState() == FLIGHT_LANDING;
        this.setFlightState(FLIGHT_GROUNDED);
        this.setNoGravity(false);
        this.setXRot(0.0F);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.28D, 0.0D, 0.28D));
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
        if (wasLanding) {
            this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.65F, 0.72F);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY() + 0.15D, this.getZ(),
                        18, 1.8D, 0.15D, 1.8D, 0.045D);
            }
        }
    }

    public void beginTakeoff() {
        if (this.getFlightState() == FLIGHT_GROUNDED
                || this.getFlightState() == FLIGHT_APPROACHING
                || this.getFlightState() == FLIGHT_LANDING) {
            this.setSleeping(false);
            this.setFlightState(FLIGHT_TAKING_OFF);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.09D, 0.0D));
        }
    }

    public boolean beginLanding(Vec3 near) {
        if (this.getFlightState() != FLIGHT_FLYING) return false;
        Vec3 spot = this.findLandingSpot(near);
        if (spot == null) return false;
        this.landingTarget = spot;
        this.setFlightState(FLIGHT_APPROACHING);
        return true;
    }

    public void setFlightState(byte state) {
        if (this.getFlightState() == state) return;
        this.entityData.set(FLIGHT_STATE, state);
        this.stateTicks = 0;
        if (state != FLIGHT_LANDING) this.touchdownTicks = 0;
        if (state != FLIGHT_CRASHING) this.crashRecoveryTicks = 0;
        boolean gravityDriven = state == FLIGHT_GROUNDED
                || state == FLIGHT_FALLING || state == FLIGHT_CRASHING;
        this.setNoGravity(!gravityDriven);
        this.applyMovementMode(state);
        if (state != FLIGHT_APPROACHING && state != FLIGHT_LANDING) this.landingTarget = null;
    }

    public byte getFlightState() {
        return this.entityData.get(FLIGHT_STATE);
    }

    public boolean isAirborne() {
        return this.getFlightState() != FLIGHT_GROUNDED;
    }

    private boolean isTransitioning() {
        return this.getFlightState() == FLIGHT_TAKING_OFF
                || this.getFlightState() == FLIGHT_APPROACHING
                || this.getFlightState() == FLIGHT_LANDING
                || this.getFlightState() == FLIGHT_STAGGERED
                || this.getFlightState() == FLIGHT_FALLING
                || this.getFlightState() == FLIGHT_CRASHING;
    }

    @Nullable
    private Vec3 findLandingSpot(Vec3 near) {
        Vec3 best = null;
        double bestScore = Double.MAX_VALUE;
        int baseX = Mth.floor(near.x);
        int baseZ = Mth.floor(near.z);

        for (int ring = 0; ring <= 12; ring += 3) {
            int samples = ring == 0 ? 1 : 12;
            for (int sample = 0; sample < samples; sample++) {
                double angle = sample * Mth.TWO_PI / samples + this.getId() * 0.31D;
                int x = baseX + Mth.floor(Math.cos(angle) * ring);
                int z = baseZ + Mth.floor(Math.sin(angle) * ring);
                if (!this.level().hasChunkAt(new BlockPos(x, Mth.floor(this.getY()), z))) continue;

                int minY = Integer.MAX_VALUE;
                int maxY = Integer.MIN_VALUE;
                boolean solid = true;
                for (int dx = -2; dx <= 2 && solid; dx += 2) {
                    for (int dz = -2; dz <= 2; dz += 2) {
                        int y = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x + dx, z + dz);
                        BlockPos floor = new BlockPos(x + dx, y - 1, z + dz);
                        if (this.level().getBlockState(floor).isAir()
                                || !this.level().getFluidState(floor).isEmpty()) {
                            solid = false;
                            break;
                        }
                        minY = Math.min(minY, y);
                        maxY = Math.max(maxY, y);
                    }
                }
                if (!solid || maxY - minY > 1) continue;

                Vec3 candidate = new Vec3(x + 0.5D, maxY + 0.05D, z + 0.5D);
                AABB destinationBox = this.getBoundingBox().move(candidate.subtract(this.position())).inflate(-0.08D);
                if (!this.level().noCollision(this, destinationBox)) continue;
                double score = candidate.distanceToSqr(near) + Math.abs(candidate.y - near.y) * 2.0D;
                if (score < bestScore) {
                    bestScore = score;
                    best = candidate;
                }
            }
        }
        return best;
    }

    private double getGroundDistance() {
        int groundY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mth.floor(this.getX()), Mth.floor(this.getZ()));
        return this.getY() - groundY;
    }

    @Override
    public void travel(Vec3 input) {
        LivingEntity passenger = this.getControllingPassenger();
        if (this.isAlive() && passenger instanceof Player rider) {
            this.setYRot(rider.getYRot());
            this.yRotO = this.getYRot();
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            this.setXRot(rider.getXRot() * 0.68F);

            byte flightState = this.getFlightState();
            if (flightState == FLIGHT_TAKING_OFF || flightState == FLIGHT_APPROACHING
                    || flightState == FLIGHT_LANDING) {
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.calculateEntityAnimation(false);
                return;
            }
            if (flightState == FLIGHT_GROUNDED) {
                this.setSpeed(0.22F);
                super.travel(new Vec3(rider.xxa * 0.55F, input.y, rider.zza));
                return;
            }

            float forward = rider.zza;
            float strafe = rider.xxa * 0.55F;
            if (forward < 0.0F) forward *= 0.35F;
            double speed = rider.isSprinting() ? 0.78D : 0.56D;
            float yawRadians = this.getYRot() * Mth.DEG_TO_RAD;
            Vec3 forwardVector = new Vec3(-Mth.sin(yawRadians), 0.0D, Mth.cos(yawRadians));
            Vec3 sideVector = new Vec3(Mth.cos(yawRadians), 0.0D, Mth.sin(yawRadians));
            double vertical = Math.abs(forward) > 0.01F
                    ? Mth.clamp(-Mth.sin(rider.getXRot() * Mth.DEG_TO_RAD)
                    * Math.abs(forward) * speed, -0.66D, 0.66D) : 0.0D;
            Vec3 desired = forwardVector.scale(forward * speed)
                    .add(sideVector.scale(strafe * speed))
                    .add(0.0D, vertical, 0.0D);
            Vec3 motion = this.getDeltaMovement().scale(0.68D).add(desired.scale(0.32D));
            if (Math.abs(forward) < 0.01F && Math.abs(strafe) < 0.01F) {
                motion = new Vec3(motion.x * 0.7D, motion.y * 0.52D, motion.z * 0.7D);
            }
            this.setDeltaMovement(motion);
            this.move(MoverType.SELF, motion);
            this.calculateEntityAnimation(false);

            if (!this.level().isClientSide && vertical < -0.06D && this.getGroundDistance() < 2.8D) {
                this.beginLanding(this.position());
            }
            return;
        }
        super.travel(input);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        if (this.isSaddled() && passenger instanceof Player player && this.isOwnedBy(player)) return player;
        return null;
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        float yaw = -this.getYRot() * Mth.DEG_TO_RAD;
        Vec3 saddleOffset = new Vec3(0.0D, 2.85D, 0.32D).yRot(yaw);
        return this.position().add(saddleOffset);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.isSaddled() && this.getPassengers().isEmpty()
                && passenger instanceof Player player && this.isOwnedBy(player);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.GOLDEN_APPLE) && (!this.isTamed() || this.isOwnedBy(player))) {
            if (!this.level().isClientSide) {
                if (!player.getAbilities().instabuild) held.shrink(1);
                this.heal(40.0F);
                if (!this.isTamed()) this.setTamedBy(player);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isOwnedBy(player) && held.is(Items.SADDLE) && !this.isSaddled()) {
            if (!this.level().isClientSide) {
                this.setSaddled(true);
                if (!player.getAbilities().instabuild) held.shrink(1);
                this.playSound(SoundEvents.HORSE_SADDLE, 0.8F, 1.0F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.isTamed() && player.getAbilities().instabuild && held.isEmpty()) {
            if (!this.level().isClientSide) {
                this.setTamedBy(player);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isOwnedBy(player) && held.isEmpty()) {
            if (!this.level().isClientSide) {
                if (player.isShiftKeyDown()) {
                    this.setSleeping(!this.isSleeping());
                    this.ejectPassengers();
                } else if (this.isSaddled()) {
                    this.setSleeping(false);
                    player.startRiding(this, true);
                    this.beginTakeoff();
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public void setTamedBy(Player player) {
        this.entityData.set(TAMED, true);
        this.entityData.set(OWNER, Optional.of(player.getUUID()));
        this.setTarget(null);
        this.bossEvent.setVisible(false);
    }

    public boolean isTamed() {
        return this.entityData.get(TAMED);
    }

    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    private void setSaddled(boolean saddled) {
        this.entityData.set(SADDLED, saddled);
    }

    public boolean isOwnedBy(Player player) {
        return this.entityData.get(OWNER).map(uuid -> uuid.equals(player.getUUID())).orElse(false);
    }

    private boolean isOwner(Entity entity) {
        return this.entityData.get(OWNER).map(uuid -> uuid.equals(entity.getUUID())).orElse(false);
    }

    @Nullable
    private LivingEntity getOwnerEntity() {
        Optional<UUID> owner = this.entityData.get(OWNER);
        if (owner.isEmpty()) return null;
        Entity entity = this.level() instanceof ServerLevel serverLevel
                ? serverLevel.getEntity(owner.get()) : this.level().getPlayerByUUID(owner.get());
        return entity instanceof LivingEntity living ? living : null;
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
        if (sleeping) {
            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);
            this.entityData.set(ACTION, ACTION_NONE);
            this.actionTicks = 0;
            if (this.isAirborne()) this.beginLanding(this.position());
        }
    }

    public boolean isBreathingFire() {
        int action = this.entityData.get(ACTION);
        return action == ACTION_AIR_FIRE || action == ACTION_GROUND_FIRE;
    }

    private void setAction(int action, int ticks) {
        this.entityData.set(ACTION, action);
        this.actionTicks = ticks;
    }

    /** Starts a continuous breath; particles travel from the mouth toward the aim point. */
    private void startFireBreath(@Nullable LivingEntity target, Vec3 targetPosition) {
        if (this.level().isClientSide || this.isTransitioning()) return;
        this.breathTargetEntityId = target == null ? -1 : target.getId();
        this.breathTargetPosition = targetPosition;
        this.breathAge = 0;
        this.setAction(this.isAirborne() ? ACTION_AIR_FIRE : ACTION_GROUND_FIRE, 42);
        this.getNavigation().stop();
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
                SoundSource.HOSTILE, 2.7F, 0.76F + this.random.nextFloat() * 0.08F);
    }

    public void breatheFireAt(Vec3 targetPosition) {
        this.startFireBreath(null, targetPosition);
    }

    private void tickFireBreath() {
        this.breathAge++;
        Entity tracked = this.breathTargetEntityId < 0 ? null : this.level().getEntity(this.breathTargetEntityId);
        if (tracked instanceof LivingEntity living && living.isAlive()) {
            this.breathTargetPosition = living.getEyePosition();
        }

        Vec3 center = this.position().add(0.0D, 2.65D, 0.0D);
        Vec3 toTarget = this.breathTargetPosition.subtract(center);
        double rawDistance = toTarget.length();
        if (rawDistance < 0.01D) return;
        Vec3 direction = toTarget.normalize();
        float desiredYaw = (float) (Mth.atan2(-direction.x, direction.z) * Mth.RAD_TO_DEG);
        float desiredPitch = (float) (Mth.atan2(-direction.y,
                Math.sqrt(direction.x * direction.x + direction.z * direction.z)) * Mth.RAD_TO_DEG);
        this.setYRot(Mth.approachDegrees(this.getYRot(), desiredYaw, 9.5F));
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();
        this.setXRot(Mth.approachDegrees(this.getXRot(), desiredPitch, 7.0F));

        Vec3 mouth = center.add(direction.scale(3.25D));
        double range = Math.min(42.0D, rawDistance + 1.5D);
        Vec3 intendedEnd = mouth.add(direction.scale(range));
        HitResult obstruction = this.level().clip(new ClipContext(mouth, intendedEnd,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        Vec3 end = obstruction.getType() == HitResult.Type.MISS ? intendedEnd : obstruction.getLocation();
        double beamLength = mouth.distanceTo(end);

        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        if (this.breathAge <= 6) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, mouth.x, mouth.y, mouth.z,
                    5, 0.28D, 0.28D, 0.28D, 0.025D);
            serverLevel.sendParticles(ParticleTypes.FLAME, mouth.x, mouth.y, mouth.z,
                    3, 0.18D, 0.18D, 0.18D, 0.015D);
            return;
        }
        if (this.breathAge == 7) {
            this.playSound(SoundEvents.BLAZE_SHOOT, 2.2F, 0.62F);
        }

        // Advance a visible front at roughly two blocks per tick. Particles are
        // also placed along the traversed section, so flames remain visible near
        // a distant player instead of expiring after leaving the dragon's mouth.
        double activeLength = Math.min(beamLength, (this.breathAge - 6) * 1.9D);
        double phaseOffset = (this.breathAge % 3) * 0.48D;
        for (double distance = 0.35D + phaseOffset; distance <= activeLength; distance += 1.45D) {
            double cone = 0.045D + distance * 0.0075D;
            Vec3 particle = mouth.add(direction.scale(distance)).add(
                    this.random.nextGaussian() * cone,
                    this.random.nextGaussian() * cone,
                    this.random.nextGaussian() * cone);
            Vec3 velocity = direction.scale(0.10D + this.random.nextDouble() * 0.08D);
            serverLevel.sendParticles(this.random.nextInt(5) == 0
                            ? ParticleTypes.SMALL_FLAME : ParticleTypes.FLAME,
                    particle.x, particle.y, particle.z, 0,
                    velocity.x, velocity.y, velocity.z, 1.0D);
        }

        Vec3 front = mouth.add(direction.scale(activeLength));
        serverLevel.sendParticles(ParticleTypes.FLAME, front.x, front.y, front.z,
                4, 0.22D, 0.22D, 0.22D, 0.025D);
        if ((this.breathAge & 1) == 0) {
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, front.x, front.y, front.z,
                    1, 0.08D, 0.08D, 0.08D, 0.018D);
        }

        boolean reachedImpact = activeLength >= beamLength - 0.3D;
        if (reachedImpact) {
            serverLevel.sendParticles(ParticleTypes.FLAME, end.x, end.y, end.z,
                    9, 0.65D, 0.45D, 0.65D, 0.045D);
            if (this.breathAge % 3 == 0) {
                serverLevel.sendParticles(ParticleTypes.LAVA, end.x, end.y, end.z,
                        3, 0.4D, 0.3D, 0.4D, 0.025D);
            }
        }

        if (this.breathAge % 4 == 0) this.damageAlongBreath(mouth, direction, activeLength);
    }

    private void damageAlongBreath(Vec3 start, Vec3 direction, double length) {
        Vec3 end = start.add(direction.scale(length));
        AABB beamBounds = new AABB(start, end).inflate(1.45D);
        for (LivingEntity victim : this.level().getEntitiesOfClass(LivingEntity.class, beamBounds,
                living -> living != this && !this.hasPassenger(living) && this.canAttack(living))) {
            Vec3 relative = victim.getBoundingBox().getCenter().subtract(start);
            double projected = Mth.clamp(relative.dot(direction), 0.0D, length);
            Vec3 closest = start.add(direction.scale(projected));
            double radius = 1.0D + projected * 0.018D + victim.getBbWidth() * 0.5D;
            if (victim.getBoundingBox().getCenter().distanceToSqr(closest) <= radius * radius) {
                victim.hurt(this.damageSources().mobAttack(this), 5.0F);
                victim.igniteForSeconds(4.0F);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean damaged = super.hurt(source, amount);
        if (!damaged || this.level().isClientSide || !this.isAlive() || this.isTamed()) return damaged;

        byte state = this.getFlightState();
        boolean alreadyFalling = state == FLIGHT_STAGGERED
                || state == FLIGHT_FALLING || state == FLIGHT_CRASHING;
        if (source.getDirectEntity() instanceof AbstractArrow
                && this.getHealth() < this.getMaxHealth() * 0.5F
                && this.shotDownCooldown == 0 && this.isAirborne() && !alreadyFalling
                && this.getGroundDistance() > 1.4D) {
            this.startShotDown();
        }
        return damaged;
    }

    private void startShotDown() {
        this.shotDownCooldown = 320;
        this.setSleeping(false);
        this.entityData.set(ACTION, ACTION_NONE);
        this.actionTicks = 0;
        this.breathAge = 0;
        this.breathTargetEntityId = -1;
        this.getNavigation().stop();
        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.55D, Math.min(0.015D, motion.y), motion.z * 0.55D);
        this.setFlightState(FLIGHT_STAGGERED);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_HURT,
                SoundSource.HOSTILE, 2.4F, 0.82F);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 2.6D, this.getZ(),
                    22, 1.5D, 1.1D, 1.5D, 0.16D);
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 2.6D, this.getZ(),
                    12, 1.1D, 0.8D, 1.1D, 0.055D);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isOwner(target) || this.hasPassenger(target)) return false;
        if (this.isTamed() && target instanceof Player) return false;
        return super.canAttack(target);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, source, recentlyHit);
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
        // Unique Boss Drops
        this.spawnAtLocation(ModItems.SHIELD_DRAGONSLAYER.get());
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.FIRE_STAFF.get());
        }
        this.spawnAtLocation(new ItemStack(Items.BLAZE_ROD, 3 + this.random.nextInt(4)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 3 + this.random.nextInt(4)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.DRAGON_BREATH, 1 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE, 1 + this.random.nextInt(2)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("GreenDragonTamed", this.isTamed());
        compound.putBoolean("GreenDragonSleeping", this.isSleeping());
        compound.putBoolean("GreenDragonSaddled", this.isSaddled());
        compound.putByte("GreenDragonFlightState", this.getFlightState());
        this.entityData.get(OWNER).ifPresent(uuid -> compound.putUUID("GreenDragonOwner", uuid));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(TAMED, compound.getBoolean("GreenDragonTamed"));
        this.entityData.set(SLEEPING, compound.getBoolean("GreenDragonSleeping"));
        this.entityData.set(SADDLED, compound.getBoolean("GreenDragonSaddled"));
        byte flight = compound.getByte("GreenDragonFlightState");
        this.entityData.set(FLIGHT_STATE, flight == FLIGHT_FLYING ? FLIGHT_FLYING : FLIGHT_GROUNDED);
        this.setNoGravity(flight == FLIGHT_FLYING);
        this.applyMovementMode(this.getFlightState());
        if (compound.hasUUID("GreenDragonOwner")) {
            this.entityData.set(OWNER, Optional.of(compound.getUUID("GreenDragonOwner")));
        }
        if (this.hasCustomName()) this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(name != null ? name : Component.translatable(
                "entity.cqrepoured.cqr_dragon"));
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
    public void handleEntityEvent(byte id) {
        if (id == 7 || id == 6) {
            for (int i = 0; i < 14; i++) {
                this.level().addParticle(id == 7 ? ParticleTypes.HEART : ParticleTypes.SMOKE,
                        this.getRandomX(2.8D), this.getRandomY() + 0.7D, this.getRandomZ(2.8D),
                        this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D,
                        this.random.nextGaussian() * 0.02D);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private PlayState animationPredicate(AnimationState<EntityCQRDragon> state) {
        state.getController().setAnimationSpeed(1.0D);
        int action = this.entityData.get(ACTION);
        byte flight = this.getFlightState();
        RawAnimation animation;
        int animationKey;

        // Transitions have priority so a stale attack frame can never hide a
        // takeoff or landing sequence.
        // Sleeping is only a grounded pose. If the command was issued in the
        // air, the dragon must visibly finish approach and landing first.
        if (flight == FLIGHT_STAGGERED) {
            animation = SHOT_DOWN;
            animationKey = 30;
        } else if (flight == FLIGHT_FALLING) {
            animation = FREEFALL;
            animationKey = 31;
        } else if (flight == FLIGHT_CRASHING) {
            animation = CRASH;
            animationKey = 32;
        } else if (this.isSleeping() && flight == FLIGHT_GROUNDED) {
            animation = SLEEP;
            animationKey = 1;
        } else if (flight == FLIGHT_TAKING_OFF) {
            animation = TAKEOFF;
            animationKey = 2;
        } else if (flight == FLIGHT_LANDING) {
            animation = LANDING;
            animationKey = 3;
        } else if (action == ACTION_AIR_FIRE) {
            animation = AIR_FIRE;
            animationKey = 10;
        } else if (action == ACTION_GROUND_FIRE) {
            animation = GROUND_FIRE;
            animationKey = 11;
        } else if (action == ACTION_BITE) {
            animation = BITE;
            animationKey = 12;
        } else if (flight == FLIGHT_FLYING || flight == FLIGHT_APPROACHING) {
            double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
            state.getController().setAnimationSpeed(Mth.clamp(0.82D + horizontalSpeed * 0.72D,
                    0.82D, 1.18D));
            if (horizontalSpeed < 0.075D) {
                animation = FLYING_IDLE;
                animationKey = 5;
            } else {
                animation = FLYING;
                animationKey = 4;
            }
        } else if (flight == FLIGHT_GROUNDED && this.onGround()
                && this.getDeltaMovement().horizontalDistanceSqr() > 0.0004D) {
            // One walk cycle now follows actual travelled distance instead of
            double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
            state.getController().setAnimationSpeed(Mth.clamp(horizontalSpeed * 4.8D, 0.62D, 1.22D));
            animation = WALK;
            animationKey = 20;
        } else {
            animation = IDLE;
            animationKey = 21;
        }

        if (this.lastAnimationKey != animationKey) {
            state.getController().forceAnimationReset();
            this.lastAnimationKey = animationKey;
        }
        state.getController().setAnimation(animation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "green_dragon", 2, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    /** Prevents a five-block-wide creature from snapping 90 degrees at path corners. */
    private static final class SmoothDragonGroundMoveControl extends MoveControl {
        private final EntityCQRDragon dragon;

        private SmoothDragonGroundMoveControl(EntityCQRDragon dragon) {
            super(dragon);
            this.dragon = dragon;
        }

        @Override
        public void tick() {
            float previousYaw = this.dragon.getYRot();
            super.tick();
            float navigationYaw = this.dragon.getYRot();
            this.dragon.setYRot(Mth.approachDegrees(previousYaw, navigationYaw, 4.5F));
            this.dragon.yBodyRot = Mth.approachDegrees(
                    this.dragon.yBodyRot, this.dragon.getYRot(), 4.5F);
            this.dragon.setXRot(Mth.approachDegrees(this.dragon.getXRot(), 0.0F, 4.0F));
        }
    }

    private static final class GreenDragonCombatGoal extends Goal {
        private final EntityCQRDragon dragon;
        private int attackCooldown;
        private int phaseTicks;
        private int phaseLength;
        private int orbitDirection;
        private double orbitOffset;
        private int repathCooldown;
        private int stuckTicks;
        private int failedPaths;
        private int collisionTicks;
        private int lostSightTicks;
        private Vec3 lastProgressPosition = Vec3.ZERO;

        private GreenDragonCombatGoal(EntityCQRDragon dragon) {
            this.dragon = dragon;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.dragon.getTarget();
            return !this.dragon.isVehicle() && !this.dragon.isSleeping()
                    && target != null && target.isAlive()
                    && (!this.dragon.isTamed() || !(target instanceof Player));
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            this.attackCooldown = 22;
            this.phaseTicks = 0;
            this.phaseLength = this.dragon.getRandom().nextIntBetweenInclusive(220, 320);
            this.orbitDirection = this.dragon.getRandom().nextBoolean() ? 1 : -1;
            this.orbitOffset = this.dragon.getRandom().nextDouble() * Mth.TWO_PI;
            this.repathCooldown = 0;
            this.stuckTicks = 0;
            this.failedPaths = 0;
            this.collisionTicks = 0;
            this.lostSightTicks = 0;
            this.lastProgressPosition = this.dragon.position();
        }

        @Override
        public void tick() {
            LivingEntity target = this.dragon.getTarget();
            if (target == null) return;
            if (this.attackCooldown > 0) this.attackCooldown--;
            if (this.repathCooldown > 0) this.repathCooldown--;
            this.phaseTicks++;
            this.dragon.getLookControl().setLookAt(target, 18.0F, 12.0F);

            switch (this.dragon.getFlightState()) {
                case FLIGHT_GROUNDED -> this.tickGroundCombat(target);
                case FLIGHT_TAKING_OFF -> { }
                case FLIGHT_FLYING -> this.tickAirCombat(target);
                case FLIGHT_APPROACHING, FLIGHT_LANDING -> { }
                case FLIGHT_STAGGERED, FLIGHT_FALLING, FLIGHT_CRASHING -> { }
                default -> { }
            }
        }

        private void tickGroundCombat(LivingEntity target) {
            double distanceSq = this.dragon.distanceToSqr(target);
            boolean canSeeTarget = this.dragon.getSensing().hasLineOfSight(target);
            boolean wantsBite = this.attackCooldown == 0 && distanceSq < 45.0D;
            boolean wantsBreath = this.attackCooldown == 0 && distanceSq < 760.0D && canSeeTarget;
            boolean preparingAttack = wantsBite || wantsBreath || this.dragon.isBreathingFire();

            if (!preparingAttack && distanceSq > 52.0D) {
                if (this.repathCooldown == 0) {
                    boolean foundPath = this.dragon.getNavigation().moveTo(target, 0.82D);
                    this.repathCooldown = 9 + this.dragon.getRandom().nextInt(4);
                    this.failedPaths = foundPath ? 0 : this.failedPaths + 1;
                }
                this.checkGroundProgress();
            } else {
                this.dragon.getNavigation().stop();
                this.stuckTicks = 0;
                this.failedPaths = 0;
                if (distanceSq <= 52.0D && this.attackCooldown > 0
                        && !this.dragon.isBreathingFire()) this.turnForAttack(target);
            }

            this.collisionTicks = this.dragon.horizontalCollision
                    ? this.collisionTicks + 1 : Math.max(0, this.collisionTicks - 2);

            if (this.attackCooldown == 0) {
                if (distanceSq < 45.0D) {
                    if (this.turnForAttack(target) < 18.0F) {
                        this.dragon.setAction(ACTION_BITE, 16);
                        this.dragon.doHurtTarget(target);
                        this.dragon.level().playSound(null, this.dragon.blockPosition(),
                                SoundEvents.RAVAGER_ATTACK, SoundSource.HOSTILE, 1.5F, 0.72F);
                        this.attackCooldown = 30;
                    }
                } else if (distanceSq < 760.0D && canSeeTarget) {
                    if (this.turnForAttack(target) < 14.0F) {
                        this.dragon.startFireBreath(target, target.getEyePosition());
                        this.attackCooldown = 70;
                    }
                } else if (distanceSq <= 52.0D) {
                    this.turnForAttack(target);
                }
            }

            if ((this.stuckTicks > 34 || this.failedPaths >= 3 || this.collisionTicks > 8)
                    && !this.dragon.isBreathingFire()) {
                this.phaseTicks = 0;
                this.stuckTicks = 0;
                this.failedPaths = 0;
                this.collisionTicks = 0;
                this.dragon.beginTakeoff();
                return;
            }

            int groundLimit = target.getY() > this.dragon.getY() + 5.0D ? 30 : 85;
            if (this.phaseTicks > groundLimit && !this.dragon.isBreathingFire()) {
                this.phaseTicks = 0;
                this.phaseLength = this.dragon.getRandom().nextIntBetweenInclusive(220, 320);
                this.dragon.beginTakeoff();
            }
        }

        private void checkGroundProgress() {
            if (this.phaseTicks % 10 != 0) return;
            double movedSq = this.dragon.position().distanceToSqr(this.lastProgressPosition);
            if (!this.dragon.getNavigation().isDone() && movedSq < 0.16D) {
                this.stuckTicks += 10;
            } else {
                this.stuckTicks = Math.max(0, this.stuckTicks - 10);
            }
            this.lastProgressPosition = this.dragon.position();
        }

        private void tickAirCombat(LivingEntity target) {
            double distanceSq = this.dragon.distanceToSqr(target);
            boolean canSeeTarget = this.dragon.getSensing().hasLineOfSight(target);
            this.lostSightTicks = canSeeTarget ? Math.max(0, this.lostSightTicks - 3)
                    : this.lostSightTicks + 1;
            boolean preparingBreath = this.attackCooldown == 0 && distanceSq < 1800.0D
                    && canSeeTarget;

            if (preparingBreath) {
                // Leave the circular path, stabilize and face the target before
                // opening the mouth. The dragon no longer breathes sideways.
                double stableY = Mth.clamp(this.dragon.getY(), target.getY() + 3.2D,
                        target.getY() + 5.7D);
                this.dragon.getMoveControl().setWantedPosition(
                        this.dragon.getX(), stableY, this.dragon.getZ(), 0.18D);
                if (this.turnForAttack(target) < 16.0F) {
                    this.dragon.startFireBreath(target, target.getEyePosition());
                    this.attackCooldown = this.dragon.getRandom().nextIntBetweenInclusive(68, 92);
                }
            } else if (!this.dragon.isBreathingFire()) {
                double time = this.phaseTicks * 0.029D * this.orbitDirection + this.orbitOffset;
                boolean recoveringSight = this.lostSightTicks > 12;
                double radius = recoveringSight
                        ? 7.5D + Math.sin(time * 0.7D)
                        : 11.5D + Math.sin(time * 0.7D) * 1.75D;
                double height = recoveringSight
                        ? 3.25D
                        : 4.35D + Math.sin(time * 1.35D) * 1.15D;
                double desiredX = target.getX() + Math.cos(time) * radius;
                double desiredZ = target.getZ() + Math.sin(time) * radius;
                double terrainY = this.dragon.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        Mth.floor(desiredX), Mth.floor(desiredZ));
                double desiredY = Math.max(target.getY() + height, terrainY + 2.6D);
                double maximumUsefulY = Math.max(target.getY(), terrainY) + 6.2D;
                desiredY = Math.min(desiredY, maximumUsefulY);
                this.dragon.getMoveControl().setWantedPosition(desiredX, desiredY, desiredZ,
                        recoveringSight ? 1.08D : 1.0D);
            } else {
                double stableY = Mth.clamp(this.dragon.getY(), target.getY() + 3.2D,
                        target.getY() + 5.7D);
                this.dragon.getMoveControl().setWantedPosition(
                        this.dragon.getX(), stableY, this.dragon.getZ(), 0.25D);
            }

            if (this.phaseTicks > this.phaseLength && !this.dragon.isBreathingFire()
                    && target.onGround() && this.dragon.beginLanding(target.position())) {
                this.phaseTicks = 0;
                this.attackCooldown = 35;
                this.orbitDirection = -this.orbitDirection;
            }
        }

        private float turnForAttack(LivingEntity target) {
            Vec3 aim = target.getEyePosition().subtract(this.dragon.position().add(0.0D, 2.65D, 0.0D));
            float yaw = (float) (Mth.atan2(-aim.x, aim.z) * Mth.RAD_TO_DEG);
            float difference = Mth.wrapDegrees(yaw - this.dragon.getYRot());
            this.dragon.setYRot(Mth.approachDegrees(this.dragon.getYRot(), yaw, 4.5F));
            this.dragon.yBodyRot = Mth.approachDegrees(this.dragon.yBodyRot, this.dragon.getYRot(), 4.5F);
            return Math.abs(difference);
        }
    }

    private static final class GreenDragonFollowOwnerGoal extends Goal {
        private final EntityCQRDragon dragon;
        @Nullable
        private LivingEntity owner;
        private int repathCooldown;

        private GreenDragonFollowOwnerGoal(EntityCQRDragon dragon) {
            this.dragon = dragon;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.owner = this.dragon.getOwnerEntity();
            return this.dragon.isTamed() && !this.dragon.isVehicle() && !this.dragon.isSleeping()
                    && this.owner != null && this.owner.isAlive()
                    && this.dragon.distanceToSqr(this.owner) > 196.0D;
        }

        @Override
        public void start() {
            this.repathCooldown = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.owner != null && this.owner.isAlive() && !this.dragon.isVehicle()
                    && !this.dragon.isSleeping() && this.dragon.distanceToSqr(this.owner) > 64.0D;
        }

        @Override
        public void tick() {
            if (this.owner == null) return;
            if (this.repathCooldown > 0) this.repathCooldown--;
            this.dragon.getLookControl().setLookAt(this.owner, 45.0F, 45.0F);
            if (this.dragon.getFlightState() == FLIGHT_GROUNDED) {
                if (this.dragon.distanceToSqr(this.owner) > 324.0D) this.dragon.beginTakeoff();
                else if (this.repathCooldown == 0) {
                    this.dragon.getNavigation().moveTo(this.owner, 0.82D);
                    this.repathCooldown = 10;
                }
            } else if (this.dragon.getFlightState() == FLIGHT_FLYING) {
                this.dragon.getMoveControl().setWantedPosition(
                        this.owner.getX(), this.owner.getY() + 4.3D, this.owner.getZ(), 1.05D);
                if (this.owner.onGround() && this.dragon.distanceToSqr(this.owner) < 110.0D) {
                    this.dragon.beginLanding(this.owner.position());
                }
            }
        }
    }

    private static final class GreenDragonWanderGoal extends Goal {
        private final EntityCQRDragon dragon;
        private Vec3 destination = Vec3.ZERO;
        private int ticks;

        private GreenDragonWanderGoal(EntityCQRDragon dragon) {
            this.dragon = dragon;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.dragon.isVehicle() || this.dragon.isSleeping() || this.dragon.getTarget() != null
                    || this.dragon.getRandom().nextInt(180) != 0) return false;
            double angle = this.dragon.getRandom().nextDouble() * Mth.TWO_PI;
            double distance = this.dragon.getRandom().nextIntBetweenInclusive(16, 30);
            double x = this.dragon.getX() + Math.cos(angle) * distance;
            double z = this.dragon.getZ() + Math.sin(angle) * distance;
            BlockPos sample = BlockPos.containing(x, this.dragon.getY(), z);
            if (!this.dragon.level().hasChunkAt(sample)) return false;
            double groundY = this.dragon.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mth.floor(x), Mth.floor(z));
            this.destination = new Vec3(x, groundY + 4.0D + this.dragon.getRandom().nextDouble() * 1.5D, z);
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.ticks < 150 && !this.dragon.isVehicle() && !this.dragon.isSleeping()
                    && this.dragon.getTarget() == null;
        }

        @Override
        public void start() {
            this.ticks = 0;
            if (this.dragon.getFlightState() == FLIGHT_GROUNDED) this.dragon.beginTakeoff();
        }

        @Override
        public void tick() {
            this.ticks++;
            if (this.dragon.getFlightState() == FLIGHT_FLYING) {
                this.dragon.getMoveControl().setWantedPosition(
                        this.destination.x, this.destination.y, this.destination.z, 0.82D);
                if (this.ticks > 105 || this.dragon.position().distanceToSqr(this.destination) < 20.0D) {
                    this.dragon.beginLanding(this.destination);
                }
            }
        }
    }
}
