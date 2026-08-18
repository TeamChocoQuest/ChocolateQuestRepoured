package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.mob.CQMandrilEntity;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.EnumSet;
import java.util.List;

public class EntityCQRMonking extends CQMandrilEntity {
    protected final ServerBossEvent bossEvent;

    public EntityCQRMonking(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_10);
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createCQRAttributes()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 912.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 7.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public double getBaseHealth() {
        return 912.0D;
    }

    @Override
    public void startSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MonkingWhirlwindGoal(this));
        this.goalSelector.addGoal(2, new MonkingJumpAttackGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() == this) {
            return false; // Immune to own explosion
        }
        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            if (player.getY() >= this.getY() + 3.0F) {
                amount += 5.0F; // Headshot
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount % 100 == 0) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 0, false, false));
        }
    }

    class MonkingJumpAttackGoal extends Goal {
        private final EntityCQRMonking monking;
        private int jumpCooldown = 20;
        private int timer;
        private boolean isJumping = false;
        private boolean leftGround = false;

        public MonkingJumpAttackGoal(EntityCQRMonking monking) {
            this.monking = monking;
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (this.monking.getTarget() == null) return false;
            if (this.jumpCooldown > 0) {
                this.jumpCooldown--;
                return false;
            }
            double distance = this.monking.distanceToSqr(this.monking.getTarget());
            return distance >= 25.0D && distance <= 400.0D;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.isJumping = false;
            this.leftGround = false;
            this.monking.getNavigation().stop();
            this.monking.playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.25F);
        }

        @Override
        public void tick() {
            LivingEntity target = this.monking.getTarget();
            if (target == null) return;
            this.monking.getLookControl().setLookAt(target, 30.0F, 30.0F);

            this.timer++;
            if (this.timer < 15) {
                if (this.monking.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CLOUD, this.monking.getX(), this.monking.getY() + 0.2D,
                            this.monking.getZ(), 3, 0.7D, 0.05D, 0.7D, 0.02D);
                }
            } else if (!this.isJumping) {
                if (this.monking.onGround()) {
                    Vec3 targetPos = target.position();
                    Vec3 direction = new Vec3(targetPos.x - this.monking.getX(), 0.0D,
                            targetPos.z - this.monking.getZ()).normalize();
                    double speed = Math.min(1.35D, 0.75D + Math.sqrt(this.monking.distanceToSqr(target)) * 0.045D);
                    this.monking.setDeltaMovement(direction.x * speed, 1.05D, direction.z * speed);
                    this.monking.setOnGround(false);
                    this.monking.hasImpulse = true;
                    this.isJumping = true;
                    this.leftGround = false;
                    this.monking.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.3F, 0.65F);
                    if (this.monking.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, this.monking.getX(), this.monking.getY() + 0.2D,
                                this.monking.getZ(), 18, 0.8D, 0.12D, 0.8D, 0.09D);
                    }
                }
            } else {
                if (!this.monking.onGround()) this.leftGround = true;
                if (this.leftGround && this.monking.onGround() && this.monking.getDeltaMovement().y <= 0) {
                    this.slam();
                    this.isJumping = false;
                    this.leftGround = false;
                    this.timer = 80;
                }
            }
        }

        private void slam() {
            if (this.monking.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.POOF, this.monking.getX(), this.monking.getY() + 0.2D,
                        this.monking.getZ(), 28, 1.5D, 0.15D, 1.5D, 0.08D);
            }
            this.monking.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.2F, 0.8F);
            List<LivingEntity> victims = this.monking.level().getEntitiesOfClass(LivingEntity.class,
                    this.monking.getBoundingBox().inflate(3.5D), entity -> entity.isAlive()
                            && entity != this.monking && !TargetUtil.isAllyCheckingLeaders(this.monking, entity));
            for (LivingEntity victim : victims) {
                victim.hurt(this.monking.damageSources().mobAttack(this.monking), 8.0F);
                Vec3 push = victim.position().subtract(this.monking.position()).normalize();
                victim.push(push.x * 1.1D, 0.55D, push.z * 1.1D);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 100 && this.monking.getTarget() != null;
        }

        @Override
        public void stop() {
            this.jumpCooldown = 120;
            this.isJumping = false;
            this.leftGround = false;
        }
    }

    class MonkingWhirlwindGoal extends Goal {
        private final EntityCQRMonking monking;
        private int cooldown = 110;
        private int timer;

        MonkingWhirlwindGoal(EntityCQRMonking monking) {
            this.monking = monking;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            LivingEntity target = this.monking.getTarget();
            return target != null && target.isAlive() && this.monking.distanceToSqr(target) <= 100.0D;
        }

        @Override
        public void start() {
            this.timer = 0;
            this.monking.getNavigation().stop();
            this.monking.setSpinToWin(true);
            this.monking.playSound(SoundEvents.RAVAGER_ROAR, 1.1F, 0.75F);
        }

        @Override
        public boolean canContinueToUse() {
            return this.timer < 48 && this.monking.getTarget() != null;
        }

        @Override
        public void tick() {
            this.timer++;
            LivingEntity target = this.monking.getTarget();
            if (target == null) return;
            if (this.timer < 14) {
                this.monking.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (this.monking.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.monking.getX(), this.monking.getY() + 1.2D,
                            this.monking.getZ(), 2, 1.0D, 0.4D, 1.0D, 0.0D);
                }
                return;
            }
            this.monking.getNavigation().moveTo(target, 1.05D);
            if (this.timer == 18 || this.timer == 29 || this.timer == 40) {
                for (LivingEntity victim : this.monking.level().getEntitiesOfClass(LivingEntity.class,
                        this.monking.getBoundingBox().inflate(3.0D), entity -> entity.isAlive()
                                && entity != this.monking && !TargetUtil.isAllyCheckingLeaders(this.monking, entity))) {
                    victim.hurt(this.monking.damageSources().mobAttack(this.monking), 4.0F);
                    Vec3 push = victim.position().subtract(this.monking.position()).normalize();
                    victim.push(push.x * 0.65D, 0.2D, push.z * 0.65D);
                }
                this.monking.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.75F);
            }
        }

        @Override
        public void stop() {
            this.monking.setSpinToWin(false);
            this.cooldown = 170;
        }
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.MONKING_GREAT_SWORD.get()));
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND,
                new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.SHIELD_MONKING.get()));
    }

    @Override
    public boolean canUsePotion() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        if (this.random.nextFloat() < 0.6F) {
            this.spawnAtLocation(ModItems.MONKING_GREAT_SWORD.get());
        }
        if (this.random.nextFloat() < 0.6F) {
            this.spawnAtLocation(ModItems.SHIELD_MONKING.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            if (this.random.nextBoolean()) {
                this.spawnAtLocation(ModItems.MONKING_DAGGER.get());
            } else {
                this.spawnAtLocation(ModItems.MUSKET_DAGGER_MONKING.get());
            }
        }
        this.spawnAtLocation(new ItemStack(ModItems.GOLDEN_FEATHER.get(), 1 + this.random.nextInt(2)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_BLOCK, 1 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.GOLDEN_APPLE, 2 + this.random.nextInt(3)));
        if (this.random.nextFloat() < 0.25F) {
            this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE);
        }
    }
}
