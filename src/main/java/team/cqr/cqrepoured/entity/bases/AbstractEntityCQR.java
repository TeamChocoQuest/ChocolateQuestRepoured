package team.cqr.cqrepoured.entity.bases;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.core.animation.AnimationState;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.client.init.ESpeechBubble;
import team.cqr.cqrepoured.client.render.entity.layer.geo.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.IIsBeingRiddenHelper;
import team.cqr.cqrepoured.entity.ISizable;
import team.cqr.cqrepoured.entity.ITextureVariants;
import team.cqr.cqrepoured.entity.ITradeRestockOverTime;
import team.cqr.cqrepoured.entity.ai.EntityAIFireFighter;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowAttackTarget;
import team.cqr.cqrepoured.entity.ai.EntityAIFollowPath;
import team.cqr.cqrepoured.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToHome;
import team.cqr.cqrepoured.entity.ai.EntityAIMoveToLeader;
import team.cqr.cqrepoured.entity.ai.EntityAIOpenCloseDoor;
import team.cqr.cqrepoured.entity.ai.EntityAIRideHorse;
import team.cqr.cqrepoured.entity.ai.EntityAISearchMount;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttack;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttackRanged;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIBackstab;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAIAttackSpecial;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAIHooker;
import team.cqr.cqrepoured.entity.ai.attack.special.EntityAILooter;
import team.cqr.cqrepoured.entity.ai.item.EntityAICursedBoneSummoner;
import team.cqr.cqrepoured.entity.ai.item.EntityAIFireball;
import team.cqr.cqrepoured.entity.ai.item.EntityAIHealingPotion;
import team.cqr.cqrepoured.entity.ai.item.EntityAIPotionThrower;
import team.cqr.cqrepoured.entity.ai.spells.EntityAISpellHandler;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.faction.IFactionRelated;
import team.cqr.cqrepoured.faction.IHasFaction;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;
import team.cqr.cqrepoured.item.ItemBadge;
import team.cqr.cqrepoured.item.ItemPotionHealing;
import team.cqr.cqrepoured.item.ItemShieldDummy;
import team.cqr.cqrepoured.item.armor.ItemBackpack;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.network.CQRNetworkHooks;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;

public abstract class AbstractEntityCQR extends PathfinderMob implements IEntityAdditionalSpawnData, ISizable, IHasTextureOverride, ITextureVariants, ITradeRestockOverTime, IIsBeingRiddenHelper, IFactionRelated {

	private static final UUID BASE_ATTACK_SPEED_ID = UUID.fromString("be37de40-8857-48b1-aa99-49dd243fc22c");
	private static final UUID HEALTH_SCALE_SLIDER_ID = UUID.fromString("4b654c1d-fb8f-42b9-a278-0d49dab6d176");
	private static final UUID HEALTH_SCALE_DISTANCE_TO_SPAWN_ID = UUID.fromString("cf718cfe-d6a1-4cf6-b6c8-b5cf397f334c");

	protected static final EntityDataAccessor<Boolean> IS_LEADER = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Float> INVISIBILITY = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.FLOAT);
	protected static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> TALKING = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Integer> TEXTURE_INDEX = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Boolean> MAGIC_ARMOR_ACTIVE = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Integer> SPELL_INFORMATION = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Boolean> SPIN_TO_WIN = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<String> FACTION_OVERRIDE_SYNC = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.STRING);
	protected static final EntityDataAccessor<CompoundTag> SHOULDER_ENTITY = SynchedEntityData.defineId(AbstractEntityCQR.class, EntityDataSerializers.COMPOUND_TAG);
	protected static final EntityDataAccessor<String> TEXTURE_OVERRIDE = SynchedEntityData.<String>defineId(AbstractEntityCQR.class, EntityDataSerializers.STRING);

	protected BlockPos homePosition;
	protected UUID leaderUUID;
	protected LivingEntity leader;
	private int lastTickPingedAsLeader = -1000;
	protected boolean holdingPotion;
	protected byte usedPotions;
	protected double healthScale = 1.0D;
	private ItemStack prevPotion;
	private boolean prevSneaking;
	private boolean prevSitting;
	protected float sizeScaling = 1.0F;
	protected int lastTickWithAttackTarget = Integer.MIN_VALUE;
	protected int lastTimeSeenAttackTarget = Integer.MIN_VALUE;
	protected Vec3 lastPosAttackTarget = Vec3.ZERO;
	protected EntityAISpellHandler spellHandler;
	private int invisibilityTick;

	private Faction factionInstance;
	private Faction defaultFactionInstance;
	private String factionName;

	protected int lastTickShieldDisabled = Integer.MIN_VALUE;
	protected float damageBlockedWithShield;
	protected boolean armorActive;
	protected int magicArmorCooldown = 300;

	// Riding AI
	protected EntityAIRideHorse<AbstractEntityCQR> horseAI;

	// Pathing AI stuff
	protected CQRNPCPath path = new CQRNPCPath() {
		@Override
		public boolean removeNode(CQRNPCPath.PathNode node) {
			boolean flag = super.removeNode(node);
			if (flag) {
				if (AbstractEntityCQR.this.prevPathTargetPoint == node.getIndex()) {
					AbstractEntityCQR.this.prevPathTargetPoint = -1;
				} else if (AbstractEntityCQR.this.prevPathTargetPoint > node.getIndex()) {
					AbstractEntityCQR.this.prevPathTargetPoint--;
				}
				if (AbstractEntityCQR.this.currentPathTargetPoint == node.getIndex()) {
					AbstractEntityCQR.this.currentPathTargetPoint = -1;
				} else if (AbstractEntityCQR.this.currentPathTargetPoint > node.getIndex()) {
					AbstractEntityCQR.this.currentPathTargetPoint--;
				}
			}
			return flag;
		}

		@Override
		public void clear() {
			super.clear();
			AbstractEntityCQR.this.prevPathTargetPoint = -1;
			AbstractEntityCQR.this.currentPathTargetPoint = -1;
		}

		@Override
		public void copyFrom(CQRNPCPath path, BlockPos offset) {
			super.copyFrom(path, offset);
			AbstractEntityCQR.this.prevPathTargetPoint = -1;
			AbstractEntityCQR.this.currentPathTargetPoint = -1;
		}
	};
	protected int prevPathTargetPoint = -1;
	protected int currentPathTargetPoint = -1;

	private TraderOffer trades = new TraderOffer(this);
	private long lastTimedTradeRestock = 0;

	// Texture syncing
	protected ResourceLocation textureOverride;

	protected ServerBossEvent bossInfoServer;
	
	//Sizing bullshit
	protected EntityDimensions size;
	
	// Client only
	@OnlyIn(Dist.CLIENT)
	protected ESpeechBubble currentSpeechBubbleID = ESpeechBubble.BLOCK_BED;

	protected AbstractEntityCQR(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
		if (worldIn.isClientSide) {
			this.chooseNewRandomSpeechBubble();
		}
		this.xpReward = 5;
		this.initializeSize();
		this.size = new EntityDimensions(this.getBbWidth(), this.getBbHeight(), false);
	}

	public void enableBossBar() {
		if (!this.level().isClientSide && this.bossInfoServer == null) {
			this.bossInfoServer = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
			this.bossInfoServer.setVisible(CQRConfig.SERVER_CONFIG.bosses.enableBossBars.get());
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(IS_LEADER, false);
		this.entityData.define(INVISIBILITY, 0.0F);
		this.entityData.define(IS_SITTING, false);
		this.entityData.define(HAS_TARGET, false);
		this.entityData.define(TALKING, false);
		this.entityData.define(TEXTURE_INDEX, this.getTextureVariant(this.getRandom()));
		this.entityData.define(MAGIC_ARMOR_ACTIVE, false);
		this.entityData.define(SPELL_INFORMATION, 0);
		this.entityData.define(SPIN_TO_WIN, false);
		this.entityData.define(TEXTURE_OVERRIDE, "");
		this.entityData.define(FACTION_OVERRIDE_SYNC, "");

		// Shoulder entity stuff
		this.entityData.define(SHOULDER_ENTITY, new CompoundTag());
	}
	
	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		// Not wanted
	}

	//Correct method?
	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}
	//Alternative:
	@Override
	public void checkDespawn() {
		//Nope
	}

	public static AttributeSupplier.MutableAttribute createCQRAttributes() {
		AttributeSupplier.MutableAttribute map = PathfinderMob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.ATTACK_SPEED)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
		//this.getAttributeMap().registerAttribute(Attributes.ATTACK_DAMAGE);
		// speed (in blocks per second) = x^2 * 0.98 / (1 - slipperiness * 0.91) * 20 -> usually slipperiness = 0.6
		//this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		//this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
		//this.getAttributeMap().registerAttribute(Attributes.ATTACK_SPEED);
		// Default value: 16
		//this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(64.0D);
		
		return map;
	}
	
	protected void applyAttributeValues() {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
	}

	/*@Override
	protected PathNavigator createNavigation(World worldIn) {
		PathNavigator navigator = new PathNavigateGroundCQR(this, worldIn);
		((GroundPathNavigator) navigator).setCanOpenDoors(true);
		//Seems to be moved or removed in 1.16
		//((GroundPathNavigator) navigator).setBreakDoors(this.canOpenDoors());
		return navigator;
	}*/

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return this.hurt(source, amount, false);
	}

	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		// Start IceAndFire compatibility
		if (CQRConfig.SERVER_CONFIG.advanced.enableSpecialFeatures.get() && source.getEntity() != null) {
			ResourceLocation resLoc = EntityList.getKey(source.getEntity());
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				amount *= 0.5F;
			}
		}
		// End IceAndFire compatibility

		// Shoulder entity stuff
		this.spawnShoulderEntities();

		if (this.level().getLevelData().isHardcore()) {
			amount *= 0.7F;
		} else {
			Difficulty difficulty = this.level().getDifficulty();
			if (difficulty == Difficulty.HARD) {
				amount *= 0.8F;
			} else if (difficulty == Difficulty.NORMAL) {
				amount *= 0.9F;
			}
		}
		// End of shoulder entity stuff

		amount = this.handleDamageCap(source, amount);

		if (!this.level().isClientSide && amount > 0.0F && this.canBlockDamageSource(source)) {
			if (source.getDirectEntity() instanceof LivingEntity && !(source.getDirectEntity() instanceof Player) && ((LivingEntity) source.getDirectEntity()).getMainHandItem().getItem() instanceof AxeItem) {
				this.lastTickShieldDisabled = this.tickCount;
			} else {
				this.damageBlockedWithShield += amount;
				if (this.damageBlockedWithShield >= CQRConfig.SERVER_CONFIG.general.damageBlockedByShield.get()) {
					this.damageBlockedWithShield = 0.0F;
					this.lastTickShieldDisabled = this.tickCount;
				}
			}
		}

		boolean flag = super.hurt(source, amount);

		if (flag && CQRConfig.SERVER_CONFIG.mobs.armorShattersOnMobs.get()) {
			this.handleArmorBreaking();
		}

		return flag;
	}

	@Override
	public void knockback(float strength, double xRatio, double zRatio) {
		LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(this, strength, xRatio, zRatio);
		if (event.isCanceled()) {
			return;
		}
		strength = event.getStrength();
		xRatio = event.getRatioX();
		zRatio = event.getRatioZ();

		// CQR: reduce knockback strength instead of having a chance to not be knocked backed
		double knockbackResistance = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
		strength *= 1.0F - Math.min((float) knockbackResistance, 1.0F);

		Vec3 currentMovement = this.getDeltaMovement();
		
		double vX = currentMovement.x;
		double vY = currentMovement.y;
		double vZ = currentMovement.z;
		
		this.hasImpulse = true;
		double d = 1.0D / Mth.sqrt(xRatio * xRatio + zRatio * zRatio);
		vX *= 0.5D;
		vZ *= 0.5D;
		vX -= xRatio * d * strength;
		vZ -= zRatio * d * strength;

		if (this.onGround()) {
			vY *= 0.5D;
			vY += strength;

			if (vY > 0.4D) {
				vY = 0.4D;
			}
		}
		
		this.setDeltaMovement(vX, vY, vZ);
	}

	protected boolean damageCapEnabled() {
		return CQRConfig.SERVER_CONFIG.mobs.enableDamageCapForNonBossMobs.get();
	}

	protected float maxDamageInPercentOfMaxHP() {
		return (float)(double)CQRConfig.SERVER_CONFIG.mobs.maxUncappedDamageInMaxHPPercent.get();
	}

	protected float maxUncappedDamage() {
		return (float)(double)CQRConfig.SERVER_CONFIG.mobs.maxUncappedDamageForNonBossMobs.get();
	}

	private float handleDamageCap(DamageSource source, float originalAmount) {
		if (source.isCreativePlayer() || source.isBypassInvul()) {
			return originalAmount;
		}
		if (this.damageCapEnabled()) {
			return Math.min(Math.max(this.maxUncappedDamage(), this.getMaxHealth() * this.maxDamageInPercentOfMaxHP()), originalAmount);
		}
		return originalAmount;
	}

	public boolean canBlockDamageSource(DamageSource damageSourceIn) {
		if (!damageSourceIn.isBypassArmor() && this.isBlocking()) {
			Vec3 vec3d = damageSourceIn.getSourcePosition();

			if (vec3d != null) {
				Vec3 vec3d1 = this.getEyePosition(1.0F);
				Vec3 vec3d2 = this.position().subtract(vec3d).normalize();
				vec3d2 = new Vec3(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dot(vec3d1) < 0.0D) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void die(DamageSource cause) {
		if (this.isHoldingPotion()) {
			this.swapWeaponAndPotionSlotItemStacks();
		}
		Item item = this.getMainHandItem().getItem();
		if (item instanceof IFakeWeapon<?>) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
		}

		super.die(cause);

		this.updateReputationOnDeath(cause);
	}

	@Override
	protected void registerGoals() {
		if (CQRConfig.SERVER_CONFIG.advanced.debugAI.get()) {
			//TODO: AI Selectors are final now, change this or not?
			//this.goalSelector = new EntityAITasksProfiled((IProfiler) this.level.getProfiler(), this.level);
			//this.targetSelector = new EntityAITasksProfiled((IProfiler) this.level.getProfiler(), this.level);
		}
		this.spellHandler = this.createSpellHandler();
		this.goalSelector.addGoal(0, new RandomSwimmingGoal(this));
		this.goalSelector.addGoal(1, new EntityAIOpenCloseDoor(this));
		// TODO disabled for now as it doesn't work properly
		// this.tasks.addTask(2, new EntityAISneakUnderSmallObstacle<AbstractEntityCQR>(this));

		if (this.canMountEntity()) {
			this.horseAI = new EntityAIRideHorse<>(this, 1.5);
			this.goalSelector.addGoal(8, this.horseAI);
		}
		this.goalSelector.addGoal(9, new EntityAIHealingPotion(this));
		this.goalSelector.addGoal(11, this.spellHandler);
		this.goalSelector.addGoal(12, new EntityAIAttackSpecial(this));
		this.goalSelector.addGoal(13, new EntityAIAttackRanged<>(this));
		this.goalSelector.addGoal(14, new EntityAIPotionThrower(this));
		this.goalSelector.addGoal(15, new EntityAIFireball(this));
		this.goalSelector.addGoal(16, new EntityAIHooker(this));
		this.goalSelector.addGoal(17, new EntityAIBackstab(this));
		this.goalSelector.addGoal(18, new EntityAIAttack(this));
		this.goalSelector.addGoal(19, new EntityAICursedBoneSummoner(this));

		this.goalSelector.addGoal(20, new EntityAIFollowAttackTarget(this));
		this.goalSelector.addGoal(22, new EntityAIFireFighter(this));
		//this.goalSelector.addGoal(23, new EntityAITorchIgniter(this));
		this.goalSelector.addGoal(24, new EntityAILooter(this));
		//this.goalSelector.addGoal(25, new EntityAITameAndLeashPet(this));
		this.goalSelector.addGoal(26, new EntityAISearchMount(this));

		this.goalSelector.addGoal(30, new EntityAIMoveToLeader(this));
		this.goalSelector.addGoal(31, new EntityAIFollowPath(this));
		this.goalSelector.addGoal(32, new EntityAIMoveToHome(this));
		this.goalSelector.addGoal(33, new EntityAIIdleSit(this));

		// Electrocution stuff
		// this.tasks.addTask(10, new EntityAIPanicElectrocute(this, 2.0D));
		// this.tasks.addTask(10, new EntityAIPanicFire(this, 2.0D));
		// this.tasks.addTask(3, new EntityAIAvoidEntity<EntityLivingBase>(this, EntityLivingBase.class,
		// TargetUtil.PREDICATE_IS_ELECTROCUTED, 8.0F, 1.5D, 2.0D));
		// this.tasks.addTask(2, new EntityAIAvoidEntity<EntityElectricField>(this, EntityElectricField.class, 1.0F, 1.5D,
		// 1.5D));

		this.targetSelector.addGoal(0, new EntityAICQRNearestAttackTarget(this));
		this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this));
	}
	
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, MobSpawnType p_213386_3_, ILivingEntityData setDamageValue, CompoundTag p_213386_5_) {
		this.setHealingPotions(1);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, new ItemStack(CQRItems.BADGE.get()));
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			this.setDropChance(slot, 0.04F);
		}

		//Apply base attributes
		this.applyAttributeValues();
		
		this.setHealth(this.getMaxHealth());
		
		return super.finalizeSpawn(p_213386_1_, difficulty, p_213386_3_, setDamageValue, p_213386_5_);
	}
	
	@Override
	protected void dropEquipment() {
		//Nope
	}
	
	protected void dropEquipmentCQR(int lootingModifier, boolean wasRecentlyHit) {
		ItemStack stack = this.getMainHandItem();
		if (!stack.isEmpty() && stack.getItem() instanceof BowItem) {
			ItemStack stack1 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.ARROW);
			if (stack1.isEmpty()) {
				stack1 = new ItemStack(Items.ARROW, this.getRandom().nextInt(3));
			} else {
				stack1 = stack1.copy();
				stack1.setCount(this.getRandom().nextInt(3));
			}
			this.spawnAtLocation(stack1, 0.0F);
		}

		double modalValue = CQRConfig.SERVER_CONFIG.mobs.dropDurabilityModalValue.get();
		double standardDeviation = CQRConfig.SERVER_CONFIG.mobs.dropDurabilityStandardDeviation.get();
		double min = Math.min(CQRConfig.SERVER_CONFIG.mobs.dropDurabilityMinimum.get(), modalValue);
		double max = Math.max(CQRConfig.SERVER_CONFIG.mobs.dropDurabilityMaximum.get(), modalValue);

		for (EquipmentSlot entityequipmentslot : EquipmentSlot.values()) {
			ItemStack itemstack = this.getItemBySlot(entityequipmentslot);
			double d0 = this.getDropChance(entityequipmentslot);
			boolean flag = d0 > 1.0D;

			boolean backpackflag = false;
			if (itemstack.getItem() instanceof ItemBackpack) {
				LazyOptional<IItemHandler> invOpt = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if(invOpt.isPresent()) {
					IItemHandler ih = invOpt.resolve().get();
					for (int i = 0; i < ih.getSlots(); i++) {
						if (!ih.getStackInSlot(i).isEmpty()) {
							backpackflag = true;
							break;
						}
					}
				}
			}

			if (backpackflag || (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (wasRecentlyHit || flag) && this.random.nextFloat() - lootingModifier * 0.01F < d0)) {
				if (!flag && itemstack.isDamageableItem() && !backpackflag) {
					double durability = modalValue + Mth.clamp(this.random.nextGaussian() * standardDeviation, min - modalValue, max - modalValue);
					itemstack.setDamageValue((int) (itemstack.getMaxDamage() * (1.0D - durability)));
				}

				this.spawnAtLocation(itemstack, 0.0F);
			}
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if (this.homePosition != null) {
			compound.put("home", NbtUtils.writeBlockPos(this.homePosition));
		}

		if (this.leaderUUID != null) {
			compound.put("leader", NbtUtils.createUUID(this.leaderUUID));
		}
		if (this.factionName != null && !this.factionName.equalsIgnoreCase(this.getDefaultFaction().name())) {
			compound.putString("factionOverride", this.factionName);
		}
		compound.putInt("textureIndex", this.entityData.get(TEXTURE_INDEX));
		compound.putByte("usedHealingPotions", this.usedPotions);
		// compound.setFloat("sizeScaling", this.sizeScaling);
		this.callOnWriteToNBT(compound);
		// compound.setBoolean("isSitting", this.dataManager.get(IS_SITTING));
		compound.putBoolean("holdingPotion", this.holdingPotion);
		compound.putDouble("healthScale", this.healthScale);

		CompoundTag pathTag = new CompoundTag();
		pathTag.put("path", this.path.writeToNBT());
		pathTag.putInt("prevPathTargetPoint", this.prevPathTargetPoint);
		pathTag.putInt("currentPathTargetPoint", this.currentPathTargetPoint);
		compound.put("pathTag", pathTag);

		// Shoulder entity stuff
		if (!this.getLeftShoulderEntity().isEmpty()) {
			compound.put("ShoulderEntityLeft", this.getLeftShoulderEntity());
		}

		compound.put("trades", this.trades.writeToNBT(new CompoundTag()));
		compound.putLong("lastTimedRestockTime", this.getLastTimedRestockTime());
		
		if (this.hasTextureOverride()) {
			compound.putString("textureOverride", this.getTextureOverride().toString());
		}

		if (this.bossInfoServer != null) {
			compound.putBoolean("hasBossBar", true);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		if (compound.contains("home")) {
			this.homePosition = NbtUtils.readBlockPos(compound.getCompound("home"));
		}

		if (compound.contains("leader")) {
			this.leaderUUID = NbtUtils.loadUUID(compound.getCompound("leader"));
		}

		if (compound.contains("factionOverride")) {
			this.setFaction(compound.getString("factionOverride"));
		}

		this.entityData.set(TEXTURE_INDEX, compound.getInt("textureIndex"));
		this.usedPotions = compound.getByte("usedHealingPotions");
		// this.sizeScaling = compound.hasKey("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F;
		this.callOnReadFromNBT(compound);
		// this.dataManager.set(IS_SITTING, compound.getBoolean("isSitting"));
		this.holdingPotion = compound.getBoolean("holdingPotion");
		this.setHealthScale(compound.contains("healthScale", Tag.TAG_DOUBLE) ? compound.getDouble("healthScale") : 1.0D);

		if (compound.contains("pathingAI", Tag.TAG_COMPOUND)) {
			CompoundTag pathTag = compound.getCompound("pathingAI");
			ListTag nbtTagList = pathTag.getList("pathPoints", Tag.TAG_COMPOUND);
			this.path.clear();
			for (int i = 0; i < nbtTagList.size(); i++) {
				BlockPos pos = NbtUtils.readBlockPos(nbtTagList.getCompound(i));
				this.path.addNode(this.path.getNode(this.path.getSize() - 1), pos, 0, 0, 0.0F, 1, 0, 24000, true);
			}
			this.currentPathTargetPoint = pathTag.getInt("currentPathPoint");
			if (nbtTagList.size() > 1) {
				if (this.currentPathTargetPoint > 0) {
					this.prevPathTargetPoint = this.currentPathTargetPoint - 1;
				} else {
					this.prevPathTargetPoint = nbtTagList.size();
				}
			} else {
				this.prevPathTargetPoint = -1;
			}
		} else if (compound.contains("pathTag", Tag.TAG_COMPOUND)) {
			CompoundTag pathTag = compound.getCompound("pathTag");
			this.path.readFromNBT(pathTag.getCompound("path"));
			this.prevPathTargetPoint = pathTag.getInt("prevPathTargetPoint");
			this.currentPathTargetPoint = pathTag.getInt("currentPathTargetPoint");
		}

		// Shoulder entity stuff
		if (compound.contains("ShoulderEntityLeft", 10)) {
			this.setLeftShoulderEntity(compound.getCompound("ShoulderEntityLeft"));
		}

		this.trades.readFromNBT(compound.getCompound("trades"));
		if(compound.contains("lastTimedRestockTime", Tag.TAG_LONG)) {
			this.lastTimedTradeRestock = compound.getLong("lastTimedRestockTime");
		}

		if (compound.contains("textureOverride", Tag.TAG_STRING)) {
			String ct = compound.getString("textureOverride");
			if (!ct.isEmpty()) {
				this.setCustomTexture(new ResourceLocation(ct));
			}
		}

		if (compound.contains("hasBossBar")) {
			this.enableBossBar();
		}

		if (this.hasCustomName() && this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.getItemInHand(hand).getItem() instanceof NameTagItem) {
			return super.mobInteract(player, hand);
		}

		boolean flag = false;

		if(player instanceof ServerPlayer && !this.level().isClientSide()) {
			ServerPlayer spe = (ServerPlayer) player;
			if (!player.isCrouching()) {
				if (player.isCreative() || this.getLeader() == player) {
					if (!this.level().isClientSide) {
						//player.openGui(CQRMain.INSTANCE, GuiHandler.CQR_ENTITY_GUI_ID, this.level, this.getId(), 0, 0);
						CQRNetworkHooks.openGUI(spe, this.getDisplayName(), buf -> buf.writeInt(this.getId()), CQRContainerTypes.CQR_ENTITY_EDITOR.get());
					}
					flag = true;
				} else if (!this.getFaction().isEnemy(player) && this.hasTrades()) {
					if (!this.level().isClientSide) {
						//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, this.level, this.getId(), 0, 0);
						CQRNetworkHooks.openGUI(spe, this.getDisplayName(), buf -> buf.writeInt(this.getId()), CQRContainerTypes.MERCHANT.get());
					}
					flag = true;
				}
			} else if (player.isCreative() || (!this.getFaction().isEnemy(player) && this.hasTrades())) {
				if (!this.level().isClientSide) {
					//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, this.level, this.getId(), 0, 0);
					CQRNetworkHooks.openGUI(spe, this.getDisplayName(), buf -> buf.writeInt(this.getId()), CQRContainerTypes.MERCHANT.get());
				}
				flag = true;
			}
		}

		if (flag && !this.getLookControl().isLookingAtTarget() && !this.isPathFinding()) {
			double x1 = player.position().x() - this.position().x();
			double z1 = player.position().z() - this.position().z();
			float yaw = (float) Math.toDegrees(Math.atan2(-x1, z1));
			this.yBodyRot = yaw;
			this.yHeadRot = yaw;
			//Old 1.12.2
			//this.renderYawOffset = yaw;
			this.rotOffs = yaw;
		}

		return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	public boolean hasTrades() {
		return !this.trades.isEmpty();
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource deathCause, int lootingModifier, boolean wasRecentlyHit) {
		ResourceLocation resourcelocation = this.getLootTable();
		if (resourcelocation != null) {
			LootTable lootTable = this.level().getServer().getLootTables().get(resourcelocation);
			LootContext.Builder lootContextBuilder =  this.createLootContext(wasRecentlyHit, deathCause);//new LootContext.Builder((ServerWorld) this.level).withParameter(LootParameters.THIS_ENTITY, this).withOptionalParameter(LootParameters.DAMAGE_SOURCE, deathCause);
			if (wasRecentlyHit && this.lastHurtByPlayer != null) {
				lootContextBuilder = lootContextBuilder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
			}

			for (ItemStack itemstack : lootTable.getRandomItems(lootContextBuilder.withRandom(this.random).create(LootParameterSets.ENTITY))) {
				this.spawnAtLocation(itemstack, 0.0F);
			}
		}

		this.dropBadgeContentOnDeath();
		this.dropEquipmentCQR(lootingModifier, wasRecentlyHit);
	}

	protected void dropBadgeContentOnDeath() {
		ItemStack badge = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.BADGE);
		if (badge.getItem() instanceof ItemBadge) {
			LazyOptional<IItemHandler> capabilityOpt = badge.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			capabilityOpt.ifPresent(capability -> {
				for (int i = 0; i < capability.getSlots(); i++) {
					this.spawnAtLocation(capability.getStackInSlot(i), 0.0F);
				}
			});
		}
	}
	
	@Override
	public void tick() {
		LivingEntity attackTarget = this.getTarget();
		if (attackTarget != null) {
			this.lastTickWithAttackTarget = this.tickCount;
			if (this.isInSightRange(attackTarget) && this.getSensing().hasLineOfSight(attackTarget)) {
				this.lastTimeSeenAttackTarget = this.tickCount;
			}
			if (this.lastTimeSeenAttackTarget + 100 >= this.tickCount) {
				this.lastPosAttackTarget = attackTarget.position();
			}
		}

		if (this.lastTickWithAttackTarget + 60 < this.tickCount && this.damageBlockedWithShield > 0.0F) {
			this.damageBlockedWithShield = Math.max(this.damageBlockedWithShield - 0.02F, 0.0F);
		}

		ItemStack stackMainhand = this.getMainHandItem();
		if (!stackMainhand.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_SPEED)) {
			AttributeInstance attribute = this.getAttribute(Attributes.ATTACK_SPEED);
			if (attribute.getModifier(BASE_ATTACK_SPEED_ID) == null) {
				AttributeModifier modifier = new AttributeModifier(BASE_ATTACK_SPEED_ID, "Base Attack Speed", -2.4D, Operation.ADDITION);
				modifier.save();
				attribute.addPermanentModifier(modifier);
			}
		} else {
			AttributeInstance attribute = this.getAttribute(Attributes.ATTACK_SPEED);
			attribute.removeModifier(BASE_ATTACK_SPEED_ID);
		}

		super.tick();

		if (!this.level().isClientSide && this.isMagicArmorActive()) {
			this.updateCooldownForMagicArmor();
		}
		if (!this.level().isClientSide && !this.isNonBoss() && this.level().getDifficulty() == Difficulty.PEACEFUL) {
			SpawnerFactory.placeSpawner(new Entity[] { this }, false, null, this.level(), this.blockPosition());
			this.discard();
		}

		ItemStack stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		if (!this.level().isClientSide && stack != this.prevPotion) {
			CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SPacketItemStackSync(this.getId(), EntityEquipmentExtraSlot.POTION.getIndex(), stack));
		}
		this.prevPotion = stack;

		if (this.isCrouching() && !this.prevSneaking) {
			this.setPose(Pose.CROUCHING);
			//this.resize(1.0F, 0.8F);
		} else if (!this.isCrouching() && this.prevSneaking) {
			this.setPose(Pose.STANDING);
			//this.resize(1.0F, 1.25F);
		}
		if (this.isSitting() && !this.prevSitting) {
			//TODO: Create sitting pose
			//this.resize(1.0F, 0.75F);
			//Handled in GetSize directly
		} else if (!this.isSitting() && this.prevSitting) {
			this.setPose(Pose.STANDING);
			//this.resize(1.0F, 4.0F / 3.0F);
		}
		this.prevSneaking = this.isCrouching();
		this.prevSitting = this.isSitting();

		if (!this.level().isClientSide) {
			int spellInformation = 0;
			if (this.spellHandler != null) {
				if (this.spellHandler.isSpellCharging()) {
					spellInformation = spellInformation | (1 << 26);
				}
				if (this.spellHandler.isSpellCasting()) {
					spellInformation = spellInformation | (1 << 25);
				}
				if (this.spellHandler.getActiveSpell() instanceof IEntityAISpellAnimatedVanilla) {
					IEntityAISpellAnimatedVanilla spell = (IEntityAISpellAnimatedVanilla) this.spellHandler.getActiveSpell();
					spellInformation = spellInformation | (1 << 24);
					spellInformation = spellInformation | (((int) (spell.getRed() * 255.0D) & 255) << 16);
					spellInformation = spellInformation | (((int) (spell.getGreen() * 255.0D) & 255) << 8);
					spellInformation = spellInformation | ((int) (spell.getBlue() * 255.0D) & 255);
				}
			}
			this.entityData.set(SPELL_INFORMATION, spellInformation);
		} else {
			if (this.isSpellCharging() && this.isSpellAnimated()) {
				int spellColor = this.entityData.get(SPELL_INFORMATION);
				double red = ((spellColor >> 16) & 255) / 255.0D;
				double green = ((spellColor >> 8) & 255) / 255.0D;
				double blue = (spellColor & 255) / 255.0D;
				float f = /* OLD: renderYawOffset */this.rotOffs * 0.017453292F + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
				float f1 = Mth.cos(f);
				float f2 = Mth.sin(f);
				this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) f1 * (double) this.getBbWidth(), this.getY() + this.getBbHeight(), this.getZ() + (double) f2 * (double) this.getBbWidth(), red, green, blue);
				this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double) f1 * (double) this.getBbWidth(), this.getY() + this.getBbHeight(), this.getZ() - (double) f2 * (double) this.getBbWidth(), red, green, blue);
			}
			if (this.isChatting() && this.tickCount % LayerCQRSpeechbubble.CHANGE_BUBBLE_INTERVAL == 0) {
				this.chooseNewRandomSpeechBubble();
			}
		}

		this.updateInvisibility();
		this.updateLeader();
		this.updateTradeRestockTimer();
	}
	
	protected boolean isNonBoss() {
		return !this.isBoss();
	}
	
	public boolean isBoss() {
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundSource getSoundSource() {
		return SoundSource.HOSTILE;
	}

	@Override
	public void aiStep() {
		this.updateSwingTime();//Only called in aiStep if parent class is MonsterEntity
		super.aiStep();

		// Bossbar
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.addPlayer(player);
		}
	}
	
	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.removePlayer(player);
		}
	}

	protected void setCustomName(String name) {
		this.setCustomName(new TextComponent(name));
	}
	
	@Override
	public void setCustomName(Component name) {
		super.setCustomName(name);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.GENERIC_SWIM;
	}

	@Override
	protected SoundEvent getSwimSplashSound() {
		return SoundEvents.GENERIC_SPLASH;
	}

	@Override
	protected final SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.canBlockDamageSource(damageSourceIn) ? SoundEvents.SHIELD_BLOCK : this.getDefaultHurtSound(damageSourceIn);
	}

	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.HOSTILE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.HOSTILE_DEATH;
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		// Shoulder entity stuff
		this.spawnShoulderEntities();
		
		if (this.getMainHandItem().getItem() instanceof ItemStaffHealing) {
			if (entityIn instanceof LivingEntity) {
				if (!this.level().isClientSide) {
					((LivingEntity) entityIn).heal(ItemStaffHealing.HEAL_AMOUNT_ENTITIES);
					entityIn.setSecondsOnFire(0);
					Vec3 pos = entityIn.position();
					//TODO: Check if this works correctly
					((ServerLevel) this.level()).addParticle(
							ParticleTypes.HEART,
							true,
							pos.x,
							pos.y + entityIn.getBbHeight() * 0.5D, 
							pos.z, 
							0.25D, 
							0.25D, 
							0.25D 
					);
					this.level().playSound(null, pos.y, pos.y + entityIn.getBbHeight() * 0.5D, pos.z, CQRSounds.MAGIC, SoundSource.MASTER, 0.6F, 0.6F + this.random.nextFloat() * 0.2F);
				}
				return true;
			}
			return false;
		}
		float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entityIn).getMobType());
			i += EnchantmentHelper.getKnockbackBonus(this);
		}
		// Start IceAndFire compatibility
		if (CQRConfig.SERVER_CONFIG.advanced.enableSpecialFeatures.get()) {
			ResourceLocation resLoc = EntityList.getKey(entityIn);
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				f *= 2.0F;
			}
		}
		// End IceAndFire compatibility
		//boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
		boolean flag = super.doHurtTarget(entityIn);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).knockback(i * 0.5F, Mth.sin(this.yBodyRot * 0.017453292F), (-Mth.cos(this.yBodyRot * 0.017453292F)));
			
				Vec3 deltaNew = this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D);
				
				this.setDeltaMovement(deltaNew);
			}

			int j = EnchantmentHelper.getFireAspect(this);

			if (j > 0) {
				entityIn.setSecondsOnFire(j * 4);
			}

			if (entityIn instanceof Player) {
				//Handles shield stuff, however there is a method in the parent class, we might want to use that?
				Player entityplayer = (Player) entityIn;
				ItemStack itemstack = this.getMainHandItem();
				ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
					float f1 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

					if (this.random.nextFloat() < f1) {
						entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
						this.level().broadcastEntityEvent(entityplayer, (byte) 30);
					}
				}
			}

			this.doEnchantDamageEffects(this, entityIn);
			//Wasn't present in cqr but in super class
			this.setLastHurtMob(entityIn);
		}

		return flag;
	}
	
	@Override
	public void swing(InteractionHand p_184609_1_) {
		super.swing(p_184609_1_, !this.level().isClientSide);
	}

	@Override
	protected boolean shouldDropLoot() {
		return true;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		// buffer.writeFloat(this.getSizeVariation());
		buffer.writeFloat(this.getSizeVariation());
		buffer.writeDouble(this.getHealthScale());
		buffer.writeFloat(this.getDropChance(EquipmentSlot.HEAD));
		buffer.writeFloat(this.getDropChance(EquipmentSlot.CHEST));
		buffer.writeFloat(this.getDropChance(EquipmentSlot.LEGS));
		buffer.writeFloat(this.getDropChance(EquipmentSlot.FEET));
		buffer.writeFloat(this.getDropChance(EquipmentSlot.MAINHAND));
		buffer.writeFloat(this.getDropChance(EquipmentSlot.OFFHAND));
		buffer.writeItemStack(this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION), true);
		buffer.writeNbt(this.trades.writeToNBT(new CompoundTag()));
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		// this.setSizeVariation(additionalData.readFloat());
		this.setSizeVariation(additionalData.readFloat());
		this.setHealthScale(additionalData.readDouble());
		this.setDropChance(EquipmentSlot.HEAD, additionalData.readFloat());
		this.setDropChance(EquipmentSlot.CHEST, additionalData.readFloat());
		this.setDropChance(EquipmentSlot.LEGS, additionalData.readFloat());
		this.setDropChance(EquipmentSlot.FEET, additionalData.readFloat());
		this.setDropChance(EquipmentSlot.MAINHAND, additionalData.readFloat());
		this.setDropChance(EquipmentSlot.OFFHAND, additionalData.readFloat());
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, additionalData.readItem());
		this.trades.readFromNBT(additionalData.readNbt());
		this.chooseNewRandomSpeechBubble();
	}

	// #################### Chocolate Quest Repoured ####################

	public void updateLeader() {
		if (this.level().isClientSide) {
			return;
		}
		// sync with clients that this is a leader
		this.entityData.set(IS_LEADER, this.tickCount - this.lastTickPingedAsLeader < 200);
		if (this.leaderUUID == null) {
			this.leader = null;
			return;
		}
		if (this.leader == null) {
			if ((this.getId() + this.tickCount) % 20 != 0) {
				return;
			}
			Entity entity = EntityUtil.getEntityByUUID(this.level(), this.leaderUUID);
			if (entity instanceof LivingEntity) {
				this.leader = (LivingEntity) entity;
			}
		} else if (!this.leader.isAddedToWorld()) {
			this.leader = null;
		} else if (!this.leader.isAlive()) {
			this.leaderUUID = null;
			this.leader = null;
		}
		// ping leader that a follower exists
		if (this.leader instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) this.leader).lastTickPingedAsLeader = this.leader.tickCount;
		}
	}

	public LivingEntity getLeader() {
		return this.leader;
	}

	public void setLeader(LivingEntity leader) {
		if (this.level().isClientSide) {
			return;
		}
		if (leader == null) {
			this.leaderUUID = null;
			this.leader = null;
		} else {
			this.leaderUUID = leader.getUUID();
			this.leader = leader;
		}
	}

	public boolean hasLeader() {
		return this.getLeader() != null;
	}

	public boolean isLeader() {
		return this.entityData.get(IS_LEADER);
	}

	public BlockPos getHomePositionCQR() {
		return this.homePosition;
	}

	public void setHomePositionCQR(BlockPos homePosition) {
		this.homePosition = homePosition;
	}

	public boolean hasHomePositionCQR() {
		return this.getHomePositionCQR() != null;
	}

	public abstract double getBaseHealth();

	public void setBaseHealthDependingOnPos(BlockPos pos) {
		if (CQRConfig.SERVER_CONFIG.mobs.enableHealthChangeOnDistance.get() && !this.level().isClientSide) {
			double x = (double) pos.getX() - DungeonGenUtils.getSpawnX(this.level());
			double z = (double) pos.getZ() - DungeonGenUtils.getSpawnZ(this.level());
			double distance = Math.sqrt(x * x + z * z);
			double amount = 0.1D * (int) (distance / CQRConfig.SERVER_CONFIG.mobs.distanceDivisor.get());

			EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_DISTANCE_TO_SPAWN_ID, "Health Scale Distance To Spawn", amount);
		}
	}

	public void handleArmorBreaking() {
		if (!this.level().isClientSide && this.usedPotions + 1 > this.getHealingPotions()) {
			boolean armorBroke = false;
			float hpPrcntg = this.getHealth() / this.getMaxHealth();
			float[] thresholds = { 0.8F, 0.6F, 0.4F, 0.2F };
			EquipmentSlot[] slots = { EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.CHEST };

			for (int i = 0; i < 4; i++) {
				if (hpPrcntg > thresholds[i]) {
					break;
				}
				if (this.getItemBySlot(slots[i]).isEmpty()) {
					continue;
				}
				this.setItemSlot(slots[i], ItemStack.EMPTY);
				armorBroke = true;
			}

			if (armorBroke) {
				this.playSound(SoundEvents.ITEM_BREAK, 1.75F, 0.8F);
			}
		}
	}

	public int getHealingPotions() {
		ItemStack stack = this.getHeldItemPotion();
		if (stack.getItem() instanceof ItemPotionHealing) {
			return stack.getCount();
		}
		return 0;
	}

	public void setHealingPotions(int amount) {
		ItemStack stack = new ItemStack(CQRItems.POTION_HEALING.get(), amount);
		if (this.holdingPotion) {
			this.setItemSlot(EquipmentSlot.MAINHAND, stack);
		} else {
			this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, stack);
		}
	}

	public ItemStack getItemStackFromExtraSlot(EntityEquipmentExtraSlot slot) {
		LazyOptional<CapabilityExtraItemHandler> lOpCap = this.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
		if(lOpCap.isPresent()) {
			return lOpCap.resolve().get().getStackInSlot(slot.getIndex());
		}
		return ItemStack.EMPTY;
	}

	public void setItemStackToExtraSlot(EntityEquipmentExtraSlot slot, ItemStack stack) {
		LazyOptional<CapabilityExtraItemHandler> capability = this.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
		capability.ifPresent(capa -> capa.setStackInSlot(slot.getIndex(), stack));
	}

	public void swapWeaponAndPotionSlotItemStacks() {
		ItemStack stack1 = this.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack stack2 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		this.setItemSlot(EquipmentSlot.MAINHAND, stack2);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, stack1);
		this.holdingPotion = !this.holdingPotion;
	}

	public boolean isHoldingPotion() {
		return this.holdingPotion;
	}

	protected abstract EDefaultFaction getDefaultFaction();

	public Faction getDefaultFactionInstance() {
		if (this.defaultFactionInstance == null) {
			this.defaultFactionInstance = FactionRegistry.instance(this).getFactionInstance(this.getDefaultFaction().name());
		}
		return this.defaultFactionInstance;
	}

	@Nullable
	public Faction getFaction() {
		if (!this.level().isClientSide) {
			// Leader faction is set when assigning the leader
			// if (this.hasLeader()) { return FactionRegistry.instance().getFactionOf(this.getLeader()); }
			if (this.factionInstance == null && this.factionName != null && !this.factionName.isEmpty()) {
				this.factionInstance = FactionRegistry.instance(this).getFactionInstance(this.factionName);
			}
			if (this.factionInstance != null) {
				return this.factionInstance;
			}
		} else {
			String syncedFaction = this.entityData.get(FACTION_OVERRIDE_SYNC);
			if (syncedFaction != null && !syncedFaction.isEmpty() && ((this.factionName == null) || !this.factionName.equals(syncedFaction))) {
				this.factionName = syncedFaction;
				this.factionInstance = FactionRegistry.instance(this).getFactionInstance(syncedFaction);
			}
			if (this.factionInstance != null) {
				return this.factionInstance;
			}
		}
		return this.getDefaultFactionInstance();
	}

	public void setFaction(ResourceLocation newFac) {
		this.setFaction(newFac, false);
	}

	public void setFaction(String newFac, boolean ignoreCTS) {
		// TODO: Update faction on client too!!
		if (!this.level().isClientSide) {
			Faction faction = FactionRegistry.instance(this).getFactionInstance(newFac);
			if (faction != null) {
				this.factionInstance = null;
				this.factionName = newFac;
				if (!ignoreCTS) {
					ResourceLocation rs = faction.getRandomTextureFor(this);
					if (rs != null) {
						this.setCustomTexture(rs);
					}
				}
				this.entityData.set(FACTION_OVERRIDE_SYNC, newFac);
			}
		}
	}

	@Override
	public void setCustomTexture(@Nonnull ResourceLocation texture) {
		this.entityData.set(TEXTURE_OVERRIDE, texture.toString());
	}

	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getEntity() instanceof Player && this.hasFaction() && !this.level().isClientSide) {
			Player player = (Player) cause.getEntity();
			double range = CQRConfig.SERVER_CONFIG.mobs.factionUpdateRadius.get();
			final Vec3 rangeVec = new Vec3(range, range, range);
			AABB aabb = new AABB(player.position().subtract(rangeVec), player.position().add(rangeVec));

			List<Faction> checkedFactions = new ArrayList<>();
			// boolean setRepu = false;
			for (AbstractEntityCQR cqrentity : this.level().getEntitiesOfClass(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && !checkedFactions.contains(cqrentity.getFaction()) && (cqrentity.hasLineOfSight(this) || cqrentity.hasLineOfSight(player))) {
					Faction faction = cqrentity.getFaction();
					if (this.getFaction().equals(faction)) {
						// DONE decrement the players repu on this entity's faction
						faction.decrementReputation(player, faction.getRepuMemberKill());
						// setRepu = true;
					} else if (this.getFaction().isEnemy(faction)) {
						// DONE increment the players repu at CQREntity's faction
						faction.incrementReputation(player, faction.getRepuEnemyKill());
						// setRepu = true;
					} else if (this.getFaction().isAlly(faction)) {
						// DONE decrement the players repu on CQREntity's faction
						faction.decrementReputation(player, faction.getRepuAllyKill());
						// setRepu = true;
					}
					checkedFactions.add(faction);
				}
			}
		}
	}

	public void onExportFromWorld() {
		for (CQRNPCPath.PathNode node : this.path.getNodes()) {
			node.setPos(node.getPos().subtract(this.blockPosition()));
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon(DungeonPlacement placement) {
		this.setHomePositionCQR(this.blockPosition());
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
		this.setHealth(this.getMaxHealth());
		this.setBaseHealthDependingOnPos(placement.getPos());

		//Reset lastTimedRestockTick
		this.setLastTimedRestockTime(this.level().getGameTime());
		
		// Recalculate path points
		for (CQRNPCPath.PathNode node : this.path.getNodes()) {
			node.setPos(DungeonPlacement.transform(node.getPos().getX(), node.getPos().getY(), node.getPos().getZ(), BlockPos.ZERO, placement.getMirror(), placement.getRotation()));
			node.setWaitingRotation(getTransformedYaw(node.getWaitingRotation(), placement.getMirror(), placement.getRotation()));
		}

		// Replace shield
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = this.getItemBySlot(slot);
			Item item = stack.getItem();
			if (item instanceof ItemShieldDummy && placement.getInhabitant() != null) {
				this.setItemSlot(slot, new ItemStack(placement.getInhabitant().getShieldReplacement(), 1));
			}
		}
		if (placement.getInhabitant() != null && placement.getInhabitant().getFactionOverride() != null && !placement.getInhabitant().getFactionOverride().isEmpty() && FactionRegistry.instance(this).getFactionInstance(placement.getInhabitant().getFactionOverride()) != null) {
			this.setFaction(placement.getInhabitant().getFactionOverride());
		}
	}

	private static float getTransformedYaw(float rotationYaw, Mirror mirror, Rotation rotation) {
		float f = Mth.wrapDegrees(rotationYaw);
		switch (mirror) {
		case LEFT_RIGHT:
			f = 180.0F - f;
			break;
		case FRONT_BACK:
			f = -f;
			break;
		default:
			break;
		}
		switch (rotation) {
		case CLOCKWISE_90:
			f += 90.0F;
			break;
		case CLOCKWISE_180:
			f += 180.0F;
			break;
		case COUNTERCLOCKWISE_90:
			f -= 90.0F;
			break;
		default:
			break;
		}
		return Mth.wrapDegrees(f);
	}

	public boolean hasCape() {
		return false;
	}

	public ResourceLocation getResourceLocationOfCape() {
		return null;
	}

	public void setSitting(boolean sitting) {
		this.entityData.set(IS_SITTING, sitting);
	}

	public boolean isSitting() {
		if (this.hasTrades()) {
			return false;
		}
		return this.entityData.get(IS_SITTING);
	}

	public void setChatting(boolean chatting) {
		this.entityData.set(TALKING, chatting);
	}

	public boolean isChatting() {
		return this.entityData.get(TALKING);
	}

	@OnlyIn(Dist.CLIENT)
	public ESpeechBubble getCurrentSpeechBubble() {
		return this.currentSpeechBubbleID;
	}

	@OnlyIn(Dist.CLIENT)
	public void chooseNewRandomSpeechBubble() {
		if (this.hasTrades()) {
			this.currentSpeechBubbleID = ESpeechBubble.getRandTraderBubble(this.random);
		} else {
			this.currentSpeechBubbleID = ESpeechBubble.getRandNormalBubble(this.random);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getTextureIndex() {
		return this.entityData.get(TEXTURE_INDEX);
	}

	public double getAttackReach(LivingEntity target) {
		double reach = ((double) this.getBbWidth() + (double) target.getBbWidth()) * 0.5D + 0.85D;
		ItemStack stack = this.getMainHandItem();
		/*if (stack.getItem() instanceof ItemSpearBase) {
			reach += ((ItemSpearBase) stack.getItem()).getReach();
		}*/
		if(stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(ForgeMod.ENTITY_REACH.get())) {
			for(AttributeModifier am : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(ForgeMod.ENTITY_REACH.get())) {
				reach += am.getAmount();
			}
		}
		return reach;
	}

	public boolean isInAttackReach(LivingEntity target) {
		return this.isInReach(target, this.getAttackReach(target));
	}

	public boolean isInReach(LivingEntity target, double distance) {
		final Vec3 targetPos = target.position();
		final Vec3 myPos = this.position();
		double x =targetPos.x - myPos.x;
		double y;
		if (targetPos.y + target.getBbHeight() < myPos.y) {
			y = (targetPos.y + target.getBbHeight()) - myPos.y;
			y *= 1.0D + ((double) this.getEyeHeight() / this.getBbHeight());
		} else if (targetPos.y > myPos.y + this.getBbHeight()) {
			y = targetPos.y - (myPos.y + this.getBbHeight());
			y *= 1.0D + (((double) this.getBbHeight() - this.getEyeHeight()) / this.getBbHeight());
		} else {
			y = 0.0D;
		}
		double z = targetPos.z - myPos.z;
		return x * x + y * y + z * z <= distance * distance;
	}

	public boolean canStrafe() {
		if (this.horseAI != null) {
			return this.getVehicle() == null;
		}
		return this.getControllingPassenger() == null;
	}

	public boolean canOpenDoors() {
		return true;
	}

	public boolean canPutOutFire() {
		return true;
	}

	public boolean canIgniteTorch() {
		return true;
	}

	public boolean canTameEntity() {
		return true;
	}

	public boolean canMountEntity() {
		return true;
	}

	public boolean isEntityInFieldOfView(LivingEntity target) {
		final Vec3 targetPos = target.position();
		final Vec3 myPos = this.position();
		
		double x = targetPos.x - myPos.x;
		double z = targetPos.z - myPos.z;
		double d = Math.toDegrees(Math.atan2(-x, z));
		if (!ItemUtil.compareRotations(this.yHeadRot, d, 80.0D)) {
			return false;
		}
		double y = targetPos.y + target.getEyeHeight() - myPos.y - this.getEyeHeight();
		double xz = Math.sqrt(x * x + z * z);
		double d1 = Math.toDegrees(Math.atan2(y, xz));
		return ItemUtil.compareRotations(this.yRot, d1, 50.0D);
	}

	public void setHealthScale(double newHealthScale) {
		if (this.healthScale != newHealthScale) {
			if (!this.level().isClientSide) {
				EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_SLIDER_ID, "Health Scale Slider", newHealthScale - 1.0D);
			}
			this.healthScale = newHealthScale;
		}
	}

	public double getHealthScale() {
		return this.healthScale;
	}

	public float getDropChance(EquipmentSlot slot) {
		switch (slot.getType()) {
		case HAND:
			return this.handDropChances[slot.getIndex()];
		case ARMOR:
			return this.armorDropChances[slot.getIndex()];
		default:
			return 0.0F;
		}
	}

	public boolean isInSightRange(Entity target) {
		double sightRange = 32.0D;
		sightRange *= 0.6D + 0.4D * this.level().getRawBrightness(target.blockPosition(), 0) / 15.0D;
		sightRange *= this.hasEffect(MobEffects.BLINDNESS) ? 0.5D : 1.0D;
		return this.distanceToSqr(target) <= sightRange * sightRange;
	}

	public ItemStack getHeldItemWeapon() {
		return this.isHoldingPotion() ? this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION) : this.getMainHandItem();
	}

	public ItemStack getHeldItemPotion() {
		return this.isHoldingPotion() ? this.getMainHandItem() : this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
	}

	public boolean isMagicArmorActive() {
		if (!this.level().isClientSide) {
			return this.armorActive;
		}
		return this.entityData.get(MAGIC_ARMOR_ACTIVE);
	}

	public void setMagicArmorActive(boolean val) {
		if (val != this.armorActive) {
			this.armorActive = val;
			this.setInvulnerable(this.armorActive);
			this.entityData.set(MAGIC_ARMOR_ACTIVE, val);
		}
	}

	protected void updateCooldownForMagicArmor() {
		this.magicArmorCooldown--;
		if (this.magicArmorCooldown <= 0) {
			this.setMagicArmorActive(false);
		}
	}

	public void setMagicArmorCooldown(int val) {
		this.magicArmorCooldown = val;
		this.setMagicArmorActive(true);
	}

	public CQRNPCPath getPath() {
		return this.path;
	}

	public void setCurrentPathTargetPoint(int value) {
		this.prevPathTargetPoint = this.currentPathTargetPoint;
		this.currentPathTargetPoint = value;
	}

	public int getCurrentPathTargetPoint() {
		return this.currentPathTargetPoint;
	}

	public int getPrevPathTargetPoint() {
		return this.prevPathTargetPoint;
	}

	public int getLastTickWithAttackTarget() {
		return this.lastTickWithAttackTarget;
	}

	public int getLastTimeSeenAttackTarget() {
		return this.lastTimeSeenAttackTarget;
	}

	public Vec3 getLastPosAttackTarget() {
		return this.lastPosAttackTarget;
	}

	public EntityAISpellHandler createSpellHandler() {
		return new EntityAISpellHandler(this, 200);
	}

	public boolean isSpellCharging() {
		return ((this.entityData.get(SPELL_INFORMATION) >> 26) & 1) == 1;
	}

	public boolean isSpellCasting() {
		return ((this.entityData.get(SPELL_INFORMATION) >> 25) & 1) == 1;
	}

	public boolean isSpellAnimated() {
		return ((this.entityData.get(SPELL_INFORMATION) >> 24) & 1) == 1;
	}

	public void setLastTimeHitByAxeWhileBlocking(int tick) {
		this.lastTickShieldDisabled = tick;
	}

	public int getLastTimeHitByAxeWhileBlocking() {
		return this.lastTickShieldDisabled;
	}

	// @SideOnly(Side.CLIENT)
	public boolean hasAttackTarget() {
		if (this.level().isClientSide) {
			return this.entityData.get(HAS_TARGET);
		} else {
			return this.getTarget() != null && this.getTarget().isAlive();
		}
	}

	@Override
	public void setTarget(LivingEntity entitylivingbaseIn) {
		LivingEntity prevAttackTarget = this.getTarget();
		super.setTarget(entitylivingbaseIn);
		LivingEntity attackTarget = this.getTarget();
		if (prevAttackTarget == attackTarget) {
			return;
		}
		if (attackTarget == null) {
			this.entityData.set(HAS_TARGET, false);
			this.lastTimeSeenAttackTarget = Integer.MIN_VALUE;
			this.lastPosAttackTarget = this.position();

			Item item = this.getMainHandItem().getItem();
			if (item instanceof IFakeWeapon<?>) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
			}
		} else {
			this.entityData.set(HAS_TARGET, true);
			this.lastTimeSeenAttackTarget = this.tickCount;
			this.lastPosAttackTarget = attackTarget.position();

			Item item = this.getMainHandItem().getItem();
			if (TargetUtil.isAllyCheckingLeaders(this, attackTarget)) {
				if (item instanceof IFakeWeapon<?>) {
					this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
				}
			} else if (item instanceof ISupportWeapon<?>) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(((ISupportWeapon<?>) item).getFakeWeapon()));
			}
		}
	}

	// Shoulder entity stuff

	public boolean addShoulderEntity(CompoundTag compound) {
		if (!this.isPassenger() && this.onGround() && !this.isInWater()) {
			if (this.getLeftShoulderEntity().isEmpty()) {
				this.setLeftShoulderEntity(compound);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected void spawnShoulderEntities() {
		this.spawnShoulderEntity(this.getLeftShoulderEntity());
		this.setLeftShoulderEntity(new CompoundTag());
	}

	private void spawnShoulderEntity(@Nullable CompoundTag compound) {
		if (!this.level().isClientSide && compound != null && !compound.isEmpty()) {
			Entity entity = EntityList.createEntityFromNBT(compound, this.level());

			if (entity instanceof TamableAnimal) {
				((TamableAnimal) entity).setOwnerUUID(this.getUUID());
			}

			final Vec3 pos = this.position().add(0, 0.699999988079071D, 0);
			entity.setPos(pos.x, pos.y, pos.z);
			this.level().addFreshEntity(entity);
		}
	}

	public CompoundTag getLeftShoulderEntity() {
		return this.entityData.get(SHOULDER_ENTITY);
	}

	protected void setLeftShoulderEntity(CompoundTag tag) {
		this.entityData.set(SHOULDER_ENTITY, tag);
	}

	public boolean canUseSpinToWinAttack() {
		return this.getVehicle() == null;
	}

	public boolean isSpinToWinActive() {
		return this.canUseSpinToWinAttack() && this.entityData.get(SPIN_TO_WIN);
	}

	public void setSpinToWin(boolean value) {
		if (this.isServerWorld()) {
			this.entityData.set(SPIN_TO_WIN, value);
		}
	}

	protected boolean isServerWorld() {
		return this.level() != null && !this.level().isClientSide;
	}

	public TraderOffer getTrades() {
		return this.trades;
	}

	public void teleport(double x, double y, double z) {
		this.setPos(x, y, z);
		CQRMain.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SPacketUpdateEntityPrevPos(this));
	}

	// Custom textures
	@Override
	public boolean hasTextureOverride() {
		return this.entityData.get(TEXTURE_OVERRIDE) != null && !this.entityData.get(TEXTURE_OVERRIDE).isEmpty();
	}

	@Override
	public ResourceLocation getTextureOverride() {
		if (this.textureOverride == null || !this.textureOverride.toString().equals(this.entityData.get(TEXTURE_OVERRIDE))) {
			this.textureOverride = new ResourceLocation(this.entityData.get(TEXTURE_OVERRIDE));
		}
		return this.textureOverride;
	}

	public void updateInvisibility() {
		if (!this.level().isClientSide) {
			if (this.invisibilityTick > 0) {
				this.invisibilityTick--;
				this.entityData.set(INVISIBILITY, Math.min(this.entityData.get(INVISIBILITY) + 1.0F / this.getInvisibilityTurningTime(), 1.0F));
			} else {
				this.entityData.set(INVISIBILITY, Math.max(this.entityData.get(INVISIBILITY) - 1.0F / this.getInvisibilityTurningTime(), 0.0F));
			}
		}
	}

	protected int getInvisibilityTurningTime() {
		return 15;
	}

	/**
	 * Makes the entity invisible for the passed amount of ticks.<br>
	 * Keep in mind that the entity needs {@link AbstractEntityCQR#getInvisibilityTurningTime()} ticks
	 * to get fully invisible.<br>
	 * After the passed amount of ticks have passed the entity needs
	 * {@link AbstractEntityCQR#getInvisibilityTurningTime()} ticks to get fully visible again.
	 */
	public void setInvisibility(int ticks) {
		this.invisibilityTick = ticks;
	}

	public float getInvisibility() {
		return this.entityData.get(INVISIBILITY);
	}

	// ISizable stuff
	public EntityDimensions getSize() {
		if(this.size == null) {
			this.size = new EntityDimensions(this.getBbWidth(), this.getBbHeight(), false);
		}
		return this.size;
	}
	
	@Override
	public float getSizeVariation() {
		return this.sizeScaling;
	}

	@Override
	public void applySizeVariation(float value) {
		this.sizeScaling = value;
	}
	
	@Nullable
	private EntityDimensions getDimensionsCloneFromEntity(Pose poseIn) {
		if(poseIn != null) {
			EntityDimensions result = poseIn == Pose.SLEEPING ? SLEEPING_DIMENSIONS : this.getSize();
			if(result == null) {
				return null;
			}
			float scale = this.getSizeVariation();
			result = result.scale(scale);
			
			if(poseIn != Pose.SLEEPING) {
				if(this.isSitting()) {
					result.scale(1.0F, 0.75F);
				}
			}
			
			return result;
		}
		return this.getSize();
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		EntityDimensions result = super.getDimensions(pPose);//this.getDimensionsCloneFromEntity(pPose);
		if(this instanceof ISizable) {
			ISizable is = (ISizable)this;
			return is.callOnGetDimensions(result);
		}
		return result;
	}

	//ITradeRestockOverTime data accessors
	@Override
	public long getLastTimedRestockTime() {
		return this.lastTimedTradeRestock;
	}
	
	@Override
	public void setLastTimedRestockTime(long newValue) {
		this.lastTimedTradeRestock = newValue;
	}

	public Level getWorld() {
		return this.level();
	}
	
	//Misc
	
	public boolean canPlayDeathAnimation() {
		return this.dead || this.getHealth() < 0.01 || this.dead || !this.isAlive();
	}
	
	@Override
	public boolean isSensitiveToWater() {
		return super.isSensitiveToWater() || this.getMobType() == CQRCreatureAttributes.MECHANICAL;
	}
	
	@Override
	public boolean isMultipartEntity() {
		return super.isMultipartEntity() || (this.getParts() != null && this.getParts().length > 0);
	}
	
	//Multipart fix
	@Override
	public void setId(int pId) {
		super.setId(pId);
		if(this.isMultipartEntity() && this.getParts() != null) {
			for (int i = 0; i < this.getParts().length; i++) {
				this.getParts()[i].setId(pId + i + 1);
			}
		}
	}
	
	protected void callLastInConstructorForMultiparts() {
		if(this.isMultipartEntity() && this.getParts() != null) {
			this.setId(ENTITY_COUNTER.getAndAdd(this.getParts().length +1) +1);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public  <E extends AbstractEntityCQR & IAnimatableCQR> boolean isSwinging(InteractionHand hand, AnimationState<E> event) {
		return this.isSwinging(hand, event.getAnimatable());
	}
	
	@OnlyIn(Dist.CLIENT)
	public  boolean isSwinging(InteractionHand hand, AbstractEntityCQR instance) {
		if(instance.swinging) {
			return instance.swingingArm.equals(hand);
		}
		return false;
	}

	@Override
	public void containerChanged(Container pInvBasic) {
		// TODO Auto-generated method stub
		
	}
}
