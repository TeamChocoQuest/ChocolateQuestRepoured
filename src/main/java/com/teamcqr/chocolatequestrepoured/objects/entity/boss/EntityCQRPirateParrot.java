package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotThrowPotions;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.target.EntityAIPetNearestAttackTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRPirateParrot extends EntityParrot {

	public EntityCQRPirateParrot(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
		// this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new BossAIPirateParrotThrowPotions(this));
		// this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
		this.tasks.addTask(4, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
		this.tasks.addTask(5, new BossAIPirateParrotLandOnCaptainsShoulder(this));
		this.tasks.addTask(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));

		this.targetTasks.addTask(0, new EntityAIPetNearestAttackTarget<>(this, EntityLiving.class, 100, true, false));
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		if (effect.getPotion().isBadEffect()) {
			return;
		}
		super.addPotionEffect(effect);
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!this.world.isRemote) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2, true);
		}
	}

	@Override
	public void setSitting(boolean sitting) {
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Nullable
	@Override
	public EntityLivingBase getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.getOwnerInRange(uuid);
		} catch (IllegalArgumentException var2) {
			return null;
		}
	}

	private EntityLivingBase getOwnerInRange(UUID uuid) {
		List<Entity> ents = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.posX - 20, this.posY - 20, this.posZ - 20, this.posX + 20, this.posY + 20, this.posZ + 20), new Predicate<Entity>() {

			@Override
			public boolean apply(Entity input) {
				return input instanceof EntityLivingBase && input.getPersistentID().equals(uuid);
			}
		});
		return ents.isEmpty() ? null : (EntityLivingBase) ents.get(0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
	}

	public boolean setCQREntityOnShoulder(AbstractEntityCQR p_191994_1_) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("id", this.getEntityString());
		this.writeToNBT(nbttagcompound);

		if (p_191994_1_.addShoulderEntity(nbttagcompound)) {
			this.world.removeEntity(this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.FIRE_CHARGE, 1));
	}

	@Override
	public boolean canSitOnShoulder() {
		return true;
	}

	@Override
	public boolean setEntityOnShoulder(EntityPlayer p_191994_1_) {
		return super.setEntityOnShoulder(p_191994_1_);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.isTamed() && player != this.getOwner()) {
			return true;
		}
		return super.processInteract(player, hand);
	}

}
