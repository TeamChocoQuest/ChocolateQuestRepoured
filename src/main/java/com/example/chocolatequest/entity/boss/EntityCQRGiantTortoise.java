package com.example.chocolatequest.entity.boss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.projectile.BubbleProjectileEntity;
import java.util.EnumSet;
import java.util.List;
import com.example.chocolatequest.entity.ai.target.TargetUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQRGiantTortoise extends Monster implements GeoEntity {
    private static final int ACTION_NORMAL = 0;
    private static final int ACTION_ENTER_SHELL = 1;
    private static final int ACTION_IN_SHELL = 2;
    private static final int ACTION_EXIT_SHELL = 3;
    private static final int ACTION_SPIN = 4;
    private static final int ACTION_STUN = 5;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10)).setDarkenScreen(true);

    public EntityCQRGiantTortoise(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 480.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    private PlayState predicate(AnimationState<EntityCQRGiantTortoise> event) {
        String animation = switch (this.getAction()) {
            case ACTION_ENTER_SHELL -> "animation.giant_tortoise.enter_shell";
            case ACTION_IN_SHELL -> "animation.giant_tortoise.inShell";
            case ACTION_EXIT_SHELL -> "animation.giant_tortoise.exit_shell";
            case ACTION_SPIN -> "animation.giant_tortoise.spin";
            case ACTION_STUN -> "animation.giant_tortoise.stun";
            default -> event.isMoving() ? "animation.giant_tortoise.walk" : null;
        };
        if (animation == null) return PlayState.STOP;
        if (this.getAction() == ACTION_SPIN || this.getAction() == ACTION_IN_SHELL || this.getAction() == ACTION_NORMAL) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop(animation));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold(animation));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
    public void customServerAiStep() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep();
    }

    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(EntityCQRGiantTortoise.class, EntityDataSerializers.INT);
    private int idleCombatTicks;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, ACTION_NORMAL);
    }

    public boolean isSpinning() {
        return this.getAction() == ACTION_SPIN;
    }

    public void setSpinning(boolean spinning) {
        this.setAction(spinning ? ACTION_SPIN : ACTION_NORMAL);
    }

    private int getAction() {
        return this.entityData.get(ACTION);
    }

    private void setAction(int action) {
        this.entityData.set(ACTION, action);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GiantTortoiseShellGuardGoal(this));
        this.goalSelector.addGoal(2, new GiantTortoiseSpinAttackGoal(this));
        this.goalSelector.addGoal(3, new GiantTortoiseBubbleAttackGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAction() == ACTION_ENTER_SHELL || this.getAction() == ACTION_IN_SHELL
                || this.getAction() == ACTION_EXIT_SHELL) {
            amount *= 0.3F;
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.6F);
        } else if (this.getAction() == ACTION_SPIN) {
            amount *= 0.55F;
        } else if (this.getAction() == ACTION_STUN) {
            amount *= 1.25F;
        }
        if (!this.level().isClientSide && source.getEntity() != null) {
            this.playSound(SoundEvents.BLAZE_HURT, 1.0F, 1.0F);
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || this.getAction() != ACTION_NORMAL) {
            this.idleCombatTicks = 0;
            return;
        }
        double distance = this.distanceToSqr(target);
        if (distance > 12.25D && this.getNavigation().isDone()) {
            this.idleCombatTicks++;
            if (this.idleCombatTicks >= 8) {
                this.getNavigation().moveTo(target, 1.08D);
                this.idleCombatTicks = 0;
            }
        } else {
            this.idleCombatTicks = 0;
        }
    }

    private void shootBubble(LivingEntity target, float speed, float inaccuracy) {
        BubbleProjectileEntity bubble = new BubbleProjectileEntity(this.level(), this);
        double dx = target.getX() - bubble.getX();
        double dy = target.getY() + target.getEyeHeight() * 0.55D - bubble.getY();
        double dz = target.getZ() - bubble.getZ();
        bubble.shoot(dx, dy + Math.sqrt(dx * dx + dz * dz) * 0.12D, dz, speed, inaccuracy);
        this.level().addFreshEntity(bubble);
        this.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 0.9F, 0.8F + this.random.nextFloat() * 0.2F);
    }

    class GiantTortoiseShellGuardGoal extends Goal {
        private final EntityCQRGiantTortoise tortoise;
        private int cooldown = 180;
        private int timer;

        GiantTortoiseShellGuardGoal(EntityCQRGiantTortoise tortoise) {
            this.tortoise = tortoise;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            LivingEntity target = this.tortoise.getTarget();
            return target != null && target.isAlive() && this.tortoise.getHealth() < this.tortoise.getMaxHealth() * 0.75F;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.tortoise.getNavigation().stop();
            this.tortoise.setAction(ACTION_ENTER_SHELL);
            this.tortoise.playSound(SoundEvents.TURTLE_EGG_CRACK, 1.4F, 0.55F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 100 && this.tortoise.getTarget() != null;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void tick() {
            this.timer++;
            this.tortoise.setDeltaMovement(0.0D, this.tortoise.getDeltaMovement().y, 0.0D);
            if (this.timer == 30) {
                this.tortoise.setAction(ACTION_IN_SHELL);
                this.tortoise.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 0.6F);
            }
            if (this.timer >= 30 && this.timer < 70 && this.timer % 10 == 0) this.tortoise.heal(1.0F);
            if (this.timer == 70) {
                this.tortoise.setAction(ACTION_EXIT_SHELL);
                this.releaseBubbleRing();
                this.tortoise.playSound(SoundEvents.GENERIC_SPLASH, 1.5F, 0.6F);
            }
        }

        private void releaseBubbleRing() {
            for (int i = 0; i < 6; i++) {
                BubbleProjectileEntity bubble = new BubbleProjectileEntity(this.tortoise.level(), this.tortoise);
                double angle = i * Math.PI / 3.0D;
                bubble.shoot(Math.cos(angle), 0.12D, Math.sin(angle), 0.75F, 0.0F);
                this.tortoise.level().addFreshEntity(bubble);
            }
        }

        @Override
        public void stop() {
            this.tortoise.setAction(ACTION_NORMAL);
            this.cooldown = 300;
        }
    }

    class GiantTortoiseSpinAttackGoal extends Goal {
        private final EntityCQRGiantTortoise tortoise;
        private int attackTimer = 0;
        private int cooldown = 70;

        public GiantTortoiseSpinAttackGoal(EntityCQRGiantTortoise tortoise) {
            this.tortoise = tortoise;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.tortoise.getTarget() == null) return false;
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            return this.tortoise.distanceToSqr(this.tortoise.getTarget()) < 64.0D;
        }

        @Override
        public void start() {
            this.attackTimer = 0;
            this.tortoise.getNavigation().stop();
            this.tortoise.setAction(ACTION_ENTER_SHELL);
            this.tortoise.playSound(SoundEvents.RAVAGER_ROAR, 1.2F, 0.6F);
        }

        @Override
        public void tick() {
            this.attackTimer++;
            LivingEntity target = this.tortoise.getTarget();
            if (target != null) {
                this.tortoise.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (this.attackTimer == 18) {
                this.tortoise.setAction(ACTION_SPIN);
                this.tortoise.playSound(SoundEvents.TRIDENT_RIPTIDE_3.value(), 1.4F, 0.7F);
            }
            if (this.attackTimer > 18 && this.attackTimer <= 55 && target != null) {
                this.tortoise.getNavigation().moveTo(target, 1.35D);
            }
            if (this.attackTimer == 28 || this.attackTimer == 40 || this.attackTimer == 52) {
                List<LivingEntity> nearby = this.tortoise.level().getEntitiesOfClass(LivingEntity.class,
                        this.tortoise.getBoundingBox().inflate(3.0D), entity -> entity.isAlive()
                                && entity != this.tortoise && !TargetUtil.isAllyCheckingLeaders(this.tortoise, entity));
                for (LivingEntity e : nearby) {
                    e.hurt(this.tortoise.damageSources().mobAttack(this.tortoise), 4.0F);
                    Vec3 push = e.position().subtract(this.tortoise.position()).normalize();
                    e.push(push.x * 0.8D, 0.25D, push.z * 0.8D);
                }
            }
            if ((this.attackTimer == 31 || this.attackTimer == 47) && target != null) {
                this.tortoise.shootBubble(target, 1.2F, 8.0F);
            }
            if (this.attackTimer == 56) {
                this.tortoise.getNavigation().stop();
                this.tortoise.setAction(ACTION_STUN);
                this.tortoise.playSound(SoundEvents.ANVIL_LAND, 1.2F, 0.65F);
                if (this.tortoise.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT, this.tortoise.getX(), this.tortoise.getY() + 1.5D,
                            this.tortoise.getZ(), 24, 1.1D, 0.6D, 1.1D, 0.08D);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackTimer < 96 && this.tortoise.getTarget() != null;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void stop() {
            this.cooldown = 180;
            this.tortoise.setAction(ACTION_NORMAL);
        }
    }

    class GiantTortoiseBubbleAttackGoal extends Goal {
        private final EntityCQRGiantTortoise tortoise;
        private int attackTimer = 0;
        private int cooldown = 85;

        public GiantTortoiseBubbleAttackGoal(EntityCQRGiantTortoise tortoise) {
            this.tortoise = tortoise;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.tortoise.getTarget() == null) return false;
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            return this.tortoise.distanceToSqr(this.tortoise.getTarget()) >= 25.0D;
        }

        @Override
        public void start() {
            this.attackTimer = 0;
            this.tortoise.getNavigation().stop();
            this.tortoise.setAction(ACTION_ENTER_SHELL);
            this.tortoise.playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 1.3F, 0.7F);
        }

        @Override
        public void tick() {
            this.attackTimer++;
            LivingEntity target = this.tortoise.getTarget();
            if (target == null) return;
            this.tortoise.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.attackTimer == 18) this.tortoise.setAction(ACTION_IN_SHELL);
            if (this.attackTimer == 24 || this.attackTimer == 33 || this.attackTimer == 42)
                this.tortoise.shootBubble(target, 1.35F, 5.0F);
            if (this.attackTimer == 45) this.tortoise.setAction(ACTION_EXIT_SHELL);
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackTimer < 65 && this.tortoise.getTarget() != null;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void stop() {
            this.cooldown = 130;
            this.tortoise.setAction(ACTION_NORMAL);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, net.minecraft.world.DifficultyInstance difficulty) {
        // Do not equip standard items
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.SCALE_TURTLE.get(), 4 + this.random.nextInt(5)));
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.TURTLE_SWORD.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.SHIELD_TURTLE.get());
        }

        // Random Turtle armor pieces
        int piecesCount = 1 + (this.random.nextFloat() < 0.5F ? 1 : 0);
        for (int i = 0; i < piecesCount; i++) {
            int piece = this.random.nextInt(4);
            if (piece == 0) this.spawnAtLocation(ModItems.TURTLE_HELMET.get());
            else if (piece == 1) this.spawnAtLocation(ModItems.TURTLE_CHESTPLATE.get());
            else if (piece == 2) this.spawnAtLocation(ModItems.TURTLE_LEGGINGS.get());
            else this.spawnAtLocation(ModItems.TURTLE_BOOTS.get());
        }

        this.spawnAtLocation(new ItemStack(ModItems.SCALE_TURTLE.get(), 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.TURTLE_SCUTE, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.PRISMARINE_SHARD, 6 + this.random.nextInt(7)));
    }
}
