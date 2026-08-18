package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.entity.projectile.EnderBlockProjectileEntity;
import com.example.chocolatequest.entity.projectile.EnderCalamityOrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQREndermenace extends Monster implements GeoEntity {
    private static final int PHASE_IDLE = 0;
    private static final int PHASE_BUILDING = 1;
    private static final int PHASE_LASER = 2;
    private static final int PHASE_TENNIS = 3;
    private static final int PHASE_STUNNED = 4;
    private static final int ACTION_BODY = 0;
    private static final int ACTION_CHARGE = 1;
    private static final int ACTION_SHOOT_ORB = 2;
    private static final int ACTION_LASER = 3;
    private static final int ACTION_STUN = 4;
    private static final int ACTION_THROW_RU = 10;
    private static final String[] THROW_ANIMATIONS = {"RU", "RM", "RL", "LU", "LM", "LL"};

    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(EntityCQREndermenace.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(EntityCQREndermenace.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(),
            BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_10).setDarkenScreen(true);
    private int phaseTime;
    private int attackSequence;
    private int projectileDodgeCooldown;
    private Vec3 laserOrigin = Vec3.ZERO;
    private Vec3 laserDirection = Vec3.ZERO;
    private EnderCalamityOrbEntity activeOrb;
    private EndermenaceLaserEntity activeLaser;

    public EntityCQREndermenace(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 360.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PHASE, PHASE_IDLE);
        builder.define(ACTION, ACTION_BODY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 28.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "body", 2, this::bodyAnimation));
        String[] arms = {"RU", "RM", "RL", "LU", "LM", "LL"};
        for (String arm : arms) {
            controllers.add(new AnimationController<>(this, "idle_" + arm, 2,
                    state -> this.armAnimation(state, "animation.ender_calamity.idle_arm" + arm)));
        }
    }

    private PlayState bodyAnimation(AnimationState<EntityCQREndermenace> state) {
        int action = this.entityData.get(ACTION);
        RawAnimation animation;
        if (action >= ACTION_THROW_RU && action < ACTION_THROW_RU + THROW_ANIMATIONS.length) {
            animation = RawAnimation.begin().thenPlayAndHold("animation.ender_calamity.throwBlock_"
                    + THROW_ANIMATIONS[action - ACTION_THROW_RU]);
        } else {
            animation = switch (action) {
                case ACTION_CHARGE -> RawAnimation.begin().thenLoop("animation.ender_calamity.prepareEnergyBall");
                case ACTION_SHOOT_ORB -> RawAnimation.begin().thenPlayAndHold("animation.ender_calamity.shootEnergyBall");
                case ACTION_LASER -> RawAnimation.begin().thenLoop("animation.ender_calamity.laser_stationary");
                case ACTION_STUN -> RawAnimation.begin().thenPlayAndHold("animation.ender_calamity.hit");
                default -> RawAnimation.begin().thenLoop("animation.ender_calamity.idle");
            };
        }
        state.getController().setAnimation(animation);
        return PlayState.CONTINUE;
    }

    private PlayState armAnimation(AnimationState<EntityCQREndermenace> state, String animation) {
        if (this.entityData.get(ACTION) != ACTION_BODY) {
            state.getController().forceAnimationReset();
            return PlayState.STOP;
        }
        state.getController().setAnimation(RawAnimation.begin().thenLoop(animation));
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide || !this.isAlive()) return;
        if (this.projectileDodgeCooldown > 0) this.projectileDodgeCooldown--;
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            this.getNavigation().stop();
            if (this.entityData.get(PHASE) != PHASE_IDLE) this.enterPhase(PHASE_IDLE);
            return;
        }
        this.getNavigation().stop();
        this.setNoGravity(true);
        this.fallDistance = 0.0F;
        double desiredY = target.getY() + 6.0D;
        double lift = net.minecraft.util.Mth.clamp((desiredY - this.getY()) * 0.08D, -0.18D, 0.18D);
        this.setDeltaMovement(this.getDeltaMovement().x * 0.72D, lift, this.getDeltaMovement().z * 0.72D);
        this.getLookControl().setLookAt(target, 60.0F, 60.0F);
        this.phaseTime++;
        switch (this.entityData.get(PHASE)) {
            case PHASE_BUILDING -> this.tickBuilding(target);
            case PHASE_LASER -> this.tickLaser(target);
            case PHASE_TENNIS -> this.tickTennis(target);
            case PHASE_STUNNED -> this.tickStunned();
            default -> this.tickIdle();
        }
    }

    private void tickIdle() {
        if (this.phaseTime == 1) this.playSound(SoundEvents.PORTAL_AMBIENT, 0.65F, 0.8F);
        if (this.phaseTime == 22 && this.getTarget() != null) this.teleportAboveTarget(this.getTarget(), 7.0D);
        if (this.phaseTime >= 45) {
            int next = switch (this.attackSequence++ % 3) {
                case 0 -> PHASE_BUILDING;
                case 1 -> PHASE_LASER;
                default -> PHASE_TENNIS;
            };
            this.enterPhase(next);
        }
    }

    private void tickBuilding(LivingEntity target) {
        if (this.phaseTime == 1) {
            this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.5F, 0.7F);
            this.burst(ParticleTypes.ENCHANT, 45, 1.8D);
        }
        if (this.phaseTime == 64) this.teleportAboveTarget(target, 8.0D);
        if (this.phaseTime >= 20 && this.phaseTime <= 110 && (this.phaseTime - 20) % 18 == 0) {
            int hand = ((this.phaseTime - 20) / 18) % THROW_ANIMATIONS.length;
            this.entityData.set(ACTION, ACTION_THROW_RU + hand);
            this.throwEndBlock(target, hand);
        }
        if (this.phaseTime >= 120) this.enterPhase(PHASE_IDLE);
    }

    private void throwEndBlock(LivingEntity target, int hand) {
        EnderBlockProjectileEntity block = new EnderBlockProjectileEntity(this.level(), this);
        double side = hand < 3 ? -1.1D : 1.1D;
        double height = 0.7D + (hand % 3) * 0.65D;
        Vec3 lateral = this.getLookAngle().cross(new Vec3(0.0D, 1.0D, 0.0D)).normalize().scale(side);
        block.setPos(this.getX() + lateral.x, this.getY() + height, this.getZ() + lateral.z);
        Vec3 aim = target.getEyePosition().subtract(block.position());
        block.shoot(aim.x, aim.y, aim.z, 0.95F, 4.0F);
        this.level().addFreshEntity(block);
        this.playSound(SoundEvents.ENDER_PEARL_THROW, 1.1F, 0.55F + hand * 0.06F);
    }

    private void tickLaser(LivingEntity target) {
        if (this.phaseTime == 1) {
            this.entityData.set(ACTION, ACTION_LASER);
            this.teleportAboveTarget(target, 10.0D);
            this.playSound(SoundEvents.BEACON_ACTIVATE, 1.5F, 0.55F);
        }
        if (this.phaseTime == 24) {
            this.activeLaser = new EndermenaceLaserEntity(this, target);
            this.activeLaser.setupPositionAndRotation();
            this.level().addFreshEntity(this.activeLaser);
            this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 1.5F, 0.55F);
        }
        if (this.phaseTime >= 24 && this.phaseTime <= 104 && this.phaseTime % 12 == 0) {
            this.playSound(SoundEvents.BEACON_AMBIENT, 0.9F, 0.65F);
        }
        if (this.phaseTime >= 112) this.enterPhase(PHASE_IDLE);
    }

    private void tickTennis(LivingEntity target) {
        if (this.phaseTime == 1) {
            this.entityData.set(ACTION, ACTION_CHARGE);
            this.playSound(SoundEvents.END_PORTAL_SPAWN, 1.4F, 1.45F);
        }
        if (this.phaseTime % 10 == 0 && this.phaseTime < 65) this.burst(ParticleTypes.REVERSE_PORTAL, 12, 1.2D);
        if (this.phaseTime == 50) this.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.4F, 0.65F);
        if (this.phaseTime == 68) {
            this.entityData.set(ACTION, ACTION_SHOOT_ORB);
            this.activeOrb = new EnderCalamityOrbEntity(this.level(), this);
            this.activeOrb.setPos(this.getX(), this.getY() + this.getBbHeight() * 0.65D, this.getZ());
            Vec3 aim = target.getEyePosition().subtract(this.activeOrb.position());
            this.activeOrb.shoot(aim.x, aim.y, aim.z, 0.48F, 0.0F);
            this.level().addFreshEntity(this.activeOrb);
            this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 1.4F, 1.35F);
        }
        if (this.phaseTime > 170 || (this.phaseTime > 75 && (this.activeOrb == null || !this.activeOrb.isAlive()))) {
            this.enterPhase(PHASE_IDLE);
        }
    }

    private void tickStunned() {
        if (this.phaseTime == 1) {
            this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.5F, 0.65F);
            this.burst(ParticleTypes.END_ROD, 60, 2.0D);
        }
        if (this.phaseTime >= 90) this.enterPhase(PHASE_IDLE);
    }

    private void enterPhase(int phase) {
        if (this.activeLaser != null) {
            this.activeLaser.discard();
            this.activeLaser = null;
        }
        this.entityData.set(PHASE, phase);
        this.entityData.set(ACTION, phase == PHASE_STUNNED ? ACTION_STUN : ACTION_BODY);
        this.phaseTime = 0;
        if (phase != PHASE_TENNIS && this.activeOrb != null) {
            this.activeOrb.discard();
            this.activeOrb = null;
        }
    }

    private void teleportAboveTarget(LivingEntity target, double height) {
        double oldX = this.getX();
        double oldY = this.getY() + this.getBbHeight() * 0.5D;
        double oldZ = this.getZ();
        for (int attempt = 0; attempt < 10; attempt++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            double radius = 5.0D + this.random.nextDouble() * 5.0D;
            double x = target.getX() + Math.cos(angle) * radius;
            double y = target.getY() + height + this.random.nextDouble() * 3.0D;
            double z = target.getZ() + Math.sin(angle) * radius;
            // so validate free air and move directly to it without the ground-snapping pass.
            if (this.tryTeleportToOpenAir(x, y, z)) {
                this.setNoGravity(true);
                this.setDeltaMovement(Vec3.ZERO);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.4F, 0.55F);
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, oldX, oldY, oldZ,
                            50, 1.0D, 1.5D, 1.0D, 0.18D);
                    serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY() + 2.0D, this.getZ(),
                            50, 1.0D, 1.5D, 1.0D, 0.18D);
                }
                return;
            }
        }
    }

    private boolean tryTeleportToOpenAir(double x, double y, double z) {
        BlockPos destinationPos = BlockPos.containing(x, y, z);
        AABB destination = this.getBoundingBox().move(x - this.getX(), y - this.getY(), z - this.getZ());
        if (!this.level().hasChunkAt(destinationPos) || !this.level().noCollision(this, destination)) return false;
        this.teleportTo(x, y, z);
        this.setNoGravity(true);
        this.fallDistance = 0.0F;
        this.setDeltaMovement(Vec3.ZERO);
        return true;
    }

    @Override
    public void die(DamageSource source) {
        if (this.activeLaser != null) this.activeLaser.discard();
        super.die(source);
    }

    public void onEnergyBallReturned() {
        if (!this.level().isClientSide && this.entityData.get(PHASE) == PHASE_TENNIS) {
            this.enterPhase(PHASE_STUNNED);
        }
    }

    public void onEnergyBallMissed() {
        if (!this.level().isClientSide && this.entityData.get(PHASE) == PHASE_TENNIS) {
            this.activeOrb = null;
        }
    }

    private void burst(net.minecraft.core.particles.SimpleParticleType particle, int count, double spread) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particle, this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
                    count, spread, spread * 0.65D, spread, 0.06D);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && source.getDirectEntity() instanceof Projectile
                && !(source.getDirectEntity() instanceof EnderCalamityOrbEntity)
                && this.entityData.get(PHASE) == PHASE_IDLE && this.projectileDodgeCooldown <= 0
                && this.random.nextFloat() < 0.35F) {
            for (int attempt = 0; attempt < 6; attempt++) {
                if (this.tryTeleportToOpenAir(this.getX() + (this.random.nextDouble() - 0.5D) * 14.0D,
                        this.getY() + this.random.nextInt(7) - 3,
                        this.getZ() + (this.random.nextDouble() - 0.5D) * 14.0D)) {
                    this.projectileDodgeCooldown = 60;
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 0.75F);
                    return false;
                }
            }
        }
        if (this.entityData.get(PHASE) == PHASE_STUNNED) amount *= 1.4F;
        return super.hurt(source, amount);
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
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.TELEPORT_STONE.get());
        this.spawnAtLocation(ModItems.CAPE_ENDERMAN.get());
        this.spawnAtLocation(ModItems.BANNER_END.get());
        this.spawnAtLocation(new ItemStack(Items.ECHO_SHARD, 2 + this.random.nextInt(3)));

        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.DARK_STAFF.get());
        }

        this.spawnAtLocation(new ItemStack(ModItems.DARK_ARROW.get(), 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(ModItems.DARK_POTION.get(), 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.ENDER_PEARL, 6 + this.random.nextInt(7)));
        this.spawnAtLocation(new ItemStack(Items.ENDER_EYE, 2 + this.random.nextInt(3)));
    }
}
