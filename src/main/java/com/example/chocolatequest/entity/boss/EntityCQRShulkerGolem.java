package com.example.chocolatequest.entity.boss;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.EnumSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

public class EntityCQRShulkerGolem extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_10)
            .setDarkenScreen(true);

    public EntityCQRShulkerGolem(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 120;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 420.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ARMOR, 18.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GroundSmashGoal(this));
        this.goalSelector.addGoal(2, new LevitationPulseGoal(this));
        this.goalSelector.addGoal(3, new VoidEruptionGoal(this));
        this.goalSelector.addGoal(4, new ShulkerVolleyGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.05D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.75D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ShulkerBullet) return false;
        return super.hurt(source, amount);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "base_controller", 4, state -> {
            state.getController().setAnimation(state.isMoving()
                    ? RawAnimation.begin().thenLoop("animation.shulkergolem.walk")
                    : RawAnimation.begin().thenLoop("animation.shulkergolem.idle"));
            return software.bernie.geckolib.animation.PlayState.CONTINUE;
        }));
        controllers.add(new AnimationController<>(this, "action_controller", 0, state ->
                software.bernie.geckolib.animation.PlayState.STOP)
                .triggerableAnim("smash_prepare", RawAnimation.begin().thenPlay("animation.shulkergolem.ground_smash.prepare"))
                .triggerableAnim("smash", RawAnimation.begin().thenPlay("animation.shulkergolem.ground_smash.smash"))
                .triggerableAnim("stop_action", RawAnimation.begin()));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private static final class GroundSmashGoal extends Goal {
        private final EntityCQRShulkerGolem golem;
        private int timer;
        private int cooldown = 60;

        private GroundSmashGoal(EntityCQRShulkerGolem golem) {
            this.golem = golem;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown-- > 0) return false;
            LivingEntity target = this.golem.getTarget();
            return target != null && target.isAlive() && this.golem.distanceToSqr(target) <= 49.0D;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.golem.getNavigation().stop();
            this.golem.triggerAnim("action_controller", "smash_prepare");
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.golem.getTarget();
            if (target != null) this.golem.getLookControl().setLookAt(target, 30.0F, 20.0F);
            if (this.timer == 14) {
                this.golem.triggerAnim("action_controller", "smash");
                this.golem.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.4F, 0.65F);
                for (LivingEntity entity : this.golem.level().getEntitiesOfClass(LivingEntity.class,
                        this.golem.getBoundingBox().inflate(6.0D, 2.0D, 6.0D))) {
                    if (entity == this.golem || !this.golem.hasLineOfSight(entity)) continue;
                    entity.hurt(this.golem.damageSources().mobAttack(this.golem), 12.0F);
                    Vec3 push = entity.position().subtract(this.golem.position()).normalize().scale(1.3D);
                    entity.push(push.x, 0.65D, push.z);
                    entity.hurtMarked = true;
                }
                if (this.golem.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.POOF, this.golem.getX(), this.golem.getY() + 0.2D,
                            this.golem.getZ(), 70, 4.0D, 0.3D, 4.0D, 0.05D);
                    serverLevel.sendParticles(ParticleTypes.END_ROD, this.golem.getX(), this.golem.getY() + 0.5D,
                            this.golem.getZ(), 35, 3.0D, 0.4D, 3.0D, 0.08D);
                }
            }
            if (this.timer == 23) this.golem.triggerAnim("action_controller", "stop_action");
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 28;
        }

        @Override
        public void stop() {
            this.cooldown = 100;
            this.golem.triggerAnim("action_controller", "stop_action");
        }
    }

    /** A close-range shulker shockwave that lifts players out of melee range. */
    private static final class LevitationPulseGoal extends Goal {
        private final EntityCQRShulkerGolem golem;
        private int timer;
        private int cooldown = 110;

        private LevitationPulseGoal(EntityCQRShulkerGolem golem) {
            this.golem = golem;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown-- > 0) return false;
            LivingEntity target = this.golem.getTarget();
            return target != null && target.isAlive() && this.golem.distanceToSqr(target) <= 144.0D;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.golem.getNavigation().stop();
            this.golem.triggerAnim("action_controller", "smash_prepare");
            this.golem.playSound(SoundEvents.SHULKER_OPEN, 1.25F, 0.65F);
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.golem.getTarget();
            if (target != null) this.golem.getLookControl().setLookAt(target, 30.0F, 25.0F);

            if (this.golem.level() instanceof ServerLevel serverLevel && this.timer < 20) {
                double radius = 1.5D + this.timer * 0.28D;
                for (int i = 0; i < 10; i++) {
                    double angle = (Math.PI * 2.0D * i / 10.0D) + this.timer * 0.17D;
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            this.golem.getX() + Math.cos(angle) * radius,
                            this.golem.getY() + 0.25D,
                            this.golem.getZ() + Math.sin(angle) * radius,
                            1, 0.05D, 0.05D, 0.05D, 0.0D);
                }
            }

            if (this.timer == 20) {
                this.golem.triggerAnim("action_controller", "smash");
                this.golem.playSound(SoundEvents.SHULKER_TELEPORT, 1.6F, 0.55F);
                for (LivingEntity entity : this.golem.level().getEntitiesOfClass(LivingEntity.class,
                        this.golem.getBoundingBox().inflate(9.0D, 4.0D, 9.0D))) {
                    if (entity == this.golem || !this.golem.hasLineOfSight(entity)) continue;
                    entity.hurt(this.golem.damageSources().magic(), 7.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 55, 1), this.golem);
                    Vec3 push = entity.position().subtract(this.golem.position()).normalize().scale(0.85D);
                    entity.push(push.x, 0.25D, push.z);
                    entity.hurtMarked = true;
                }
                if (this.golem.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, this.golem.getX(), this.golem.getY() + 1.0D,
                            this.golem.getZ(), 90, 5.5D, 1.0D, 5.5D, 0.12D);
                }
            }
            if (this.timer == 30) this.golem.triggerAnim("action_controller", "stop_action");
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 34;
        }

        @Override
        public void stop() {
            this.cooldown = 150;
            this.golem.triggerAnim("action_controller", "stop_action");
        }
    }

    /** Marks the player's position and erupts a delayed column of End energy. */
    private static final class VoidEruptionGoal extends Goal {
        private final EntityCQRShulkerGolem golem;
        private int timer;
        private int cooldown = 90;
        private Vec3 strikePos = Vec3.ZERO;

        private VoidEruptionGoal(EntityCQRShulkerGolem golem) {
            this.golem = golem;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown-- > 0) return false;
            LivingEntity target = this.golem.getTarget();
            double distance = target == null ? 0.0D : this.golem.distanceToSqr(target);
            return target != null && target.isAlive() && distance >= 36.0D && distance <= 400.0D
                    && this.golem.getHealth() <= this.golem.getMaxHealth() * 0.75F;
        }

        @Override
        public void start() {
            this.timer = 0;
            LivingEntity target = this.golem.getTarget();
            this.strikePos = target == null ? this.golem.position() : target.position();
            this.golem.getNavigation().stop();
            this.golem.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.15F, 1.45F);
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.golem.getTarget();
            if (target != null) this.golem.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.golem.level() instanceof ServerLevel serverLevel && this.timer < 28) {
                double radius = Math.max(0.35D, 3.5D - this.timer * 0.11D);
                serverLevel.sendParticles(ParticleTypes.PORTAL, this.strikePos.x, this.strikePos.y + 0.15D,
                        this.strikePos.z, 8, radius, 0.05D, radius, 0.02D);
            }

            if (this.timer == 28) {
                this.golem.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.6F, 0.45F);
                net.minecraft.world.phys.AABB area = new net.minecraft.world.phys.AABB(
                        this.strikePos.x - 3.5D, this.strikePos.y - 2.0D, this.strikePos.z - 3.5D,
                        this.strikePos.x + 3.5D, this.strikePos.y + 7.0D, this.strikePos.z + 3.5D);
                for (LivingEntity entity : this.golem.level().getEntitiesOfClass(LivingEntity.class, area)) {
                    if (entity == this.golem) continue;
                    entity.hurt(this.golem.damageSources().magic(), 11.0F);
                    entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 75, 2), this.golem);
                    entity.push(0.0D, 0.65D, 0.0D);
                    entity.hurtMarked = true;
                }
                if (this.golem.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, this.strikePos.x, this.strikePos.y + 2.0D,
                            this.strikePos.z, 110, 2.2D, 3.5D, 2.2D, 0.1D);
                    serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, this.strikePos.x, this.strikePos.y + 0.3D,
                            this.strikePos.z, 55, 2.8D, 0.25D, 2.8D, 0.04D);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 36;
        }

        @Override
        public void stop() {
            this.cooldown = 135;
        }
    }

    private static final class ShulkerVolleyGoal extends Goal {
        private final EntityCQRShulkerGolem golem;
        private int timer;
        private int cooldown = 40;

        private ShulkerVolleyGoal(EntityCQRShulkerGolem golem) {
            this.golem = golem;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown-- > 0) return false;
            LivingEntity target = this.golem.getTarget();
            return target != null && target.isAlive() && this.golem.distanceToSqr(target) > 36.0D;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.golem.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.golem.getTarget();
            if (target == null) return;
            this.golem.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.timer == 5 || this.timer == 15 || this.timer == 25) {
                ShulkerBullet bullet = new ShulkerBullet(this.golem.level(), this.golem, target, Direction.Axis.Y);
                this.golem.level().addFreshEntity(bullet);
                this.golem.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 0.75F);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 32 && this.golem.getTarget() != null;
        }

        @Override
        public void stop() {
            this.cooldown = 90;
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.NEXUS_CORE.get());
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.FORCE_FIELD_NEXUS.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.ELECTRIC_STAFF.get());
        }
        this.spawnAtLocation(new ItemStack(Items.AMETHYST_SHARD, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.SHULKER_SHELL, 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.PURPUR_BLOCK, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.POPPED_CHORUS_FRUIT, 6 + this.random.nextInt(7)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 2 + this.random.nextInt(4)));
    }
}
