package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public abstract class AbstractEntityCQRMageBase extends AbstractEntityCQR implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public static final EntityDataAccessor<Boolean> IS_CASTING_SPELL = SynchedEntityData.defineId(AbstractEntityCQRMageBase.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_SHIELD_ACTIVE = SynchedEntityData.defineId(AbstractEntityCQRMageBase.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_REVEALED = SynchedEntityData.defineId(AbstractEntityCQRMageBase.class, EntityDataSerializers.BOOLEAN);
    
    public int spellCastTicks = 0;
    
    // Shield mechanics
    private int hitsTaken = 0;
    private int shieldDurationTicks = 0;
    
    protected final ServerBossEvent bossEvent;

    public AbstractEntityCQRMageBase(EntityType<? extends AbstractEntityCQR> entityType, Level level) {
        super(entityType, level);
        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(net.minecraft.network.chat.Component.literal("???"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10)).setDarkenScreen(false);
    }
    
    protected BossEvent.BossBarColor getBossBarColor() {
        return BossEvent.BossBarColor.PURPLE;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CASTING_SPELL, false);
        builder.define(IS_SHIELD_ACTIVE, false);
        builder.define(IS_REVEALED, false);
    }

    @Override
    public com.example.chocolatequest.faction.EDefaultFaction getDefaultFaction() {
        return com.example.chocolatequest.faction.EDefaultFaction.UNDEAD;
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, net.minecraft.world.DifficultyInstance difficulty) {
        // Do not call super to avoid random items
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DARK_STAFF.get()));
    }

    @Override
    protected void equipBasedOnTier(com.example.chocolatequest.entity.mob.enums.MobTier tier) {
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new net.minecraft.world.item.ItemStack(com.example.chocolatequest.registry.ModItems.DARK_STAFF.get()));
        
        // Ensure all other slots are empty
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.LEGS, net.minecraft.world.item.ItemStack.EMPTY);
        this.setItemSlot(net.minecraft.world.entity.EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public double getBaseHealth() {
        return 180.0D;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 180.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (this.level().isClientSide) {
            // Ambient magic particles
            if (this.tickCount % 3 == 0) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.WITCH, 
                    this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 
                    0.0D, 0.0D, 0.0D);
            }
            // More particles when shield is active
            if (this.entityData.get(IS_SHIELD_ACTIVE)) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, 
                    this.getRandomX(0.8D), this.getY() + this.getRandom().nextDouble() * 2.0D, this.getRandomZ(0.8D), 
                    0.0D, 0.05D, 0.0D);
            }
            // Particles when casting spell
            if (this.entityData.get(IS_CASTING_SPELL)) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.ENCHANT, 
                    this.getRandomX(1.0D), this.getY() + 1.0D + this.getRandom().nextDouble(), this.getRandomZ(1.0D), 
                    (this.getRandom().nextDouble() - 0.5D) * 0.5D, -this.getRandom().nextDouble(), (this.getRandom().nextDouble() - 0.5D) * 0.5D);
            }
            // Special particles when revealed (e.g. they took enough damage)
            if (this.entityData.get(IS_REVEALED)) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, 
                    this.getRandomX(0.5D), this.getY(), this.getRandomZ(0.5D), 
                    0.0D, 0.05D, 0.0D);
            }
        }
        
        if (!this.level().isClientSide) {
            if (this.spellCastTicks > 0) {
                this.spellCastTicks--;
            }
            if (this.spellCastTicks <= 0 && this.entityData.get(IS_CASTING_SPELL)) {
                this.entityData.set(IS_CASTING_SPELL, false);
            }

            if (this.shieldDurationTicks > 0) {
                this.shieldDurationTicks--;
                if (this.shieldDurationTicks <= 0 && this.entityData.get(IS_SHIELD_ACTIVE)) {
                    this.entityData.set(IS_SHIELD_ACTIVE, false);
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        
        if (this.entityData.get(IS_REVEALED)) {
            this.bossEvent.setName(this.getDisplayName());
            this.bossEvent.setColor(this.getBossBarColor());
            this.bossEvent.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
        } else {
            this.bossEvent.setName(net.minecraft.network.chat.Component.literal("???"));
            this.bossEvent.setColor(BossEvent.BossBarColor.RED);
            this.bossEvent.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
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

    public void startCastingSpell(int ticks) {
        this.entityData.set(IS_CASTING_SPELL, true);
        this.spellCastTicks = ticks;
        this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean shieldWasActive = this.entityData.get(IS_SHIELD_ACTIVE);
        if (shieldWasActive) {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.2F);
            // Spawn some cool particles on server side to indicate shield hit
            if (!this.level().isClientSide) {
                net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) this.level();
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, 
                    this.getX(), this.getY() + 1.0D, this.getZ(), 
                    10, 0.5D, 0.5D, 0.5D, 0.1D);
            }
            float multiplier = this.getShieldDamageMultiplier();
            if (multiplier <= 0.0F) return false;
            amount *= multiplier;
        }

        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide) {
            // Check for reveal
            if (!this.entityData.get(IS_REVEALED) && this.getHealth() <= this.getMaxHealth() / 2.0F) {
                this.entityData.set(IS_REVEALED, true);
                this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) this.level();
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, 
                    this.getX(), this.getY() + 1.0D, this.getZ(), 
                    50, 0.5D, 1.0D, 0.5D, 0.05D);
            }

            if (!shieldWasActive) this.hitsTaken++;
            if (this.hitsTaken >= this.getShieldHitThreshold()) {
                this.hitsTaken = 0;
                this.shieldDurationTicks = this.getShieldDurationTicks();
                this.entityData.set(IS_SHIELD_ACTIVE, true);
                this.playSound(SoundEvents.WITHER_SPAWN, 0.5F, 2.0F); // Cool activation sound
            }
        }
        return hurt;
    }

    protected int getShieldHitThreshold() {
        return 5;
    }

    protected int getShieldDurationTicks() {
        return 100;
    }

    protected float getShieldDamageMultiplier() {
        return 0.0F;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "mage_spell_controller", 5, this::predicateMageSpell));
        controllers.add(new AnimationController<>(this, "mage_shield_controller", 5, this::predicateMageShield));
    }

    protected <E extends GeoEntity> software.bernie.geckolib.animation.PlayState predicateMageSpell(AnimationState<E> event) {
        if (this.entityData.get(IS_CASTING_SPELL)) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.biped.arms.cast-spell"));
            return software.bernie.geckolib.animation.PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        return software.bernie.geckolib.animation.PlayState.STOP;
    }

    protected <E extends GeoEntity> software.bernie.geckolib.animation.PlayState predicateMageShield(AnimationState<E> event) {
        if (this.entityData.get(IS_SHIELD_ACTIVE)) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.bipednecromancer.boneshield.loop"));
            return software.bernie.geckolib.animation.PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        return software.bernie.geckolib.animation.PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

