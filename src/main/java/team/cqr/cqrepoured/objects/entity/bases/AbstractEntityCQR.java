package team.cqr.cqrepoured.objects.entity.bases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import team.cqr.cqrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import team.cqr.cqrepoured.client.init.ESpeechBubble;
import team.cqr.cqrepoured.client.render.entity.layers.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.network.server.packet.SPacketItemStackSync;
import team.cqr.cqrepoured.network.server.packet.SPacketUpdateEntityPrevPos;
import team.cqr.cqrepoured.objects.entity.ECQREntityArmPoses;
import team.cqr.cqrepoured.objects.entity.EntityEquipmentExtraSlot;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIAttack;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIAttackRanged;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIAttackSpecial;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIBackstab;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIFireFighter;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIFireball;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIFollowAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIFollowPath;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIHealingPotion;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIHooker;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIIdleSit;
import team.cqr.cqrepoured.objects.entity.ai.EntityAILooter;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIMoveToHome;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIMoveToLeader;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIPotionThrower;
import team.cqr.cqrepoured.objects.entity.ai.EntityAIRideHorse;
import team.cqr.cqrepoured.objects.entity.ai.EntityAISearchMount;
import team.cqr.cqrepoured.objects.entity.ai.EntityAITameAndLeashPet;
import team.cqr.cqrepoured.objects.entity.ai.EntityAITorchIgniter;
import team.cqr.cqrepoured.objects.entity.ai.spells.EntityAISpellHandler;
import team.cqr.cqrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import team.cqr.cqrepoured.objects.entity.pathfinding.Path;
import team.cqr.cqrepoured.objects.entity.pathfinding.PathNavigateGroundCQR;
import team.cqr.cqrepoured.objects.factories.SpawnerFactory;
import team.cqr.cqrepoured.objects.items.IFakeWeapon;
import team.cqr.cqrepoured.objects.items.ISupportWeapon;
import team.cqr.cqrepoured.objects.items.ItemBadge;
import team.cqr.cqrepoured.objects.items.ItemPotionHealing;
import team.cqr.cqrepoured.objects.items.ItemShieldDummy;
import team.cqr.cqrepoured.objects.items.armor.ItemBackpack;
import team.cqr.cqrepoured.objects.items.spears.ItemSpearBase;
import team.cqr.cqrepoured.objects.items.staves.ItemStaffHealing;
import team.cqr.cqrepoured.objects.npc.trading.TraderOffer;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.EntityUtil;
import team.cqr.cqrepoured.util.ItemUtil;
import team.cqr.cqrepoured.util.Reference;

public abstract class AbstractEntityCQR extends EntityCreature implements IMob, IEntityAdditionalSpawnData {

	private static final UUID HEALTH_SCALE_SLIDER_ID = UUID.fromString("4b654c1d-fb8f-42b9-a278-0d49dab6d176");
	private static final UUID HEALTH_SCALE_DISTANCE_TO_SPAWN_ID = UUID.fromString("cf718cfe-d6a1-4cf6-b6c8-b5cf397f334c");

	protected BlockPos homePosition = null;
	protected UUID leaderUUID;
	protected EntityLivingBase leader = null;
	private final Map<UUID, EntityLivingBase> followers = new HashMap<>();
	protected boolean holdingPotion;
	protected byte usedPotions = (byte) 0;
	protected double healthScale = 1.0D;
	private ItemStack prevPotion;
	private boolean prevSneaking;
	private boolean prevSitting;
	protected float sizeScaling = 1.0F;
	protected int lastTickWithAttackTarget = Integer.MIN_VALUE;
	protected int lastTimeSeenAttackTarget = Integer.MIN_VALUE;
	protected Vec3d lastPosAttackTarget = Vec3d.ZERO;
	protected EntityAISpellHandler spellHandler;
	private int invisibilityTick;

	private CQRFaction factionInstance;
	private CQRFaction defaultFactionInstance;
	private String factionName;

	protected int lastTickShieldDisabled = Integer.MIN_VALUE;
	protected float damageBlockedWithShield = 0.0F;
	protected boolean armorActive = false;
	protected int magicArmorCooldown = 300;
	
	//Riding AI
	protected EntityAIRideHorse<AbstractEntityCQR> horseAI = null;

	// Pathing AI stuff
	protected Path path = new Path() {
		@Override
		public boolean removeNode(Path.PathNode node) {
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
		public void copyFrom(Path path, BlockPos offset) {
			super.copyFrom(path, offset);
			AbstractEntityCQR.this.prevPathTargetPoint = -1;
			AbstractEntityCQR.this.currentPathTargetPoint = -1;
		}
	};
	protected int prevPathTargetPoint = -1;
	protected int currentPathTargetPoint = -1;

	private TraderOffer trades = new TraderOffer(this);

	// Sync with client
	protected static final DataParameter<Float> INVISIBILITY = EntityDataManager.<Float>createKey(AbstractEntityCQR.class, DataSerializers.FLOAT);
	protected static final DataParameter<Boolean> IS_SITTING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> HAS_TARGET = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<String> ARM_POSE = EntityDataManager.<String>createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	protected static final DataParameter<Boolean> TALKING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.<Integer>createKey(AbstractEntityCQR.class, DataSerializers.VARINT);
	protected static final DataParameter<Boolean> MAGIC_ARMOR_ACTIVE = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> SPELL_INFORMATION = EntityDataManager.<Integer>createKey(AbstractEntityCQR.class, DataSerializers.VARINT);
	protected static final DataParameter<Boolean> SPIN_TO_WIN = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<String> FACTION_OVERRIDE_SYNC = EntityDataManager.<String>createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	// Shoulder entity stuff
	protected static final DataParameter<NBTTagCompound> SHOULDER_ENTITY = EntityDataManager.<NBTTagCompound>createKey(AbstractEntityCQR.class, DataSerializers.COMPOUND_TAG);

	// Texture syncing
	protected static final DataParameter<String> TEXTURE_OVERRIDE = EntityDataManager.<String>createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	protected ResourceLocation textureOverride = null;

	protected BossInfoServer bossInfoServer = null;

	// Client only
	@SideOnly(Side.CLIENT)
	protected int currentSpeechBubbleID;

	public AbstractEntityCQR(World worldIn) {
		super(worldIn);
		if (worldIn.isRemote) {
			this.currentSpeechBubbleID = this.getRNG().nextInt(ESpeechBubble.values().length);
		}
		this.experienceValue = 5;
		this.setSize(this.getDefaultWidth(), this.getDefaultHeight());
	}

	public void enableBossBar() {
		if (!this.world.isRemote && this.bossInfoServer == null) {
			this.bossInfoServer = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);
			this.bossInfoServer.setVisible(CQRConfig.bosses.enableBossBars);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(INVISIBILITY, 0.0F);
		this.dataManager.register(IS_SITTING, false);
		this.dataManager.register(HAS_TARGET, false);
		this.dataManager.register(ARM_POSE, ECQREntityArmPoses.NONE.toString());
		this.dataManager.register(TALKING, false);
		this.dataManager.register(TEXTURE_INDEX, this.getTextureVariant());
		this.dataManager.register(MAGIC_ARMOR_ACTIVE, false);
		this.dataManager.register(SPELL_INFORMATION, 0);
		this.dataManager.register(SPIN_TO_WIN, false);
		this.dataManager.register(TEXTURE_OVERRIDE, "");
		this.dataManager.register(FACTION_OVERRIDE_SYNC, "");

		// Shoulder entity stuff
		this.dataManager.register(SHOULDER_ENTITY, new NBTTagCompound());
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		// Not wanted
	}

	@Override
	protected boolean canDespawn() {
		return !CQRConfig.general.mobsFromCQSpawnerDontDespawn;
	}

	protected int getTextureVariant() {
		return this.getRNG().nextInt(this.getTextureCount());
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		// speed (in blocks per second) = x^2 * 0.98 / (1 - slipperiness * 0.91) * 20 -> usually slipperiness = 0.6
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		PathNavigate navigator = new PathNavigateGroundCQR(this, worldIn);
		((PathNavigateGround) navigator).setEnterDoors(this.canOpenDoors());
		((PathNavigateGround) navigator).setBreakDoors(this.canOpenDoors());
		return navigator;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.attackEntityFrom(source, amount, false);
	}

	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		// Start IceAndFire compatibility
		if (CQRConfig.advanced.enableSpecialFeatures && source.getTrueSource() != null) {
			ResourceLocation resLoc = EntityList.getKey(source.getTrueSource());
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				amount *= 0.5F;
			}
		}
		// End IceAndFire compatibility

		// Shoulder entity stuff
		this.spawnShoulderEntities();

		if (this.world.getWorldInfo().isHardcoreModeEnabled()) {
			amount *= 0.7F;
		} else {
			EnumDifficulty difficulty = this.world.getDifficulty();
			if (difficulty == EnumDifficulty.HARD) {
				amount *= 0.8F;
			} else if (difficulty == EnumDifficulty.NORMAL) {
				amount *= 0.9F;
			}
		}
		// End of shoulder entity stuff

		amount = this.handleDamageCap(source, amount);

		if (!this.world.isRemote && amount > 0.0F && this.canBlockDamageSource(source)) {
			if (source.getImmediateSource() instanceof EntityLivingBase && !(source.getImmediateSource() instanceof EntityPlayer) && ((EntityLivingBase) source.getImmediateSource()).getHeldItemMainhand().getItem() instanceof ItemAxe) {
				this.lastTickShieldDisabled = this.ticksExisted;
			} else {
				this.damageBlockedWithShield = this.damageBlockedWithShield + amount * 0.0625F;
				if (this.damageBlockedWithShield >= 1.0F) {
					this.damageBlockedWithShield = 0.0F;
					this.lastTickShieldDisabled = this.ticksExisted;
				}
			}
		}

		boolean flag = super.attackEntityFrom(source, amount);

		if (flag && CQRConfig.mobs.armorShattersOnMobs) {
			this.handleArmorBreaking();
		}

		return flag;
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
		if (source.isCreativePlayer() || source.canHarmInCreative()) {
			return originalAmount;
		}
		if (CQRConfig.advanced.enableMaxDamageCaps && this.damageCapEnabled()) {
			return Math.min(Math.max(this.maxUncappedDamage(), this.getMaxHealth() * this.maxDamageInPercentOfMaxHP()), originalAmount);
		}
		return originalAmount;
	}

	public boolean canBlockDamageSource(DamageSource damageSourceIn) {
		if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking()) {
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null) {
				Vec3d vec3d1 = this.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dotProduct(vec3d1) < 0.0D) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onDeath(DamageSource cause) {
		if (this.isHoldingPotion()) {
			this.swapWeaponAndPotionSlotItemStacks();
		}
		Item item = this.getHeldItemMainhand().getItem();
		if (item instanceof IFakeWeapon<?>) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
		}

		super.onDeath(cause);

		this.updateReputationOnDeath(cause);
	}

	@Override
	protected void initEntityAI() {
		this.spellHandler = this.createSpellHandler();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIOpenDoor(this, true) {
			@Override
			public boolean shouldExecute() {
				return AbstractEntityCQR.this.canOpenDoors() && super.shouldExecute();
			}
		});

		if(this.canMountEntity()) {
			this.horseAI = new EntityAIRideHorse<AbstractEntityCQR>(this, 1.5);
			this.tasks.addTask(8, this.horseAI);
		}
		this.tasks.addTask(9, new EntityAIHealingPotion(this));
		this.tasks.addTask(11, this.spellHandler);
		this.tasks.addTask(12, new EntityAIAttackSpecial(this));
		this.tasks.addTask(13, new EntityAIAttackRanged(this));
		this.tasks.addTask(14, new EntityAIPotionThrower(this)); /* AI for secondary Item */
		this.tasks.addTask(15, new EntityAIFireball(this)); /* AI for secondary Item */
		this.tasks.addTask(16, new EntityAIHooker(this)); /* AI for secondary Item */
		this.tasks.addTask(17, new EntityAIBackstab(this));
		this.tasks.addTask(18, new EntityAIAttack(this));

		this.tasks.addTask(20, new EntityAIFollowAttackTarget(this));
		this.tasks.addTask(21, new EntityAIFireFighter(this));
		this.tasks.addTask(22, new EntityAITorchIgniter(this));
		this.tasks.addTask(23, new EntityAILooter(this));
		this.tasks.addTask(24, new EntityAITameAndLeashPet(this));
		this.tasks.addTask(25, new EntityAISearchMount(this));

		this.tasks.addTask(30, new EntityAIMoveToLeader(this));
		this.tasks.addTask(31, new EntityAIFollowPath(this));
		this.tasks.addTask(32, new EntityAIMoveToHome(this));
		this.tasks.addTask(33, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHealingPotions(1);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, new ItemStack(CQRItems.BADGE));
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			this.setDropChance(slot, 0.04F);
		}
		return livingdata;
	}

	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		double modalValue = CQRConfig.mobs.dropDurabilityModalValue;
		double standardDeviation = CQRConfig.mobs.dropDurabilityStandardDeviation;
		double min = Math.min(CQRConfig.mobs.dropDurabilityMinimum, modalValue);
		double max = Math.max(CQRConfig.mobs.dropDurabilityMaximum, modalValue);

		for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
			ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
			double d0 = (double) this.getDropChance(entityequipmentslot);
			boolean flag = d0 > 1.0D;

			boolean backpackflag = false;
			if (itemstack.getItem() instanceof ItemBackpack) {
				IItemHandler inv = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (inv != null) {
					for (int i = 0; i < inv.getSlots(); i++) {
						if (!inv.getStackInSlot(i).isEmpty()) {
							backpackflag = true;
							break;
						}
					}
				}
			}

			if (backpackflag || (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (wasRecentlyHit || flag) && (double) (this.rand.nextFloat() - (float) lootingModifier * 0.01F) < d0)) {
				if (!flag && itemstack.isItemStackDamageable() && !backpackflag) {
					double durability = modalValue + MathHelper.clamp(this.rand.nextGaussian() * standardDeviation, min - modalValue, max - modalValue);
					itemstack.setItemDamage((int) ((double) itemstack.getMaxDamage() * (1.0D - durability)));
				}

				this.entityDropItem(itemstack, 0.0F);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if (this.homePosition != null) {
			compound.setTag("home", NBTUtil.createPosTag(this.homePosition));
		}

		if (this.leaderUUID != null) {
			compound.setTag("leader", NBTUtil.createUUIDTag(this.leaderUUID));
		}
		if (this.factionName != null && !this.factionName.equalsIgnoreCase(this.getDefaultFaction().name())) {
			compound.setString("factionOverride", this.factionName);
		}
		compound.setInteger("textureIndex", this.dataManager.get(TEXTURE_INDEX));
		compound.setByte("usedHealingPotions", this.usedPotions);
		compound.setFloat("sizeScaling", this.sizeScaling);
		//compound.setBoolean("isSitting", this.dataManager.get(IS_SITTING));
		compound.setBoolean("holdingPotion", this.holdingPotion);
		compound.setDouble("healthScale", this.healthScale);

		NBTTagCompound pathTag = new NBTTagCompound();
		pathTag.setTag("path", this.path.writeToNBT());
		pathTag.setInteger("prevPathTargetPoint", this.prevPathTargetPoint);
		pathTag.setInteger("currentPathTargetPoint", this.currentPathTargetPoint);
		compound.setTag("pathTag", pathTag);

		// Shoulder entity stuff
		if (!this.getLeftShoulderEntity().isEmpty()) {
			compound.setTag("ShoulderEntityLeft", this.getLeftShoulderEntity());
		}

		compound.setTag("trades", this.trades.writeToNBT(new NBTTagCompound()));
		if (this.hasTextureOverride()) {
			compound.setString("textureOverride", this.getTextureOverride().toString());
		}

		if (this.bossInfoServer != null) {
			compound.setBoolean("hasBossBar", true);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.hasKey("home")) {
			this.homePosition = NBTUtil.getPosFromTag(compound.getCompoundTag("home"));
		}

		if (compound.hasKey("leader")) {
			this.leaderUUID = NBTUtil.getUUIDFromTag(compound.getCompoundTag("leader"));
		}

		if (compound.hasKey("factionOverride")) {
			this.setFaction(compound.getString("factionOverride"));
		}

		this.dataManager.set(TEXTURE_INDEX, compound.getInteger("textureIndex"));
		this.usedPotions = compound.getByte("usedHealingPotions");
		this.sizeScaling = compound.hasKey("sizeScaling") ? compound.getFloat("sizeScaling") : 1.0F;
		//this.dataManager.set(IS_SITTING, compound.getBoolean("isSitting"));
		this.holdingPotion = compound.getBoolean("holdingPotion");
		this.setHealthScale(compound.hasKey("healthScale", Constants.NBT.TAG_DOUBLE) ? compound.getDouble("healthScale") : 1.0D);

		if (compound.hasKey("pathingAI", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound pathTag = compound.getCompoundTag("pathingAI");
			NBTTagList nbtTagList = pathTag.getTagList("pathPoints", Constants.NBT.TAG_COMPOUND);
			this.path.clear();
			for (int i = 0; i < nbtTagList.tagCount(); i++) {
				BlockPos pos = NBTUtil.getPosFromTag(nbtTagList.getCompoundTagAt(i));
				this.path.addNode(this.path.getNode(this.path.getSize() - 1), pos, 0, 0, 0.0F, 1, 0, 24000, true);
			}
			this.currentPathTargetPoint = pathTag.getInteger("currentPathPoint");
			if (nbtTagList.tagCount() > 1) {
				if (this.currentPathTargetPoint > 0) {
					this.prevPathTargetPoint = this.currentPathTargetPoint - 1;
				} else {
					this.prevPathTargetPoint = nbtTagList.tagCount();
				}
			} else {
				this.prevPathTargetPoint = -1;
			}
		} else if (compound.hasKey("pathTag", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound pathTag = compound.getCompoundTag("pathTag");
			this.path.readFromNBT(pathTag.getCompoundTag("path"));
			this.prevPathTargetPoint = pathTag.getInteger("prevPathTargetPoint");
			this.currentPathTargetPoint = pathTag.getInteger("currentPathTargetPoint");
		}

		// Shoulder entity stuff
		if (compound.hasKey("ShoulderEntityLeft", 10)) {
			this.setLeftShoulderEntity(compound.getCompoundTag("ShoulderEntityLeft"));
		}

		this.trades.readFromNBT(compound.getCompoundTag("trades"));

		if (compound.hasKey("textureOverride", Constants.NBT.TAG_STRING)) {
			String ct = compound.getString("textureOverride");
			if (!ct.isEmpty()) {
				this.setCustomTexture(new ResourceLocation(ct));
			}
		}

		if (compound.hasKey("hasBossBar")) {
			this.enableBossBar();
		}

		if (this.hasCustomName() && this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		boolean flag = false;

		if (player.getHeldItem(hand).getItem() instanceof ItemNameTag) {
			return super.processInteract(player, hand);
		}

		if (!player.isSneaking()) {
			if (player.isCreative() || this.getLeader() == player) {
				if (!this.world.isRemote) {
					player.openGui(CQRMain.INSTANCE, Reference.CQR_ENTITY_GUI_ID, this.world, this.getEntityId(), 0, 0);
				}
				flag = true;
			} else if (!this.getFaction().isEnemy(player) && !this.trades.isEmpty()) {
				if (!this.world.isRemote) {
					player.openGui(CQRMain.INSTANCE, Reference.MERCHANT_GUI_ID, this.world, this.getEntityId(), 0, 0);
				}
				flag = true;
			}
		} else if (player.isCreative() || (!this.getFaction().isEnemy(player) && !this.trades.isEmpty())) {
			if (!this.world.isRemote) {
				player.openGui(CQRMain.INSTANCE, Reference.MERCHANT_GUI_ID, this.world, this.getEntityId(), 0, 0);
			}
			flag = true;
		}

		if (flag && !this.getLookHelper().getIsLooking() && !this.hasPath()) {
			double x1 = player.posX - this.posX;
			double z1 = player.posZ - this.posZ;
			float yaw = (float) Math.toDegrees(Math.atan2(-x1, z1));
			this.rotationYaw = yaw;
			this.rotationYawHead = yaw;
			this.renderYawOffset = yaw;
		}

		return flag;
	}

	@Override
	protected abstract ResourceLocation getLootTable();

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
		ResourceLocation resourcelocation = this.getLootTable();
		if (resourcelocation != null) {
			LootTable lootTable = this.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
			LootContext.Builder lootContextBuilder = new LootContext.Builder((WorldServer) this.world).withLootedEntity(this).withDamageSource(source);
			if (wasRecentlyHit && this.attackingPlayer != null) {
				lootContextBuilder = lootContextBuilder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
			}

			for (ItemStack itemstack : lootTable.generateLootForPools(this.rand, lootContextBuilder.build())) {
				this.entityDropItem(itemstack, 0.0F);
			}
		}

		ItemStack badge = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.BADGE);
		if (badge.getItem() instanceof ItemBadge) {
			IItemHandler capability = badge.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i = 0; i < capability.getSlots(); i++) {
				this.entityDropItem(capability.getStackInSlot(i), 0.0F);
			}
		}
		this.dropEquipment(wasRecentlyHit, lootingModifier);
	}

	@Override
	public void onUpdate() {
		EntityLivingBase attackTarget = this.getAttackTarget();
		if (attackTarget != null) {
			this.lastTickWithAttackTarget = this.ticksExisted;
			if (this.isInSightRange(attackTarget) && this.getEntitySenses().canSee(attackTarget)) {
				this.lastTimeSeenAttackTarget = this.ticksExisted;
			}
			if (this.lastTimeSeenAttackTarget + 100 >= this.ticksExisted) {
				this.lastPosAttackTarget = attackTarget.getPositionVector();
			}
		}

		if (this.lastTickWithAttackTarget + 60 < this.ticksExisted && this.damageBlockedWithShield > 0.0F) {
			this.damageBlockedWithShield = Math.max(this.damageBlockedWithShield - 0.02F, 0.0F);
		}

		super.onUpdate();

		if (!this.world.isRemote && this.isMagicArmorActive()) {
			this.updateCooldownForMagicArmor();
		}
		if (!this.world.isRemote && !this.isNonBoss() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			/*
			 * BlockPos pos = new BlockPos(this); TileEntity te = this.world.getTileEntity(pos); if (!(te instanceof TileEntitySpawner)) { this.world.setBlockState(pos,
			 * CQRBlocks.SPAWNER.getDefaultState(), 3); te = this.world.getTileEntity(pos); } if
			 * (te instanceof TileEntitySpawner) { TileEntitySpawner spawner = (TileEntitySpawner) te; for (int i = 0; i < spawner.inventory.getSlots(); i++) { if
			 * (spawner.inventory.getStackInSlot(i).isEmpty()) { ItemStack stack = new
			 * ItemStack(CQRItems.SOUL_BOTTLE); NBTTagCompound stackNBT = new NBTTagCompound(); NBTTagCompound entityNBT = new NBTTagCompound();
			 * this.writeToNBTAtomically(entityNBT); stackNBT.setTag("EntityIn", entityNBT);
			 * stack.setTagCompound(stackNBT); spawner.inventory.setStackInSlot(i, stack); break; } } }
			 */
			SpawnerFactory.placeSpawner(new Entity[] { this }, false, null, this.world, this.getPosition());
			this.setDead();
		}

		ItemStack stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		if (!this.world.isRemote && stack != this.prevPotion) {
			CQRMain.NETWORK.sendToAllTracking(new SPacketItemStackSync(this.getEntityId(), EntityEquipmentExtraSlot.POTION.getIndex(), stack), this);
		}
		this.prevPotion = stack;

		if (this.isSneaking() && !this.prevSneaking) {
			this.resize(1.0F, 0.8F);
		} else if (!this.isSneaking() && this.prevSneaking) {
			this.resize(1.0F, 1.25F);
		}
		if (this.isSitting() && !this.prevSitting) {
			this.resize(1.0F, 0.75F);
		} else if (!this.isSitting() && this.prevSitting) {
			this.resize(1.0F, 4.0F / 3.0F);
		}
		this.prevSneaking = this.isSneaking();
		this.prevSitting = this.isSitting();

		if (!this.world.isRemote) {
			int spellInformation = 0;
			if (this.spellHandler != null) {
				if (this.spellHandler.isSpellCharging()) {
					spellInformation = spellInformation | 1 << 26;
				}
				if (this.spellHandler.isSpellCasting()) {
					spellInformation = spellInformation | 1 << 25;
				}
				if (this.spellHandler.getActiveSpell() instanceof IEntityAISpellAnimatedVanilla) {
					IEntityAISpellAnimatedVanilla spell = (IEntityAISpellAnimatedVanilla) this.spellHandler.getActiveSpell();
					spellInformation = spellInformation | 1 << 24;
					spellInformation = spellInformation | ((int) (spell.getRed() * 255.0D) & 255) << 16;
					spellInformation = spellInformation | ((int) (spell.getGreen() * 255.0D) & 255) << 8;
					spellInformation = spellInformation | (int) (spell.getBlue() * 255.0D) & 255;
				}
			}
			this.dataManager.set(SPELL_INFORMATION, spellInformation);
		} else {
			if (this.isSpellCharging() && this.isSpellAnimated()) {
				int spellColor = this.dataManager.get(SPELL_INFORMATION);
				double red = (double) ((spellColor >> 16) & 255) / 255.0D;
				double green = (double) ((spellColor >> 8) & 255) / 255.0D;
				double blue = (double) (spellColor & 255) / 255.0D;
				float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
				float f1 = MathHelper.cos(f);
				float f2 = MathHelper.sin(f);
				this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) f1 * (double) this.width, this.posY + (double) this.height, this.posZ + (double) f2 * (double) this.width, red, green, blue);
				this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - (double) f1 * (double) this.width, this.posY + (double) this.height, this.posZ - (double) f2 * (double) this.width, red, green, blue);
			}
			if (this.isChatting() && this.ticksExisted % LayerCQRSpeechbubble.CHANGE_BUBBLE_INTERVAL == 0) {
				this.chooseNewRandomSpeechBubble();
			}
		}

		this.updateInvisibility();
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public void onLivingUpdate() {
		this.updateArmSwingProgress();
		super.onLivingUpdate();

		// Bossbar
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.addPlayer(player);
		}
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.removePlayer(player);
		}
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		if (this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_HOSTILE_SWIM;
	}

	@Override
	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_HOSTILE_SPLASH;
	}

	@Override
	protected final SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.canBlockDamageSource(damageSourceIn) ? SoundEvents.ITEM_SHIELD_BLOCK : this.getDefaultHurtSound(damageSourceIn);
	}

	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_HOSTILE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_HOSTILE_DEATH;
	}

	@Override
	protected SoundEvent getFallSound(int heightIn) {
		return heightIn > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		// Shoulder entity stuff
		this.spawnShoulderEntities();

		if (this.getHeldItemMainhand().getItem() instanceof ItemStaffHealing) {
			if (entityIn instanceof EntityLivingBase) {
				if (!this.world.isRemote) {
					((EntityLivingBase) entityIn).heal(ItemStaffHealing.HEAL_AMOUNT_ENTITIES);
					entityIn.setFire(0);
					((WorldServer) this.world).spawnParticle(EnumParticleTypes.HEART, entityIn.posX, entityIn.posY + entityIn.height * 0.5D, entityIn.posZ, 4, 0.25D, 0.25D, 0.25D, 0.0D);
					this.world.playSound(null, entityIn.posX, entityIn.posY + entityIn.height * 0.5D, entityIn.posZ, CQRSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + this.rand.nextFloat() * 0.2F);
				}
				return true;
			}
			return false;
		}
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}
		// Start IceAndFire compatibility
		if (CQRConfig.advanced.enableSpecialFeatures) {
			ResourceLocation resLoc = EntityList.getKey(entityIn);
			if (resLoc != null && resLoc.getNamespace().equalsIgnoreCase("iceandfire")) {
				f *= 2.0F;
			}
		}
		// End IceAndFire compatibility
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	protected boolean canDropLoot() {
		return true;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(this.getSizeVariation());
		buffer.writeDouble(this.getHealthScale());
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.HEAD));
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.CHEST));
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.LEGS));
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.FEET));
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.MAINHAND));
		buffer.writeFloat(this.getDropChance(EntityEquipmentSlot.OFFHAND));
		ByteBufUtils.writeItemStack(buffer, this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION));
		ByteBufUtils.writeTag(buffer, this.trades.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.setSizeVariation(additionalData.readFloat());
		this.setHealthScale(additionalData.readDouble());
		this.setDropChance(EntityEquipmentSlot.HEAD, additionalData.readFloat());
		this.setDropChance(EntityEquipmentSlot.CHEST, additionalData.readFloat());
		this.setDropChance(EntityEquipmentSlot.LEGS, additionalData.readFloat());
		this.setDropChance(EntityEquipmentSlot.FEET, additionalData.readFloat());
		this.setDropChance(EntityEquipmentSlot.MAINHAND, additionalData.readFloat());
		this.setDropChance(EntityEquipmentSlot.OFFHAND, additionalData.readFloat());
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, ByteBufUtils.readItemStack(additionalData));
		this.trades.readFromNBT(ByteBufUtils.readTag(additionalData));
	}

	// #################### Chocolate Quest Repoured ####################

	public EntityLivingBase getLeader() {
		if (this.leaderUUID == null) {
			this.leader = null;
			return null;
		}

		if (this.leader == null) {
			Entity entity = EntityUtil.getEntityByUUID(this.world, this.leaderUUID);

			if (entity instanceof EntityLivingBase) {
				this.leader = (EntityLivingBase) entity;
			}
		}

		if (this.leader != null && !this.leader.isEntityAlive()) {
			this.leaderUUID = null;
			this.leader = null;
		}

		return this.leader;
	}

	public void setLeader(EntityLivingBase leader) {
		if (leader != null && leader.isEntityAlive() && !this.world.isRemote) {
			if (this.dimension == leader.dimension) {
				this.leader = leader;
			}
			this.leaderUUID = leader.getPersistentID();

			CQRFaction leaderFaction = FactionRegistry.instance().getFactionOf(leader);
			if (leaderFaction != null) {
				this.setFaction(leaderFaction.getName(), true);
			}
		}
	}

	public boolean hasLeader() {
		return this.getLeader() != null;
	}

	public boolean isLeader() {
		boolean isLeader = false;
		List<UUID> toRemove = new ArrayList<>();

		for (Map.Entry<UUID, EntityLivingBase> entry : this.followers.entrySet()) {
			UUID uuid = entry.getKey();
			EntityLivingBase entity = entry.getValue();

			if (entity != null) {
				if (!entity.isEntityAlive()) {
					toRemove.add(uuid);
				} else {
					isLeader = true;
				}
			} else {
				Entity newEntity = EntityUtil.getEntityByUUID(this.world, uuid);

				if (newEntity != null) {
					if (newEntity instanceof EntityLivingBase && newEntity.isEntityAlive()) {
						this.followers.put(uuid, (EntityLivingBase) newEntity);
						isLeader = true;
					} else {
						toRemove.add(uuid);
					}
				}
			}
		}

		for (UUID uuid : toRemove) {
			this.followers.remove(uuid);
		}

		return isLeader;
	}

	public void addFollower(EntityLivingBase entity) {
		if (entity == null) {
			return;
		}
		if (!entity.isEntityAlive()) {
			return;
		}
		this.followers.put(entity.getPersistentID(), entity);
	}

	public void removeFollower(EntityLivingBase entity) {
		if (entity == null) {
			return;
		}
		this.followers.remove(entity.getPersistentID());
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
		if (CQRConfig.mobs.enableHealthChangeOnDistance && !this.world.isRemote) {
			int x = pos.getX() - DungeonGenUtils.getSpawnX(this.world);
			int z = pos.getZ() - DungeonGenUtils.getSpawnZ(this.world);
			double distance = Math.sqrt(x * x + z * z);
			double amount = 0.1D * (int) (distance / CQRConfig.mobs.distanceDivisor);

			EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_DISTANCE_TO_SPAWN_ID, "Health Scale Distance To Spawn", amount);
		}
	}

	public void handleArmorBreaking() {
		if (!this.world.isRemote && this.usedPotions + 1 > this.getHealingPotions()) {
			boolean armorBroke = false;
			float hpPrcntg = this.getHealth() / this.getMaxHealth();

			// below 80% health -> remove boobs
			if (hpPrcntg <= 0.8F) {
				if (!this.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()) {
					this.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
					armorBroke = true;
				}

				// below 60% health -> remove helmet
				if (hpPrcntg <= 0.6F) {
					if (!this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
						this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
						armorBroke = true;
					}

					// below 40% health -> remove leggings
					if (hpPrcntg <= 0.4F) {
						if (!this.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) {
							this.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
							armorBroke = true;
						}

						// below 20% health -> remove chestplate
						if (hpPrcntg <= 0.2F) {
							if (!this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && !(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemBackpack)) {
								this.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
								armorBroke = true;
							}
						}
					}
				}
			}

			if (armorBroke) {
				this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.75F, 0.8F);
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
		ItemStack stack = new ItemStack(CQRItems.POTION_HEALING, amount);
		if (this.holdingPotion) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
		} else {
			this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, stack);
		}
	}

	public ItemStack getItemStackFromExtraSlot(EntityEquipmentExtraSlot slot) {
		CapabilityExtraItemHandler capability = this.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
		return capability.getStackInSlot(slot.getIndex());
	}

	public void setItemStackToExtraSlot(EntityEquipmentExtraSlot slot, ItemStack stack) {
		CapabilityExtraItemHandler capability = this.getCapability(CapabilityExtraItemHandlerProvider.EXTRA_ITEM_HANDLER, null);
		capability.setStackInSlot(slot.getIndex(), stack);
	}

	public void swapWeaponAndPotionSlotItemStacks() {
		ItemStack stack1 = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		ItemStack stack2 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack2);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.POTION, stack1);
		this.holdingPotion = !this.holdingPotion;
	}

	public boolean isHoldingPotion() {
		return this.holdingPotion;
	}

	protected abstract EDefaultFaction getDefaultFaction();

	protected CQRFaction getDefaultFactionInstance() {
		if (this.defaultFactionInstance == null) {
			this.defaultFactionInstance = FactionRegistry.instance().getFactionInstance(this.getDefaultFaction().name());
		}
		return this.defaultFactionInstance;
	}

	@Nullable
	public CQRFaction getFaction() {
		if (!this.world.isRemote) {
			// Leader faction is set when assigning the leader
			/*
			 * if (this.hasLeader()) { return FactionRegistry.instance().getFactionOf(this.getLeader()); }
			 */
			if (this.factionInstance == null && this.factionName != null && !this.factionName.isEmpty()) {
				this.factionInstance = FactionRegistry.instance().getFactionInstance(this.factionName);
			}
			if (this.factionInstance != null) {
				return this.factionInstance;
			}
		} else {
			String syncedFaction = this.dataManager.get(FACTION_OVERRIDE_SYNC);
			if (syncedFaction != null && !syncedFaction.isEmpty() && !(this.factionName != null && this.factionName.equals(syncedFaction))) {
				this.factionName = syncedFaction;
				this.factionInstance = FactionRegistry.instance().getFactionInstance(syncedFaction);
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
		// System.out.println("Setting faction...");
		// System.out.println("Fac: " + newFac + " ignoreCTS: " + ignoreCTS);
		if (!this.world.isRemote) {
			CQRFaction faction = FactionRegistry.instance().getFactionInstance(newFac);
			if (faction != null) {
				this.factionInstance = null;
				this.factionName = newFac;
				if (!ignoreCTS) {
					ResourceLocation rs = faction.getRandomTextureFor(this);
					if (rs != null) {
						// System.out.println("Setting custom texture");
						this.setCustomTexture(rs);
					}
				}
				this.dataManager.set(FACTION_OVERRIDE_SYNC, newFac);
			}
		}
	}

	public void setCustomTexture(@Nonnull ResourceLocation texture) {
		// System.out.println("Applying texture to dataManager");
		this.dataManager.set(TEXTURE_OVERRIDE, texture.toString());
	}

	public boolean hasFaction() {
		return this.getFaction() != null;
	}

	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getTrueSource() instanceof EntityPlayer && this.hasFaction() && !this.world.isRemote) {
			EntityPlayer player = (EntityPlayer) cause.getTrueSource();
			int range = CQRConfig.mobs.factionUpdateRadius;
			double x1 = player.posX - range;
			double y1 = player.posY - range;
			double z1 = player.posZ - range;
			double x2 = player.posX + range;
			double y2 = player.posY + range;
			double z2 = player.posZ + range;
			AxisAlignedBB aabb = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);

			List<CQRFaction> checkedFactions = new ArrayList<>();
			// boolean setRepu = false;
			for (AbstractEntityCQR cqrentity : this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && !checkedFactions.contains(cqrentity.getFaction()) && (cqrentity.canEntityBeSeen(this) || cqrentity.canEntityBeSeen(player))) {
					CQRFaction faction = cqrentity.getFaction();
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
			// System.out.println("Repu changed: " + setRepu);
		}
	}

	public void onExportFromWorld() {
		int x = MathHelper.floor(this.posX);
		int y = MathHelper.floor(this.posY);
		int z = MathHelper.floor(this.posZ);

		for (Path.PathNode node : this.path.getNodes()) {
			node.setPos(node.getPos().add(-x, -y, -z));
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon(BlockPos dungeonPos, PlacementSettings placementSettings, DungeonInhabitant mobType) {
		this.setHomePositionCQR(new BlockPos(this));
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
		this.setHealth(this.getMaxHealth());
		this.setBaseHealthDependingOnPos(dungeonPos);

		// Recalculate path points
		for (Path.PathNode node : this.path.getNodes()) {
			node.setPos(Template.transformedBlockPos(placementSettings, node.getPos()));
			node.setWaitingRotation(this.getTransformedYaw(node.getWaitingRotation(), placementSettings.getMirror(), placementSettings.getRotation()));
		}

		// Replace shield
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			ItemStack stack = this.getItemStackFromSlot(slot);
			Item item = stack.getItem();
			if (item instanceof ItemShieldDummy && mobType != null) {
				this.setItemStackToSlot(slot, new ItemStack(mobType.getShieldReplacement(), 1));
			}
		}
		if (mobType != null && mobType.getFactionOverride() != null && !mobType.getFactionOverride().isEmpty() && FactionRegistry.instance().getFactionInstance(mobType.getFactionOverride()) != null) {
			this.setFaction(mobType.getFactionOverride());
		}
	}

	private float getTransformedYaw(float rotationYaw, Mirror mirror, Rotation rotation) {
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

	public void setSizeVariation(float size) {
		this.resize(size / this.sizeScaling, size / this.sizeScaling);
		this.sizeScaling = size;
	}

	public float getSizeVariation() {
		return this.sizeScaling;
	}

	public void setSitting(boolean sitting) {
		this.dataManager.set(IS_SITTING, sitting);
	}

	public boolean isSitting() {
		return this.dataManager.get(IS_SITTING);
	}

	public void setChatting(boolean chatting) {
		this.dataManager.set(TALKING, chatting);
	}

	public boolean isChatting() {
		return this.dataManager.get(TALKING);
	}

	public void setArmPose(ECQREntityArmPoses pose) {
		this.dataManager.set(ARM_POSE, pose.toString());
	}

	public ECQREntityArmPoses getArmPose() {
		return ECQREntityArmPoses.valueOf(this.dataManager.get(ARM_POSE));
	}

	@SideOnly(Side.CLIENT)
	public ESpeechBubble getCurrentSpeechBubble() {
		return ESpeechBubble.values()[this.currentSpeechBubbleID];
	}

	@SideOnly(Side.CLIENT)
	public void chooseNewRandomSpeechBubble() {
		this.currentSpeechBubbleID = this.rand.nextInt(ESpeechBubble.values().length);
	}

	@SideOnly(Side.CLIENT)
	public int getTextureIndex() {
		return this.dataManager.get(TEXTURE_INDEX);
	}

	public int getTextureCount() {
		return 1;
	}

	public double getAttackReach(EntityLivingBase target) {
		double reach = ((double) this.width + (double) target.width) * 0.5D + 0.85D;
		ItemStack stack = this.getHeldItemMainhand();
		if (stack.getItem() instanceof ItemSpearBase) {
			reach += ((ItemSpearBase) stack.getItem()).getReach();
		}
		return reach;
	}

	public boolean isInAttackReach(EntityLivingBase target) {
		return this.isInReach(target, this.getAttackReach(target));
	}

	public boolean isInReach(EntityLivingBase target, double distance) {
		double x = target.posX - this.posX;
		double y = MathHelper.clamp(this.posY + this.getEyeHeight(), target.posY, target.posY + target.height) - (this.posY + this.getEyeHeight());
		double z = target.posZ - this.posZ;
		return x * x + y * y + z * z <= distance * distance;
	}

	public boolean canStrafe() {
		if(this.horseAI != null) {
			return this.getLowestRidingEntity() == null;
		}
		if(this.getRidingEntity() != null) {
			return false;
		}
		return true;
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

	public boolean isEntityInFieldOfView(EntityLivingBase target) {
		double x = target.posX - this.posX;
		double z = target.posZ - this.posZ;
		double d = Math.toDegrees(Math.atan2(-x, z));
		if (!ItemUtil.compareRotations(this.rotationYawHead, d, 80.0D)) {
			return false;
		}
		double y = target.posY + target.getEyeHeight() - this.posY - this.getEyeHeight();
		double xz = Math.sqrt(x * x + z * z);
		double d1 = Math.toDegrees(Math.atan2(y, xz));
		return ItemUtil.compareRotations(this.rotationPitch, d1, 50.0D);
	}

	public void setHealthScale(double newHealthScale) {
		if (this.healthScale != newHealthScale) {
			if (!this.world.isRemote) {
				EntityUtil.applyMaxHealthModifier(this, HEALTH_SCALE_SLIDER_ID, "Health Scale Slider", newHealthScale - 1.0D);
			}
			this.healthScale = newHealthScale;
		}
	}

	public double getHealthScale() {
		return this.healthScale;
	}

	public float getDropChance(EntityEquipmentSlot slot) {
		switch (slot.getSlotType()) {
		case HAND:
			return this.inventoryHandsDropChances[slot.getIndex()];
		case ARMOR:
			return this.inventoryArmorDropChances[slot.getIndex()];
		default:
			return 0.0F;
		}
	}

	public boolean isInSightRange(Entity target) {
		double sightRange = 32.0D;
		sightRange *= 0.6D + 0.4D * (double) this.world.getLight(new BlockPos(target)) / 15.0D;
		sightRange *= this.isPotionActive(MobEffects.BLINDNESS) ? 0.5D : 1.0D;
		return this.getDistanceSq(target) <= sightRange * sightRange;
	}

	public ItemStack getHeldItemWeapon() {
		return this.isHoldingPotion() ? this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION) : this.getHeldItemMainhand();
	}

	public ItemStack getHeldItemPotion() {
		return this.isHoldingPotion() ? this.getHeldItemMainhand() : this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
	}

	public boolean isMagicArmorActive() {
		if (!this.world.isRemote) {
			return this.armorActive;
		}
		return this.dataManager.get(MAGIC_ARMOR_ACTIVE);
	}

	public void setMagicArmorActive(boolean val) {
		if (val != this.armorActive) {
			this.armorActive = val;
			this.setEntityInvulnerable(this.armorActive);
			this.dataManager.set(MAGIC_ARMOR_ACTIVE, val);
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

	public float getDefaultWidth() {
		return 0.6F;
	}

	public float getDefaultHeight() {
		return 1.95F;
	}

	public void resize(float widthScale, float heightSacle) {
		this.setSize(this.width * widthScale, this.height * heightSacle);
		if (this.stepHeight * heightSacle >= 1.0) {
			this.stepHeight *= heightSacle;
		}
	}

	public Path getPath() {
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

	public Vec3d getLastPosAttackTarget() {
		return this.lastPosAttackTarget;
	}

	public EntityAISpellHandler createSpellHandler() {
		return new EntityAISpellHandler(this, 200);
	}

	public boolean isSpellCharging() {
		return (this.dataManager.get(SPELL_INFORMATION) >> 26 & 1) == 1;
	}

	public boolean isSpellCasting() {
		return (this.dataManager.get(SPELL_INFORMATION) >> 25 & 1) == 1;
	}

	public boolean isSpellAnimated() {
		return (this.dataManager.get(SPELL_INFORMATION) >> 24 & 1) == 1;
	}

	public void setLastTimeHitByAxeWhileBlocking(int tick) {
		this.lastTickShieldDisabled = tick;
	}

	public int getLastTimeHitByAxeWhileBlocking() {
		return this.lastTickShieldDisabled;
	}

	// @SideOnly(Side.CLIENT)
	public boolean hasAttackTarget() {
		if (this.world.isRemote) {
			return this.dataManager.get(HAS_TARGET);
		} else {
			return this.getAttackTarget() != null && !this.getAttackTarget().isDead;
		}
	}

	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		super.setAttackTarget(entitylivingbaseIn);
		EntityLivingBase attackTarget = this.getAttackTarget();
		if (attackTarget == null) {
			this.dataManager.set(HAS_TARGET, false);
			this.lastTimeSeenAttackTarget = Integer.MIN_VALUE;
			this.lastPosAttackTarget = this.getPositionVector();

			Item item = this.getHeldItemMainhand().getItem();
			if (item instanceof IFakeWeapon<?>) {
				this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
			}
		} else {
			this.dataManager.set(HAS_TARGET, true);
			this.lastTimeSeenAttackTarget = this.ticksExisted;
			this.lastPosAttackTarget = attackTarget.getPositionVector();

			CQRFaction faction = this.getFaction();
			if (faction != null) {
				Item item = this.getHeldItemMainhand().getItem();
				if (faction.isAlly(attackTarget)) {
					if (item instanceof IFakeWeapon<?>) {
						this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((IFakeWeapon<?>) item).getOriginalItem()));
					}
				} else if (item instanceof ISupportWeapon<?>) {
					this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(((ISupportWeapon<?>) item).getFakeWeapon()));
				}
			}
		}
	}

	// Shoulder entity stuff

	public boolean addShoulderEntity(NBTTagCompound compound) {
		if (!this.isRiding() && this.onGround && !this.isInWater()) {
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
		this.setLeftShoulderEntity(new NBTTagCompound());
	}

	private void spawnShoulderEntity(@Nullable NBTTagCompound compound) {
		if (!this.world.isRemote && compound != null && !compound.isEmpty()) {
			Entity entity = EntityList.createEntityFromNBT(compound, this.world);

			if (entity instanceof EntityTameable) {
				((EntityTameable) entity).setOwnerId(this.entityUniqueID);
			}

			entity.setPosition(this.posX, this.posY + 0.699999988079071D, this.posZ);
			this.world.spawnEntity(entity);
		}
	}

	public NBTTagCompound getLeftShoulderEntity() {
		return this.dataManager.get(SHOULDER_ENTITY);
	}

	protected void setLeftShoulderEntity(NBTTagCompound tag) {
		this.dataManager.set(SHOULDER_ENTITY, tag);
	}

	public boolean canUseSpinToWinAttack() {
		return true;
	}

	public boolean isSpinToWinActive() {
		return this.canUseSpinToWinAttack() && this.dataManager.get(SPIN_TO_WIN);
	}

	@SideOnly(Side.SERVER)
	public void setSpinToWin(boolean value) {
		this.dataManager.set(SPIN_TO_WIN, value);
	}

	public TraderOffer getTrades() {
		return this.trades;
	}

	public void teleport(double x, double y, double z) {
		this.setPosition(x, y, z);
		CQRMain.NETWORK.sendToAllTracking(new SPacketUpdateEntityPrevPos(this), this);
	}

	// Custom textures
	public boolean hasTextureOverride() {
		return this.dataManager.get(TEXTURE_OVERRIDE) != null && !this.dataManager.get(TEXTURE_OVERRIDE).isEmpty();
	}

	public ResourceLocation getTextureOverride() {
		if (this.textureOverride == null || !this.textureOverride.toString().equals(this.dataManager.get(TEXTURE_OVERRIDE))) {
			this.textureOverride = new ResourceLocation(this.dataManager.get(TEXTURE_OVERRIDE));
		}
		return this.textureOverride;
	}

	public void updateInvisibility() {
		if (!this.world.isRemote) {
			if (this.invisibilityTick > 0) {
				this.invisibilityTick--;
				this.dataManager.set(INVISIBILITY, Math.min(this.dataManager.get(INVISIBILITY) + 1.0F / this.getInvisibilityTurningTime(), 1.0F));
			} else {
				this.dataManager.set(INVISIBILITY, Math.max(this.dataManager.get(INVISIBILITY) - 1.0F / this.getInvisibilityTurningTime(), 0.0F));
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
		return this.dataManager.get(INVISIBILITY);
	}

}
