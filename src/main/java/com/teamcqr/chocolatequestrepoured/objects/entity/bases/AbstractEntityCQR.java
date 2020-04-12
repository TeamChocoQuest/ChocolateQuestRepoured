package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.client.init.ESpeechBubble;
import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.ItemStackSyncPacket;
import com.teamcqr.chocolatequestrepoured.objects.entity.ECQREntityArmPoses;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttackRanged;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIBackstab;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFollowAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFollowPath;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAISearchMount;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAITameAndLeashPet;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.EntityAISpellHandler;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.IEntityAISpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIHurtByTarget;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

public abstract class AbstractEntityCQR extends EntityCreature implements IMob, IEntityAdditionalSpawnData {

	protected BlockPos homePosition = null;
	protected UUID leaderUUID;
	protected EntityLivingBase leader = null;
	protected boolean holdingPotion;
	protected ResourceLocation lootTable;
	protected byte usedPotions = (byte) 0;
	protected double healthScale = 1D;
	public ItemStack prevPotion;
	public boolean prevSneaking;
	public boolean prevSitting;
	protected float sizeScaling = 1.0F;
	protected int lastTimeSeenAttackTarget;
	protected Vec3d lastPosAttackTarget;
	protected EntityAISpellHandler spellHandler;

	private CQRFaction factionInstance;
	private String factionName;
	private CQRFaction defaultFactionInstance;

	protected boolean armorActive = false;
	protected int magicArmorCooldown = 300;

	// Pathing AI stuff
	protected BlockPos[] pathPoints = new BlockPos[] {};
	protected boolean pathIsLoop = false;
	protected int currentTargetPoint = 0;

	// Sync with client
	protected static final DataParameter<Boolean> IS_SITTING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<String> ARM_POSE = EntityDataManager.<String>createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	protected static final DataParameter<Boolean> TALKING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.<Integer>createKey(AbstractEntityCQR.class, DataSerializers.VARINT);
	protected static final DataParameter<Boolean> MAGIC_ARMOR_ACTIVE = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> IS_SPELL_CHARGING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> IS_SPELL_CASTING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);

	public int deathTicks = 0;
	public static float MAX_DEATH_TICKS = 200.0F;

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

	@Override
	protected void entityInit() {
		super.entityInit();

		this.dataManager.register(IS_SITTING, false);
		this.dataManager.register(ARM_POSE, ECQREntityArmPoses.NONE.toString());
		this.dataManager.register(TALKING, false);
		this.dataManager.register(TEXTURE_INDEX, this.getRNG().nextInt(this.getTextureCount()));
		this.dataManager.register(MAGIC_ARMOR_ACTIVE, false);
		this.dataManager.register(IS_SPELL_CHARGING, false);
		this.dataManager.register(IS_SPELL_CASTING, false);
	}

	@Override
	protected boolean canDespawn() {
		return !CQRConfig.general.mobsFromCQSpawnerDontDespawn;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getBaseHealth());
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		PathNavigate navigator = new PathNavigateGround(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 256.0F;
			}
		};
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
			if (resLoc != null && resLoc.getResourceDomain().equalsIgnoreCase("iceandfire")) {
				amount *= 0.5F;
			}
		}
		// End IceAndFire compatibility

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

		if (super.attackEntityFrom(source, amount)) {
			if (CQRConfig.mobs.armorShattersOnMobs) {
				this.handleArmorBreaking();
			}

			return true;
		}

		return false;
	}

	@Override
	public void onDeath(DamageSource cause) {
		if (this.isHoldingPotion()) {
			this.swapWeaponAndPotionSlotItemStacks();
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

		this.tasks.addTask(10, new EntityAIHealingPotion(this));
		this.tasks.addTask(11, this.spellHandler);
		this.tasks.addTask(12, new EntityAIAttackRanged(this));
		this.tasks.addTask(13, new EntityAIBackstab(this));
		this.tasks.addTask(14, new EntityAIAttack(this));

		this.tasks.addTask(20, new EntityAIFollowAttackTarget(this));
		this.tasks.addTask(21, new EntityAITameAndLeashPet(this));
		this.tasks.addTask(22, new EntityAISearchMount(this));
		this.tasks.addTask(23, new EntityAIMoveToLeader(this));
		this.tasks.addTask(24, new EntityAIFollowPath(this));
		this.tasks.addTask(25, new EntityAIMoveToHome(this));
		this.tasks.addTask(26, new EntityAIIdleSit(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this));
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		this.setHealingPotions(CQRConfig.mobs.defaultHealingPotionCount);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BADGE, new ItemStack(ModItems.BADGE));
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

			if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (wasRecentlyHit || flag) && (double) (this.rand.nextFloat() - (float) lootingModifier * 0.01F) < d0) {
				if (!flag && itemstack.isItemStackDamageable()) {
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
		compound.setBoolean("isSitting", this.dataManager.get(IS_SITTING));
		compound.setBoolean("holdingPotion", this.holdingPotion);
		compound.setDouble("healthScale", this.healthScale);

		if (this.pathPoints.length > 0) {
			NBTTagCompound pathTag = new NBTTagCompound();
			pathTag.setInteger("pointcount", this.pathPoints.length);
			NBTTagList pathPoints = pathTag.getTagList("points", Constants.NBT.TAG_COMPOUND);
			if (pathPoints.tagCount() != this.pathPoints.length) {
				pathPoints = new NBTTagList();
			}
			for (int i = 0; i < this.pathPoints.length; i++) {
				pathPoints.appendTag(NBTUtil.createPosTag(this.pathPoints[i]));
			}
			pathTag.setTag("points", pathPoints);

			compound.setTag("pathingAI", pathTag);
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
		this.dataManager.set(IS_SITTING, compound.getBoolean("isSitting"));
		this.holdingPotion = compound.getBoolean("holdingPotion");
		this.healthScale = compound.getDouble("healthScale");
		if (this.healthScale <= 1.0D) {
			this.healthScale = 1.0D;
		}

		if (compound.hasKey("pathingAI")) {
			NBTTagCompound pathTag = compound.getCompoundTag("pathingAI");
			int pointcount = compound.getInteger("pointcount");
			NBTTagList pathPoints = pathTag.getTagList("points", Constants.NBT.TAG_COMPOUND);
			this.pathPoints = new BlockPos[pointcount];
			for (int i = 0; i < this.pathPoints.length; i++) {
				this.pathPoints[i] = NBTUtil.getPosFromTag(pathPoints.getCompoundTagAt(i));
			}
		}
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (player.isCreative() && !player.isSneaking()) {
			if (!this.world.isRemote) {
				ItemStack stack = player.getHeldItem(hand);

				if (stack.getItem() instanceof ItemArmor) {
					EntityEquipmentSlot slot = getSlotForItemStack(stack);

					player.setHeldItem(hand, this.getItemStackFromSlot(slot));
					this.setItemStackToSlot(slot, stack);
					return true;
				}

				if (stack.getItem() instanceof ItemSword) {
					player.setHeldItem(hand, this.getHeldItemMainhand());
					this.setHeldItem(EnumHand.MAIN_HAND, stack);
					return true;
				}

				if (stack.getItem() instanceof ItemShield) {
					player.setHeldItem(hand, this.getHeldItemOffhand());
					this.setHeldItem(EnumHand.OFF_HAND, stack);
					return true;
				}

				if (!this.getLookHelper().getIsLooking() && !this.hasPath()) {
					double x1 = player.posX - this.posX;
					double z1 = player.posZ - this.posZ;
					float yaw = (float) Math.toDegrees(Math.atan2(-x1, z1));
					this.rotationYaw = yaw;
					this.rotationYawHead = yaw;
					this.renderYawOffset = yaw;
				}
				player.openGui(CQRMain.INSTANCE, Reference.CQR_ENTITY_GUI_ID, this.world, this.getEntityId(), 0, 0);
			}
			return true;
		}
		if (this.hasLeader() && this.getLeader() == player && !player.isSneaking()) {
			if (!this.world.isRemote) {
				player.openGui(CQRMain.INSTANCE, Reference.CQR_ENTITY_GUI_ID, this.world, this.getEntityId(), 0, 0);
			}
			return true;
		}
		return false;
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
			if (this.getEntitySenses().canSee(attackTarget) && this.isEntityInFieldOfView(attackTarget)) {
				this.lastTimeSeenAttackTarget = this.ticksExisted;
			}
			if (this.lastTimeSeenAttackTarget + 10 > this.ticksExisted) {
				this.lastPosAttackTarget = attackTarget.getPositionVector();
			}
		}

		super.onUpdate();

		if (!this.world.isRemote && this.isMagicArmorActive()) {
			this.updateCooldownForMagicArmor();
		}
		if (!this.world.isRemote && !this.isNonBoss() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			SpawnerFactory.placeSpawner(new Entity[] { this }, false, null, this.world, this.getPosition());
			this.setDead();
		}

		ItemStack stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.POTION);
		if (!this.world.isRemote && stack != this.prevPotion) {
			CQRMain.NETWORK.sendToAll(new ItemStackSyncPacket(this.getEntityId(), EntityEquipmentExtraSlot.POTION.getIndex(), stack));
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
			this.dataManager.set(IS_SPELL_CHARGING, this.spellHandler != null && this.spellHandler.isSpellCharging());
			this.dataManager.set(IS_SPELL_CASTING, this.spellHandler != null && this.spellHandler.isSpellCasting());
		}
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public void onLivingUpdate() {
		this.updateArmSwingProgress();
		super.onLivingUpdate();
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
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
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
		if (this.getHeldItemMainhand().getItem() instanceof ItemStaffHealing) {
			if (entityIn instanceof EntityLivingBase) {
				if (!this.world.isRemote) {
					((EntityLivingBase) entityIn).heal(ItemStaffHealing.HEAL_AMOUNT_ENTITIES);
					entityIn.setFire(0);
					((WorldServer) this.world).spawnParticle(EnumParticleTypes.HEART, entityIn.posX, entityIn.posY + entityIn.height * 0.5D, entityIn.posZ, 4, 0.25D, 0.25D, 0.25D, 0.0D);
					this.world.playSound(null, entityIn.posX, entityIn.posY + entityIn.height * 0.5D, entityIn.posZ, ModSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + this.rand.nextFloat() * 0.2F);
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
			if (resLoc != null && resLoc.getResourceDomain().equalsIgnoreCase("iceandfire")) {
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
	public PathNavigate getNavigator() {
		if (this.isRiding()) {
			Entity ridden = this.getRidingEntity();
			if (ridden instanceof EntityLiving) {
				return ((EntityLiving) ridden).getNavigator();
			}
		}
		return super.getNavigator();
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
	}

	// Chocolate Quest Repoured
	public EntityLivingBase getLeader() {
		if (this.leaderUUID != null) {
			if (this.leader != null) {
				if (this.leader.isEntityAlive()) {
					return this.leader;
				}
				this.leader = null;
				this.leaderUUID = null;
			} else {
				for (Entity entity : this.world.loadedEntityList) {
					if (entity instanceof EntityLivingBase && this.leaderUUID.equals(entity.getPersistentID()) && entity.isEntityAlive()) {
						this.leader = (EntityLivingBase) entity;
						return (EntityLivingBase) entity;
					}
				}
			}
		} else {
			this.leader = null;
		}
		return null;
	}

	public void setLeader(EntityLivingBase leader) {
		if (leader != null && leader.isEntityAlive()) {
			if (this.dimension == leader.dimension) {
				this.leader = leader;
			}
			this.leaderUUID = leader.getPersistentID();
		}
	}

	public boolean hasLeader() {
		return this.getLeader() != null;
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

	public float calculateBaseHealth(double x, double z, float health) {
		BlockPos spawn = this.world.getSpawnPoint();
		x -= (double) spawn.getX();
		z -= (double) spawn.getZ();
		float distance = (float) Math.sqrt(x * x + z * z);

		health *= 1.0F + 0.1F * (int) (distance / CQRConfig.mobs.distanceDivisor);
		health *= this.healthScale;

		return (float) (int) health;
	}

	public void setBaseHealth(BlockPos pos, float health) {
		health = this.calculateBaseHealth(pos.getX(), pos.getZ(), health);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
		this.setHealth(health);
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
							if (!this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) {
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
		ItemStack stack = new ItemStack(ModItems.POTION_HEALING, amount);
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

	public abstract EDefaultFaction getDefaultFaction();

	public CQRFaction getDefaultFactionInstance() {
		if (this.defaultFactionInstance == null) {
			this.defaultFactionInstance = FactionRegistry.instance().getFactionInstance(this.getDefaultFaction().name());
		}
		return this.defaultFactionInstance;
	}

	public CQRFaction getFaction() {
		if (this.factionInstance == null && this.factionName != null && !this.factionName.isEmpty()) {
			this.factionInstance = FactionRegistry.instance().getFactionInstance(this.factionName);
		}
		if (this.hasLeader()) {
			return FactionRegistry.instance().getFactionOf(this.getLeader());
		}
		if (this.factionInstance != null) {
			return this.factionInstance;
		}
		return this.getDefaultFactionInstance();
	}

	public void setFaction(String newFac) {
		this.factionInstance = null;
		this.factionName = newFac;
	}

	public boolean hasFaction() {
		return this.getFaction() != null;
	}

	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getTrueSource() instanceof EntityPlayer && this.hasFaction()) {
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
			for (AbstractEntityCQR cqrentity : this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && !checkedFactions.contains(cqrentity.getFaction()) && (cqrentity.canEntityBeSeen(this) || cqrentity.canEntityBeSeen(player))) {
					CQRFaction faction = cqrentity.getFaction();
					if (this.getFaction().equals(faction)) {
						// DONE decrement the players repu on this entity's faction
						faction.decrementReputation(player, faction.getRepuMemberKill());
					} else if (this.getFaction().isEnemy(faction)) {
						// DONE increment the players repu at CQREntity's faction
						faction.incrementReputation(player, faction.getRepuEnemyKill());
					} else if (this.getFaction().isAlly(faction)) {
						// DONE decrement the players repu on CQREntity's faction
						faction.decrementReputation(player, faction.getRepuAllyKill());
					}
					checkedFactions.add(faction);
				}
			}
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon(PlacementSettings placementSettings) {
		this.setHomePositionCQR(this.getPosition());
		this.setBaseHealth(this.getPosition(), this.getBaseHealth());

		// Recalculate path points
		if (this.pathPoints.length > 0) {
			for (int i = 0; i < this.pathPoints.length; i++) {
				this.pathPoints[i] = Template.transformedBlockPos(placementSettings, this.pathPoints[i]);
			}
		}
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

	public boolean isLeader() {
		// TODO: Implement team building
		return false;
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
		return this.width + target.width + 0.25D;
	}

	public boolean isInAttackReach(EntityLivingBase target) {
		return target != null && this.getDistance(target) <= this.getAttackReach(target);
	}

	public boolean canOpenDoors() {
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

	public void setHealthScale(double healthScale) {
		this.healthScale = healthScale;
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
		return this.getDistance(target) <= sightRange;
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

	public BlockPos[] getGuardPathPoints() {
		return this.pathPoints;
	}

	public boolean isGuardPathLoop() {
		return this.pathIsLoop;
	}

	public int getCurrentGuardPathTargetPoint() {
		return this.currentTargetPoint;
	}

	public void setCurrentGuardPathTargetPoint(int value) {
		this.currentTargetPoint = value;
	}

	public void addPathPoint(BlockPos position) {
		if (this.getHomePositionCQR() == null) {
			this.setHomePositionCQR(position);
		}
		BlockPos[] newPosArr = new BlockPos[this.pathPoints.length + 1];
		for (int i = 0; i < this.pathPoints.length; i++) {
			newPosArr[i] = this.pathPoints[i];
		}
		position = position.subtract(this.getHomePositionCQR());
		newPosArr[this.pathPoints.length] = position;
		this.pathPoints = newPosArr;
	}

	public void clearPathPoints() {
		this.pathPoints = new BlockPos[] {};
	}

	public void setPath(final BlockPos[] path) {
		if (path.length <= 0) {
			this.pathPoints = new BlockPos[] {};
		}
		this.pathPoints = new BlockPos[path.length];
		for (int i = 0; i < path.length; i++) {
			if (this.getHomePositionCQR() == null) {
				this.setHomePositionCQR(path[i]);
			}
			this.pathPoints[i] = path[i].subtract(this.getHomePositionCQR());
		}
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

	@Nullable
	public IEntityAISpell getActiveSpell() {
		return this.spellHandler != null ? this.spellHandler.getActiveSpell() : null;
	}

	public boolean isSpellCharging() {
		return this.dataManager.get(IS_SPELL_CHARGING);
	}

	public boolean isSpellCasting() {
		return this.dataManager.get(IS_SPELL_CASTING);
	}

}
