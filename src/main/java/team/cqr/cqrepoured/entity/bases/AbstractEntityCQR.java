package team.cqr.cqrepoured.entity.bases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.NameTagItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.client.init.ESpeechBubble;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.IHasTextureOverride;
import team.cqr.cqrepoured.entity.EntityEquipmentExtraSlot;
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
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;
import team.cqr.cqrepoured.util.SpawnerFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;

public abstract class AbstractEntityCQR extends CreatureEntity implements IMob, IEntityAdditionalSpawnData, ISizable, IHasTextureOverride, ITextureVariants, ITradeRestockOverTime, IIsBeingRiddenHelper {

	private static final UUID BASE_ATTACK_SPEED_ID = UUID.fromString("be37de40-8857-48b1-aa99-49dd243fc22c");
	private static final UUID HEALTH_SCALE_SLIDER_ID = UUID.fromString("4b654c1d-fb8f-42b9-a278-0d49dab6d176");
	private static final UUID HEALTH_SCALE_DISTANCE_TO_SPAWN_ID = UUID.fromString("cf718cfe-d6a1-4cf6-b6c8-b5cf397f334c");

	protected static final DataParameter<Boolean> IS_LEADER = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Float> INVISIBILITY = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.FLOAT);
	protected static final DataParameter<Boolean> IS_SITTING = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> HAS_TARGET = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> TALKING = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.INT);
	protected static final DataParameter<Boolean> MAGIC_ARMOR_ACTIVE = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> SPELL_INFORMATION = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.INT);
	protected static final DataParameter<Boolean> SPIN_TO_WIN = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<String> FACTION_OVERRIDE_SYNC = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.STRING);
	protected static final DataParameter<CompoundNBT> SHOULDER_ENTITY = EntityDataManager.defineId(AbstractEntityCQR.class, DataSerializers.COMPOUND_TAG);
	protected static final DataParameter<String> TEXTURE_OVERRIDE = EntityDataManager.<String>defineId(AbstractEntityCQR.class, DataSerializers.STRING);

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
	protected Vector3d lastPosAttackTarget = Vector3d.ZERO;
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

	protected ServerBossInfo bossInfoServer;
	
	//Sizing bullshit
	protected EntitySize size;
	
	// Client only
	@OnlyIn(Dist.CLIENT)
	protected ESpeechBubble currentSpeechBubbleID = ESpeechBubble.BLOCK_BED;

	protected AbstractEntityCQR(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
		if (worldIn.isClientSide) {
			this.chooseNewRandomSpeechBubble();
		}
		this.xpReward = 5;
		this.initializeSize();
		this.size = new EntitySize(this.getBbWidth(), this.getBbHeight(), false);
	}

	public void enableBossBar() {
		if (!this.level.isClientSide && this.bossInfoServer == null) {
			this.bossInfoServer = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);
			this.bossInfoServer.setVisible(CQRConfig.bosses.enableBossBars);
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
		this.entityData.define(SHOULDER_ENTITY, new CompoundNBT());
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

	public static AttributeModifierMap.MutableAttribute createCQRAttributes() {
		AttributeModifierMap.MutableAttribute map = CreatureEntity.createMobAttributes()
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
		if (CQRConfig.advanced.enableSpecialFeatures && source.getEntity() != null) {
			ResourceLocation resLoc = EntityList.getKey(source.getEntity());
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				amount *= 0.5F;
			}
		}
		// End IceAndFire compatibility

		// Shoulder entity stuff
		this.spawnShoulderEntities();

		if (this.level.getLevelData().isHardcore()) {
			amount *= 0.7F;
		} else {
			Difficulty difficulty = this.level.getDifficulty();
			if (difficulty == Difficulty.HARD) {
				amount *= 0.8F;
			} else if (difficulty == Difficulty.NORMAL) {
				amount *= 0.9F;
			}
		}
		// End of shoulder entity stuff

		amount = this.handleDamageCap(source, amount);

		if (!this.level.isClientSide && amount > 0.0F && this.canBlockDamageSource(source)) {
			if (source.getDirectEntity() instanceof LivingEntity && !(source.getDirectEntity() instanceof PlayerEntity) && ((LivingEntity) source.getDirectEntity()).getMainHandItem().getItem() instanceof AxeItem) {
				this.lastTickShieldDisabled = this.tickCount;
			} else {
				this.damageBlockedWithShield += amount;
				if (this.damageBlockedWithShield >= CQRConfig.general.damageBlockedByShield) {
					this.damageBlockedWithShield = 0.0F;
					this.lastTickShieldDisabled = this.tickCount;
				}
			}
		}

		boolean flag = super.hurt(source, amount);

		if (flag && CQRConfig.mobs.armorShattersOnMobs) {
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

		Vector3d currentMovement = this.getDeltaMovement();
		
		double vX = currentMovement.x;
		double vY = currentMovement.y;
		double vZ = currentMovement.z;
		
		this.hasImpulse = true;
		double d = 1.0D / MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
		vX *= 0.5D;
		vZ *= 0.5D;
		vX -= xRatio * d * strength;
		vZ -= zRatio * d * strength;

		if (this.onGround) {
			vY *= 0.5D;
			vY += strength;

			if (vY > 0.4D) {
				vY = 0.4D;
			}
		}
		
		this.setDeltaMovement(vX, vY, vZ);
	}

	protected boolean damageCapEnabled() {
		return CQRConfig.mobs.enableDamageCapForNonBossMobs;
	}

	protected float maxDamageInPercentOfMaxHP() {
		return CQRConfig.mobs.maxUncappedDamageInMaxHPPercent;
	}

	protected float maxUncappedDamage() {
		return CQRConfig.mobs.maxUncappedDamageForNonBossMobs;
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
			Vector3d vec3d = damageSourceIn.getSourcePosition();

			if (vec3d != null) {
				Vector3d vec3d1 = this.getEyePosition(1.0F);
				Vector3d vec3d2 = this.position().subtract(vec3d).normalize();
				vec3d2 = new Vector3d(vec3d2.x, 0.0D, vec3d2.z);

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
			this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
		}

		super.die(cause);

		this.updateReputationOnDeath(cause);
	}

	@Override
	protected void registerGoals() {
		if (CQRConfig.advanced.debugAI) {
			//TODO: AI Selectors are final now, change this or not?
			//this.goalSelector = new EntityAITasksProfiled((IProfiler) this.level.getProfiler(), this.level);
			//this.targetSelector = new EntityAITasksProfiled((IProfiler) this.level.getProfiler(), this.level);
		}
		this.spellHandler = this.createSpellHandler();
		this.goalSelector.addGoal(0, new SwimGoal(this));
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
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance difficulty, SpawnReason p_213386_3_, ILivingEntityData setDamageValue, CompoundNBT p_213386_5_) {
		this.setHealingPotions(1);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, new ItemStack(CQRItems.BADGE.get()));
		for (EquipmentSlotType slot : EquipmentSlotType.values()) {
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

		double modalValue = CQRConfig.mobs.dropDurabilityModalValue;
		double standardDeviation = CQRConfig.mobs.dropDurabilityStandardDeviation;
		double min = Math.min(CQRConfig.mobs.dropDurabilityMinimum, modalValue);
		double max = Math.max(CQRConfig.mobs.dropDurabilityMaximum, modalValue);

		for (EquipmentSlotType entityequipmentslot : EquipmentSlotType.values()) {
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
					double durability = modalValue + MathHelper.clamp(this.random.nextGaussian() * standardDeviation, min - modalValue, max - modalValue);
					itemstack.setDamageValue((int) (itemstack.getMaxDamage() * (1.0D - durability)));
				}

				this.spawnAtLocation(itemstack, 0.0F);
			}
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);

		if (this.homePosition != null) {
			compound.put("home", NBTUtil.writeBlockPos(this.homePosition));
		}

		if (this.leaderUUID != null) {
			compound.put("leader", NBTUtil.createUUID(this.leaderUUID));
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

		CompoundNBT pathTag = new CompoundNBT();
		pathTag.put("path", this.path.writeToNBT());
		pathTag.putInt("prevPathTargetPoint", this.prevPathTargetPoint);
		pathTag.putInt("currentPathTargetPoint", this.currentPathTargetPoint);
		compound.put("pathTag", pathTag);

		// Shoulder entity stuff
		if (!this.getLeftShoulderEntity().isEmpty()) {
			compound.put("ShoulderEntityLeft", this.getLeftShoulderEntity());
		}

		compound.put("trades", this.trades.writeToNBT(new CompoundNBT()));
		compound.putLong("lastTimedRestockTime", this.getLastTimedRestockTime());
		
		if (this.hasTextureOverride()) {
			compound.putString("textureOverride", this.getTextureOverride().toString());
		}

		if (this.bossInfoServer != null) {
			compound.putBoolean("hasBossBar", true);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);

		if (compound.contains("home")) {
			this.homePosition = NBTUtil.readBlockPos(compound.getCompound("home"));
		}

		if (compound.contains("leader")) {
			this.leaderUUID = NBTUtil.loadUUID(compound.getCompound("leader"));
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
		this.setHealthScale(compound.contains("healthScale", Constants.NBT.TAG_DOUBLE) ? compound.getDouble("healthScale") : 1.0D);

		if (compound.contains("pathingAI", Constants.NBT.TAG_COMPOUND)) {
			CompoundNBT pathTag = compound.getCompound("pathingAI");
			ListNBT nbtTagList = pathTag.getList("pathPoints", Constants.NBT.TAG_COMPOUND);
			this.path.clear();
			for (int i = 0; i < nbtTagList.size(); i++) {
				BlockPos pos = NBTUtil.readBlockPos(nbtTagList.getCompound(i));
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
		} else if (compound.contains("pathTag", Constants.NBT.TAG_COMPOUND)) {
			CompoundNBT pathTag = compound.getCompound("pathTag");
			this.path.readFromNBT(pathTag.getCompound("path"));
			this.prevPathTargetPoint = pathTag.getInt("prevPathTargetPoint");
			this.currentPathTargetPoint = pathTag.getInt("currentPathTargetPoint");
		}

		// Shoulder entity stuff
		if (compound.contains("ShoulderEntityLeft", 10)) {
			this.setLeftShoulderEntity(compound.getCompound("ShoulderEntityLeft"));
		}

		this.trades.readFromNBT(compound.getCompound("trades"));
		if(compound.contains("lastTimedRestockTime", Constants.NBT.TAG_LONG)) {
			this.lastTimedTradeRestock = compound.getLong("lastTimedRestockTime");
		}

		if (compound.contains("textureOverride", Constants.NBT.TAG_STRING)) {
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
	protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if (player.getItemInHand(hand).getItem() instanceof NameTagItem) {
			return super.mobInteract(player, hand);
		}

		boolean flag = false;

		if(player instanceof ServerPlayerEntity) {
			ServerPlayerEntity spe = (ServerPlayerEntity) player;
			if (!player.isCrouching()) {
				if (player.isCreative() || this.getLeader() == player) {
					if (!this.level.isClientSide) {
						//player.openGui(CQRMain.INSTANCE, GuiHandler.CQR_ENTITY_GUI_ID, this.level, this.getId(), 0, 0);
						NetworkHooks.openGui(spe, new INamedContainerProvider() {
							
							@Override
							public Container createMenu(int windowId, PlayerInventory invPlayer, PlayerEntity lePlayer) {
								PacketBuffer buf = new PacketBuffer(Unpooled.buffer(32));
								buf.writeInt(AbstractEntityCQR.this.getId());
								return CQRContainerTypes.CQR_ENTITY_EDITOR.get().create(windowId, invPlayer, buf);
							}
							
							@Override
							public ITextComponent getDisplayName() {
								return AbstractEntityCQR.this.getDisplayName();
							}
						});
					}
					flag = true;
				} else if (!this.getFaction().isEnemy(player) && this.hasTrades()) {
					if (!this.level.isClientSide) {
						//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, this.level, this.getId(), 0, 0);
						NetworkHooks.openGui(spe, new INamedContainerProvider() {
							
							@Override
							public Container createMenu(int windowId, PlayerInventory invPlayer, PlayerEntity lePlayer) {
								return CQRContainerTypes.MERCHANT.get().create(windowId, invPlayer);
							}
							
							@Override
							public ITextComponent getDisplayName() {
								return AbstractEntityCQR.this.getDisplayName();
							}
						});
					}
					flag = true;
				}
			} else if (player.isCreative() || (!this.getFaction().isEnemy(player) && this.hasTrades())) {
				if (!this.level.isClientSide) {
					//player.openGui(CQRMain.INSTANCE, GuiHandler.MERCHANT_GUI_ID, this.level, this.getId(), 0, 0);
					NetworkHooks.openGui(spe, new INamedContainerProvider() {
						
						@Override
						public Container createMenu(int windowId, PlayerInventory invPlayer, PlayerEntity lePlayer) {
							return CQRContainerTypes.MERCHANT.get().create(windowId, invPlayer);
						}
						
						@Override
						public ITextComponent getDisplayName() {
							return AbstractEntityCQR.this.getDisplayName();
						}
					});
				}
				flag = true;
			}
		}

		if (flag && !this.getLookControl().isHasWanted() && !this.isPathFinding()) {
			double x1 = player.position().x() - this.position().x();
			double z1 = player.position().z() - this.position().z();
			float yaw = (float) Math.toDegrees(Math.atan2(-x1, z1));
			this.yBodyRot = yaw;
			this.yHeadRot = yaw;
			//Old 1.12.2
			//this.renderYawOffset = yaw;
			this.rotOffs = yaw;
		}

		return flag ? ActionResultType.SUCCESS : ActionResultType.PASS;
	}

	public boolean hasTrades() {
		return !this.trades.isEmpty();
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource deathCause, int lootingModifier, boolean wasRecentlyHit) {
		ResourceLocation resourcelocation = this.getLootTable();
		if (resourcelocation != null) {
			LootTable lootTable = this.level.getServer().getLootTables().get(resourcelocation);
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
			if (this.isInSightRange(attackTarget) && this.getSensing().canSee(attackTarget)) {
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
		if (!stackMainhand.getAttributeModifiers(EquipmentSlotType.MAINHAND).containsKey(Attributes.ATTACK_SPEED)) {
			ModifiableAttributeInstance attribute = this.getAttribute(Attributes.ATTACK_SPEED);
			if (attribute.getModifier(BASE_ATTACK_SPEED_ID) == null) {
				AttributeModifier modifier = new AttributeModifier(BASE_ATTACK_SPEED_ID, "Base Attack Speed", -2.4D, Operation.ADDITION);
				modifier.save();
				attribute.addPermanentModifier(modifier);
			}
		} else {
			ModifiableAttributeInstance attribute = this.getAttribute(Attributes.ATTACK_SPEED);
			attribute.removeModifier(BASE_ATTACK_SPEED_ID);
		}

		super.tick();

		if (!this.level.isClientSide && this.isMagicArmorActive()) {
			this.updateCooldownForMagicArmor();
		}
		if (!this.level.isClientSide && !this.isNonBoss() && this.level.getDifficulty() == Difficulty.PEACEFUL) {
			SpawnerFactory.placeSpawner(new Entity[] { this }, false, null, this.level, this.blockPosition());
			this.remove();
		}

		ItemStack stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		if (!this.level.isClientSide && stack != this.prevPotion) {
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

		if (!this.level.isClientSide) {
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
				float f = /* OLD: renderYawOffset */this.rotOffs * 0.017453292F + MathHelper.cos(this.tickCount * 0.6662F) * 0.25F;
				float f1 = MathHelper.cos(f);
				float f2 = MathHelper.sin(f);
				this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) f1 * (double) this.getBbWidth(), this.getY() + this.getBbHeight(), this.getZ() + (double) f2 * (double) this.getBbWidth(), red, green, blue);
				this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double) f1 * (double) this.getBbWidth(), this.getY() + this.getBbHeight(), this.getZ() - (double) f2 * (double) this.getBbWidth(), red, green, blue);
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
	public SoundCategory getSoundSource() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public void aiStep() {
		//this.updateSwingTime(); //Already called in aiStep()
		super.aiStep();

		// Bossbar
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public void startSeenByPlayer(ServerPlayerEntity player) {
		super.startSeenByPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.addPlayer(player);
		}
	}
	
	@Override
	public void stopSeenByPlayer(ServerPlayerEntity player) {
		super.stopSeenByPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.removePlayer(player);
		}
	}

	protected void setCustomName(String name) {
		this.setCustomName(new StringTextComponent(name));
	}
	
	@Override
	public void setCustomName(ITextComponent name) {
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
	protected SoundEvent getFallDamageSound(int heightIn) {
		return heightIn > 4 ? SoundEvents.GENERIC_BIG_FALL : SoundEvents.GENERIC_SMALL_FALL;
	}

	@Override
	public boolean doHurtTarget(Entity entityIn) {
		// Shoulder entity stuff
		this.spawnShoulderEntities();
		
		if (this.getMainHandItem().getItem() instanceof ItemStaffHealing) {
			if (entityIn instanceof LivingEntity) {
				if (!this.level.isClientSide) {
					((LivingEntity) entityIn).heal(ItemStaffHealing.HEAL_AMOUNT_ENTITIES);
					entityIn.setSecondsOnFire(0);
					Vector3d pos = entityIn.position();
					//TODO: Check if this works correctly
					((ServerWorld) this.level).addParticle(
							ParticleTypes.HEART,
							true,
							pos.x,
							pos.y + entityIn.getBbHeight() * 0.5D, 
							pos.z, 
							0.25D, 
							0.25D, 
							0.25D 
					);
					this.level.playSound(null, pos.y, pos.y + entityIn.getBbHeight() * 0.5D, pos.z, CQRSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + this.random.nextFloat() * 0.2F);
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
		if (CQRConfig.advanced.enableSpecialFeatures) {
			ResourceLocation resLoc = EntityList.getKey(entityIn);
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				f *= 2.0F;
			}
		}
		// End IceAndFire compatibility
		boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).knockback(i * 0.5F, MathHelper.sin(this.yBodyRot * 0.017453292F), (-MathHelper.cos(this.yBodyRot * 0.017453292F)));
			
				Vector3d deltaNew = this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D);
				
				this.setDeltaMovement(deltaNew);
			}

			int j = EnchantmentHelper.getFireAspect(this);

			if (j > 0) {
				entityIn.setSecondsOnFire(j * 4);
			}

			if (entityIn instanceof PlayerEntity) {
				//Handles shield stuff, however there is a method in the parent class, we might want to use that?
				PlayerEntity entityplayer = (PlayerEntity) entityIn;
				ItemStack itemstack = this.getMainHandItem();
				ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
					float f1 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

					if (this.random.nextFloat() < f1) {
						entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
						this.level.broadcastEntityEvent(entityplayer, (byte) 30);
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
	protected boolean shouldDropLoot() {
		return true;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		// buffer.writeFloat(this.getSizeVariation());
		buffer.writeFloat(this.getSizeVariation());
		buffer.writeDouble(this.getHealthScale());
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.HEAD));
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.CHEST));
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.LEGS));
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.FEET));
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.MAINHAND));
		buffer.writeFloat(this.getDropChance(EquipmentSlotType.OFFHAND));
		buffer.writeItemStack(this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION), true);
		buffer.writeNbt(this.trades.writeToNBT(new CompoundNBT()));
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		// this.setSizeVariation(additionalData.readFloat());
		this.setSizeVariation(additionalData.readFloat());
		this.setHealthScale(additionalData.readDouble());
		this.setDropChance(EquipmentSlotType.HEAD, additionalData.readFloat());
		this.setDropChance(EquipmentSlotType.CHEST, additionalData.readFloat());
		this.setDropChance(EquipmentSlotType.LEGS, additionalData.readFloat());
		this.setDropChance(EquipmentSlotType.FEET, additionalData.readFloat());
		this.setDropChance(EquipmentSlotType.MAINHAND, additionalData.readFloat());
		this.setDropChance(EquipmentSlotType.OFFHAND, additionalData.readFloat());
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, additionalData.readItem());
		this.trades.readFromNBT(additionalData.readNbt());
		this.chooseNewRandomSpeechBubble();
	}

	// #################### Chocolate Quest Repoured ####################

	public void updateLeader() {
		if (this.level.isClientSide) {
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
			Entity entity = EntityUtil.getEntityByUUID(this.level, this.leaderUUID);
			if (entity instanceof LivingEntity) {
				this.leader = (LivingEntity) entity;
			}
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
		if (this.level.isClientSide) {
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

	public abstract float getBaseHealth();

	public void setBaseHealthDependingOnPos(BlockPos pos) {
		if (CQRConfig.mobs.enableHealthChangeOnDistance && !this.level.isClientSide) {
			double x = (double) pos.getX() - DungeonGenUtils.getSpawnX(this.level);
			double z = (double) pos.getZ() - DungeonGenUtils.getSpawnZ(this.level);
			double distance = Math.sqrt(x * x + z * z);
			double amount = 0.1D * (int) (distance / CQRConfig.mobs.distanceDivisor);

			EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_DISTANCE_TO_SPAWN_ID, "Health Scale Distance To Spawn", amount);
		}
	}

	public void handleArmorBreaking() {
		if (!this.level.isClientSide && this.usedPotions + 1 > this.getHealingPotions()) {
			boolean armorBroke = false;
			float hpPrcntg = this.getHealth() / this.getMaxHealth();
			float[] thresholds = { 0.8F, 0.6F, 0.4F, 0.2F };
			EquipmentSlotType[] slots = { EquipmentSlotType.FEET, EquipmentSlotType.LEGS, EquipmentSlotType.HEAD, EquipmentSlotType.CHEST };

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
			this.setItemSlot(EquipmentSlotType.MAINHAND, stack);
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
		ItemStack stack1 = this.getItemBySlot(EquipmentSlotType.MAINHAND);
		ItemStack stack2 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		this.setItemSlot(EquipmentSlotType.MAINHAND, stack2);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, stack1);
		this.holdingPotion = !this.holdingPotion;
	}

	public boolean isHoldingPotion() {
		return this.holdingPotion;
	}

	protected abstract EDefaultFaction getDefaultFaction();

	protected Faction getDefaultFactionInstance() {
		if (this.defaultFactionInstance == null) {
			this.defaultFactionInstance = FactionRegistry.instance(this).getFactionInstance(this.getDefaultFaction().name());
		}
		return this.defaultFactionInstance;
	}

	@Nullable
	public Faction getFaction() {
		if (!this.level.isClientSide) {
			// Leader faction is set when assigning the leader
			/*
			 * if (this.hasLeader()) { return FactionRegistry.instance().getFactionOf(this.getLeader()); }
			 */
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

	public void setFaction(String newFac) {
		this.setFaction(newFac, false);
	}

	public void setFaction(String newFac, boolean ignoreCTS) {
		// TODO: Update faction on client too!!
		if (!this.level.isClientSide) {
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

	public boolean hasFaction() {
		return this.getFaction() != null;
	}

	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getEntity() instanceof PlayerEntity && this.hasFaction() && !this.level.isClientSide) {
			PlayerEntity player = (PlayerEntity) cause.getEntity();
			int range = CQRConfig.mobs.factionUpdateRadius;
			final Vector3d rangeVec = new Vector3d(range, range, range);
			AxisAlignedBB aabb = new AxisAlignedBB(player.position().subtract(rangeVec), player.position().add(rangeVec));

			List<Faction> checkedFactions = new ArrayList<>();
			// boolean setRepu = false;
			for (AbstractEntityCQR cqrentity : this.level.getEntitiesOfClass(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && !checkedFactions.contains(cqrentity.getFaction()) && (cqrentity.canSee(this) || cqrentity.canSee(player))) {
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
		this.setLastTimedRestockTime(this.level.getGameTime());
		
		// Recalculate path points
		for (CQRNPCPath.PathNode node : this.path.getNodes()) {
			node.setPos(DungeonPlacement.transform(node.getPos().getX(), node.getPos().getY(), node.getPos().getZ(), BlockPos.ZERO, placement.getMirror(), placement.getRotation()));
			node.setWaitingRotation(getTransformedYaw(node.getWaitingRotation(), placement.getMirror(), placement.getRotation()));
		}

		// Replace shield
		for (EquipmentSlotType slot : EquipmentSlotType.values()) {
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
		float f = MathHelper.wrapDegrees(rotationYaw);
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
		return MathHelper.wrapDegrees(f);
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

	@OnlyIn(Dist.CLIENT)
	public int getTextureIndex() {
		return this.entityData.get(TEXTURE_INDEX);
	}

	public double getAttackReach(LivingEntity target) {
		double reach = ((double) this.getBbWidth() + (double) target.getBbWidth()) * 0.5D + 0.85D;
		ItemStack stack = this.getMainHandItem();
		if (stack.getItem() instanceof ItemSpearBase) {
			reach += ((ItemSpearBase) stack.getItem()).getReach();
		}
		return reach;
	}

	public boolean isInAttackReach(LivingEntity target) {
		return this.isInReach(target, this.getAttackReach(target));
	}

	public boolean isInReach(LivingEntity target, double distance) {
		final Vector3d targetPos = target.position();
		final Vector3d myPos = this.position();
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
		final Vector3d targetPos = target.position();
		final Vector3d myPos = this.position();
		
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
			if (!this.level.isClientSide) {
				EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_SLIDER_ID, "Health Scale Slider", newHealthScale - 1.0D);
			}
			this.healthScale = newHealthScale;
		}
	}

	public double getHealthScale() {
		return this.healthScale;
	}

	public float getDropChance(EquipmentSlotType slot) {
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
		sightRange *= 0.6D + 0.4D * this.level.getLightEmission(target.blockPosition()) / 15.0D;
		sightRange *= this.hasEffect(Effects.BLINDNESS) ? 0.5D : 1.0D;
		return this.distanceToSqr(target) <= sightRange * sightRange;
	}

	public ItemStack getHeldItemWeapon() {
		return this.isHoldingPotion() ? this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION) : this.getMainHandItem();
	}

	public ItemStack getHeldItemPotion() {
		return this.isHoldingPotion() ? this.getMainHandItem() : this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
	}

	public boolean isMagicArmorActive() {
		if (!this.level.isClientSide) {
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

	public Vector3d getLastPosAttackTarget() {
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
		if (this.level.isClientSide) {
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
				this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
			}
		} else {
			this.entityData.set(HAS_TARGET, true);
			this.lastTimeSeenAttackTarget = this.tickCount;
			this.lastPosAttackTarget = attackTarget.position();

			Item item = this.getMainHandItem().getItem();
			if (TargetUtil.isAllyCheckingLeaders(this, attackTarget)) {
				if (item instanceof IFakeWeapon<?>) {
					this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
				}
			} else if (item instanceof ISupportWeapon<?>) {
				this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(((ISupportWeapon<?>) item).getFakeWeapon()));
			}
		}
	}

	// Shoulder entity stuff

	public boolean addShoulderEntity(CompoundNBT compound) {
		if (!this.isPassenger() && this.onGround && !this.isInWater()) {
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
		this.setLeftShoulderEntity(new CompoundNBT());
	}

	private void spawnShoulderEntity(@Nullable CompoundNBT compound) {
		if (!this.level.isClientSide && compound != null && !compound.isEmpty()) {
			Entity entity = EntityList.createEntityFromNBT(compound, this.level);

			if (entity instanceof TameableEntity) {
				((TameableEntity) entity).setOwnerUUID(this.getUUID());
			}

			final Vector3d pos = this.position().add(0, 0.699999988079071D, 0);
			entity.setPos(pos.x, pos.y, pos.z);
			this.level.addFreshEntity(entity);
		}
	}

	public CompoundNBT getLeftShoulderEntity() {
		return this.entityData.get(SHOULDER_ENTITY);
	}

	protected void setLeftShoulderEntity(CompoundNBT tag) {
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
		return this.level != null && !this.level.isClientSide;
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
		if (!this.level.isClientSide) {
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
	public EntitySize getSize() {
		if(this.size == null) {
			this.size = new EntitySize(this.getBbWidth(), this.getBbHeight(), false);
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
	private EntitySize getDimensionsCloneFromEntity(Pose poseIn) {
		if(poseIn != null) {
			EntitySize result = poseIn == Pose.SLEEPING ? SLEEPING_DIMENSIONS : this.getSize();
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
	public EntitySize getDimensions(Pose pPose) {
		EntitySize result = super.getDimensions(pPose);//this.getDimensionsCloneFromEntity(pPose);
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

	public World getWorld() {
		return this.level;
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
}
