package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToLeader;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractEntityCQR extends EntityMob {

	protected int healingPotions;
	protected BlockPos homePosition;
	protected UUID leaderUUID;

	public AbstractEntityCQR(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.8F);
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
		this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(10, new EntityAIMoveToLeader(this));
		this.tasks.addTask(15, new EntityAIMoveToHome(this));
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

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
		this.setHealth(health);
	}

	public void handleArmorBreaking() {
		boolean armorBroke = false;
		float hpPrcntg = this.getHealth() / this.getMaxHealth();

		if (hpPrcntg <= 0.8F) {
			ItemStack feet = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			if (!feet.isEmpty()) {
				feet = ItemStack.EMPTY;
				armorBroke = true;
			}

			if (hpPrcntg <= 0.6F) {
				ItemStack head = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				if (!head.isEmpty()) {
					head = ItemStack.EMPTY;
					armorBroke = true;
				}

				if (hpPrcntg <= 0.4F) {
					ItemStack legs = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
					if (!legs.isEmpty()) {
						legs = ItemStack.EMPTY;
						armorBroke = true;
					}

					if (hpPrcntg <= 0.2F) {
						ItemStack chest = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
						if (!chest.isEmpty()) {
							chest = ItemStack.EMPTY;
							armorBroke = true;
						}
					}
				}
			}
		}

		if (armorBroke && !this.world.isRemote) {
			this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.75F, 0.8F);
		}
	}

	public int getRemainingPotions() {
		return this.healingPotions;
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
					if (this.getFaction().isEnemy(cqrentity.getFaction())) {

					} else if (this.getFaction().isAlly(cqrentity.getFaction())) {

					}
				}
			}
		}
	}

	public void onSpawnFromCQRSpawnerInDungeon() {
		this.setHomePosition(this.getPosition());
		this.setBaseHealthForPosition(this.posX, this.posZ, this.getBaseHealth());
	}

}
