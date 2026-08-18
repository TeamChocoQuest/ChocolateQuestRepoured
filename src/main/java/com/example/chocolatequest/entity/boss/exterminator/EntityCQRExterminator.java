package com.example.chocolatequest.entity.boss.exterminator;

import com.example.chocolatequest.entity.ai.boss.exterminator.BossAIArmCannon;
import com.example.chocolatequest.entity.ai.boss.exterminator.BossAIExterminatorHulkSmash;
import com.example.chocolatequest.entity.ai.boss.exterminator.BossAIExterminatorHandLaser;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.BossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.InstancedAnimatableInstanceCache;
import com.example.chocolatequest.ChocolateQuestReDone;

import com.example.chocolatequest.entity.ai.boss.exterminator.BossAIExterminatorStun;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;
import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.registry.ModEntities;
import com.example.chocolatequest.registry.ModItems;
import com.example.chocolatequest.registry.ModArmorMaterials;

import net.minecraft.util.Mth;

public class EntityCQRExterminator extends AbstractEntityCQR {
	// Entity parts
	// 0 => Backpack
	// 1 => Emitter left
	// 2 => Emitter right
	// 3 & 4 => Artificial hitbox (left and right), purpose is to avoid entities punching though the boss when it is in
	// non-stunned state
	private PartEntity<?>[] parts;

	private LivingEntity electroCuteTargetEmitterLeft;
	private LivingEntity electroCuteTargetEmitterRight;

	private int stunTime = 0;
	private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(),
			BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10).setDarkenScreen(false);

	protected static final EntityDataAccessor<Boolean> IS_STUNNED = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> ARMS_BLOCKED_BY_LONG_ANIMATION = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> PUNCH_IS_KICK = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> CANNON_RAISED = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);

	protected static final EntityDataAccessor<Boolean> EMITTER_LEFT_ACTIVE = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> EMITTER_RIGHT_ACTIVE = SynchedEntityData.<Boolean>defineId(EntityCQRExterminator.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> EMITTER_LEFT_TARGET_ID = SynchedEntityData.defineId(EntityCQRExterminator.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> EMITTER_RIGHT_TARGET_ID = SynchedEntityData.defineId(EntityCQRExterminator.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> LONG_ANIMATION = SynchedEntityData.defineId(EntityCQRExterminator.class, EntityDataSerializers.INT);
	private static final int LONG_ANIMATION_NONE = 0;
	private static final int LONG_ANIMATION_CANNON = 1;
	private static final int LONG_ANIMATION_THROW = 2;
	private static final int LONG_ANIMATION_SMASH = 3;

	// Geckolib
	private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
	private boolean partSoundFlag;

	public EntityCQRExterminator(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
		this.xpReward = 100;

		this.parts = new PartEntity[5];
		this.parts[0] = new SubEntityExterminatorBackpack(this, "exterminator_backpack", this::isAnyEmitterActive);
		this.parts[1] = new SubEntityExterminatorFieldEmitter(this, "emitter_left", this::getElectroCuteTargetLeft, this::isEmitterLeftActive, this::setEmitterLeftActive);
		this.parts[2] = new SubEntityExterminatorFieldEmitter(this, "emitter_right", this::getElectroCuteTargetRight, this::isEmitterRightActive, this::setEmitterRightActive);
		this.parts[3] = new SubEntityExterminatorHitboxPart(this, "main_hitbox_left", this.getBbWidth() / 3, this.getBbHeight());
		this.parts[4] = new SubEntityExterminatorHitboxPart(this, "main_hitbox_right", this.getBbWidth() / 3, this.getBbHeight());

	}

	protected boolean isAnyEmitterActive() {
		try {
			return EntityCQRExterminator.this.getEmitterLeft().isActive() || EntityCQRExterminator.this.getEmitterRight().isActive();
		} catch (NullPointerException npe) {
			return false;
		}
	}

	@Nullable
	public SubEntityExterminatorFieldEmitter getEmitterLeft() {
		return (SubEntityExterminatorFieldEmitter) this.parts[1];
	}

	@Nullable
	public SubEntityExterminatorFieldEmitter getEmitterRight() {
		return (SubEntityExterminatorFieldEmitter) this.parts[2];
	}

	protected boolean isEmitterLeftActive() {
		return this.entityData.get(EMITTER_LEFT_ACTIVE);
	}

	protected void setEmitterLeftActive(boolean value) {
		this.entityData.set(EMITTER_LEFT_ACTIVE, value);
	}

	protected boolean isEmitterRightActive() {
		return this.entityData.get(EMITTER_RIGHT_ACTIVE);
	}

	protected void setEmitterRightActive(boolean value) {
		this.entityData.set(EMITTER_RIGHT_ACTIVE, value);
	}

	@Override protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);

		builder.define(IS_STUNNED, false);
		builder.define(CANNON_RAISED, false);
		builder.define(PUNCH_IS_KICK, false);
		builder.define(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
		builder.define(EMITTER_LEFT_ACTIVE, false);
		builder.define(EMITTER_RIGHT_ACTIVE, false);
		builder.define(EMITTER_LEFT_TARGET_ID, -1);
		builder.define(EMITTER_RIGHT_TARGET_ID, -1);
		builder.define(LONG_ANIMATION, LONG_ANIMATION_NONE);
	}
	
	
	protected void applyAttributeValues() {
		

		
		
	}

	
	@Override
	public void push(Entity entityIn) {
		if (entityIn.getBbWidth() * entityIn.getBbWidth() * entityIn.getBbHeight() > this.getBbWidth() * this.getBbWidth() * this.getBbHeight()) {
			super.doPush(entityIn);
		}
	}
	public int globalAttackCooldown = 0;

	@Override
	protected void registerGoals() {
		
		this.goalSelector.addGoal(0, new BossAIExterminatorStun(this));
		this.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(this));
		this.goalSelector.addGoal(1, new BossAIExterminatorHulkSmash(this));
		
		this.goalSelector.addGoal(2, new com.example.chocolatequest.entity.ai.boss.exterminator.BossAINewExterminatorCannon(this));
		this.goalSelector.addGoal(3, new BossAIExterminatorHandLaser(this));
		
		this.goalSelector.addGoal(5, new com.example.chocolatequest.entity.ai.goal.MeleeAttackGoalCQR(this) {
			@Override
			public boolean canUse() {
				return super.canUse() && !EntityCQRExterminator.this.isStunned();
			}
		});
		
		this.goalSelector.addGoal(7, new net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(9, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(this));
		
		this.targetSelector.addGoal(0, new com.example.chocolatequest.entity.bases.AbstractEntityCQR.CQRHurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new com.example.chocolatequest.entity.ai.target.EntityAICQRNearestAttackTarget(this));
		this.targetSelector.addGoal(2, new com.example.chocolatequest.entity.ai.target.exterminator.EntityAITargetElectrocute(this, this::getElectroCuteTargetLeft, this::setElectroCuteTargetLeft));
		this.targetSelector.addGoal(3, new com.example.chocolatequest.entity.ai.target.exterminator.EntityAITargetElectrocute(this, this::getElectroCuteTargetRight, this::setElectroCuteTargetRight));
	}

	public LivingEntity getElectroCuteTargetLeft() {
		if (this.level().isClientSide()) {
			Entity target = this.level().getEntity(this.entityData.get(EMITTER_LEFT_TARGET_ID));
			return target instanceof LivingEntity living && living.isAlive() ? living : null;
		}
		return this.electroCuteTargetEmitterLeft;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.PathfinderMob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 250.0D)
				.add(Attributes.ATTACK_DAMAGE, 12.0D)
				.add(Attributes.ATTACK_SPEED, 3.2D)
				.add(Attributes.MOVEMENT_SPEED, 0.24D)
				.add(Attributes.FOLLOW_RANGE, 48.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.85D)
				.add(Attributes.STEP_HEIGHT, 1.0D);
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
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossEvent.setName(this.getDisplayName());
	}

	public void setElectroCuteTargetLeft(LivingEntity electroCuteTargetA) {
		this.electroCuteTargetEmitterLeft = electroCuteTargetA;

		if (!this.level().isClientSide()) {
			int targetId = electroCuteTargetA == null ? -1 : electroCuteTargetA.getId();
			if (this.entityData.get(EMITTER_LEFT_TARGET_ID) != targetId) {
				this.entityData.set(EMITTER_LEFT_TARGET_ID, targetId);
			}
		}
	}

	public LivingEntity getElectroCuteTargetRight() {
		if (this.level().isClientSide()) {
			Entity target = this.level().getEntity(this.entityData.get(EMITTER_RIGHT_TARGET_ID));
			return target instanceof LivingEntity living && living.isAlive() ? living : null;
		}
		return this.electroCuteTargetEmitterRight;
	}

	public void setElectroCuteTargetRight(LivingEntity electroCuteTargetB) {
		this.electroCuteTargetEmitterRight = electroCuteTargetB;

		if (!this.level().isClientSide()) {
			int targetId = electroCuteTargetB == null ? -1 : electroCuteTargetB.getId();
			if (this.entityData.get(EMITTER_RIGHT_TARGET_ID) != targetId) {
				this.entityData.set(EMITTER_RIGHT_TARGET_ID, targetId);
			}
		}
	}

	public void setStunned(boolean value, final int ticks) {
		if (!this.level().isClientSide() && value) {
			this.stunTime = Math.max(this.stunTime, ticks);
		}
		this.setStunned(value);
	}

	public void setStunned(boolean value) {
		if (this.isCannonRaised() && value) {
			this.switchCannonArmState(false);
		}
		this.entityData.set(IS_STUNNED, value);
	}
	
	@Override
	public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
		if (this.isStunned()) {
			this.stunTime = Math.min(240, this.stunTime + 50);
		} else {
			this.setStunned(true, 100);
		}
	}

	public boolean isStunned() {
		return this.entityData.get(IS_STUNNED);
	}

	@Override
	public double getBaseHealth() {
		return 250.0;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ILLAGERS;
	}

	
	public Level getWorld() {
		return this.level();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	
	public boolean canReceiveElectricDamageCurrently() {
		return !this.isStunned();
	}

	@Override
	public void registerControllers(software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar data) {
		// Spin animation for tesla coils
		data.add(new AnimationController<>(this, "controller_spin_coils", 0, this::predicateSpinCoils));
		
		// Walking animation
		data.add(new AnimationController<>(this, "controller_walking", 10, this::predicateWalking));

		// Punch and Kick
		data.add(new AnimationController<>(this, "controller_kick_and_punch", 0, this::predicateSimpleAttack));

		// Throw and smash animation and shooting the cannon
		data.add(new AnimationController<>(this, "controller_long_animations", 0, this::predicateBigAnimations));

		// Cannon controller (raising and lowering)
		data.add(new AnimationController<>(this, "controller_cannon_arm_state", CANNON_RAISE_OR_LOWER_DURATION, this::predicateCannonArmPosition));

		// Main animations (Stun, inactive, death)
		data.add(new AnimationController<>(this, "controller_main", 30, this::predicateAnimationMain));
	}

	private static final String ANIM_NAME_PREFIX = "animation.exterminator.";

	public static final String ANIM_NAME_SPIN_COILS = ANIM_NAME_PREFIX + "spin_tesla_coils";

	private <E extends EntityCQRExterminator> PlayState predicateSpinCoils(AnimationState<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_SPIN_COILS));
		}
		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_INACTIVE = ANIM_NAME_PREFIX + "inactive";
	public static final String ANIM_NAME_DEATH = ANIM_NAME_PREFIX + "death";
	public static final String ANIM_NAME_STUN = ANIM_NAME_PREFIX + "stun";

	@SuppressWarnings("unchecked")
	private <E extends EntityCQRExterminator> PlayState predicateAnimationMain(AnimationState<E> event) {
		E animatable = event.getAnimatable();
		// Death animation
		if (animatable.isDeadOrDying()) {
			event.getController().transitionLength(0);
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenPlay(ANIM_NAME_DEATH));
			return PlayState.CONTINUE;
		}

		// Stunned
		if (animatable.isStunned()) {
			event.getController().transitionLength(0);
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_STUN));
			return PlayState.CONTINUE;
		}

		event.getController().transitionLength(30);

		// Inactive animation
		if (animatable.isSitting()) {
			// if(event.getController().getCurrentAnimation() == null || event.getController().isJustStarting) {
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenPlay(ANIM_NAME_INACTIVE));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(null);
		event.getController().forceAnimationReset();
		return PlayState.STOP;
	}

	public static final String ANIM_NAME_CANNON_RAISED = ANIM_NAME_PREFIX + "raised_cannon";
	public static final String ANIM_NAME_CANNON_LOWERED = ANIM_NAME_PREFIX + "lowered_cannon";

	private <E extends EntityCQRExterminator> PlayState predicateCannonArmPosition(AnimationState<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.dead || animatable.getHealth() < 0.01 || /*this.isDead ||*/ !animatable.isAlive() || animatable.isSitting()) {
			return PlayState.STOP;
		}

		if (animatable.entityData.get(CANNON_RAISED)) {
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_CANNON_RAISED));
		} else {
			event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_CANNON_LOWERED));
		}

		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_CANNON_SHOOT = ANIM_NAME_PREFIX + "shoot_cannon";
	public static final String ANIM_NAME_THROW = ANIM_NAME_PREFIX + "throw";
	public static final String ANIM_NAME_GROUND_SMASH = ANIM_NAME_PREFIX + "ground_slam";
	@OnlyIn(Dist.CLIENT)
	protected boolean shootIndicator;// = false; Default value for boolean field is false, for boolean wrapper object it is null (cause it is an object...)
	@OnlyIn(Dist.CLIENT)
	protected boolean throwIndicator;// = false;
	@OnlyIn(Dist.CLIENT)
	protected boolean smashIndicator;// = false;

	private <E extends EntityCQRExterminator> PlayState predicateBigAnimations(AnimationState<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.dead || animatable.getHealth() < 0.01 || /*this.isDead ||*/ !animatable.isAlive()) {
			return PlayState.STOP;
		}

		int animation = animatable.entityData.get(LONG_ANIMATION);
		if (animation != LONG_ANIMATION_NONE) {
			String name = animation == LONG_ANIMATION_CANNON ? ANIM_NAME_CANNON_SHOOT
					: animation == LONG_ANIMATION_THROW ? ANIM_NAME_THROW : ANIM_NAME_GROUND_SMASH;
			event.getController().setAnimation(RawAnimation.begin().thenPlayAndHold(name));
			return PlayState.CONTINUE;
		}
		event.getController().forceAnimationReset();
		return PlayState.STOP;
	}

	public static final String ANIM_NAME_PUNCH = ANIM_NAME_PREFIX + "punch";
	public static final String ANIM_NAME_KICK = ANIM_NAME_PREFIX + "kick";

	private <E extends EntityCQRExterminator> PlayState predicateSimpleAttack(AnimationState<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.dead || animatable.getHealth() < 0.01 || /*this.isDead ||*/ !animatable.isAlive()) {
			return PlayState.STOP;
		}

		if (this.entityData.get(ARMS_BLOCKED_BY_LONG_ANIMATION)) {
			return PlayState.STOP;
		}

		if (animatable.swinging) {
			boolean isKicking = animatable.entityData.get(PUNCH_IS_KICK);
			this.kickInProgressClient = isKicking;
			event.getController().setAnimation(RawAnimation.begin().thenPlay(isKicking ? ANIM_NAME_KICK : ANIM_NAME_PUNCH));
			return PlayState.CONTINUE;
		} else {
			animatable.kickInProgressClient = false;
		}
		return PlayState.STOP;
	}

	public static final String ANIM_NAME_WALK_NO_BODY_SWING = ANIM_NAME_PREFIX + "walk_legs_only";
	public static final String ANIM_NAME_WALK = ANIM_NAME_PREFIX + "walk";
	
	private <E extends EntityCQRExterminator> PlayState predicateWalking(AnimationState<E> event) {
		E animatable = event.getAnimatable();
		if (animatable.dead || animatable.getHealth() < 0.01 || /*this.isDead ||*/ !animatable.isAlive()) {
			return PlayState.STOP;
		}

		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			if ((animatable.isCannonRaised() && animatable.isCannonArmPlayingAnimation()) || animatable.isExecutingThrow()) {
				event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_WALK_NO_BODY_SWING));
			} else {
				event.getController().setAnimation(software.bernie.geckolib.animation.RawAnimation.begin().thenLoop(ANIM_NAME_WALK));
			}
			event.getController().setAnimationSpeed(animatable.isSprinting() ? 2.0D : 1.0D);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.factory;
	}

	protected boolean isEmitterShortCircuited(SubEntityExterminatorFieldEmitter emitter) {
		if (emitter.isActive()) {
			if (emitter.isInWater() || emitter.isInWaterOrRain() || emitter.isUnderWater()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.bossEvent.setProgress(this.getMaxHealth() <= 0.0F ? 0.0F : this.getHealth() / this.getMaxHealth());
		}

		if (this.globalAttackCooldown > 0) {
			this.globalAttackCooldown--;
		}

		if (false) {
			this.setStunned(true, 10);
		}

		if (!this.isStunned()) {
			this.setStunned(this.isEmitterShortCircuited(this.getEmitterLeft()) || this.isEmitterShortCircuited(this.getEmitterRight()), 100);
		}

		if (!this.level().isClientSide()) {
			if (this.stunTime > 0) {
				this.stunTime--;
			}
			this.setStunned(this.stunTime > 0);
		}
		for (PartEntity<?> part : this.parts) {
			part.tick();
		}

		if (this.isCannonRaised()) {
			this.yBodyRot = this.yHeadRot;
			this.yBodyRotO = this.yHeadRotO;
		}

		this.alignParts();
	}

	public void baseTick() {
		this.updateAnimationTimersServer();
		super.baseTick();
	}

	public static final int ARMS_THROW_DURATION = 14;
	public static final int GROUND_SLAM_DURATION = 60;

	// Does nothing if the entity is currently playing an animation
	// NEVER DIRECTLY ACCESS THIS METHOD!!
	
	public void sendAnimationUpdate(String animationName) {
		
		if (this.isCurrentlyPlayingAnimation()) {
			return;
		}
		switch (animationName) {
		case ANIM_NAME_CANNON_SHOOT:
			this.animationTimer = this.cannonArmTimer;
			this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, true);
			this.entityData.set(LONG_ANIMATION, LONG_ANIMATION_CANNON);
			break;
		case ANIM_NAME_THROW:
			this.animationTimer = ARMS_THROW_DURATION;
			this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, true);
			this.entityData.set(LONG_ANIMATION, LONG_ANIMATION_THROW);
			break;
		case ANIM_NAME_GROUND_SMASH:
			this.setEmitterLeftActive(false);
			this.setEmitterRightActive(false);
			this.animationTimer = GROUND_SLAM_DURATION;
			this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, true);
			this.entityData.set(LONG_ANIMATION, LONG_ANIMATION_SMASH);
			break;
		// All others are no normal animations
		default:
			this.currentAnimationPlaying = null;
			this.animationTimer = -1;
			return;
		}
		this.currentAnimationPlaying = animationName;
	}

	private int animationTimer = -1;
	private String currentAnimationPlaying;

	public int getCurrentAnimationTicks() {
		return this.animationTimer;
	}

	protected void updateAnimationTimersServer() {
		if (this.cannonArmTimer > 0) {
			this.cannonArmTimer--;
		}
		if (this.cannonTimeOut > 0) {
			this.cannonTimeOut--;
			if (this.cannonTimeOut <= 0) {
				if (!this.switchCannonArmState(false)) {
					this.cannonTimeOut = 1; // try again next tick
				}
			}
		}
		if (this.animationTimer > 0) {
			this.animationTimer--;
			if (this.animationTimer <= 0) {
				this.onAnimationEnd(this.currentAnimationPlaying);
				this.currentAnimationPlaying = null;
				this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
				this.entityData.set(LONG_ANIMATION, LONG_ANIMATION_NONE);
			}
		}
	}

	public void onAnimationEnd(final String animationName) {
		// Currently unused
	}

	@Nullable
	public String getCurrentAnimation() {
		return this.currentAnimationPlaying;
	}

	public boolean isCurrentlyPlayingAnimation() {
		return this.animationTimer > 0;
	}

	
	public boolean hurt(PartEntity<EntityCQRExterminator> part, DamageSource source, float damage) {
		boolean isMainHBPart = ((part != this.parts[3]) && (part != this.parts[4])) || part == null;
		return this.hurt(source, damage, isMainHBPart);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return this.hurt(source, amount, true);
	}

	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		this.handleAttackedByLargeGroups();

		boolean overrideFlag = false;

		// We got hit by a water bottle
		if (source.is(net.minecraft.tags.DamageTypeTags.IS_DROWNING) && !this.isInWater()) {
			if (this.isAnyEmitterActive()) {
				amount *= 2.0F;
				this.setStunned(true, 150);
			}
		}

		if (source.getDirectEntity() instanceof com.example.chocolatequest.entity.projectile.ProjectileCannonBall && source.getEntity() != this) {
			return super.hurt(source, amount);
		}

		if (source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) && source.getEntity() != null && source.getEntity() == this) {
			return false;
		}

		if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY) || (source.getEntity() instanceof net.minecraft.world.entity.player.Player && ((net.minecraft.world.entity.player.Player) source.getEntity()).isCreative())) {
			return super.hurt(source, amount);
		}

		if (source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
			return false;
		}

		if (this.isStunned()) {
			if (source.is(net.minecraft.tags.DamageTypeTags.IS_LIGHTNING)) {
				amount /= 4.0F;
				overrideFlag = true;
			}
			if (sentFromPart) {
				amount *= 2.0F;
			}
		} else if (false) {
			this.partSoundFlag = true;
			this.playSound(this.getHurtSound(source), 1.0F, 1.0F);
			return true;
		}

		if (!sentFromPart && !this.isStunned()) {
			this.partSoundFlag = true;
			this.playSound(this.getHurtSound(source), 1.0F, 1.0F);
			return true;
		}
		this.partSoundFlag = false;

		overrideFlag |= super.hurt(source, amount);

		return overrideFlag;
	}

	private void handleAttackedByLargeGroups() {
		if (this.getRandom().nextBoolean() && !this.isCannonRaised() && !this.isCurrentlyPlayingAnimation()) {
			List<Entity> groupInFrontOfMe = this.isSurroundedByGroupWithMinSize(5);
			if (groupInFrontOfMe != null) {
				this.tryStartThrowingAnimation(groupInFrontOfMe, null);
			}
		}
	}

	@Nullable
	public List<Entity> isSurroundedByGroupWithMinSize(int minSize) {
		List<net.minecraft.world.entity.Entity> groupInFrontOfMe = this.level().getEntities(this, this.getBoundingBox().move(this.getLookAngle().normalize().scale(this.getBbWidth() / 2)).inflate(1));
		groupInFrontOfMe.removeIf((Entity entity) -> (entity instanceof PartEntity || ( entity instanceof Projectile && ((Projectile)entity).getOwner() == this)));
		if (groupInFrontOfMe.size() >= minSize) {
			return groupInFrontOfMe;
		}
		return null;
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isStunned();
	}

	// Kick handling
	@Override
	public boolean doHurtTarget(Entity entityIn) {
		if(this.isStunned()) {
			return false;
		}
		boolean result = super.doHurtTarget(entityIn);

		if (result) {
			if (this.isCurrentlyPlayingAnimation()) {
				if (this.currentAnimationPlaying.equalsIgnoreCase(ANIM_NAME_THROW)) {
					if (true) {
						Vec3 v = entityIn.position().subtract(this.position());
						v = v.normalize().scale(1.5D);

						// YEET!
						Vec3 yeet = entityIn.getDeltaMovement().add(v).add(0, 0.75, 0);
						entityIn.setDeltaMovement(yeet);

						this.entityData.set(PUNCH_IS_KICK, true);
					}
				}
			} else {
				this.entityData.set(PUNCH_IS_KICK, false);
				if (this.getRandom().nextBoolean() && !this.isCannonRaised()) {
					// Throw animation
					List<Entity> affectedEntities = this.level().getEntities(this, this.getBoundingBox().move(this.getLookAngle().normalize().scale(this.getBbWidth() * 0.75 * 1.0)));
					this.tryStartThrowingAnimation(affectedEntities, entityIn);
				}
			}
		}

		return result;
	}

	protected void tryStartThrowingAnimation(List<Entity> affectedEntities, Entity attackingMob) {
		if (!affectedEntities.isEmpty()) {
			Predicate<Entity> checkPred = (e -> false);
			affectedEntities.forEach((Entity entity) -> {
				if ((entity instanceof LivingEntity && (true && true)) || checkPred.test(entity)) {
					Vec3 flyDirection = entity.position().subtract(this.position()).add(0, 1.0 * 0.4 * net.minecraft.util.Mth.nextInt(this.getRandom(), 1, 5), 0);

					entity.setDeltaMovement(entity.getDeltaMovement().add(flyDirection));

					if (entity != attackingMob) {
						super.doHurtTarget(entity);
					}
				}
			});

			// Now, play the animation
			this.sendAnimationUpdate(ANIM_NAME_THROW);
		}
	}

	// Multipart stuff
	@Override
	public PartEntity<?>[] getParts() {
		return this.parts;
	}

	private void alignParts() {
		// Artificial main hitbox
		final Vec3 offsetMainHitbox = this.getLookAngle().normalize().scale(this.getBbWidth()/ 6).yRot((float)Math.toRadians(90));
		this.parts[4].setPos(this.getX() + offsetMainHitbox.x, this.getY(), this.getZ() + offsetMainHitbox.z);
		this.parts[3].setPos(this.getX() - offsetMainHitbox.x, this.getY(), this.getZ() - offsetMainHitbox.z);

		// Backpack and emitters
		Vec3 offset = this.getLookAngle().normalize().scale(-0.25D * 1.0);
		offset = offset.add(0, 1.25D * 1.0, 0);

		this.parts[0].setPos(this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);

		Vec3 offsetEmittersHorizontal = this.getLookAngle().normalize().scale(0.5 * 1.0);

		Vec3 offsetEmitters = this.getLookAngle().normalize().scale(-0.4D * 1.0);
		offsetEmitters = offsetEmitters.add(0, 1.5D * 1.0, 0);

		offsetEmittersHorizontal = offsetEmittersHorizontal.yRot((float)Math.toRadians(90));
		this.parts[2].setPos(this.getX() + offsetEmitters.x + offsetEmittersHorizontal.x, this.getY() + offsetEmitters.y, this.getZ() + offsetEmitters.z + offsetEmittersHorizontal.z);

		offsetEmittersHorizontal = offsetEmittersHorizontal.yRot((float)Math.toRadians(180));
		this.parts[1].setPos(this.getX() + offsetEmitters.x + offsetEmittersHorizontal.x, this.getY() + offsetEmitters.y, this.getZ() + offsetEmitters.z + offsetEmittersHorizontal.z);
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	public void remove() {
		for (PartEntity<?> part : this.parts) {
			part.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
		}

		super.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
	}

	// Cannon arm
	public boolean isCannonArmReadyToShoot() {
		return this.isCannonRaised() && !this.isCannonArmPlayingAnimation() && !this.isCurrentlyPlayingAnimation();
	}

	public boolean isExecutingThrow() {
		if (this.isCurrentlyPlayingAnimation()) {
			if (this.getCurrentAnimation() != null) {
				return this.getCurrentAnimation().equalsIgnoreCase(ANIM_NAME_THROW);
			}
		}
		return false;
	}

	public boolean isExecutingGroundSlam() {
		if (this.isCurrentlyPlayingAnimation()) {
			if (this.getCurrentAnimation() != null) {
				return this.getCurrentAnimation().equalsIgnoreCase(ANIM_NAME_GROUND_SMASH);
			}
		}
		return false;
	}

	public boolean isMagicArmorActive() {
		// When stunned it uses the same render effect
		return false || this.isStunned();
	}

	protected void updateCooldownForMagicArmor() {
		if (this.isStunned()) {
			return;
		}
	}

	// Death code
	private DamageSource deathCause = null;

	@Override
	public void die(DamageSource cause) {
		this.deathCause = cause;
		super.die(cause);
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean wasRecentlyHit) {
		super.dropCustomDeathLoot(serverLevel, source, wasRecentlyHit);
		this.spawnAtLocation(ModItems.FLAMETHROWER.get());
		this.spawnAtLocation(ModItems.BACKPACK.get());
		this.spawnAtLocation(new ItemStack(Items.LIGHTNING_ROD, 1 + this.random.nextInt(2)));
		this.spawnAtLocation(new ItemStack(Items.TNT, 3 + this.random.nextInt(4)));
		this.spawnAtLocation(new ItemStack(Items.GUNPOWDER, 8 + this.random.nextInt(9)));
		this.spawnAtLocation(new ItemStack(ModItems.CANNON_BALL.get(), 8 + this.random.nextInt(9)));

		if (this.random.nextFloat() < 0.5F) {
			int piece = this.random.nextInt(4);
			if (piece == 0) this.spawnAtLocation(ModItems.HEAVY_IRON_HELMET.get());
			else if (piece == 1) this.spawnAtLocation(ModItems.HEAVY_IRON_CHESTPLATE.get());
			else if (piece == 2) this.spawnAtLocation(ModItems.HEAVY_IRON_LEGGINGS.get());
			else this.spawnAtLocation(ModItems.HEAVY_IRON_BOOTS.get());
		}

		this.spawnAtLocation(new ItemStack(Items.IRON_BLOCK, 2 + this.random.nextInt(3)));
		this.spawnAtLocation(new ItemStack(Items.REDSTONE_BLOCK, 2 + this.random.nextInt(3)));
	}
	protected void tickDeath() {
		++this.deathTime;

		if (this.deathTime == 65) {
			
		}

		if (this.deathTime >= 70 && (!this.level().isClientSide())) {
			if (this.deathCause != null) {
				super.dropAllDeathLoot((net.minecraft.server.level.ServerLevel)this.level(), deathCause);
			}

			// Copied from EntityLivingBase
			if (!this.level().isClientSide() && this.lastHurtByPlayerTime > 0 && this.shouldDropLoot() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				int i = this.getExperienceReward((net.minecraft.server.level.ServerLevel)this.level(), this.lastHurtByPlayer);
				i = i;
				while (i > 0) {
					int j = ExperienceOrb.getExperienceValue(i);
					i -= j;
					this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), j));
				}
			}

			this.remove();

			
		}
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (!this.partSoundFlag) {
			return SoundEvents.ANVIL_LAND;
		}
		return SoundEvents.IRON_GOLEM_HURT;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.IRON_GOLEM_STEP;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}
	
	
	public boolean ignoreExplosion() {
		return true;
	}

	// Arm cannon
	private static final int CANNON_RAISE_OR_LOWER_DURATION = 40;
	private static final int CANNON_SHOOT_DURATION = 12;
	private int cannonArmTimer = 0;

	// Returns wether or not it is switching to that state or if it is already in that state
	public boolean switchCannonArmState(boolean raised) {
		// First: reset the timeout
		this.resetCannonTimeout();

		if (raised == this.entityData.get(CANNON_RAISED)) {
			return true;
		}
		if (this.cannonArmTimer != 0) {
			return false;
		}

		this.entityData.set(CANNON_RAISED, raised);
		this.cannonArmTimer = CANNON_RAISE_OR_LOWER_DURATION;

		return true;
	}

	private int cannonTimeOut = 0;

	public void setCannonArmAutoTimeoutForLowering(int value) {
		this.cannonTimeOut = value;
	}

	public void resetCannonTimeout() {
		this.setCannonArmAutoTimeoutForLowering(0);
	}

	public boolean startShootingAnimation(boolean fastShot) {
		if (this.isCannonArmReadyToShoot()) {
			this.cannonArmTimer = CANNON_SHOOT_DURATION;
			if (fastShot) {
				this.cannonArmTimer /= 2;
			}

			// DONE: Send animation update to client!!!
			this.sendAnimationUpdate(ANIM_NAME_CANNON_SHOOT);

			return true;
		}
		return false;
	}

	public boolean isCannonArmPlayingAnimation() {
		return this.cannonArmTimer != 0;
	}

	public boolean isCannonRaised() {
		return this.entityData.get(CANNON_RAISED);
	}

	public Vec3 getCannonFiringPointOffset() {
		Vec3 result = Vec3.ZERO;

		final float scale = 1.0f;

		result = result.add(0, 1.88, 0);

		final Vec3 facing = Vec3.directionFromRotation(this.getXRot(), this.yBodyRot);
		result = result.add(facing.scale(1.25));
		result = result.add(facing.yRot((float)Math.toRadians(270)).scale(0.68));

		result = result.scale(scale);

		return result;
	}

	public Vec3 getCannonFiringLocation() {
		Vec3 result = this.getCannonFiringPointOffset();
		result = result.add(this.position());
		return result;
	}

	// Kick stuff
	@OnlyIn(Dist.CLIENT)
	protected boolean kickInProgressClient;

	@OnlyIn(Dist.CLIENT)
	public boolean isUsingKickAnimation() {
		return this.kickInProgressClient;
	}

	// IServerAnimationReceiver logic
	
	@OnlyIn(Dist.CLIENT)
	public void processAnimationUpdate(String animationID) {
		this.currentAnimationPlaying = animationID;
		switch (animationID) {
		// Cannon shoot animation
		case ANIM_NAME_CANNON_SHOOT:
			this.shootIndicator = true;
			break;

		// Throw animation
		case ANIM_NAME_THROW:
			this.throwIndicator = true;
			break;

		// Hulk smash
		case ANIM_NAME_GROUND_SMASH:
			this.smashIndicator = true;
			break;
		}
	}

	public boolean canElectricCoilsBeActive() {
		return (!super.isSitting() && !this.isStunned() && !this.isExecutingGroundSlam());
	}

	// Datasync stuff
	public void updateEmitterTargetRightClient(Entity object) {
		if (object != null && object instanceof LivingEntity) {
			this.setElectroCuteTargetRight((LivingEntity) object);
		} else {
			this.setElectroCuteTargetRight(null);
		}
	}

	public void updateEmitterTargetLeftClient(Entity object) {
		if (object != null && object instanceof LivingEntity) {
			this.setElectroCuteTargetLeft((LivingEntity) object);
		} else {
			this.setElectroCuteTargetLeft(null);
		}
	}

	@Override
	public boolean isInWaterOrRain() {
		return this.isInWater();
	}

	
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(this.getRandom(), difficulty);

		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(net.minecraft.world.item.Items.IRON_AXE, 1));
	}

	//No fall damage
	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}
	
	@Override
	protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
		return;
	}
	
	
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return null;
	}
	

    @Override
    public boolean canUsePotion() {
        return false;
    }
}

