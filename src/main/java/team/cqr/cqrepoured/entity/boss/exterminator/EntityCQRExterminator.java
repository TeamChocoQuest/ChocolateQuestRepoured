package team.cqr.cqrepoured.entity.boss.exterminator;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.entity.ISizable;
import team.cqr.cqrepoured.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowAttackTarget;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowPath;
import team.cqr.cqrepoured.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToHome;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToLeader;
import team.cqr.cqrepoured.entity.ai.EntityAIOpenCloseDoor;
import team.cqr.cqrepoured.entity.ai.EntityAITasksProfiled;
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
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRMaterials;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.network.server.packet.exterminator.SPacketUpdateEmitterTarget;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PartialTicksUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRExterminator extends AbstractEntityCQRBoss implements IDontSpreadElectrocution, IMechanical, IDontRenderFire, IEntityMultiPart, IAnimatable, IServerAnimationReceiver, IAnimationTickable {

	// Entity parts
	// 0 => Backpack
	// 1 => Emitter left
	// 2 => Emitter right
	// 3 & 4 => Artificial hitbox (left and right), purpose is to avoid entities punching though the boss when it is in
	// non-stunned state
	private MultiPartEntityPart[] parts;

	private EntityLivingBase electroCuteTargetEmitterLeft;
	private EntityLivingBase electroCuteTargetEmitterRight;

	private int stunTime = 0;

	protected static final DataParameter<Boolean> IS_STUNNED = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> ARMS_BLOCKED_BY_LONG_ANIMATION = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> PUNCH_IS_KICK = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> CANNON_RAISED = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);

	protected static final DataParameter<Boolean> EMITTER_LEFT_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> EMITTER_RIGHT_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQRExterminator.class, DataSerializers.BOOLEAN);

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	private boolean partSoundFlag;

	public EntityCQRExterminator(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;

		this.parts = new MultiPartEntityPart[5];

		this.parts[0] = new SubEntityExterminatorBackpack(this, "exterminator_backpack", this::isAnyEmitterActive);
		this.parts[1] = new SubEntityExterminatorFieldEmitter(this, "emitter_left", this::getElectroCuteTargetLeft, this::isEmitterLeftActive, this::setEmitterLeftActive);
		this.parts[2] = new SubEntityExterminatorFieldEmitter(this, "emitter_right", this::getElectroCuteTargetRight, this::isEmitterRightActive, this::setEmitterRightActive);
		this.parts[3] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_left", this.getDefaultWidth() / 3, this.getDefaultHeight()) {
			@Override
			public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
				if (this.parent == null || ((EntityLivingBase) this.parent).isDead) {
					return false;
				}
				return ((EntityLivingBase) this.parent).processInitialInteract(player, hand);
			}
		};
		this.parts[4] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_right", this.getDefaultWidth() / 3, this.getDefaultHeight()) {
			@Override
			public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
				if (this.parent == null || ((EntityLivingBase) this.parent).isDead) {
					return false;
				}
				return ((EntityLivingBase) this.parent).processInitialInteract(player, hand);
			}
		};
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
		return this.dataManager.get(EMITTER_LEFT_ACTIVE);
	}

	protected void setEmitterLeftActive(boolean value) {
		this.dataManager.set(EMITTER_LEFT_ACTIVE, value);
	}

	protected boolean isEmitterRightActive() {
		return this.dataManager.get(EMITTER_RIGHT_ACTIVE);
	}

	protected void setEmitterRightActive(boolean value) {
		this.dataManager.set(EMITTER_RIGHT_ACTIVE, value);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_STUNNED, false);
		this.dataManager.register(CANNON_RAISED, false);
		this.dataManager.register(PUNCH_IS_KICK, false);
		this.dataManager.register(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
		this.dataManager.register(EMITTER_LEFT_ACTIVE, false);
		this.dataManager.register(EMITTER_RIGHT_ACTIVE, false);
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		return;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON.getDamageReductionAmount(EntityEquipmentSlot.CHEST));
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON.getToughness());
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn.width * entityIn.width * entityIn.height > this.getWidth() * this.getWidth() * this.getHeight()) {
			super.applyEntityCollision(entityIn);
		}
	}

	@Override
	protected void initEntityAI() {
		if (CQRConfig.advanced.debugAI) {
			this.tasks = new EntityAITasksProfiled(this.world.profiler, this.world);
			this.targetTasks = new EntityAITasksProfiled(this.world.profiler, this.world);
		}
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIOpenCloseDoor(this));

		this.tasks.addTask(0, new BossAIExterminatorStun(this));

		this.tasks.addTask(2, new BossAIExterminatorHulkSmash(this));
		this.tasks.addTask(3, new BossAIExterminatorHandLaser(this));
		this.tasks.addTask(4, new BossAIArmCannon(this));

		this.tasks.addTask(12, new EntityAIAttackSpecial(this));
		this.tasks.addTask(13, new EntityAIAttackRanged<AbstractEntityCQR>(this));
		this.tasks.addTask(14, new EntityAIPotionThrower(this)); /* AI for secondary Item */
		this.tasks.addTask(15, new EntityAIFireball(this)); /* AI for secondary Item */
		this.tasks.addTask(16, new EntityAIHooker(this)); /* AI for secondary Item */
		this.tasks.addTask(17, new EntityAIBackstab(this));
		this.tasks.addTask(18, new EntityAIAttack(this) {
			@Override
			public boolean shouldExecute() {
				return super.shouldExecute() && !EntityCQRExterminator.this.isStunned();
			}
			
			@Override
			public boolean shouldContinueExecuting() {
				return super.shouldContinueExecuting() && !EntityCQRExterminator.this.isStunned();
			}
		});
		this.tasks.addTask(19, new EntityAICursedBoneSummoner(this));

		this.tasks.addTask(20, new EntityAIFollowAttackTarget(this));
		this.tasks.addTask(24, new EntityAILooter(this));

		this.tasks.addTask(30, new EntityAIMoveToLeader(this));
		this.tasks.addTask(31, new EntityAIFollowPath(this));
		this.tasks.addTask(32, new EntityAIMoveToHome(this));
		this.tasks.addTask(33, new EntityAIIdleSit(this));

		// Target tasks for the electro stuff
		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAITargetElectrocute(this, this::getElectroCuteTargetLeft, this::setElectroCuteTargetLeft));
		this.targetTasks.addTask(2, new EntityAITargetElectrocute(this, this::getElectroCuteTargetRight, this::setElectroCuteTargetRight));
	}

	public EntityLivingBase getElectroCuteTargetLeft() {
		return this.electroCuteTargetEmitterLeft;
	}

	public void setElectroCuteTargetLeft(EntityLivingBase electroCuteTargetA) {
		this.electroCuteTargetEmitterLeft = electroCuteTargetA;

		if (this.isServerWorld()) {
			CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateEmitterTarget(this, true), this);
		}
	}

	public EntityLivingBase getElectroCuteTargetRight() {
		return this.electroCuteTargetEmitterRight;
	}

	public void setElectroCuteTargetRight(EntityLivingBase electroCuteTargetB) {
		this.electroCuteTargetEmitterRight = electroCuteTargetB;

		if (this.isServerWorld()) {
			CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateEmitterTarget(this, false), this);
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
		this.dataManager.set(IS_STUNNED, value);
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (this.isStunned()) {
			this.stunTime += (50 / 3);
		} else if (TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this)) {
			return;
		} else {
			this.setStunned(true, 100);
		}
	}

	public boolean isStunned() {
		return this.dataManager.get(IS_STUNNED);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_EXTERMINATOR;
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
		return this.getEntityWorld();
	}

	@Override
	public boolean canBePushed() {
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
		if (this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive() || super.isSitting()) {
			return PlayState.STOP;
		}

		if (this.dataManager.get(CANNON_RAISED)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_RAISED, true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_LOWERED, true));
		}

		return PlayState.CONTINUE;
	}

	public static final String ANIM_NAME_CANNON_SHOOT = ANIM_NAME_PREFIX + "shoot_cannon";
	public static final String ANIM_NAME_THROW = ANIM_NAME_PREFIX + "throw";
	public static final String ANIM_NAME_GROUND_SMASH = ANIM_NAME_PREFIX + "ground_slam";
	@SideOnly(Side.CLIENT)
	private boolean shootIndicator;// = false; Default value for boolean field is false, for boolean wrapper object it is null (cause it is an object...)
	@SideOnly(Side.CLIENT)
	private boolean throwIndicator;// = false;
	@SideOnly(Side.CLIENT)
	private boolean smashIndicator;// = false;

	private <E extends IAnimatable> PlayState predicateBigAnimations(AnimationEvent<E> event) {
		if (this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive()) {
			return PlayState.STOP;
		}

		// Second condition: XOR Operator "^"
		if (this.dataManager.get(ARMS_BLOCKED_BY_LONG_ANIMATION)) {
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
		if (this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive()) {
			return PlayState.STOP;
		}

		if (this.dataManager.get(ARMS_BLOCKED_BY_LONG_ANIMATION)) {
			return PlayState.STOP;
		}

		if (this.getSwingProgress(PartialTicksUtil.getCurrentPartialTicks()) > 0.0F) {
			boolean isKicking = this.dataManager.get(PUNCH_IS_KICK);
			this.kickInProgressClient = isKicking;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(isKicking ? ANIM_NAME_KICK : ANIM_NAME_PUNCH, false));

		} else {
			this.kickInProgressClient = false;

			event.getController().setAnimation(null);
			event.getController().clearAnimationCache();
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	// Default hitbox size
	@Override
	public float getDefaultHeight() {
		return 2.75F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}

	protected boolean isEmitterShortCircuited(SubEntityExterminatorFieldEmitter emitter) {
		if (emitter.isActive()) {
			if (emitter.isInWater() || emitter.isWet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(this) && (this.isWet() || this.isInWater()) && !this.isStunned()) {
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
		for (MultiPartEntityPart part : this.parts) {
			this.world.updateEntityWithOptionalForce(part, true);
			part.onUpdate();
		}

		this.alignParts();
	}

	@Override
	public void onEntityUpdate() {
		this.updateAnimationTimersServer();
		super.onEntityUpdate();
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
		this.dataManager.set(ARMS_BLOCKED_BY_LONG_ANIMATION, true);
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
				this.dataManager.set(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
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
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		boolean isMainHBPart = ((part != this.parts[3]) && (part != this.parts[4])) || part == null;
		return this.attackEntityFrom(source, damage, isMainHBPart);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		this.handleAttackedByLargeGroups();

		boolean overrideFlag = false;

		// We got hit by a water bottle
		if (source == DamageSource.DROWN && !this.isInWater()) {
			if (this.isAnyEmitterActive()) {
				amount *= 2.0F;
				this.setStunned(true, 150);
			}
		}

		if (source.getImmediateSource() instanceof ProjectileCannonBall && source.getTrueSource() != this) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}

		if (source.isExplosion() && source.getTrueSource() != null && source.getTrueSource() == this) {
			return false;
		}

		if (source.canHarmInCreative() || source == DamageSource.OUT_OF_WORLD || (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative())) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}

		if (source.isFireDamage()) {
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

		overrideFlag |= super.attackEntityFrom(source, amount, sentFromPart);

		return overrideFlag;
	}

	private void handleAttackedByLargeGroups() {
		if (this.getRNG().nextBoolean() && !this.isCannonRaised() && !this.isCurrentlyPlayingAnimation()) {
			List<Entity> groupInFrontOfMe = this.isSurroundedByGroupWithMinSize(5);
			if (groupInFrontOfMe != null) {
				this.tryStartThrowingAnimation(groupInFrontOfMe, null);
			}
		}
	}

	@Nullable
	public List<Entity> isSurroundedByGroupWithMinSize(int minSize) {
		List<Entity> groupInFrontOfMe = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().offset(this.getLookVec().normalize().scale(this.getWidth() / 2)).grow(1));
		groupInFrontOfMe.removeIf((Entity entity) -> (entity instanceof MultiPartEntityPart));
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
	public boolean attackEntityAsMob(Entity entityIn) {
		if(this.isStunned()) {
			return false;
		}
		boolean result = super.attackEntityAsMob(entityIn);

		if (result) {
			if (this.isCurrentlyPlayingAnimation()) {
				if (this.currentAnimationPlaying.equalsIgnoreCase(ANIM_NAME_THROW)) {
					if (!(this.getHeldItemMainhand().getItem() instanceof ItemStaffHealing)) {
						Vec3d v = entityIn.getPositionVector().subtract(this.getPositionVector());
						v = v.normalize().scale(1.5D);

						// YEET!
						entityIn.motionX += v.x;
						entityIn.motionY += v.y + 0.75;
						entityIn.motionZ += v.z;
						entityIn.velocityChanged = true;

						this.dataManager.set(PUNCH_IS_KICK, true);
					}
				}
			} else {
				this.dataManager.set(PUNCH_IS_KICK, false);
				if (this.getRNG().nextBoolean() && !this.isCannonRaised()) {
					// Throw animation
					List<Entity> affectedEntities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().offset(this.getLookVec().normalize().scale(this.getWidth() * 0.75 * this.getSizeVariation())));
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
				if ((entity instanceof EntityLivingBase && (!TargetUtil.areInSameParty(this, entity) && !TargetUtil.isAllyCheckingLeaders(this, (EntityLivingBase) entity))) || checkPred.test(entity)) {
					Vec3d flyDirection = entity.getPositionVector().subtract(this.getPositionVector()).add(0, this.getSizeVariation() * 0.4 * DungeonGenUtils.randomBetween(1, 5, this.getRNG()), 0);

					entity.motionX += flyDirection.x;
					entity.motionY += flyDirection.y;
					entity.motionZ += flyDirection.z;

					entity.velocityChanged = true;

					if (entity != attackingMob) {
						super.attackEntityAsMob(entity);
					}
				}
			});

			// Now, play the animation
			this.sendAnimationUpdate(ANIM_NAME_THROW);
		}
	}

	// Multipart stuff
	@Override
	public Entity[] getParts() {
		return this.parts;
	}

	private void alignParts() {
		// Artificial main hitbox
		final Vec3d offsetMainHitbox = VectorUtil.rotateVectorAroundY(this.getLookVec().normalize().scale((this.getDefaultWidth() * this.getSizeVariation()) / 6), 90.0D);
		this.parts[4].setPosition(this.posX + offsetMainHitbox.x, this.posY, this.posZ + offsetMainHitbox.z);
		this.parts[3].setPosition(this.posX - offsetMainHitbox.x, this.posY, this.posZ - offsetMainHitbox.z);

		// Backpack and emitters
		Vec3d offset = this.getLookVec().normalize().scale(-0.25D * this.getSizeVariation());
		offset = offset.add(0, 1.25D * this.getSizeVariation(), 0);

		this.parts[0].setPosition(this.posX + offset.x, this.posY + offset.y, this.posZ + offset.z);

		Vec3d offsetEmittersHorizontal = this.getLookVec().normalize().scale(0.5 * this.getSizeVariation());

		Vec3d offsetEmitters = this.getLookVec().normalize().scale(-0.4D * this.getSizeVariation());
		offsetEmitters = offsetEmitters.add(0, 2.375D * this.getSizeVariation(), 0);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 90);
		this.parts[2].setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);

		offsetEmittersHorizontal = VectorUtil.rotateVectorAroundY(offsetEmittersHorizontal, 180);
		this.parts[1].setPosition(this.posX + offsetEmitters.x + offsetEmittersHorizontal.x, this.posY + offsetEmitters.y, this.posZ + offsetEmitters.z + offsetEmittersHorizontal.z);
	}

	@Override
	public void resize(float widthScale, float heightSacle) {
		super.resize(widthScale, heightSacle);

		for (MultiPartEntityPart part : this.parts) {
			if (part instanceof ISizable) {
				((ISizable) part).resize(widthScale, heightSacle);
			}
		}
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void setDead() {
		for (MultiPartEntityPart part : this.parts) {
			this.world.removeEntityDangerously(part);
		}

		super.setDead();
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
	public void onDeath(DamageSource cause) {
		this.deathCause = cause;
		super.onDeath(cause);
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		// Nope
	}

	// Death animation
	// Death animation time: 1.44s => 29 ticks
	@Override
	protected void onDeathUpdate() {
		++this.deathTime;

		if (this.deathTime == 65) {
			this.spawnDeathPoofParticles();
		}

		if (this.deathTime >= 70 && this.isServerWorld()) {
			if (this.deathCause != null) {
				super.dropLoot(this.recentlyHit > 0, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getTrueSource(), this.deathCause), this.deathCause);
			}

			// Copied from EntityLivingBase
			if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
				int i = this.getExperiencePoints(this.attackingPlayer);
				i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
				while (i > 0) {
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
				}
			}

			this.setDead();

			this.onFinalDeath();
		}
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		if (!this.partSoundFlag) {
			return SoundEvents.BLOCK_ANVIL_LAND;
		}
		return SoundEvents.ENTITY_IRONGOLEM_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_IRONGOLEM_STEP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_IRONGOLEM_DEATH;
	}

	@Override
	public boolean isImmuneToExplosions() {
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

		if (raised == this.dataManager.get(CANNON_RAISED)) {
			return true;
		}
		if (this.cannonArmTimer != 0) {
			return false;
		}

		this.dataManager.set(CANNON_RAISED, raised);
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
		return this.dataManager.get(CANNON_RAISED);
	}

	public Vec3d getCannonFiringPointOffset() {
		Vec3d result = Vec3d.ZERO;

		final float scale = this.getSizeVariation();

		result = result.add(0, 1.88, 0);

		final Vec3d facing = this.getVectorForRotation(this.rotationPitch, this.renderYawOffset);
		result = result.add(facing.scale(1.25));
		result = result.add(VectorUtil.rotateVectorAroundY(facing, 270).scale(0.68));

		result = result.scale(scale);

		return result;
	}

	public Vec3d getCannonFiringLocation() {
		Vec3d result = this.getCannonFiringPointOffset();
		result = result.add(this.posX, this.posY, this.posZ);
		return result;
	}

	// Kick stuff
	@SideOnly(Side.CLIENT)
	private boolean kickInProgressClient;

	@SideOnly(Side.CLIENT)
	public boolean isUsingKickAnimation() {
		return this.kickInProgressClient;
	}

	// IServerAnimationReceiver logic
	@Override
	@SideOnly(Side.CLIENT)
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
		if (object != null && object instanceof EntityLivingBase) {
			this.setElectroCuteTargetRight((EntityLivingBase) object);
		} else {
			this.setElectroCuteTargetRight(null);
		}
	}

	public void updateEmitterTargetLeftClient(Entity object) {
		if (object != null && object instanceof EntityLivingBase) {
			this.setElectroCuteTargetLeft((EntityLivingBase) object);
		} else {
			this.setElectroCuteTargetLeft(null);
		}
	}

	@Override
	public boolean isWet() {
		return this.isInWater();
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(CQRItems.BATTLE_AXE_BULL));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public int tickTimer() {
		return this.ticksExisted;
	}
	
}
