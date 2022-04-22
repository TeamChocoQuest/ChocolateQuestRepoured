package team.cqr.cqrepoured.entity.boss.exterminator;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.electric.IDontSpreadElectrocution;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.entity.IEntityMultiPart;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowAttackTarget;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowPath;
import team.cqr.cqrepoured.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToHome;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToLeader;
import team.cqr.cqrepoured.entity.ai.EntityAIOpenCloseDoor;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttack;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttackRanged;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIBackstab;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAIAttackSpecial;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAIHooker;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAILooter;
import team.cqr.cqrepoured.entity.ai.boss.exterminator.BossAIArmCannon;
import team.cqr.cqrepoured.entity.ai.boss.exterminator.BossAIExterminatorHandLaser;
import team.cqr.cqrepoured.entity.ai.boss.exterminator.BossAIExterminatorHulkSmash;
import team.cqr.cqrepoured.entity.ai.boss.exterminator.BossAIExterminatorStun;
import team.cqr.cqrepoured.entity.ai.item.EntityAICursedBoneSummoner;
import team.cqr.cqrepoured.entity.ai.item.EntityAIFireball;
import team.cqr.cqrepoured.entity.ai.item.EntityAIPotionThrower;
import team.cqr.cqrepoured.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.ai.target.exterminator.EntityAITargetElectrocute;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRMaterials;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRExterminator extends AbstractEntityCQRBoss implements IDontSpreadElectrocution, IMechanical, IDontRenderFire, IEntityMultiPart<EntityCQRExterminator>, IAnimatable, IServerAnimationReceiver, IAnimationTickable {
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

	protected static final DataParameter<Boolean> IS_STUNNED = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> ARMS_BLOCKED_BY_LONG_ANIMATION = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> PUNCH_IS_KICK = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> CANNON_RAISED = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);

	protected static final DataParameter<Boolean> EMITTER_LEFT_ACTIVE = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> EMITTER_RIGHT_ACTIVE = EntityDataManager.<Boolean>defineId(EntityCQRExterminator.class, DataSerializers.BOOLEAN);

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	private boolean partSoundFlag;

	public EntityCQRExterminator(World world) {
		this(CQREntityTypes.EXTERMINATOR.get(), world);
	}
	
	public EntityCQRExterminator(EntityType<? extends EntityCQRExterminator> type, World worldIn) {
		super(type, worldIn);
		this.xpReward = 100;

		this.parts = new PartEntity[5];
		this.parts[0] = new SubEntityExterminatorBackpack(this, "exterminator_backpack", this::isAnyEmitterActive);
		this.parts[1] = new SubEntityExterminatorFieldEmitter(this, "emitter_left", this::getElectroCuteTargetLeft, this::isEmitterLeftActive, this::setEmitterLeftActive);
		this.parts[2] = new SubEntityExterminatorFieldEmitter(this, "emitter_right", this::getElectroCuteTargetRight, this::isEmitterRightActive, this::setEmitterRightActive);
		this.parts[3] = new SubEntityExterminatorHitboxPart(this, "main_hitbox_left", this.getDefaultWidth() / 3, this.getDefaultHeight());
		this.parts[4] = new SubEntityExterminatorHitboxPart(this, "main_hitbox_right", this.getDefaultWidth() / 3, this.getDefaultHeight());

		this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1);
	}

	@Override
	public void setId(int pId) {
		super.setId(pId);
		for (int i = 0; i < this.parts.length; i++) {
			this.parts[i].setId(pId + i + 1);
		}
	}

	protected boolean isAnyEmitterActive() {
		try {
			return EntityCQRExterminator.this.getEmitterLeft().isActive() || EntityCQRExterminator.this.getEmitterRight().isActive();
		} catch (NullPointerException npe) {
			return false;
		}
	}

	@Nullable
	private SubEntityExterminatorFieldEmitter getEmitterLeft() {
		return (SubEntityExterminatorFieldEmitter) this.parts[1];
	}

	@Nullable
	private SubEntityExterminatorFieldEmitter getEmitterRight() {
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

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IS_STUNNED, false);
		this.entityData.define(CANNON_RAISED, false);
		this.entityData.define(PUNCH_IS_KICK, false);
		this.entityData.define(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
		this.entityData.define(EMITTER_LEFT_ACTIVE, false);
		this.entityData.define(EMITTER_RIGHT_ACTIVE, false);
	}
	
	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();

		this.getAttribute(Attributes.ARMOR).setBaseValue(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON.getDefenseForSlot(EquipmentSlotType.CHEST));
		this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON.getToughness());
	}

	
	@Override
	public void push(Entity entityIn) {
		if (entityIn.getBbWidth() * entityIn.getBbWidth() * entityIn.getBbHeight() > this.getBbWidth() * this.getBbWidth() * this.getBbHeight()) {
			super.doPush(entityIn);
		}
	}

	@Override
	protected void registerGoals() {
		if (CQRConfig.advanced.debugAI) {
			//this.goalSelector = new EntityAITasksProfiled(this.level.profiler, this.level);
			//this.targetSelector = new EntityAITasksProfiled(this.level.profiler, this.level);
		}
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(2, new EntityAIOpenCloseDoor(this));

		this.goalSelector.addGoal(0, new BossAIExterminatorStun(this));

		this.goalSelector.addGoal(2, new BossAIExterminatorHulkSmash(this));
		//this.goalSelector.addGoal(3, new BossAIExterminatorHandLaser(this));
		this.goalSelector.addGoal(4, new BossAIArmCannon(this));

		this.goalSelector.addGoal(12, new EntityAIAttackSpecial(this));
		this.goalSelector.addGoal(13, new EntityAIAttackRanged<AbstractEntityCQR>(this));
		this.goalSelector.addGoal(14, new EntityAIPotionThrower(this)); // AI for secondary Item
		this.goalSelector.addGoal(15, new EntityAIFireball(this)); // AI for secondary Item
		this.goalSelector.addGoal(16, new EntityAIHooker(this)); // AI for secondary Item
		this.goalSelector.addGoal(17, new EntityAIBackstab(this));
		this.goalSelector.addGoal(18, new EntityAIAttack(this) {
			@Override
			public boolean canUse() {
				return super.canUse() && !EntityCQRExterminator.this.isStunned();
			}
			
			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && !EntityCQRExterminator.this.isStunned();
			}
		});
		this.goalSelector.addGoal(19, new EntityAICursedBoneSummoner(this));

		this.goalSelector.addGoal(20, new EntityAIFollowAttackTarget(this));
		this.goalSelector.addGoal(24, new EntityAILooter(this));

		//this.goalSelector.addGoal(30, new EntityAIMoveToLeader(this));
		this.goalSelector.addGoal(31, new EntityAIFollowPath(this));
		this.goalSelector.addGoal(32, new EntityAIMoveToHome(this));
		this.goalSelector.addGoal(33, new EntityAIIdleSit(this));

		// Target tasks for the electro stuff
		this.targetSelector.addGoal(0, new EntityAICQRNearestAttackTarget(this));
		this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this));
		this.targetSelector.addGoal(2, new EntityAITargetElectrocute(this, this::getElectroCuteTargetLeft, this::setElectroCuteTargetLeft));
		this.targetSelector.addGoal(2, new EntityAITargetElectrocute(this, this::getElectroCuteTargetRight, this::setElectroCuteTargetRight));
	}

	public LivingEntity getElectroCuteTargetLeft() {
		return this.electroCuteTargetEmitterLeft;
	}

	public void setElectroCuteTargetLeft(LivingEntity electroCuteTargetA) {
		this.electroCuteTargetEmitterLeft = electroCuteTargetA;

		if (this.isServerWorld()) {
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this) ,new SPacketUpdateEmitterTarget(this, true));
		}
	}

	public LivingEntity getElectroCuteTargetRight() {
		return this.electroCuteTargetEmitterRight;
	}

	public void setElectroCuteTargetRight(LivingEntity electroCuteTargetB) {
		this.electroCuteTargetEmitterRight = electroCuteTargetB;

		if (this.isServerWorld()) {
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this) ,new SPacketUpdateEmitterTarget(this, false));
		}
	}

	public void setStunned(boolean value, final int ticks) {
		if (this.isServerWorld() && value && (ticks / 3) >= this.stunTime) {
			this.stunTime += (ticks / 3);// update is only executed every 3 ticks
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
	public void thunderHit(ServerWorld pLevel, LightningBoltEntity pLightning) {
		if (this.isStunned()) {
			this.stunTime += (50 / 3);
		} else if (TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this)) {
			return;
		} else {
			this.setStunned(true, 100);
		}
	}

	public boolean isStunned() {
		return this.entityData.get(IS_STUNNED);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Exterminatior;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ILLAGERS;
	}

	@Override
	public World getWorld() {
		return this.level;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean canReceiveElectricDamageCurrently() {
		return IMechanical.super.canReceiveElectricDamageCurrently() || this.isStunned();
	}

	@Override
	public void registerControllers(AnimationData data) {
		// Spin animation for tesla coils
		data.addAnimationController(new AnimationController<>(this, "controller_spin_coils", 0, this::predicateSpinCoils));
		
		// Walking animation
		data.addAnimationController(new AnimationController<>(this, "controller_walking", 10, this::predicateWalking));

		// Punch and Kick
		data.addAnimationController(new AnimationController<>(this, "controller_kick_and_punch", 0, this::predicateSimpleAttack));

		// Throw and smash animation and shooting the cannon
		data.addAnimationController(new AnimationController<>(this, "controller_long_animations", 0, this::predicateBigAnimations));

		// Cannon controller (raising and lowering)
		data.addAnimationController(new AnimationController<>(this, "controller_cannon_arm_state", CANNON_RAISE_OR_LOWER_DURATION, this::predicateCannonArmPosition));

		// Main animations (Stun, inactive, death)
		data.addAnimationController(new AnimationController<>(this, "controller_main", 30, this::predicateAnimationMain));
	}

	private static final String ANIM_NAME_PREFIX = "animation.exterminator.";

	public static final String ANIM_NAME_SPIN_COILS = ANIM_NAME_PREFIX + "spin_tesla_coils";

	private <E extends IAnimatable> PlayState predicateSpinCoils(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPIN_COILS, true));
		}
		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_INACTIVE = ANIM_NAME_PREFIX + "inactive";
	public static final String ANIM_NAME_DEATH = ANIM_NAME_PREFIX + "death";
	public static final String ANIM_NAME_STUN = ANIM_NAME_PREFIX + "stun";

	@SuppressWarnings("unchecked")
	private <E extends IAnimatable> PlayState predicateAnimationMain(AnimationEvent<E> event) {
		// Death animation
		if (this.canPlayDeathAnimation()) {
			event.getController().transitionLengthTicks = 0.0D;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_DEATH, false));
			return PlayState.CONTINUE;
		}

		// Stunned
		if (this.isStunned()) {
			event.getController().transitionLengthTicks = 0.0D;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_STUN, true));
			return PlayState.CONTINUE;
		}

		event.getController().transitionLengthTicks = 30;

		// Inactive animation
		if (super.isSitting()) {
			// if(event.getController().getCurrentAnimation() == null || event.getController().isJustStarting) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_INACTIVE, false));
			// }
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(null);
		event.getController().clearAnimationCache();
		return PlayState.STOP;
	}

	public static final String ANIM_NAME_CANNON_RAISED = ANIM_NAME_PREFIX + "raised_cannon";
	public static final String ANIM_NAME_CANNON_LOWERED = ANIM_NAME_PREFIX + "lowered_cannon";

	private <E extends IAnimatable> PlayState predicateCannonArmPosition(AnimationEvent<E> event) {
		if (this.dead || this.getHealth() < 0.01 || /*this.isDead ||*/ !this.isAlive() || super.isSitting()) {
			return PlayState.STOP;
		}

		if (this.entityData.get(CANNON_RAISED)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_RAISED, true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_LOWERED, true));
		}

		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_CANNON_SHOOT = ANIM_NAME_PREFIX + "shoot_cannon";
	public static final String ANIM_NAME_THROW = ANIM_NAME_PREFIX + "throw";
	public static final String ANIM_NAME_GROUND_SMASH = ANIM_NAME_PREFIX + "ground_slam";
	@OnlyIn(Dist.CLIENT)
	private boolean shootIndicator;// = false; Default value for boolean field is false, for boolean wrapper object it is null (cause it is an object...)
	@OnlyIn(Dist.CLIENT)
	private boolean throwIndicator;// = false;
	@OnlyIn(Dist.CLIENT)
	private boolean smashIndicator;// = false;

	private <E extends IAnimatable> PlayState predicateBigAnimations(AnimationEvent<E> event) {
		if (this.dead || this.getHealth() < 0.01 || /*this.isDead ||*/ !this.isAlive()) {
			return PlayState.STOP;
		}

		// Second condition: XOR Operator "^"
		if (this.entityData.get(ARMS_BLOCKED_BY_LONG_ANIMATION)) {
			if (this.shootIndicator) {
				event.getController().clearAnimationCache();
				this.shootIndicator = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_SHOOT, false));
			} else if (this.throwIndicator) {
				event.getController().clearAnimationCache();
				this.throwIndicator = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_THROW, false));
			} else if (this.smashIndicator) {
				event.getController().clearAnimationCache();
				this.smashIndicator = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_GROUND_SMASH, false));
			}
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	public static final String ANIM_NAME_PUNCH = ANIM_NAME_PREFIX + "punch";
	public static final String ANIM_NAME_KICK = ANIM_NAME_PREFIX + "kick";

	private <E extends IAnimatable> PlayState predicateSimpleAttack(AnimationEvent<E> event) {
		if (this.dead || this.getHealth() < 0.01 || /*this.isDead ||*/ !this.isAlive()) {
			return PlayState.STOP;
		}

		if (this.entityData.get(ARMS_BLOCKED_BY_LONG_ANIMATION)) {
			return PlayState.STOP;
		}

		if (this.getAttackAnim(event.getPartialTick()) > 0.0F) {
			boolean isKicking = this.entityData.get(PUNCH_IS_KICK);
			this.kickInProgressClient = isKicking;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(isKicking ? ANIM_NAME_KICK : ANIM_NAME_PUNCH, false));

		} else {
			this.kickInProgressClient = false;

			event.getController().setAnimation(null);
			event.getController().clearAnimationCache();
		}
		return PlayState.CONTINUE;
	}
	
	public static final String ANIM_NAME_WALK_NO_BODY_SWING = ANIM_NAME_PREFIX + "walk_legs_only";
	public static final String ANIM_NAME_WALK = ANIM_NAME_PREFIX + "walk";
	
	private <E extends IAnimatable> PlayState predicateWalking(AnimationEvent<E> event) {
		if (this.dead || this.getHealth() < 0.01 || /*this.isDead ||*/ !this.isAlive()) {
			return PlayState.STOP;
		}

		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			if ((this.isCannonRaised() && this.isCannonArmPlayingAnimation()) || this.isExecutingThrow()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_WALK_NO_BODY_SWING, true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_WALK, true));
			}
			event.getController().setAnimationSpeed(this.isSprinting() ? 2.0D : 1.0D);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Override
	public AnimationFactory getFactory() {
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

		if (TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this) && (this.isInWaterOrRain() || this.isInWater()) && !this.isStunned()) {
			this.setStunned(true, 10);
		}

		if (!this.isStunned()) {
			this.setStunned(this.isEmitterShortCircuited(this.getEmitterLeft()) || this.isEmitterShortCircuited(this.getEmitterRight()), 100);
		}

		if (this.isServerWorld()) {
			if (this.stunTime > 0) {
				this.stunTime--;
			}
			this.setStunned(this.stunTime > 0);
		}
		for (PartEntity<?> part : this.parts) {
			//this.level.updateEntityWithOptionalForce(part, true);
			part.tick();
		}

		this.alignParts();
	}

	@Override
	public void baseTick() {
		this.updateAnimationTimersServer();
		super.baseTick();
	}

	public static final int ARMS_THROW_DURATION = 14;
	public static final int GROUND_SLAM_DURATION = 60;

	// Does nothing if the entity is currently playing an animation
	// NEVER DIRECTLY ACCESS THIS METHOD!!
	// TODO: Add this to the interface?
	@Override
	public void sendAnimationUpdate(String animationName) {
		if (this.isCurrentlyPlayingAnimation()) {
			return;
		}

		IServerAnimationReceiver.super.sendAnimationUpdate(animationName);
		switch (animationName) {
		case ANIM_NAME_CANNON_SHOOT:
			this.animationTimer = this.cannonArmTimer;
			break;
		case ANIM_NAME_THROW:
			this.animationTimer = ARMS_THROW_DURATION;
			break;
		case ANIM_NAME_GROUND_SMASH:
			this.setEmitterLeftActive(false);
			this.setEmitterRightActive(false);
			this.animationTimer = GROUND_SLAM_DURATION;
			break;
		// All others are no normal animations
		default:
			this.currentAnimationPlaying = null;
			this.animationTimer = -1;
			return;
		}
		this.currentAnimationPlaying = animationName;
		this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, true);
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
				this.switchCannonArmState(false);
			}
		}
		if (this.animationTimer > 0) {
			this.animationTimer--;
			if (this.animationTimer <= 0) {
				this.onAnimationEnd(this.currentAnimationPlaying);
				this.currentAnimationPlaying = null;
				this.entityData.set(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
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

	@Override
	public boolean hurt(PartEntity part, DamageSource source, float damage) {
		boolean isMainHBPart = ((part != this.parts[3]) && (part != this.parts[4])) || part == null;
		return this.hurt(source, damage, isMainHBPart);
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		this.handleAttackedByLargeGroups();

		boolean overrideFlag = false;

		// We got hit by a water bottle
		if (source == DamageSource.DROWN && !this.isInWater()) {
			if (this.isAnyEmitterActive()) {
				amount *= 2.0F;
				this.setStunned(true, 150);
			}
		}

		if (source.getDirectEntity() instanceof ProjectileCannonBall && source.getEntity() != this) {
			return super.hurt(source, amount, sentFromPart);
		}

		if (source.isExplosion() && source.getEntity() != null && source.getEntity() == this) {
			return false;
		}

		if (source.isCreativePlayer() || source == DamageSource.OUT_OF_WORLD || (source.getEntity() instanceof PlayerEntity && ((PlayerEntity) source.getEntity()).isCreative())) {
			return super.hurt(source, amount, sentFromPart);
		}

		if (source.isFire()) {
			return false;
		}

		if (this.isStunned()) {
			if (source == DamageSource.LIGHTNING_BOLT) {
				amount /= 4.0F;
				overrideFlag = true;
			}
			if (sentFromPart) {
				amount *= 2.0F;
			}
		} else if (TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this)) {
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

		overrideFlag |= super.hurt(source, amount, sentFromPart);

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
		List<Entity> groupInFrontOfMe = this.level.getEntities(this, this.getBoundingBox().move(this.getLookAngle().normalize().scale(this.getWidth() / 2)).inflate(1));
		groupInFrontOfMe.removeIf((Entity entity) -> (entity instanceof PartEntity || ( entity instanceof ProjectileEntity && ((ProjectileEntity)entity).getOwner() == this)));
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
					if (!(this.getMainHandItem().getItem() instanceof ItemStaffHealing)) {
						Vector3d v = entityIn.position().subtract(this.position());
						v = v.normalize().scale(1.5D);

						// YEET!
						Vector3d yeet = entityIn.getDeltaMovement().add(v).add(0, 0.75, 0);
						/*entityIn.motionX += v.x;
						entityIn.motionY += v.y + 0.75;
						entityIn.motionZ += v.z;
						entityIn.velocityChanged = true;*/
						entityIn.setDeltaMovement(yeet);

						this.entityData.set(PUNCH_IS_KICK, true);
					}
				}
			} else {
				this.entityData.set(PUNCH_IS_KICK, false);
				if (this.getRandom().nextBoolean() && !this.isCannonRaised()) {
					// Throw animation
					List<Entity> affectedEntities = this.level.getEntities(this, this.getBoundingBox().move(this.getLookAngle().normalize().scale(this.getWidth() * 0.75 * this.getSizeVariation())));
					this.tryStartThrowingAnimation(affectedEntities, entityIn);
				}
			}
		}

		return result;
	}

	protected void tryStartThrowingAnimation(List<Entity> affectedEntities, Entity attackingMob) {
		if (!affectedEntities.isEmpty()) {
			Predicate<Entity> checkPred = TargetUtil.createPredicateNonAlly(this.getFaction());
			affectedEntities.forEach((Entity entity) -> {
				if ((entity instanceof LivingEntity && (!TargetUtil.areInSameParty(this, entity) && !TargetUtil.isAllyCheckingLeaders(this, (LivingEntity) entity))) || checkPred.test(entity)) {
					Vector3d flyDirection = entity.position().subtract(this.position()).add(0, this.getSizeVariation() * 0.4 * DungeonGenUtils.randomBetween(1, 5, this.getRandom()), 0);

					/*entity.motionX += flyDirection.x;
					entity.motionY += flyDirection.y;
					entity.motionZ += flyDirection.z;

					entity.velocityChanged = true;*/
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
		final Vector3d offsetMainHitbox = VectorUtil.rotateVectorAroundY(this.getLookAngle().normalize().scale((this.getDefaultWidth() * this.getSizeVariation()) / 6), 90.0D);
		this.parts[4].setPos(this.getX() + offsetMainHitbox.x, this.getY(), this.getZ() + offsetMainHitbox.z);
		this.parts[3].setPos(this.getX() - offsetMainHitbox.x, this.getY(), this.getZ() - offsetMainHitbox.z);

		// Backpack and emitters
		Vector3d offset = this.getLookAngle().normalize().scale(-0.25D * this.getSizeVariation());
		offset = offset.add(0, 1.25D * this.getSizeVariation(), 0);

		this.parts[0].setPos(this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);

		Vector3d offsetEmittersHorizontal = this.getLookAngle().normalize().scale(0.5 * this.getSizeVariation());

		Vector3d offsetEmitters = this.getLookAngle().normalize().scale(-0.4D * this.getSizeVariation());
		offsetEmitters = offsetEmitters.add(0, 2.375D * this.getSizeVariation(), 0);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 90);
		this.parts[2].setPos(this.getX() + offsetEmitters.x + offsetEmittersHorizontal.x, this.getY() + offsetEmitters.y, this.getZ() + offsetEmitters.z + offsetEmittersHorizontal.z);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 180);
		this.parts[1].setPos(this.getX() + offsetEmitters.x + offsetEmittersHorizontal.x, this.getY() + offsetEmitters.y, this.getZ() + offsetEmitters.z + offsetEmittersHorizontal.z);
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void remove() {
		for (PartEntity<?> part : this.parts) {
			part.remove(false);
		}

		super.remove();
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

	@Override
	public boolean isMagicArmorActive() {
		// When stunned it uses the same render effect
		return super.isMagicArmorActive() || this.isStunned();
	}

	@Override
	protected void updateCooldownForMagicArmor() {
		if (this.isStunned()) {
			return;
		}
		super.updateCooldownForMagicArmor();
	}

	// Death code
	private DamageSource deathCause = null;

	@Override
	public void die(DamageSource cause) {
		this.deathCause = cause;
		super.die(cause);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource deathCause, int lootingModifier, boolean wasRecentlyHit) {
		//Nope
	}
	
	// Death animation
	// Death animation time: 1.44s => 29 ticks
	@Override
	protected void tickDeath() {
		++this.deathTime;

		if (this.deathTime == 65) {
			this.spawnDeathPoofParticles();
		}

		if (this.deathTime >= 70 && this.isServerWorld()) {
			if (this.deathCause != null) {
				super.dropAllDeathLoot(deathCause);
				//super.dropLoot(this.lastHurtByPlayerTime > 0, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getEntity(), this.deathCause), this.deathCause);
			}

			// Copied from EntityLivingBase
			if (!this.level.isClientSide && this.lastHurtByPlayerTime > 0 && this.shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				int i = this.getExperienceReward(this.lastHurtByPlayer);
				i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.lastHurtByPlayer, i);
				while (i > 0) {
					int j = ExperienceOrbEntity.getExperienceValue(i);
					i -= j;
					this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), j));
				}
			}

			this.remove();

			this.onFinalDeath();
		}
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (!this.partSoundFlag) {
			return SoundEvents.ANVIL_LAND;
		}
		return SoundEvents.IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.IRON_GOLEM_STEP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.IRON_GOLEM_DEATH;
	}
	
	@Override
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

	public Vector3d getCannonFiringPointOffset() {
		Vector3d result = Vector3d.ZERO;

		final float scale = this.getSizeVariation();

		result = result.add(0, 1.88, 0);

		final Vector3d facing = Vector3d.directionFromRotation(this.xRot, this.yRot);
		result = result.add(facing.scale(1.25));
		result = result.add(VectorUtil.rotateVectorAroundY(facing, 270).scale(0.68));

		result = result.scale(scale);

		return result;
	}

	public Vector3d getCannonFiringLocation() {
		Vector3d result = this.getCannonFiringPointOffset();
		result = result.add(this.position());
		return result;
	}

	// Kick stuff
	@OnlyIn(Dist.CLIENT)
	private boolean kickInProgressClient;

	@OnlyIn(Dist.CLIENT)
	public boolean isUsingKickAnimation() {
		return this.kickInProgressClient;
	}

	// IServerAnimationReceiver logic
	@Override
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
		return (!super.isSitting() && !this.isStunned() && !this.isExecutingGroundSlam() && !this.canPlayDeathAnimation());
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

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(difficulty);

		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(CQRItems.BATTLE_AXE_BULL.get(), 1));
	}

	@Override
	public int tickTimer() {
		return this.tickCount;
	}

	@Override
	public LivingEntity getEntity() {
		return this;
	}
	
	//No fall damage
	@Override
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		return false;
	}
	
	@Override
	protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
		return;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
}
