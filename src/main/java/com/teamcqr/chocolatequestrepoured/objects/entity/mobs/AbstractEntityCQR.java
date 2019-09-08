package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.UUID;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.CapabilityExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.ExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.capability.extraitemhandler.IExtraItemHandler;
import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityEquipmentExtraSlot;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHealingPotion;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class AbstractEntityCQR extends EntityMob {

	protected BlockPos homePosition;
	protected UUID leaderUUID;
	protected boolean holdingPotion;

	public AbstractEntityCQR(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.8F);
		this.setHealingPotions(3);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.BadgeSlot, new ItemStack(ModItems.BADGE));
	}

	@Override
	protected boolean canDespawn() {
		return !Reference.CONFIG_HELPER_INSTANCE.areMobsFromCQSpawnersPersistent();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
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
		this.tasks.addTask(10, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(15, new EntityAIMoveToLeader(this));
		this.tasks.addTask(20, new EntityAIMoveToHome(this));

		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return ientitylivingdata;
	}

	@Override
	protected abstract void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty);

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		boolean hasHome = this.homePosition != null;
		compound.setBoolean("hasHome", hasHome);
		if (hasHome) {
			compound.setTag("home", NBTUtil.createPosTag(this.homePosition));
		}

		boolean hasLeader = this.leaderUUID != null;
		compound.setBoolean("hasLeader", hasLeader);
		if (hasLeader) {
			compound.setTag("leader", NBTUtil.createUUIDTag(this.leaderUUID));
		}

		compound.setBoolean("holdingPotion", this.holdingPotion);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.getBoolean("hasHome")) {
			this.homePosition = NBTUtil.getPosFromTag(compound.getCompoundTag("home"));
		}

		if (compound.getBoolean("hasLeader")) {
			this.leaderUUID = NBTUtil.getUUIDFromTag(compound.getCompoundTag("leader"));
		}

		this.holdingPotion = compound.getBoolean("holdingPotion");
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (player.isCreative() && !player.isSneaking()) {
			if (!this.world.isRemote) {
				player.openGui(CQRMain.INSTANCE, Reference.CQR_ENTITY_GUI_ID, world, this.getEntityId(), 0, 0);
			}
			return true;
		}
		return false;
	}

	public EntityLivingBase getLeader() {
		if (this.hasLeader()) {
			for (Entity entity : this.world.loadedEntityList) {
				if (entity instanceof EntityLivingBase && this.leaderUUID.equals(entity.getPersistentID())) {
					return (EntityLivingBase) entity;
				}
			}
		}
		return null;
	}

	public void setLeader(EntityLivingBase leader) {
		this.leaderUUID = leader.getPersistentID();
	}

	public boolean hasLeader() {
		return this.leaderUUID != null && !this.getLeader().isDead;
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

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
		this.setHealth(health);
	}

	public void handleArmorBreaking() {
		if (!this.world.isRemote) {
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
		if (this.holdingPotion) {
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.POTION_HEALING, amount));
		} else {
			this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.PotionSlot,
					new ItemStack(ModItems.POTION_HEALING, amount));
		}
	}

	public void removeHealingPotion() {
		ItemStack stack;
		if (this.holdingPotion) {
			stack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		} else {
			stack = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.PotionSlot);
		}
		if (stack.getItem() instanceof ItemPotionHealing) {
			stack.shrink(1);
		}
	}

	public ItemStack getItemStackFromExtraSlot(EntityEquipmentExtraSlot slot) {
		IExtraItemHandler capability = this.getCapability(CapabilityExtraItemHandler.EXTRA_ITEM_HANDLER, null);
		return capability.getStackInSlot(slot.getIndex());
	}

	public void setItemStackToExtraSlot(EntityEquipmentExtraSlot slot, ItemStack stack) {
		ExtraItemHandler capability = (ExtraItemHandler) this
				.getCapability(CapabilityExtraItemHandler.EXTRA_ITEM_HANDLER, null);
		capability.setStackInSlot(slot.getIndex(), stack);
	}

	public void swapItemStacks() {
		ItemStack stack1 = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		ItemStack stack2 = this.getItemStackFromExtraSlot(EntityEquipmentExtraSlot.PotionSlot);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack2);
		this.setItemStackToExtraSlot(EntityEquipmentExtraSlot.PotionSlot, stack1);
		this.holdingPotion = !this.holdingPotion;
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
				if (this.canEntityBeSeen(cqrentity) || player.canEntityBeSeen(cqrentity)) {
					if(this.getFaction().equals(cqrentity.getFaction())) {
						// TODO decrement the players repu on this entity's faction
					} else if (this.getFaction().isEnemy(cqrentity.getFaction())) {
						// TODO increment the players repu at CQREntity's faction
					} else if (this.getFaction().isAlly(cqrentity.getFaction())) {
						// TODO decrement the players repu on CQREntity's faction
					}
				}
			}
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon() {
		this.setHomePosition(this.getPosition());
		this.setBaseHealthForPosition(this.posX, this.posZ, this.getBaseHealth());
	}

	@Override
	protected ResourceLocation getLootTable() {
		return super.getLootTable();
	}

	public boolean isHoldingPotion() {
		return this.holdingPotion;
	}

}
