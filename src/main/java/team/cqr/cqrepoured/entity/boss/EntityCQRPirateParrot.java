package team.cqr.cqrepoured.entity.boss;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.FollowOwnerFlyingGoal;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotLandOnCaptainsShoulder;
import team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot.BossAIPirateParrotThrowPotions;
import team.cqr.cqrepoured.entity.ai.target.EntityAIPetNearestAttackTarget;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityCQRPirateParrot extends ParrotEntity {

	public EntityCQRPirateParrot(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new SitGoal(this);
		// this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new BossAIPirateParrotThrowPotions(this));
		// this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(2, new FollowOwnerFlyingGoal(this, 1.0D, 5.0F, 1.0F));
		this.tasks.addTask(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.tasks.addTask(5, new BossAIPirateParrotLandOnCaptainsShoulder(this));
		this.tasks.addTask(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));

		this.targetTasks.addTask(0, new EntityAIPetNearestAttackTarget<>(this, MobEntity.class, 100, true, false));
	}

	@Override
	public void addPotionEffect(EffectInstance effect) {
		if (effect.getPotion().isBadEffect()) {
			return;
		}
		super.addPotionEffect(effect);
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.die(cause);
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
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.getOwnerInRange(uuid);
		} catch (IllegalArgumentException var2) {
			return null;
		}
	}

	private LivingEntity getOwnerInRange(UUID uuid) {
		List<Entity> ents = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.posX - 20, this.posY - 20, this.posZ - 20, this.posX + 20, this.posY + 20, this.posZ + 20), input -> input instanceof LivingEntity && input.getPersistentID().equals(uuid));
		return ents.isEmpty() ? null : (LivingEntity) ents.get(0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(Attributes.MAX_HEALTH).setBaseValue(40);
	}

	public boolean setCQREntityOnShoulder(AbstractEntityCQR p_191994_1_) {
		CompoundNBT nbttagcompound = new CompoundNBT();
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
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.FIRE_CHARGE, 1));
	}

	@Override
	public boolean canSitOnShoulder() {
		return true;
	}

	@Override
	public boolean setEntityOnShoulder(PlayerEntity p_191994_1_) {
		return super.setEntityOnShoulder(p_191994_1_);
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		if (this.isTamed() && player != this.getOwner()) {
			return true;
		}
		return super.processInteract(player, hand);
	}

}
