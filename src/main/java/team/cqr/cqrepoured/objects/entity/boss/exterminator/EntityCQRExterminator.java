package team.cqr.cqrepoured.objects.entity.boss.exterminator;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.IDontRenderFire;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.objects.entity.ISizable;
import team.cqr.cqrepoured.objects.entity.MultiPartEntityPartSizable;
import team.cqr.cqrepoured.objects.entity.ai.boss.exterminator.BossAIExterminatorHandLaser;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.ai.target.exterminator.EntityAITargetElectrocute;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.items.staves.ItemStaffHealing;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PartialTicksUtil;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityCQRExterminator extends AbstractEntityCQRBoss implements IMechanical, IDontRenderFire, IEntityMultiPart, IAnimatable, IServerAnimationReceiver {

	// Entity parts
	// 0 => Backpack
	// 1 => Emitter left
	// 2 => Emitter right
	// 3 & 4 => Artificial hitbox (left and right), purpose is to avoid entities punching though the boss when it is in non-stunned state
	private MultiPartEntityPart[] parts;

	private EntityLivingBase electroCuteTargetA;
	private EntityLivingBase electroCuteTargetB;

	protected static final DataParameter<Boolean> IS_STUNNED = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> ARMS_BLOCKED_BY_LONG_ANIMATION = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> PUNCH_IS_KICK = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> CANNON_RAISED = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	// Geckolib
	private AnimationFactory factory = new AnimationFactory(this);
	private boolean partSoundFlag;

	// Entity state handling
	// Arm state

	public EntityCQRExterminator(World worldIn) {
		super(worldIn);
		this.experienceValue = 100;

		this.parts = new MultiPartEntityPart[5];

		this.parts[0] = new SubEntityExterminatorBackpack(this, "exterminator_backpack");
		this.parts[1] = new SubEntityExterminatorFieldEmitter(this, "emitter_left");
		this.parts[2] = new SubEntityExterminatorFieldEmitter(this, "emitter_right");
		this.parts[3] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_left", this.getDefaultWidth() / 3, this.getDefaultHeight());
		this.parts[4] = new MultiPartEntityPartSizable<EntityCQRExterminator>(this, "main_hitbox_right", this.getDefaultWidth() / 3, this.getDefaultHeight());
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_STUNNED, false);
		this.dataManager.register(CANNON_RAISED, false);
		this.dataManager.register(PUNCH_IS_KICK, false);
		this.dataManager.register(ARMS_BLOCKED_BY_LONG_ANIMATION, false);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		this.tasks.addTask(2, new BossAIExterminatorHandLaser(this));

		// Target tasks for the electro stuff
		//TODO: Figure out how to do this with method references
		this.targetTasks.addTask(2, new EntityAITargetElectrocute(this, new Function<Object, EntityLivingBase>() {

			@Override
			public EntityLivingBase apply(Object t) {
				return EntityCQRExterminator.this.getElectroCuteTargetA();
			}
		}, new Function<EntityLivingBase, Object>() {

			@Override
			public Object apply(EntityLivingBase t) {
				EntityCQRExterminator.this.setElectroCuteTargetA(t);
				return null;
			}
		}));
		this.targetTasks.addTask(2, new EntityAITargetElectrocute(this, new Function<Object, EntityLivingBase>() {

			@Override
			public EntityLivingBase apply(Object t) {
				return EntityCQRExterminator.this.getElectroCuteTargetB();
			}
		}, new Function<EntityLivingBase, Object>() {

			@Override
			public Object apply(EntityLivingBase t) {
				EntityCQRExterminator.this.setElectroCuteTargetB(t);
				return null;
			}
		}));
	}

	public EntityLivingBase getElectroCuteTargetA() {
		return electroCuteTargetA;
	}

	public void setElectroCuteTargetA(EntityLivingBase electroCuteTargetA) {
		this.electroCuteTargetA = electroCuteTargetA;
	}

	public EntityLivingBase getElectroCuteTargetB() {
		return electroCuteTargetB;
	}

	public void setElectroCuteTargetB(EntityLivingBase electroCuteTargetB) {
		this.electroCuteTargetB = electroCuteTargetB;
	}

	public void setStunned(boolean value) {
		this.dataManager.set(IS_STUNNED, value);
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
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		boolean isMainHBPart = !(part == this.parts[3] || part == this.parts[4]);
		return this.attackEntityFrom(source, damage, isMainHBPart);
	}

	@Override
	public void registerControllers(AnimationData data) {
		// Spin animation for tesla coils
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_spin_coils", 0, this::predicateSpinCoils));

		// Punch and Kick
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_kick_and_punch", 0, this::predicateSimpleAttack));

		// Throw and smash animation and shooting the cannon
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_long_animations", 0, this::predicateBigAnimations));

		// Cannon controller (raising and lowering)
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_cannon_arm_state", CANNON_RAISE_OR_LOWER_DURATION, this::predicateCannonArmPosition));

		// Main animations (Stun, inactive, death)
		data.addAnimationController(new AnimationController<EntityCQRExterminator>(this, "controller_main", 30, this::predicateAnimationMain));
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
		if (this.dead || this.getHealth() < 0.01 || this.isDead || !this.isEntityAlive()) {
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
			if (shootIndicator) {
				event.getController().clearAnimationCache();
				shootIndicator = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_CANNON_SHOOT, false));
			} else if (throwIndicator) {
				event.getController().clearAnimationCache();
				throwIndicator = false;
				event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_THROW, false));
			} else if (smashIndicator) {
				event.getController().clearAnimationCache();
				smashIndicator = false;
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

	@Override
	public void onUpdate() {
		super.onUpdate();

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

	private static final int ARMS_THROW_DURATION = 14;
	private static final int GROUND_SLAM_DURATION = 60;

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
			this.animationTimer = CANNON_SHOOT_DURATION;
			break;
		case ANIM_NAME_THROW:
			this.animationTimer = ARMS_THROW_DURATION;
			break;
		case ANIM_NAME_GROUND_SMASH:
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
				this.onAnimationEnd(currentAnimationPlaying);
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
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {

		if (source.canHarmInCreative() || source == DamageSource.OUT_OF_WORLD || (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative())) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}

		if (source.isFireDamage()) {
			return false;
		}

		if (this.isStunned()) {
			amount *= 2.0F;
		}

		this.partSoundFlag = sentFromPart;
		if (!sentFromPart && !this.isStunned()) {
			this.playSound(this.getHurtSound(source), 1.0F, 1.0F);
			return true;
		}
		return super.attackEntityFrom(source, amount, sentFromPart);
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isStunned();
	}

	// Kick handling
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
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
					if (!affectedEntities.isEmpty()) {
						Predicate<Entity> checkPred = TargetUtil.createPredicateNonAlly(this.getFaction());
						affectedEntities.forEach((Entity entity) -> {
							if ((entity instanceof EntityLivingBase && TargetUtil.isAllyCheckingLeaders(this, (EntityLivingBase) entity)) || TargetUtil.areInSameParty(this, entity) || checkPred.test(entity)) {
								Vec3d flyDirection = entity.getPositionVector().subtract(this.getPositionVector()).add(0, this.getSizeVariation() * 0.4 * DungeonGenUtils.randomBetween(1, 5, this.getRNG()), 0);

								entity.motionX += flyDirection.x;
								entity.motionY += flyDirection.y;
								entity.motionZ += flyDirection.z;

								entity.velocityChanged = true;

								if (entity != entityIn) {
									super.attackEntityAsMob(entity);
								}
							}
						});

						// Now, play the animation
						this.sendAnimationUpdate(ANIM_NAME_THROW);
					}
				}
			}
		}

		return result;
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

		return false;
	}

	public boolean isExecutingGroundSlam() {

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
		if (this.deathTime >= 70 && this.isServerWorld()) {
			if (this.deathCause != null) {
				super.dropLoot(this.recentlyHit > 0, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getTrueSource(), this.deathCause), this.deathCause);
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
		if (cannonArmTimer != 0) {
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

			// DONE: Send animation update to client!!!
			this.sendAnimationUpdate(ANIM_NAME_CANNON_SHOOT);

			this.cannonArmTimer = CANNON_SHOOT_DURATION;
			if (fastShot) {
				this.cannonArmTimer /= 2;
			}

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

	public Vec3d getCannonFiringPointOffset(boolean forLaser) {
		Vec3d result = Vec3d.ZERO;

		final float scale = this.getSizeVariation();

		result = result.add(0, 2.0D, 0);

		final Vec3d facing = this.getLookVec().normalize();
		result = result.add(facing.scale(0.75));
		result = result.add(VectorUtil.rotateVectorAroundY(facing, forLaser ? 270 : 90).scale(0.65));

		result = result.scale(scale);

		return result;
	}

	public Vec3d getCannonFiringLocation() {
		Vec3d result = this.getCannonFiringPointOffset(false);
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

}
