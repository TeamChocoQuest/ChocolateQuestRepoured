package team.cqr.cqrepoured.objects.entity.boss.endercalamity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateHand;
import team.cqr.cqrepoured.objects.entity.ICirclingEntity;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAIAreaLightnings;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAIBlockThrower;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAICalamityHealing;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAIRandomTeleportEyes;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAIRandomTeleportLaser;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAISummonMinions;
import team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity.BossAITeleportAroundHome;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAINearestAttackTargetAtHomeArea;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.objects.entity.bases.ISummoner;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.util.CQRConfig;

// TODO: Move the minion & lightning handling to a AI class, it is cleaner that way
// DONE: Create helper classes to control arm management (status, animations, etc)
public class EntityCQREnderCalamity extends AbstractEntityCQRBoss implements IAnimatable, ISummoner, ICirclingEntity {

	private static final int HURT_DURATION = 24; // 1.2 * 20
	private static final int ARENA_RADIUS = 20;

	private int cqrHurtTime = 0;
	protected static final DataParameter<Boolean> IS_HURT = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> SHIELD_ACTIVE = EntityDataManager.<Boolean>createKey(EntityCQREnderCalamity.class, DataSerializers.BOOLEAN);

	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_LEFT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_UPPER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_MIDDLE = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private static final DataParameter<Optional<IBlockState>> BLOCK_RIGHT_LOWER = EntityDataManager.<Optional<IBlockState>>createKey(EntityCQREnderCalamity.class, DataSerializers.OPTIONAL_BLOCK_STATE);

	// AI stuff
	private boolean isDowned = false;

	private int currentPhaseTimer = 0;
	private int currentPhaseRunningTime = 0;
	private EEnderCalamityPhase currentPhase = EEnderCalamityPhase.PHASE_NO_TARGET;

	public EEnderCalamityPhase getCurrentPhase() {
		return this.currentPhase;
	}

	public static enum E_CALAMITY_ANIMATION_SPECIAL {
		SHOOT_LASER, STUNNED, SHOOT_ENERGY_BALL, DYING, LASERING
	}

	public static enum E_CALAMITY_HAND {
		LEFT_UPPER("handLeftUpper"), LEFT_MIDDLE("handLeftMiddle"), LEFT_LOWER("handLeftLower"), RIGHT_UPPER("handRightUpper"), RIGHT_MIDDLE("handRightMiddle"), RIGHT_LOWER("handRightLower");

		private String boneName;
		private boolean isLeft;

		public String getBoneName() {
			return this.boneName;
		}
		
		public boolean isLeftSided() {
			return this.isLeft;
		}

		private E_CALAMITY_HAND(String bone) {
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
	private AnimationFactory factory = new AnimationFactory(this);

	// Direct AI access
	private BossAITeleportAroundHome teleportAI;
	private BossAIBlockThrower blockThrowerAI;

	public EntityCQREnderCalamity(World worldIn) {
		super(worldIn);
		setSizeVariation(2.5F);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		
		this.teleportAI = new BossAITeleportAroundHome(this, 40);
		this.tasks.addTask(8, teleportAI);
		
		this.blockThrowerAI = new BossAIBlockThrower(this);
		this.tasks.addTask(6, blockThrowerAI);
		
		this.tasks.addTask(6, new BossAICalamityHealing(this));
		this.tasks.addTask(8, new BossAISummonMinions(this));
		this.tasks.addTask(8, new BossAIAreaLightnings(this, ARENA_RADIUS));
		this.tasks.addTask(7, new BossAIRandomTeleportEyes(this));
		this.tasks.addTask(7, new BossAIRandomTeleportLaser(this));
		
		this.targetTasks.taskEntries.clear();
		this.targetTasks.addTask(0, new EntityAINearestAttackTargetAtHomeArea<EntityCQREnderCalamity>(this));
		this.targetTasks.addTask(2, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_HURT, false);
		this.dataManager.register(SHIELD_ACTIVE, true);

		this.dataManager.register(BLOCK_LEFT_UPPER, Optional.absent());// of(Blocks.END_STONE.getDefaultState()));
		this.dataManager.register(BLOCK_LEFT_MIDDLE, Optional.absent());
		this.dataManager.register(BLOCK_LEFT_LOWER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_UPPER, Optional.absent());
		this.dataManager.register(BLOCK_RIGHT_MIDDLE, Optional.absent());// of(Blocks.OBSIDIAN.getDefaultState()));
		this.dataManager.register(BLOCK_RIGHT_LOWER, Optional.absent());
	}

	@Override
	public float getDefaultHeight() {
		return 2F;
	}

	@Override
	public float getDefaultWidth() {
		return 2F;
	}

	public static final String ANIM_NAME_PREFIX = "animation.ender_calamity.";
	public static final String ANIM_NAME_IDLE_BODY = ANIM_NAME_PREFIX + "idle";
	public static final String ANIM_NAME_HURT = ANIM_NAME_PREFIX + "hit";
	//Duration: 5.0s => 100 ticks
	public static final String ANIM_NAME_SHOOT_LASER = ANIM_NAME_PREFIX + "shoot_laser"; //6s
	public static final String ANIM_NAME_SHOOT_LASER_LONG = ANIM_NAME_PREFIX + "shoot_laser_long"; //12s
	public static final String ANIM_NAME_DEFLECT_BALL = ANIM_NAME_PREFIX + "deflectBall";
	public static final String ANIM_NAME_SHOOT_BALL = ANIM_NAME_PREFIX + "shootEnergyBall";
	public static final String ANIM_NAME_SPIN_HANDS = ANIM_NAME_PREFIX + "spin_hands";

	private String currentAnimation = null;
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.dataManager.get(IS_HURT)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_HURT, false));
			return PlayState.CONTINUE;
		}

		if (this.newAnimation.isPresent()) {
			this.currentAnimation = newAnimation.get();
			this.newAnimation = Optional.absent();
		}
		if(this.currentAnimation != null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(this.currentAnimation, false));
		}

		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_IDLE_BODY, false));
		}

		/*if(this.ticksExisted % 5 == 0) {
			System.out.println("Animation: " + event.getController().getCurrentAnimation().animationName);
		}*/
		
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState predicateSpinHands(AnimationEvent<E> event) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NAME_SPIN_HANDS, true));
		}
		
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState execHandAnimationPredicate(AnimationEvent<E> event, final String IDLE_ANIM, final String THROW_ANIM, Boolean updateIndicator) {
		if (event.getController().getCurrentAnimation() == null) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE_ANIM, false));
		}
		if (updateIndicator) {
			updateIndicator = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(THROW_ANIM).addAnimation(IDLE_ANIM, false));
		}
		return PlayState.CONTINUE;
	}

	private static final String ANIM_NAME_ARM_RU_IDLE = ANIM_NAME_PREFIX + "idle_armRU";
	private static final String ANIM_NAME_ARM_RU_THROW = ANIM_NAME_PREFIX + "throwBlock_RU";
	private boolean updateIndicator_Hand_RU = false;

	private <E extends IAnimatable> PlayState predicateArmRightUpper(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_RU_IDLE, ANIM_NAME_ARM_RU_THROW, updateIndicator_Hand_RU);
	}

	private static final String ANIM_NAME_ARM_RM_IDLE = ANIM_NAME_PREFIX + "idle_armRM";
	private static final String ANIM_NAME_ARM_RM_THROW = ANIM_NAME_PREFIX + "throwBlock_RM";
	private boolean updateIndicator_Hand_RM = false;

	private <E extends IAnimatable> PlayState predicateArmRightMiddle(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_RM_IDLE, ANIM_NAME_ARM_RM_THROW, updateIndicator_Hand_RM);
	}

	private static final String ANIM_NAME_ARM_RL_IDLE = ANIM_NAME_PREFIX + "idle_armRL";
	private static final String ANIM_NAME_ARM_RL_THROW = ANIM_NAME_PREFIX + "throwBlock_RL";
	private boolean updateIndicator_Hand_RL = false;

	private <E extends IAnimatable> PlayState predicateArmRightLower(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_RL_IDLE, ANIM_NAME_ARM_RL_THROW, updateIndicator_Hand_RL);
	}

	private static final String ANIM_NAME_ARM_LU_IDLE = ANIM_NAME_PREFIX + "idle_armLU";
	private static final String ANIM_NAME_ARM_LU_THROW = ANIM_NAME_PREFIX + "throwBlock_LU";
	private boolean updateIndicator_Hand_LU = false;

	private <E extends IAnimatable> PlayState predicateArmLeftUpper(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_LU_IDLE, ANIM_NAME_ARM_LU_THROW, updateIndicator_Hand_LU);
	}

	private static final String ANIM_NAME_ARM_LM_IDLE = ANIM_NAME_PREFIX + "idle_armLM";
	private static final String ANIM_NAME_ARM_LM_THROW = ANIM_NAME_PREFIX + "throwBlock_LM";
	private boolean updateIndicator_Hand_LM = false;

	private <E extends IAnimatable> PlayState predicateArmLeftMiddle(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_LM_IDLE, ANIM_NAME_ARM_LM_THROW, updateIndicator_Hand_LM);
	}

	private static final String ANIM_NAME_ARM_LL_IDLE = ANIM_NAME_PREFIX + "idle_armLL";
	private static final String ANIM_NAME_ARM_LL_THROW = ANIM_NAME_PREFIX + "throwBlock_LL";
	private boolean updateIndicator_Hand_LL = false;
	private boolean dontUpdatePhase = false;

	private <E extends IAnimatable> PlayState predicateArmLeftLower(AnimationEvent<E> event) {
		return execHandAnimationPredicate(event, ANIM_NAME_ARM_LL_IDLE, ANIM_NAME_ARM_LL_THROW, updateIndicator_Hand_LL);
	}

	public boolean forceTeleport() {
		try {
			this.teleportAI.forceExecution();
			return true;
		} catch (NullPointerException npe) {
			// Ignore
		}
		return false;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller", 10, this::predicate));
		//Spin hands controller
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_spin_hands", 10, this::predicateSpinHands));

		// Arms
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_ru", 5, this::predicateArmRightUpper));
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_rm", 5, this::predicateArmRightMiddle));
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_rl", 5, this::predicateArmRightLower));
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_lu", 5, this::predicateArmLeftUpper));
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_lm", 5, this::predicateArmLeftMiddle));
		data.addAnimationController(new AnimationController<EntityCQREnderCalamity>(this, "controller_arm_ll", 5, this::predicateArmLeftLower));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ENDERMAN_BOSS;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.EnderCalamity;
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
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		// super.move(type, x, y, z);
		return;
	}

	@Override
	public int getHealingPotions() {
		return 0;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 2F * super.getSoundVolume();
	}

	@Override
	protected float getSoundPitch() {
		return 0.75F * super.getSoundPitch();
	}

	@Override
	public int getTalkInterval() {
		// Super: 80
		return 60;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16.0D);
	}

	public boolean isShieldActive() {
		return this.dataManager.get(SHIELD_ACTIVE);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		if (source.canHarmInCreative()) {
			return super.attackEntityFrom(source, amount, sentFromPart);
		}
		// Projectile attack
		if (source.getImmediateSource() instanceof EntityEnergyOrb || source.getTrueSource() instanceof EntityEnergyOrb) {
			// TODO: Hit by energy ball
			/*
			 * If already hit often enough, Spawn explosion, then teleport to center and be unconscious
			 */
			return false;
		}

		if (source instanceof EntityDamageSourceIndirect) {
			// DONE: Switch attack target to the shooter
			// DONE: Teleport
			// DONE: Spawn homing ender eyes => Handled by AI
			/*
			 * Spawn a few homing ender eyes at random, then teleport to a different location There also is the chance for it to start "lazoring", in this stage, it
			 * teleports to a different location, waits 2 seconds, fires a laser for 3 seconds,
			 * waits 1 second, repeat
			 */
			if (this.teleportAI != null) {
				if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase) {
					this.setAttackTarget((EntityLivingBase) source.getTrueSource());
				}

				this.teleportAI.forceExecution();
				
				switch(this.getCurrentPhase()) {
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
		if (!this.dataManager.get(IS_HURT) && !this.isShieldActive()) {
			if (!super.attackEntityFrom(source, amount, sentFromPart)) {
				return false;
			}
			if (!this.world.isRemote) {
				this.dataManager.set(IS_HURT, true);
				this.cqrHurtTime = HURT_DURATION;

				if (this.getRNG().nextBoolean()) {
					this.dataManager.set(SHIELD_ACTIVE, true);
					this.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_STUNNED.getPhaseObject());
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onEntityUpdate() {
		if (this.firstUpdate && !this.hasHomePositionCQR() && !this.world.isRemote) {
			this.setHomePositionCQR(this.getPosition());
			this.teleportAI.forceExecution();
		}
		super.onEntityUpdate();
	}

	@Override
	protected void updateAITasks() {
		if (this.isWet() && !this.getSummonedEntities().isEmpty()) {

			this.world.getWorldInfo().setCleanWeatherTime(20000);
			this.world.getWorldInfo().setRainTime(0);
			this.world.getWorldInfo().setThunderTime(0);
			this.world.getWorldInfo().setRaining(false);
			this.world.getWorldInfo().setThundering(false);

			this.world.playSound(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, this.getSoundPitch(), false);
		}

		super.updateAITasks();
	}

	@Override
	public void onLivingUpdate() {
		if (this.world.isRemote) {
			// Client
			for (int i = 0; i < 2; ++i) {
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D,
						-this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
			}
		} else {
			// SErver
			if (this.cqrHurtTime > 0) {
				this.cqrHurtTime--;
			}
			this.dataManager.set(IS_HURT, cqrHurtTime > 0);

			this.handlePhases();
		}

		this.isJumping = false;
		super.onLivingUpdate();
	}
	
	public int getCurrentPhaseRunningTicks() {
		return this.currentPhaseRunningTime;
	}

	private void handlePhases() {
		if (this.cantUpdatePhase()) {
			return;
		}
		IEnderCalamityPhase phase = this.currentPhase.getPhaseObject();
		if (this.currentPhase.equals(EEnderCalamityPhase.PHASE_NO_TARGET)) {
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
		}

	}

	public void setCantUpdatePhase(boolean value) {
		if (this.world.isRemote) {
			return;
		}
		this.dontUpdatePhase = value;
	}

	private boolean cantUpdatePhase() {
		if (this.world.isRemote) {
			return true;
		}
		return this.dontUpdatePhase;
	}

	private void switchToNextPhaseOf(IEnderCalamityPhase phase) {
		ITextComponent msg = new TextComponentString("Switching phase! Old phase: " + this.currentPhase.name());
		if (this.getServer() != null)
			this.getServer().getPlayerList().sendMessage(msg);

		java.util.Optional<IEnderCalamityPhase> nextPhase = phase.getNextPhase(this);
		if (nextPhase.isPresent()) {
			this.currentPhase = EEnderCalamityPhase.getByPhaseObject(nextPhase.get());
			if (this.getServer() != null)
				this.getServer().getPlayerList().sendMessage(new TextComponentString("New phase: " + this.currentPhase.name()));
			if (nextPhase.get().isPhaseTimed()) {
				this.currentPhaseTimer = nextPhase.get().getRandomExecutionTime().get();
			}
			this.currentPhaseRunningTime = 0;
			switch(this.currentPhase) {
			case PHASE_DYING:
			case PHASE_ENERGY_TENNIS:
			case PHASE_NO_TARGET:
			case PHASE_STUNNED:
				this.blockThrowerAI.forceDropAllBlocks();
				break;
			default:
				break;
			}
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
			if (ent == null || ent.isDead) {
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
		CQRMain.NETWORK.sendToAllTracking(packet, this);
	}

	public Optional<IBlockState> getBlockFromHand(E_CALAMITY_HAND hand) {
		if (hand == null) {
			return Optional.absent();
		}
		switch (hand) {
		case LEFT_LOWER:
			return this.dataManager.get(BLOCK_LEFT_LOWER);
		case LEFT_MIDDLE:
			return this.dataManager.get(BLOCK_LEFT_MIDDLE);
		case LEFT_UPPER:
			return this.dataManager.get(BLOCK_LEFT_UPPER);
		case RIGHT_LOWER:
			return this.dataManager.get(BLOCK_RIGHT_LOWER);
		case RIGHT_MIDDLE:
			return this.dataManager.get(BLOCK_RIGHT_MIDDLE);
		case RIGHT_UPPER:
			return this.dataManager.get(BLOCK_RIGHT_UPPER);
		default:
			return Optional.absent();
		}
	}

	public void removeHandBlock(E_CALAMITY_HAND hand) {
		Optional<IBlockState> value = Optional.absent();
		this.equipBlock(hand, value);
	}

	public void equipBlock(E_CALAMITY_HAND hand, Block block) {
		this.equipBlock(hand, block.getDefaultState());
	}

	public void equipBlock(E_CALAMITY_HAND hand, Optional<IBlockState> value) {
		// Don't execute this on client side
		if (this.world.isRemote) {
			return;
		}
		switch (hand) {
		case LEFT_LOWER:
			this.dataManager.set(BLOCK_LEFT_LOWER, value);
			break;
		case LEFT_MIDDLE:
			this.dataManager.set(BLOCK_LEFT_MIDDLE, value);
			break;
		case LEFT_UPPER:
			this.dataManager.set(BLOCK_LEFT_UPPER, value);
			break;
		case RIGHT_LOWER:
			this.dataManager.set(BLOCK_RIGHT_LOWER, value);
			break;
		case RIGHT_MIDDLE:
			this.dataManager.set(BLOCK_RIGHT_MIDDLE, value);
			break;
		case RIGHT_UPPER:
			this.dataManager.set(BLOCK_RIGHT_UPPER, value);
			break;
		default:
			break;
		}
	}

	public void equipBlock(E_CALAMITY_HAND hand, IBlockState blockstate) {
		equipBlock(hand, Optional.of(blockstate));
	}

	@Override
	public void setFire(int seconds) {
		// Nope
	}

	// ISummoner stuff
	@Override
	public CQRFaction getSummonerFaction() {
		return this.getFaction();
	}

	private List<Entity> summonedEntities = new ArrayList<>();

	@Override
	public List<Entity> getSummonedEntities() {
		return this.summonedEntities;
	}

	@Override
	public EntityLivingBase getSummoner() {
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
		double oldX = this.posX;
		double oldY = this.posY;
		double oldZ = this.posZ;
		super.teleport(x, y, z);
		this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 0.9F + this.rand.nextFloat() * 0.2F);
		((WorldServer) this.world).spawnParticle(EnumParticleTypes.PORTAL, oldX, oldY + this.height * 0.5D, oldZ, 4, 0.2D, 0.2D, 0.2D, 0.0D);
		((WorldServer) this.world).spawnParticle(EnumParticleTypes.PORTAL, x, y + this.height * 0.5D, z, 4, 0.2D, 0.2D, 0.2D, 0.0D);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("isDowned", isDowned);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.isDowned = compound.getBoolean("isDowned");
	}

	public static int getArenaRadius() {
		return ARENA_RADIUS;
	}

	// Hand syncing
	public void processHandUpdates(byte[] handStates) {
		// Only process this on client!!
		if (this.world.isRemote) {
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
	public EnumCreatureAttribute getCreatureAttribute() {
		return CQRCreatureAttributes.CREATURE_TYPE_ENDERMAN;
	}

	private Optional<String> newAnimation = Optional.absent();

	public void processAnimationUpdate(String animationID) {
		// Only process this on client!!
		if (this.world.isRemote) {
			this.newAnimation = Optional.of(animationID);
			//System.out.println("New animation!" + this.newAnimation.get());
		}
	}

	@Override
	public BlockPos getCirclingCenter() {
		if(this.hasHomePositionCQR()) {
			return this.getHomePositionCQR();
		}
		return this.getPosition();
	}

}
