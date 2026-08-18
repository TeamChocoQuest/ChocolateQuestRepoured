package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.goal.boss.ShelobHookGoal;
import com.example.chocolatequest.entity.ai.goal.boss.ShelobLeapGoal;
import com.example.chocolatequest.entity.ai.goal.boss.ShelobSummonGoal;
import com.example.chocolatequest.entity.ai.goal.boss.ShelobWebshotGoal;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.AnimatableManager;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ShelobEntity extends AbstractEntityCQR {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final List<Entity> activeEggs = new ArrayList<>();
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10)).setDarkenScreen(true);

    public ShelobEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createCQRAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.MAX_HEALTH, 360.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D); // Boss health
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ShelobSummonGoal(this));
        this.goalSelector.addGoal(2, new ShelobWebshotGoal(this));
        this.goalSelector.addGoal(3, new ShelobHookGoal(this));
        this.goalSelector.addGoal(4, new ShelobLeapGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.5D, false) {
            @Override
            protected void checkAndPerformAttack(LivingEntity target) {
                if (this.canPerformAttack(target)) {
                    this.resetAttackCooldown();
                    this.mob.swing(net.minecraft.world.InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                    if (this.mob instanceof ShelobEntity shelob) {
                        boolean isMoving = shelob.getDeltaMovement().horizontalDistanceSqr() > 0.001 || !shelob.getNavigation().isDone();
                        if (isMoving) {
                            shelob.triggerAnim("action_controller", "walk_attack");
                        } else {
                            shelob.triggerAnim("action_controller", "attack");
                        }
                    }
                }
            }
        });

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean result = super.doHurtTarget(entityIn);
        if (result && entityIn instanceof LivingEntity target) {
            int effectlvl = 0;
            if (this.getRandom().nextDouble() > 0.7) {
                effectlvl = 1;
                this.heal(Math.min(20.0F, target.getHealth() * 0.25F));
            }
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, effectlvl));
        }
        return result;
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
        // Unaffected by webs
        if (state.is(net.minecraft.world.level.block.Blocks.COBWEB) || state.is(com.example.chocolatequest.registry.ModBlocks.POISONOUS_WEB.get())) {
            return;
        }
        super.makeStuckInBlock(state, motionMultiplier);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.getEffect() == MobEffects.POISON || effect.getEffect() == MobEffects.WEAKNESS || effect.getEffect() == MobEffects.WITHER) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    public void addSummonedEntityToList(Entity summoned) {
        this.activeEggs.add(summoned);
    }

    public List<Entity> getSummonedEntities() {
        this.activeEggs.removeIf(e -> e == null || !e.isAlive());
        return this.activeEggs;
    }

    @Override
    public double getBaseHealth() {
        return 360.0D;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.BEASTS;
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        // Shelob has no equipment
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getAmbientSound() {
        return net.minecraft.sounds.SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getDefaultHurtSound(net.minecraft.world.damagesource.DamageSource damageSourceIn) {
        return net.minecraft.sounds.SoundEvents.SPIDER_HURT;
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getDeathSound() {
        return net.minecraft.sounds.SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(net.minecraft.core.BlockPos pos, BlockState blockIn) {
        this.playSound(net.minecraft.sounds.SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false; // No fall damage for Shelob
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
    public void die(DamageSource cause) {
        super.die(cause);
        this.triggerAnim("action_controller", "death");
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 40 && !this.level().isClientSide()) {
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        software.bernie.geckolib.animation.AnimationController<ShelobEntity> moveController = new software.bernie.geckolib.animation.AnimationController<>(this, "move_controller", 5, event -> {
            if (this.isDeadOrDying()) {
                return software.bernie.geckolib.animation.PlayState.STOP;
            }
            if (event.isMoving()) {
                event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop("animation.giant_spider.walk"));
                return software.bernie.geckolib.animation.PlayState.CONTINUE;
            }
            return software.bernie.geckolib.animation.PlayState.STOP;
        });

        software.bernie.geckolib.animation.AnimationController<ShelobEntity> actionController = new software.bernie.geckolib.animation.AnimationController<>(this, "action_controller", 5, event -> {
            return software.bernie.geckolib.animation.PlayState.CONTINUE;
        });

        // Register triggerable animations
        actionController.triggerableAnim("shoot", software.bernie.geckolib.animation.RawAnimation.begin().thenPlay("animation.giant_spider.shoot"));
        actionController.triggerableAnim("walk_shoot", software.bernie.geckolib.animation.RawAnimation.begin().thenPlay("animation.giant_spider.walk_shoot"));
        actionController.triggerableAnim("attack", software.bernie.geckolib.animation.RawAnimation.begin().thenPlay("animation.giant_spider.attack"));
        actionController.triggerableAnim("walk_attack", software.bernie.geckolib.animation.RawAnimation.begin().thenPlay("animation.giant_spider.walk_attack"));
        actionController.triggerableAnim("jump", software.bernie.geckolib.animation.RawAnimation.begin().thenPlay("animation.giant_spider.jump"));
        actionController.triggerableAnim("death", software.bernie.geckolib.animation.RawAnimation.begin().thenPlayAndHold("animation.giant_spider.death"));

        controllers.add(moveController);
        controllers.add(actionController);
    }

    @Override
    public boolean canUsePotion() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.SPIDER_HOOK.get(), 1 + this.random.nextInt(2)));
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.SPIDER_SWORD.get());
        }
        if (this.random.nextFloat() < 0.5F) {
            this.spawnAtLocation(ModItems.SHIELD_SPIDER.get());
        }
        this.spawnAtLocation(new ItemStack(ModItems.SPIDER_LEATHER.get(), 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.SPIDER_EYE, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.FERMENTED_SPIDER_EYE, 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.STRING, 8 + this.random.nextInt(9)));
        this.spawnAtLocation(new ItemStack(Items.COBWEB, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(ModItems.DARK_POTION.get(), 2 + this.random.nextInt(3)));
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, 2 + this.random.nextInt(3)));
    }
}

