package team.cqr.cqrepoured.entity.boss.endercalamity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.Explosion;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ICirclingEntity;
import team.cqr.cqrepoured.entity.IServerAnimationReceiver;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIAreaLightnings;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIBlockThrower;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAICalamityBuilding;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAICalamityHealing;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIEndLaser;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIEnergyTennis;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIRandomTeleportEyes;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIRandomTeleportLaser;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAIStunned;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAISummonMinions;
import team.cqr.cqrepoured.entity.ai.boss.endercalamity.BossAITeleportAroundHome;
import team.cqr.cqrepoured.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.entity.ai.target.EntityAINearestAttackTargetAtHomeArea;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.entity.bases.ISummoner;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEnergyOrb;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketSyncCalamityRotation;
import team.cqr.cqrepoured.util.DungeonGenUtils;

// DONE: Move the minion & lightning handling to a AI class, it is cleaner that way
// DONE: Create helper classes to control arm management (status, animations, etc)
public class EntityCQREnderCalamity extends AbstractEntityCQRBoss implements IAnimatable, ISummoner, ICirclingEntity, IServerAnimationReceiver, IAnimationTickable {

	private static final int HURT_DURATION = 8; // 1.2 * 20
	private static final int ARENA_RADIUS = 20;

	private int cqrHurtTime = 0;
	protected static final DataParameter<Boolean> IS_HURT = EntityDataManager.<Boolean>defineId(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> SHIELD_ACTIVE = EntityDataManager.<Boolean>defineId(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ROTATE_BODY_PITCH = EntityDataManager.<Boolean>defineId(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_DEAD_AND_ON_THE_GROUND = EntityDataManager.<Boolean>defineId(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Optional<BlockState>> BLOCK_LEFT_UPPER = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);
	private static final DataParameter<Optional<BlockState>> BLOCK_LEFT_MIDDLE = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);
	private static final DataParameter<Optional<BlockState>> BLOCK_LEFT_LOWER = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);
	private static final DataParameter<Optional<BlockState>> BLOCK_RIGHT_UPPER = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);
	private static final DataParameter<Optional<BlockState>> BLOCK_RIGHT_MIDDLE = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);
	private static final DataParameter<Optional<BlockState>> BLOCK_RIGHT_LOWER = EntityDataManager.<Optional<BlockState>>defineId(EntityCQREnderCalamity.class, DataSerializers.BLOCK_STATE);

	// AI stuff
	private boolean isDowned = false;

	private int attackCounter = 0;
	private int currentPhaseTimer = 0;
	private int currentPhaseRunningTime = 0;
	private int noTennisCounter = 0;
	private int blockDestructionTimer = 10;
	private EEnderCalamityPhase currentPhase = EEnderCalamityPhase.PHASE_NO_TARGET;

	public float rotationPitchCQR;
	public float prevRotationPitchCQR;
	public float serverRotationPitchCQR;

	public EEnderCalamityPhase getCurrentPhase() {
		return this.currentPhase;
	}

	public enum E_CALAMITY_HAND {
		LEFT_UPPER("handLeftUpper"), LEFT_MIDDLE("handLeftMiddle"), LEFT_LOWER("handLeftLower"), RIGHT_UPPER("handRightUpper"), RIGHT_MIDDLE("handRightMiddle"), RIGHT_LOWER("handRightLower");

		private String boneName;
		private boolean isLeft;

		public String getBoneName() {
			return this.boneName;
		}

		public boolean isLeftSided() {
			return this.isLeft;
		}

		E_CALAMITY_HAND(String bone) {
			this.boneName = bone;
			this.isLeft = this.name().startsWith("LEFT");
		}

		@Nullable
		public static E_CALAMITY_HAND getFromBoneName(String bone) {
			switch (bone) {
			case "handRightUpper":
				return RIGHT_UPPER;
			case "handRightMiddle":
				return RIGHT_MIDDLE;
			case "handRightLower":
				return RIGHT_LOWER;
			case "handLeftUpper":
				return LEFT_UPPER;
			case "handLeftMiddle":
				return LEFT_MIDDLE;
			case "handLeftLower":
				return LEFT_LOWER;
			}
			return null;
		}

		public int getIndex() {
			switch (this) {
			case LEFT_LOWER:
				return 2;
			case LEFT_MIDDLE:
				return 1;
			case LEFT_UPPER:
				return 0;
			case RIGHT_LOWER:
				return 5;
			case RIGHT_MIDDLE:
				return 4;
			case RIGHT_UPPER:
				return 3;
			default:
				return 0;
			}
		}
	}

	// Geckolib
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	// Direct AI access
	private BossAITeleportAroundHome teleportAI;
	private BossAIBlockThrower blockThrowerAI;
	private BossAIEnergyTennis tennisAI;

	public EntityCQREnderCalamity(World worldIn) {
		this(CQREntityTypes.ENDER_CALAMITY.get(), worldIn);
	}
	
	public EntityCQREnderCalamity(EntityType<? extends EntityCQREnderCalamity> type, World worldIn) {
		super(type, worldIn);
		this.setSizeVariation(2.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		this.teleportAI = new BossAITeleportAroundHome(this, 40);
		this.goalSelector.addGoal(8, this.teleportAI);

		this.goalSelector.addGoal(4, new BossAIStunned(this));
		this.tennisAI = new BossAIEnergyTennis(this);
		this.goalSelector.addGoal(5, this.tennisAI);

		this.blockThrowerAI = new BossAIBlockThrower(this);
		this.goalSelector.addGoal(6, this.blockThrowerAI);
		this.goalSelector.addGoal(6, new BossAICalamityBuilding(this));

		this.goalSelector.addGoal(5, new BossAIEndLaser(this));
		this.goalSelector.addGoal(6, new BossAICalamityHealing(this));
		this.goalSelector.addGoal(8, new BossAISummonMinions(this));
		this.goalSelector.addGoal(8, new BossAIAreaLightnings(this, ARENA_RADIUS));
		this.goalSelector.addGoal(7, new BossAIRandomTeleportEyes(this));
		this.goalSelector.addGoal(7, new BossAIRandomTeleportLaser(this));

		this.targetSelector.availableGoals.clear();
		this.targetSelector.addGoal(0, new EntityAINearestAttackTargetAtHomeArea<>(this));
		this.targetSelector.addGoal(2, new EntityAICQRNearestAttackTarget(this));
		this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IS_HURT, false);
		this.entityData.define(SHIELD_ACTIVE, true);
		this.entityData.define(ROTATE_BODY_PITCH, false);
		this.entityData.define(IS_DEAD_AND_ON_THE_GROUND, false);

		this.entityData.define(BLOCK_LEFT_UPPER, Optional.empty());// of(Blocks.END_STONE.getDefaultState()));
		this.entityData.define(BLOCK_LEFT_MIDDLE, Optional.empty());
		this.entityData.define(BLOCK_LEFT_LOWER, Optional.empty());
		this.entityData.define(BLOCK_RIGHT_UPPER, Optional.empty());
		this.entityData.define(BLOCK_RIGHT_MIDDLE, Optional.empty());// of(Blocks.OBSIDIAN.getDefaultState()));
		this.entityData.define(BLOCK_RIGHT_LOWER, Optional.empty());
	}

	public boolean rotateBodyPitch() {
		return this.entityData.get(ROTATE_BODY_PITCH);
	}

	public void setRotateBodyPitch(boolean value) {
		if (this.isServerWorld()) {
			this.entityData.set(ROTATE_BODY_PITCH, value);
		}
	}

	public static final String ANIM_NAME_PREFIX = "animation.ender_calamity.";
	public static final String ANIM_NAME_IDLE_BODY = ANIM_NAME_PREFIX + "idle";
	public static final String ANIM_NAME_HURT = ANIM_NAME_PREFIX + "hit";
	// Duration: 5.0s => 100 ticks
	public static final String ANIM_NAME_SHOOT_LASER = ANIM_NAME_PREFIX + "shoot_laser"; // 6s
	public static final String ANIM_NAME_SHOOT_LASER_LONG = ANIM_NAME_PREFIX + "shoot_laser_long"; // 12s
	public static final String ANIM_NAME_DEFLECT_BALL = ANIM_NAME_PREFIX + "deflectBall";
	public static final String ANIM_NAME_CHARGE_ENERGY_BALL = ANIM_NAME_PREFIX + "prepareEnergyBall";
	public static final String ANIM_NAME_SHOOT_BALL = ANIM_NAME_PREFIX + "shootEnergyBall";
	public static final String ANIM_NAME_SPIN_HANDS = ANIM_NAME_PREFIX + "spin_hands";
	public static final String ANIM_NAME_LASER_STATIONARY = ANIM_NAME_PREFIX + "laser_stationary";
	public static final String ANIM_NAME_DEATH_FALLING = ANIM_NAME_PREFIX + "death";
	public static final String ANIM_NAME_DEATH_ON_GROUND = ANIM_NAME_PREFIX + "death_on_ground";

	private String currentAnimation = null;

	@SuppressWarnings("unchecked")
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			if (this.entityData.get(IS_DEAD_AND_ON_THE_GROUND)) {
				event.getController().transitionLengthTicks = 0;
				event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NAME_DEATH_ON_GROUND));
			} else {
				event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NAME_DEATH_FALLING));
			}
			return PlayState.CONTINUE;
		}

		if (this.entityData.get(IS_HURT)) {
			event.getController().transitionLengthTicks = 0;
			event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_NAME_HURT));
			return PlayState.CONTINUE;
		} else {
			event.getController().transitionLengthTicks = 10;
		}

		if (this.newAnimation.isPresent()) {
			this.currentAnimation = this.newAnimation.get();
			this.newAnimation = Optional.empty();
		}
		if (this.currentAnimation != null) {
			if (this.currentAnimation.equalsIgnoreCase(ANIM_NAME_SHOOT_BALL)) {
				event.getController().transitionLengthTicks = 0;
			}
			event.getController().setAnimation(new AnimationBuilder().addAnimation(this.currentAnimation).loop(ANIM_NAME_IDLE_BODY));
		}

		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_IDLE_BODY));
		}

		return PlayState.CONTINUE;
	}

	
	private <E extends IAnimatable> PlayState predicateSpinHands(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_SPIN_HANDS));
		}
	 
		return PlayState.CONTINUE;
	}
	 

	private <E extends IAnimatable> void soundListenerArms(SoundKeyframeEvent<E> event) {
		SoundEvent sound = null;
		float pitch = 1.0F;
		float volume = 1.0F;

		// System.out.println("WE GOT SOUND!!!");
		// System.out.println("Sound: " + event.sound);

		switch (event.sound.toLowerCase()) {
		// Play throwing sound
		case "calamity_throw":
			sound = CQRSounds.ENDER_CALAMITY_THROW_ITEM;
			pitch = 1.5F;
			volume = 10.0F;
			break;
		default:
			return;
		}

		this.playSound(sound, volume, pitch);
	}

	private static final String ANIM_NAME_ARM_RU_IDLE = ANIM_NAME_PREFIX + "idle_armRU";
	private static final String ANIM_NAME_ARM_RU_THROW = ANIM_NAME_PREFIX + "throwBlock_RU";
	private boolean updateIndicator_Hand_RU = false;

	private <E extends IAnimatable> PlayState predicateArmRightUpper(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_RU_IDLE));
		}
		if (this.updateIndicator_Hand_RU) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_RU = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_RU_THROW).loop(ANIM_NAME_ARM_RU_IDLE));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_RM_IDLE = ANIM_NAME_PREFIX + "idle_armRM";
	private static final String ANIM_NAME_ARM_RM_THROW = ANIM_NAME_PREFIX + "throwBlock_RM";
	private Boolean updateIndicator_Hand_RM = false;

	private <E extends IAnimatable> PlayState predicateArmRightMiddle(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_RM_IDLE));
		}
		if (this.updateIndicator_Hand_RM) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_RM = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_RM_THROW).loop(ANIM_NAME_ARM_RM_IDLE));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_RL_IDLE = ANIM_NAME_PREFIX + "idle_armRL";
	private static final String ANIM_NAME_ARM_RL_THROW = ANIM_NAME_PREFIX + "throwBlock_RL";
	private Boolean updateIndicator_Hand_RL = false;

	private <E extends IAnimatable> PlayState predicateArmRightLower(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_RL_IDLE));
		}
		if (this.updateIndicator_Hand_RL) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_RL = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_RL_THROW).loop(ANIM_NAME_ARM_RL_IDLE));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_LU_IDLE = ANIM_NAME_PREFIX + "idle_armLU";
	private static final String ANIM_NAME_ARM_LU_THROW = ANIM_NAME_PREFIX + "throwBlock_LU";
	private Boolean updateIndicator_Hand_LU = false;

	private <E extends IAnimatable> PlayState predicateArmLeftUpper(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_LU_IDLE));
		}
		if (this.updateIndicator_Hand_LU) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_LU = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_LU_THROW).loop(ANIM_NAME_ARM_LU_IDLE));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_LM_IDLE = ANIM_NAME_PREFIX + "idle_armLM";
	private static final String ANIM_NAME_ARM_LM_THROW = ANIM_NAME_PREFIX + "throwBlock_LM";
	private Boolean updateIndicator_Hand_LM = false;

	private <E extends IAnimatable> PlayState predicateArmLeftMiddle(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_LM_IDLE));
		}
		if (this.updateIndicator_Hand_LM) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_LM = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_LM_THROW).loop(ANIM_NAME_ARM_LM_IDLE));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_LL_IDLE = ANIM_NAME_PREFIX + "idle_armLL";
	private static final String ANIM_NAME_ARM_LL_THROW = ANIM_NAME_PREFIX + "throwBlock_LL";
	private Boolean updateIndicator_Hand_LL = false;
	private boolean dontUpdatePhase = false;

	private <E extends IAnimatable> PlayState predicateArmLeftLower(AnimationEvent<E> event) {
		// Death animation
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			return PlayState.STOP;
		}
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().loop(ANIM_NAME_ARM_LL_IDLE));
		}
		if (this.updateIndicator_Hand_LL) {
			event.getController().clearAnimationCache();
			this.updateIndicator_Hand_LL = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_ARM_LL_THROW).loop(ANIM_NAME_ARM_LL_IDLE));
		}
		return PlayState.CONTINUE;
	}

	public boolean forceTeleport() {
		if (!this.hasAttackTarget()) {
			return false;
		}
		try {
			this.teleportAI.forceExecution();
			return true;
		} catch (NullPointerException npe) {
			// Ignore
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
		// Spin hands controller
		data.addAnimationController(new AnimationController<>(this, "controller_spin_hands", 0, this::predicateSpinHands));

		// Arms
		@SuppressWarnings("rawtypes")
		AnimationController[] handControllers = new AnimationController[] {
				new AnimationController<>(this, "controller_arm_ru", 5, this::predicateArmRightUpper),
				new AnimationController<>(this, "controller_arm_rm", 5, this::predicateArmRightMiddle),
				new AnimationController<>(this, "controller_arm_rl", 5, this::predicateArmRightLower),
				new AnimationController<>(this, "controller_arm_lu", 5, this::predicateArmLeftUpper),
				new AnimationController<>(this, "controller_arm_lm", 5, this::predicateArmLeftMiddle),
				new AnimationController<>(this, "controller_arm_ll", 5, this::predicateArmLeftLower) };

		for (@SuppressWarnings("rawtypes")
		AnimationController ac : handControllers) {
			ac.registerSoundListener(this::soundListenerArms);
			data.addAnimationController(ac);
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public double getBaseHealth() {
		return CQRConfig.SERVER_CONFIG.baseHealths.enderCalamity.get();
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ENDERMEN;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public void enableBossBar() {
		super.enableBossBar();

		if (this.bossInfoServer != null) {
			this.bossInfoServer.setColor(Color.PURPLE);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	
	@Override
	public void move(MoverType type, Vector3d direction) {
		if (this.entityData.get(IS_DEAD_AND_ON_THE_GROUND)) {
			return;
		}

		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			if (this.getY() <= 1 + (this.getBoundingBox().maxY - this.getBoundingBox().minY)) {
				this.isFalling = false;
				return;
			}
			super.move(type, direction.multiply(1, 0.125D, 1));
		}
		return;
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENDERMAN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENDERMAN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getVoicePitch() {
		return 0.75F * super.getVoicePitch();
	}

	@Override
	public int getAmbientSoundInterval() {
		// Super: 80
		return 60;
	}

	@Override
	protected void applyAttributeValues() {
		super.applyAttributeValues();
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12.0D);
		this.getAttribute(Attributes.ARMOR).setBaseValue(10.0D);
	}

	public boolean isShieldActive() {
		return this.entityData.get(SHIELD_ACTIVE);
	}

	private boolean canSphereDestroyShield(ProjectileEnergyOrb orb) {
		if (orb.getDeflectionCount() >= 5) {
			return true;
		}
		if (orb.getDeflectionCount() < this.level.getDifficulty().getId()) {
			return false;
		}

		double chance = (1.0D - (1.0D / orb.getDeflectionCount()));
		return DungeonGenUtils.percentageRandom(chance, this.getRandom());

	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		if (source.isBypassInvul()) {
			return super.hurt(source, amount, sentFromPart);
		}
		//amount /= 2;
		// Projectile attack
		if (source.getDirectEntity() instanceof ProjectileEnergyOrb) {
			// TODO: Hit by energy ball
			/*
			 * If already hit often enough, Spawn explosion, then teleport to center and be unconscious
			 */
			if (this.canSphereDestroyShield((ProjectileEnergyOrb) source.getDirectEntity())) {

				// Avoid switching to wrong phase
				this.tennisAI.calculateRemainingAttempts();

				this.entityData.set(SHIELD_ACTIVE, false);
				this.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_ENERGY_TENNIS.getPhaseObject());

				// TODO: Create own particle explosion that just looks nice
				this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3, Explosion.Mode.BREAK);
				this.playSound(SoundEvents.ENDERMAN_SCREAM, 10.0F, 1.0F);

				return true;
			} else {
				((ProjectileEnergyOrb) source.getDirectEntity()).redirect(source.getEntity(), this);
			}

			return false;
		}

		if (source instanceof IndirectEntityDamageSource && !isDowned()) {
			// DONE: Switch attack target to the shooter
			// DONE: Teleport
			// DONE: Spawn homing ender eyes => Handled by AI
			/*
			 * Spawn a few homing ender eyes at random, then teleport to a different location There also is the chance for it to
			 * start "lazoring", in this stage, it
			 * teleports to a different location, waits 2 seconds, fires a laser for 3 seconds,
			 * waits 1 second, repeat
			 */
			if (this.teleportAI != null) {
				if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
					this.setTarget((LivingEntity) source.getEntity());
				}

				this.forceTeleport();

				switch (this.getCurrentPhase()) {
				case PHASE_DYING:
				case PHASE_ENERGY_TENNIS:
				case PHASE_LASERING:
				case PHASE_STUNNED:
				case PHASE_TELEPORT_EYE_THROWER:
				case PHASE_TELEPORT_LASER:
					break;
				default:
					this.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_TELEPORT_EYE_THROWER.getPhaseObject());
					break;
				}
			}
			return false;
		}

		// Other attack
		if (!this.entityData.get(IS_HURT) && !this.isShieldActive()) {
			if (!super.hurt(source, amount, sentFromPart)) {
				return false;
			}
			if (!this.level.isClientSide) {
				this.entityData.set(IS_HURT, true);
				this.cqrHurtTime = HURT_DURATION;
				this.attackCounter++;
				if (this.attackCounter >= 2 * this.level.getDifficulty().getId()) {
					if (this.getRandom().nextBoolean()) {
						this.entityData.set(SHIELD_ACTIVE, true);
						this.attackCounter = 0;
						this.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_IDLE.getPhaseObject());
					}
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public void baseTick() {
		if (this.firstTick && !this.hasHomePositionCQR() && !this.level.isClientSide) {
			this.setHomePositionCQR(this.blockPosition());
			this.forceTeleport();
		}
		super.baseTick();

		this.prevRotationPitchCQR = this.rotationPitchCQR;
		if (this.level.isClientSide) {
			this.rotationPitchCQR = this.serverRotationPitchCQR;
		} else {
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity),new SPacketSyncCalamityRotation(this));
		}
	}
	
	@Override
	protected void customServerAiStep() {
		if (this.dead || this.getHealth() < 0.01 || !this.isAlive()) {
			super.customServerAiStep();
			return;
		}

		//If it rains => NO!
		if (this.isInWaterOrRain() && !this.isInWater() && !this.getSummonedEntities().isEmpty() && (this.level instanceof ServerWorld)) {

			((ServerWorld)this.level).setWeatherParameters(2000, 0, false, false);

			this.playSound(SoundEvents.ENDERMAN_STARE, 2.5F, this.getVoicePitch());
		}

		this.blockDestructionTimer--;
		if (this.blockDestructionTimer <= 0) {
			this.blockDestructionTimer = 10;
			boolean flag = false;
			Vector3i size = new Vector3i(this.getBoundingBox().maxX - this.getBoundingBox().minX, this.getBoundingBox().maxY - this.getBoundingBox().minY, this.getBoundingBox().maxZ - this.getBoundingBox().minZ);
			size = new Vector3i(size.getX() * 0.5, size.getY() * 0.5, size.getZ() * 0.5);
			for (BlockPos blockpos : BlockPos.betweenClosed(this.blockPosition().offset(size), this.blockPosition().subtract(size).offset(0, size.getY(), 0))) {
				BlockState iblockstate = this.level.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if (!block.isAir(iblockstate, this.level, blockpos) && block.canEntityDestroy(iblockstate, this.level, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate)) {
					flag = this.level.destroyBlock(blockpos, true) || flag;
				}
			}
			if (flag) {
				this.level.levelEvent((PlayerEntity) null, Constants.WorldEvents.WITHER_BREAK_BLOCK, this.blockPosition(), 0);
			}
		}

		super.customServerAiStep();
	}

	@Override
	public void aiStep() {
		if (this.level.isClientSide) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.level.addParticle(ParticleTypes.PORTAL, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), (this.getRandom().nextDouble() - 0.5D) * 2.0D, -this.getRandom().nextDouble(),
						(this.getRandom().nextDouble() - 0.5D) * 2.0D);
			}
		} else {
			// SErver
			if (this.cqrHurtTime > 0) {
				this.cqrHurtTime--;
			}
			this.entityData.set(IS_HURT, this.cqrHurtTime > 0);

			this.handlePhases();
		}

		this.jumping = false;
		super.aiStep();
	}

	public int getCurrentPhaseRunningTicks() {
		return this.currentPhaseRunningTime;
	}

	private void handlePhases() {
		if (this.cantUpdatePhase()) {
			return;
		}
		IEnderCalamityPhase phase = this.currentPhase.getPhaseObject();
		if (this.currentPhase == EEnderCalamityPhase.PHASE_NO_TARGET) {
			if (this.hasAttackTarget()) {
				this.switchToNextPhaseOf(phase);
			}
			return;
		}

		this.currentPhaseRunningTime++;
		boolean timedPhaseChange = false;
		if (phase.isPhaseTimed()) {
			this.currentPhaseTimer--;
			timedPhaseChange = this.currentPhaseTimer < 0;
		}
		if (timedPhaseChange) {
			this.switchToNextPhaseOf(phase);
			if (this.currentPhase == EEnderCalamityPhase.PHASE_BUILDING || this.currentPhase == EEnderCalamityPhase.PHASE_LASERING || this.currentPhase == EEnderCalamityPhase.PHASE_TELEPORT_EYE_THROWER || this.currentPhase == EEnderCalamityPhase.PHASE_TELEPORT_LASER) {
				if (this.currentPhase != EEnderCalamityPhase.PHASE_ENERGY_TENNIS) {
					this.tennisAI.calculateRemainingAttempts();
					this.noTennisCounter++;
					//TODO: Move to config?
					if (this.noTennisCounter > 5) {
						this.switchToPhase(EEnderCalamityPhase.PHASE_ENERGY_TENNIS.getPhaseObject());
						this.noTennisCounter = 0;
					}
				} else {
					this.noTennisCounter = 0;
				}
			}
		}

	}

	public void setCantUpdatePhase(boolean value) {
		if (this.level.isClientSide) {
			return;
		}
		this.dontUpdatePhase = value;
	}

	private boolean cantUpdatePhase() {
		if (this.level.isClientSide) {
			return true;
		}
		return this.dontUpdatePhase;
	}

	private void switchToPhase(IEnderCalamityPhase nextPhase) {
		this.currentPhase = EEnderCalamityPhase.getByPhaseObject(nextPhase);
		/*
		 * if (this.getServer() != null)
		 * this.getServer().getPlayerList().sendMessage(new TextComponentString("New phase: " + this.currentPhase.name()));
		 */
		if (nextPhase.isPhaseTimed()) {
			this.currentPhaseTimer = nextPhase.getRandomExecutionTime().get();
		}
		this.currentPhaseRunningTime = 0;
		switch (this.currentPhase) {
		case PHASE_DYING:
		case PHASE_ENERGY_TENNIS:
		case PHASE_NO_TARGET:
		case PHASE_STUNNED:
			this.blockThrowerAI.forceDropAllBlocks();
			break;
		default:
			break;
		}

		this.isDowned = this.currentPhase == EEnderCalamityPhase.PHASE_STUNNED;
	}

	private void switchToNextPhaseOf(IEnderCalamityPhase phase) {
		/*
		 * ITextComponent msg = new TextComponentString("Switching phase! Old phase: " + this.currentPhase.name());
		 * if (this.getServer() != null)
		 * this.getServer().getPlayerList().sendMessage(msg);
		 */

		java.util.Optional<IEnderCalamityPhase> nextPhase = phase.getNextPhase(this);
		if (nextPhase.isPresent()) {
			this.switchToPhase(nextPhase.get());
		}
	}

	public void forcePhaseChange() {
		this.forcePhaseChangeToNextOf(this.currentPhase.getPhaseObject());
	}

	public void forcePhaseChangeToNextOf(IEnderCalamityPhase phase) {
		this.switchToNextPhaseOf(phase);
	}

	public boolean filterSummonLists() {
		List<Entity> tmp = new ArrayList<>();
		boolean result = false;
		for (Entity ent : this.summonedEntities) {
			if (ent == null || !ent.isAlive()) {
				tmp.add(ent);
			}
		}
		for (Entity e : tmp) {
			this.summonedEntities.remove(e);
		}
		result = !tmp.isEmpty();
		tmp.clear();
		return result;
	}

	public void swingHand(E_CALAMITY_HAND hand) {
		SPacketCalamityUpdateHand packet = SPacketCalamityUpdateHand.builder(this).swingArm(hand, true).build();
		CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::getEntity), packet);
	}

	public Optional<BlockState> getBlockFromHand(E_CALAMITY_HAND hand) {
		if (hand == null) {
			return Optional.empty();
		}
		switch (hand) {
		case LEFT_LOWER:
			return this.entityData.get(BLOCK_LEFT_LOWER);
		case LEFT_MIDDLE:
			return this.entityData.get(BLOCK_LEFT_MIDDLE);
		case LEFT_UPPER:
			return this.entityData.get(BLOCK_LEFT_UPPER);
		case RIGHT_LOWER:
			return this.entityData.get(BLOCK_RIGHT_LOWER);
		case RIGHT_MIDDLE:
			return this.entityData.get(BLOCK_RIGHT_MIDDLE);
		case RIGHT_UPPER:
			return this.entityData.get(BLOCK_RIGHT_UPPER);
		default:
			return Optional.empty();
		}
	}

	public void removeHandBlock(E_CALAMITY_HAND hand) {
		Optional<BlockState> value = Optional.empty();
		this.equipBlock(hand, value);
	}

	public void equipBlock(E_CALAMITY_HAND hand, Block block) {
		this.equipBlock(hand, block.defaultBlockState());
	}

	public void equipBlock(E_CALAMITY_HAND hand, Optional<BlockState> value) {
		// Don't execute this on client side
		if (this.level.isClientSide) {
			return;
		}
		switch (hand) {
		case LEFT_LOWER:
			this.entityData.set(BLOCK_LEFT_LOWER, value);
			break;
		case LEFT_MIDDLE:
			this.entityData.set(BLOCK_LEFT_MIDDLE, value);
			break;
		case LEFT_UPPER:
			this.entityData.set(BLOCK_LEFT_UPPER, value);
			break;
		case RIGHT_LOWER:
			this.entityData.set(BLOCK_RIGHT_LOWER, value);
			break;
		case RIGHT_MIDDLE:
			this.entityData.set(BLOCK_RIGHT_MIDDLE, value);
			break;
		case RIGHT_UPPER:
			this.entityData.set(BLOCK_RIGHT_UPPER, value);
			break;
		default:
			break;
		}
	}

	public void equipBlock(E_CALAMITY_HAND hand, BlockState blockstate) {
		this.equipBlock(hand, Optional.of(blockstate));
	}

	@Override
	public void setSecondsOnFire(int seconds) {
		// Nope
	}

	// ISummoner stuff
	@Override
	public Faction getSummonerFaction() {
		return this.getFaction();
	}

	private List<Entity> summonedEntities = new ArrayList<>();

	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedEntities;
	}

	@Override
	public LivingEntity getSummoner() {
		return this;
	}

	@Override
	public void addSummonedEntityToList(Entity summoned) {
		this.summonedEntities.add(summoned);
	}

	public boolean isDowned() {
		return this.isDowned;
	}

	@Override
	public void teleport(double x, double y, double z) {
		double oldX = this.getX();
		double oldY = this.getY();
		double oldZ = this.getZ();
		super.teleport(x, y, z);
		this.playSound(SoundEvents.SHULKER_TELEPORT, 3.0F, 0.9F + this.getRandom().nextFloat() * 0.2F);
		for(int i = 0; i < 4; i++) {
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, 0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, 0.2D, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, 0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, 0.2D, -0.2D);
			
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, 0, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, 0, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, 0, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, 0, -0.2D);
			
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, -0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, 0.2D, -0.2D, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, -0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, oldX, oldY + this.getBbHeight() * 0.5D, oldZ, -0.2D, -0.2D, -0.2D);
			
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, 0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, 0.2D, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, 0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, 0.2D, -0.2D);
			
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, 0, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, 0, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, 0, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, 0, -0.2D);
			
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, -0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, 0.2D, -0.2D, -0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, -0.2D, 0.2D);
			((ServerWorld) this.level).addParticle(ParticleTypes.PORTAL, true, x, y + this.getBbHeight() * 0.5D, z, -0.2D, -0.2D, -0.2D);
		}
		
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("isDowned", this.isDowned);
		compound.putBoolean("deadAndOnGround", this.entityData.get(IS_DEAD_AND_ON_THE_GROUND));
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.isDowned = compound.getBoolean("isDowned");
		this.entityData.set(IS_DEAD_AND_ON_THE_GROUND, compound.getBoolean("deadAndOnGround"));
	}

	public static int getArenaRadius() {
		return ARENA_RADIUS;
	}

	// Hand syncing
	public void processHandUpdates(byte[] handStates) {
		// Only process this on client!!
		if (this.level.isClientSide) {
			for (int i = 0; i < handStates.length; i++) {
				if (handStates[i] != 0) {
					E_CALAMITY_HAND hand = E_CALAMITY_HAND.values()[i];
					switch (hand) {
					case LEFT_LOWER:
						this.updateIndicator_Hand_LL = true;
						break;
					case LEFT_MIDDLE:
						this.updateIndicator_Hand_LM = true;
						break;
					case LEFT_UPPER:
						this.updateIndicator_Hand_LU = true;
						break;
					case RIGHT_LOWER:
						this.updateIndicator_Hand_RL = true;
						break;
					case RIGHT_MIDDLE:
						this.updateIndicator_Hand_RM = true;
						break;
					case RIGHT_UPPER:
						this.updateIndicator_Hand_RU = true;
						break;
					}
				}
			}
		}
	}

	@Override
	public CreatureAttribute getMobType() {
		return CQRCreatureAttributes.VOID;
	}

	private Optional<String> newAnimation = Optional.empty();

	@Override
	@OnlyIn(Dist.CLIENT)
	public void processAnimationUpdate(String animationID) {
		// Only process this on client!!
		if (this.level.isClientSide) {
			this.newAnimation = Optional.of(animationID);
			// System.out.println("New animation!" + this.newAnimation.get());
		}
	}

	@Override
	public BlockPos getCirclingCenter() {
		if (this.hasHomePositionCQR()) {
			return this.getHomePositionCQR();
		}
		return this.blockPosition();
	}
	
	@Override
	public boolean canSee(Entity entityIn) {
		// 48 * 48 = 2304
		return entityIn.blockPosition().distSqr(this.getCirclingCenter()) <= 2304;
	}

	private DamageSource deathCause = null;

	@Override
	public void die(DamageSource cause) {
		this.deathCause = cause;
		this.entityData.set(SHIELD_ACTIVE, false);
		super.die(cause);
	}

	private boolean isFalling = false;

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
		this.isFalling = !onGroundIn;
		super.checkFallDamage(y, onGroundIn, state, pos);
	}

	// Death animation
	// Death animation time: 1.88s => 38 ticks
	// Transition time: 10 ticks
	// DONE: Maybe shoot out items whilst dead?
	@Override
	protected void tickDeath() {
		if (!this.isServerWorld()) {
			return;
		}

		if (/* !(this.posY <= 1+ (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY)) && */ this.isFalling) {
			return;
		}

		++this.deathTime;
		// Initial death animation takes 0.36s => 8 ticks
		if (this.deathTime < 10) {
			return;
		}

		// LOOTVOLCANO
		if (this.deathTime % 2 == 0 && this.deathCause != null) {
			//Recent second arg: recentlyHit
			this.dropSingleItemFromLoottable(CQRLoottables.CHESTS_TREASURE, this.lastHurt > 0, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getEntity(), this.deathCause), this.deathCause);
		}

		if (!this.entityData.get(IS_DEAD_AND_ON_THE_GROUND)) {
			this.entityData.set(IS_DEAD_AND_ON_THE_GROUND, true);
		}
		if (this.deathTime == 53) {
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Mode.NONE);
			this.setSizeVariation(0.0F);
		}
		if (this.deathTime >= 54) {
			if (this.deathCause != null) {
				//Recent first arg: recentlyHit
				this.dropCustomDeathLoot(this.deathCause, net.minecraftforge.common.ForgeHooks.getLootingLevel(this, this.deathCause.getEntity(), this.deathCause), this.lastHurt > 0);
			}
			this.remove();

			this.onFinalDeath();
		}
	}

	private void dropSingleItemFromLoottable(ResourceLocation table, boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		if (table != null) {
			LootTable lootTable = this.level.getServer().getLootTables().get(table);
			float luck = 0;
			if (this.lastHurtByPlayer != null && wasRecentlyHit) {
				luck = this.lastHurtByPlayer.getLuck();
			}
			LootContext lootContext = this.createLootContext(wasRecentlyHit, source).withLuck(luck).withRandom(this.getRandom()).create(LootParameterSets.ENTITY); /*new LootContext.Builder((ServerWorld) this.level)
					.withParameter(LootParameters.THIS_ENTITY, this)
					.withParameter(LootParameters.DAMAGE_SOURCE, source)
					.withOptionalParameter(LootParameters.LAST_DAMAGE_PLAYER, wasRecentlyHit ? this.lastHurtByPlayer : null)
					.withLuck(luck)
					.withRandom(this.getRandom())
					.create(LootParameterSets.ENTITY);*/

			List<ItemStack> loot = lootTable.getRandomItems(lootContext);
			if (loot.isEmpty()) {
				return;
			}
			Collections.shuffle(loot, this.getRandom());

			ItemStack rolledItem = loot.get(0);
			ItemEntity item = this.spawnAtLocation(rolledItem, 0.0F);

			double vy = 0.25D + 0.5D * this.getRandom().nextDouble();
			double vx = -0.25D + 0.5D * this.getRandom().nextDouble();
			double vz = -0.25D + 0.5D * this.getRandom().nextDouble();

			/*item.motionX = vx;
			item.motionY = vy;
			item.motionZ = vz;
			item.velocityChanged = true;*/
			item.setDeltaMovement(vx, vy, vz);
			item.hasImpulse = true;

			item.setInvulnerable(true);
		}
	}

	@Override
	public int tickTimer() {
		return this.tickCount;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
