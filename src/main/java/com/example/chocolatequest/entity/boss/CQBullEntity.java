package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.ai.boss.BossBattleCryGoal;
import com.example.chocolatequest.entity.ai.goal.boss.BullChargeGoal;
import com.example.chocolatequest.entity.ai.goal.boss.BullEarthFuryGoal;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CQBullEntity extends AbstractEntityCQR {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final ServerBossEvent bossEvent;

    public CQBullEntity(EntityType<? extends AbstractEntityCQR> type, Level level) {
        super(type, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), this.getBossBarColor(), BossEvent.BossBarOverlay.NOTCHED_10);
        this.bossEvent.setDarkenScreen(true);
    }

    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.WHITE;
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

    public boolean isEnraged() {
        return this.getHealth() <= this.getMaxHealth() * 0.3F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive() && this.level().isClientSide && this.isEnraged()) {
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), 0.0D, 0.05D, 0.0D);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        
        if (this.getTarget() != null) {
            double speed = this.isEnraged() ? 0.45D : 0.3D;
            this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(speed);
            this.entityData.set(HAS_TARGET, true);
        } else {
            this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
            this.entityData.set(HAS_TARGET, false);
        }

        // Apply enrage damage boost
        double damage = this.isEnraged() ? 7.5D : 6.0D;
        this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue(damage);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractEntityCQR.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 360.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public double getBaseHealth() {
        return 360.0D;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BossBattleCryGoal<>(this));
        this.goalSelector.addGoal(1, new BullEarthFuryGoal(this));
        this.goalSelector.addGoal(2, new BullChargeGoal(this, this.canBreakBlocks()));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected boolean canBreakBlocks() {
        return false;
    }

    @Override
    public EDefaultFaction getDefaultFaction() {
        return EDefaultFaction.BEASTS;
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        // Bulls have no equipment
    }

    @Override
    public int getTextureCount() {
        return 1;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "move_controller", 5, state -> {
            if (this.isDeadOrDying()) {
                return state.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bull.death"));
            }
            if (state.isMoving()) {
                if (this.entityData.get(HAS_TARGET)) {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.bull.run"));
                }
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.bull.walk"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.bull.idle"));
        }));

        AnimationController<CQBullEntity> actionController = new AnimationController<>(this, "action_controller", 2, state -> software.bernie.geckolib.animation.PlayState.STOP);
        actionController.triggerableAnim("charge_prepare", RawAnimation.begin().thenPlay("animation.bull.charge_prepare"));
        actionController.triggerableAnim("unstoppable_charge", RawAnimation.begin().thenLoop("animation.bull.unstoppable_charge"));
        actionController.triggerableAnim("earth_fury", RawAnimation.begin().thenPlay("animation.bull.earth_fury"));
        actionController.triggerableAnim("charge_attack", RawAnimation.begin().thenPlay("animation.bull.charge_attack"));
        actionController.triggerableAnim("battle_cry", RawAnimation.begin().thenPlay("animation.bull.battle_cry"));
        actionController.triggerableAnim("stop_action", RawAnimation.begin());
        controllers.add(actionController);
    }

    private boolean wantsToComboEarthFury;

    public void setWantsToComboEarthFury(boolean wantsToComboEarthFury) {
        this.wantsToComboEarthFury = wantsToComboEarthFury;
    }

    public boolean wantsToComboEarthFury() {
        return this.wantsToComboEarthFury;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity entityIn) {
        this.triggerAnim("action_controller", "charge_attack");
        return super.doHurtTarget(entityIn);
    }

    @Override
    public boolean canUsePotion() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.BULL_HORN.get(), 2));
        this.spawnAtLocation(new ItemStack(ModItems.BULL_LEATHER.get(), 3 + this.random.nextInt(4)));

        float weaponRoll = this.random.nextFloat();
        if (weaponRoll < 0.4F) {
            this.spawnAtLocation(ModItems.BULL_BATTLE_AXE.get());
        } else if (weaponRoll < 0.8F) {
            this.spawnAtLocation(ModItems.BULL_GREAT_SWORD.get());
        } else {
            this.spawnAtLocation(ModItems.SHIELD_BULL.get());
        }

        if (this.random.nextFloat() < 0.5F) {
            int armorRoll = this.random.nextInt(4);
            if (armorRoll == 0) this.spawnAtLocation(ModItems.BULL_HELMET.get());
            else if (armorRoll == 1) this.spawnAtLocation(ModItems.BULL_CHESTPLATE.get());
            else if (armorRoll == 2) this.spawnAtLocation(ModItems.BULL_LEGGINGS.get());
            else this.spawnAtLocation(ModItems.BULL_BOOTS.get());
        }

        this.spawnAtLocation(new ItemStack(Items.BEEF, 4 + this.random.nextInt(5)));
        this.spawnAtLocation(new ItemStack(Items.LEATHER, 4 + this.random.nextInt(5)));
    }
}

