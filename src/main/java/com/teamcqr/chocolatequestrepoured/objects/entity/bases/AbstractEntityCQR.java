package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandlerProvider;
import com.teamcqr.chocolatequestrepoured.client.init.ESpeechBubble;
import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.network.ItemStackSyncPacket;
import com.teamcqr.chocolatequestrepoured.objects.entity.ECQREntityArmPoses;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIAttack;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIBackstab;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAICQRNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIFireFighter;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIIdleSit;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAISearchMount;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAITameAndLeashPet;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAITorchIgniter;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemBadge;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractEntityCQR extends EntityCreature implements IMob,IEntityAdditionalSpawnData {

	//Client side visable vars:
	/*
	 * - usedPotions
	 * - sizeVariation
	 * - sitting
	 */
	
	protected BlockPos homePosition;
	protected UUID leaderUUID;
	protected boolean holdingPotion;
	protected ResourceLocation lootTable;
	protected byte usedPotions = (byte)0;
	protected boolean sittingState = false;
	protected double healthScale = 1D;
	public ItemStack prevPotion;
	
	//Sync with client
	protected static final DataParameter<Boolean> IS_SITTING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Float> SIZE_VAR = EntityDataManager.<Float>createKey(AbstractEntityCQR.class, DataSerializers.FLOAT);
	protected static final DataParameter<String> ARM_POSE = EntityDataManager.<String>createKey(AbstractEntityCQR.class, DataSerializers.STRING);
	protected static final DataParameter<Boolean> TALKING = EntityDataManager.<Boolean>createKey(AbstractEntityCQR.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.<Integer>createKey(AbstractEntityCQR.class, DataSerializers.VARINT); 
	
	//Client only
	@SideOnly(Side.CLIENT)
	protected int currentSpeechBubbleID = 0;

	public AbstractEntityCQR(World worldIn) {
		super(worldIn);
		currentSpeechBubbleID = getRNG().nextInt(ESpeechBubble.values().length);
		//this.setSize(0.6F * (1F + getSizeVariation()*0.8F), 1.8F *(1F + getSizeVariation()));
		this.experienceValue = 5;
	}
	@Override
	protected void entityInit() {
		super.entityInit();
		
		this.dataManager.register(SIZE_VAR, 0F);
		this.dataManager.register(IS_SITTING, false);
		this.dataManager.register(ARM_POSE, ECQREntityArmPoses.NONE.toString());
		this.dataManager.register(TALKING, false);
		this.dataManager.register(TEXTURE_INDEX, getRNG().nextInt(getTextureCount()));
	}
	

	@Override
	protected boolean canDespawn() {
		return !Reference.CONFIG_HELPER_INSTANCE.areMobsFromCQSpawnersPersistent();
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
		return new PathNavigateGround(this, worldIn) {
			@Override
			public float getPathSearchRange() {
				return 128.0F;
			}
		};
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return attackEntityFrom(source, amount, false);
	}

	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		boolean result = super.attackEntityFrom(source, amount);
		if (result) {
			this.handleArmorBreaking();
		}
		return result;
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		this.updateReputationOnDeath(cause);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIHealingPotion(this));
		this.tasks.addTask(9, new EntityAIBackstab(this));
		this.tasks.addTask(10, new EntityAIAttack(this));
		this.tasks.addTask(14, new EntityAIFireFighter(this));
		this.tasks.addTask(15, new EntityAIMoveToLeader(this));
		this.tasks.addTask(16, new EntityAISearchMount(this));
		this.tasks.addTask(16, new EntityAITameAndLeashPet(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));
		this.tasks.addTask(22, new EntityAITorchIgniter(this));
		this.tasks.addTask(21, new EntityAIIdleSit(this));
		//this.tasks.addTask(21, new EntityAILookIdle(this));

		this.targetTasks.addTask(0, new EntityAICQRNearestAttackTarget(this));
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setHealingPotions(3);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BadgeSlot, new ItemStack(ModItems.BADGE));
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		float initSizeVar = -0.125F + (this.rand.nextFloat() *0.25F);
		this.dataManager.set(SIZE_VAR, initSizeVar);
		//Adapt size of hitbox
		this.setSize(0.6F * (1F + initSizeVar*0.8F), 1.8F *(1F + initSizeVar));
		//System.out.println("Size Var: " + sizeVariation);
		return ientitylivingdata;
	}

	@Override
	protected abstract void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty);

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if (this.homePosition != null) {
			compound.setTag("home", NBTUtil.createPosTag(this.homePosition));
		}

		if (this.leaderUUID != null) {
			compound.setTag("leader", NBTUtil.createUUIDTag(this.leaderUUID));
		}
		compound.setInteger("textureIndex", this.dataManager.get(TEXTURE_INDEX));
		compound.setByte("usedHealingPotions", usedPotions);
		compound.setFloat("sizeVariation", this.dataManager.get(SIZE_VAR));
		compound.setBoolean("isSitting", this.dataManager.get(IS_SITTING));
		compound.setBoolean("holdingPotion", this.holdingPotion);
		compound.setDouble("healthScale", healthScale);
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
		if(compound.hasKey("healthScale")) {
			healthScale = compound.getDouble("healthScale");
		}

		this.dataManager.set(TEXTURE_INDEX, compound.getInteger("textureIndex"));
		this.usedPotions = compound.getByte("usedHealingPotions");
		this.dataManager.set(SIZE_VAR, compound.getFloat("sizeVariation"));
		this.dataManager.set(IS_SITTING, compound.getBoolean("isSitting"));
		this.holdingPotion = compound.getBoolean("holdingPotion");
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
				
				player.openGui(CQRMain.INSTANCE, Reference.CQR_ENTITY_GUI_ID, world, this.getEntityId(), 0, 0);
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
			LootContext.Builder lootContextBuilder = new LootContext.Builder((WorldServer) this.world)
					.withLootedEntity(this).withDamageSource(source);
			if (wasRecentlyHit && this.attackingPlayer != null) {
				lootContextBuilder = lootContextBuilder.withPlayer(this.attackingPlayer)
						.withLuck(this.attackingPlayer.getLuck());
			}

			for (ItemStack itemstack : lootTable.generateLootForPools(this.rand, lootContextBuilder.build())) {
				this.entityDropItem(itemstack, 0.0F);
			}
		}

		ItemStack badge = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.BadgeSlot);
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
		super.onUpdate();
		if (!this.world.isRemote && !this.isNonBoss() && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			SpawnerFactory.placeSpawner(new Entity[] {this}, false, null, world, this.getPosition());
			this.setDead();
		}

		ItemStack stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.PotionSlot);
		if (!this.world.isRemote && stack != this.prevPotion) {
			CQRMain.NETWORK.sendToAll(new ItemStackSyncPacket(this.getEntityId(), EntityEquipmentExtraSlot.PotionSlot.getIndex(), stack));
		}
		this.prevPotion = stack;
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public void onLivingUpdate() {
		this.updateArmSwingProgress();
		/*if(!world.isRemote) {
			if(sittingState != dataManager.get(IS_SITTING)) {
				sittingState = dataManager.get(IS_SITTING);
				if(sittingState) {
					rescaleHitbox(1, 0.8);
				} else {
					rescaleHitbox(1, height /0.8);
				}
			}
		}*/
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
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
					((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F,
						(double) MathHelper.sin(this.rotationYaw * 0.017453292F),
						(double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
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
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack()
						: ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty()
						&& itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this)
						&& itemstack1.getItem().isShield(itemstack1, entityplayer)) {
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

	// Chocolate Quest Repoured
	public EntityLivingBase getLeader() {
		if (this.hasLeader()) {
			for (EntityLivingBase entity : this.world.getEntities(EntityLivingBase.class, null)) {
				if (this.leaderUUID.equals(entity.getPersistentID())) {
					return entity;
				}
			}
		}
		return null;
	}

	public void setLeader(EntityLivingBase leader) {
		this.leaderUUID = leader.getPersistentID();
	}

	public boolean hasLeader() {
		return this.leaderUUID != null && this.getLeader().isEntityAlive();
	}

	public BlockPos getHomePosition() {
		return this.homePosition;
	}

	public void setHomePosition(BlockPos homePosition) {
		this.homePosition = homePosition;
	}

	public boolean hasHomePosition() {
		return this.getHomePosition() != null;
	}

	public abstract float getBaseHealth();

	public void setBaseHealthForPosition(double x, double z, float health) {
		BlockPos spawn = this.world.getSpawnPoint();
		x -= (double) spawn.getX();
		z -= (double) spawn.getZ();
		float distance = (float) Math.sqrt(x * x + z * z);

		health *= 1.0F + 0.1F * distance / (float) Reference.CONFIG_HELPER_INSTANCE.getHealthDistanceDivisor();

		if (this.world.getWorldInfo().isHardcoreModeEnabled()) {
			health *= 2.0F;
		} else {
			EnumDifficulty difficulty = this.world.getDifficulty();

			if (difficulty == EnumDifficulty.EASY) {
				health *= 0.5F;
			} else if (difficulty == EnumDifficulty.HARD) {
				health *= 1.5F;
			}
		}
		
		health *= healthScale;

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
		this.setHealth(health);
	}

	public void handleArmorBreaking() {
		if (!this.world.isRemote) {
			/** Example
			 * A  U   Triggered?
			 * 3  0   N
			 * 2  1   Y
			 * 1  2   Y
			 * 0  3   Y
			 * 
			 */
			if(this.usedPotions +1 < getHealingPotions()) {
				return;
			}
			
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
		ItemStack stack;
		if (this.holdingPotion) {
			stack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		} else {
			stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.PotionSlot);
		}
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
			this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.PotionSlot, stack);
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
		ItemStack stack2 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.PotionSlot);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack2);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.PotionSlot, stack1);
		this.holdingPotion = !this.holdingPotion;
	}

	public boolean isHoldingPotion() {
		return this.holdingPotion;
	}

	public abstract EFaction getFaction();

	public boolean hasFaction() {
		return this.getFaction() != null;
	}

	public void updateReputationOnDeath(DamageSource cause) {
		if (cause.getTrueSource() instanceof EntityPlayer && this.hasFaction()) {
			EntityPlayer player = (EntityPlayer) cause.getTrueSource();
			int range = Reference.CONFIG_HELPER_INSTANCE.getFactionRepuChangeRadius();
			double x1 = player.posX - range;
			double y1 = player.posY - range;
			double z1 = player.posZ - range;
			double x2 = player.posX + range;
			double y2 = player.posY + range;
			double z2 = player.posZ + range;
			AxisAlignedBB aabb = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);

			for (AbstractEntityCQR cqrentity : this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb)) {
				if (cqrentity.hasFaction() && (this.canEntityBeSeen(cqrentity) || player.canEntityBeSeen(cqrentity))) {
					if (this.getFaction().equals(cqrentity.getFaction())) {
						// DONE decrement the players repu on this entity's faction
						this.getFaction().decrementReputation(player, EFaction.REPU_DECREMENT_ON_MEMBER_KILL);
					} else if (this.getFaction().isEnemy(cqrentity.getFaction())) {
						// DONE increment the players repu at CQREntity's faction
						cqrentity.getFaction().incrementReputation(player, EFaction.REPU_DECREMENT_ON_ENEMY_KILL);
					} else if (this.getFaction().isAlly(cqrentity.getFaction())) {
						// DONE decrement the players repu on CQREntity's faction
						cqrentity.getFaction().decrementReputation(player, EFaction.REPU_DECREMENT_ON_ALLY_KILL);
					}
				}
			}
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon() {
		this.setHomePosition(this.getPosition());
		this.setBaseHealthForPosition(this.posX, this.posZ, this.getBaseHealth());
	}

	public boolean hasCape() {
		// TODO Auto-generated method stub
		return false;
	}

	public ResourceLocation getResourceLocationOfCape() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public float getSizeVariation() {
		//return this.sizeVariation;
		return this.dataManager.get(SIZE_VAR);
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
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeFloat(this.dataManager.get(SIZE_VAR));
		
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.dataManager.set(SIZE_VAR, additionalData.readFloat());
	}
	
	public void setArmPose(ECQREntityArmPoses pose) {
		this.dataManager.set(ARM_POSE, pose.toString());
	}
	public ECQREntityArmPoses getArmPose() {
		return ECQREntityArmPoses.valueOf(this.dataManager.get(ARM_POSE));
	}
	
	public boolean isLeader() {
		//TODO: Implement team building
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public ESpeechBubble getCurrentSpeechBubble() {
		return ESpeechBubble.values()[this.currentSpeechBubbleID];
	}
	
	@SideOnly(Side.CLIENT)
	public void chooseNewRandomSpeechBubble() {
		Random rdm2 = new Random();
		rdm2.setSeed(this.ticksExisted / 160 + getEntityId());
		this.currentSpeechBubbleID = rdm2.nextInt(ESpeechBubble.values().length);
	}
	
	@SideOnly(Side.CLIENT) 
	public int getTextureIndex() {
		return this.dataManager.get(TEXTURE_INDEX);
	}
	
	public abstract int getTextureCount();

	public double getAttackReach(EntityLivingBase target) {
		double d = this.width + target.width + 0.25D;
		return d;
	}

	public boolean inAttackReach(EntityLivingBase target) {
		return target != null && !target.isDead && this.getDistance(target) <= this.getAttackReach(target);
	}
	
	public abstract boolean canRide();

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
		if (!ItemUtil.compareRotations(this.rotationPitch, d1, 50.0D)) {
			return false;
		}
		return true;
	}
	
	public void rescaleHitbox(double multiplierX, double multiplierY) {
		double h = this.height * multiplierY;
		double w = this.width * multiplierX;
		resizeHitBox(w, h);
	}
	public void resizeHitBox(double hitboxX, double hitboxY) {
		if(isChild()) {
			hitboxX *= 0.5D;
			hitboxY *= 0.5D;
		}
		float oldW = width;
		float oldH = height;
		this.height = (float) hitboxY;
		this.width = (float) hitboxX;
		
		if(hitboxX < oldW) {
			//AxisAlignedBB aabb = getEntityBoundingBox();
			if(!world.isRemote) {
				posY += (oldH - height) /2F;
			}
		}
		
		double wHalf = hitboxX /2D;
		setEntityBoundingBox(new AxisAlignedBB(posX - wHalf, posY, posZ - wHalf, posX + wHalf, posY + hitboxY, posZ + wHalf));
	}
	
	@SideOnly(Side.SERVER)
	public void setHealthScale(double hs) {
		this.healthScale = hs;
	}

}
