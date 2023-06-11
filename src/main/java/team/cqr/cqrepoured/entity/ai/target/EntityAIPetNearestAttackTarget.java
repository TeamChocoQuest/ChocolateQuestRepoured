package team.cqr.cqrepoured.entity.ai.target;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.faction.FactionRegistry;

public class EntityAIPetNearestAttackTarget<T extends MobEntity> extends TargetGoal {

	protected final Class<T> targetClass;
	private final int targetChance;
	/** Instance of EntityAINearestAttackableTargetSorter. */
	protected final EntityAIPetNearestAttackTarget.Sorter sorter;
	protected T targetEntity;

	public EntityAIPetNearestAttackTarget(CreatureEntity creature, Class<T> classTarget, boolean checkSight) {
		this(creature, classTarget, checkSight, false);
	}

	public EntityAIPetNearestAttackTarget(CreatureEntity creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
		this(creature, classTarget, 10, checkSight, onlyNearby);
	}

	public EntityAIPetNearestAttackTarget(CreatureEntity creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby) {
		super(creature, checkSight, onlyNearby);
		this.targetClass = classTarget;
		this.targetChance = chance;
		this.sorter = new EntityAIPetNearestAttackTarget.Sorter(creature);
		//this.setMutexBits(1);
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse() {
		Faction faction = FactionRegistry.instance(this.mob).getFactionOf(this.mob);
		if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
			return false;
		} else if (faction != null)/* if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class) */
		{
			List<T> list = this.mob.level.<T>getEntitiesOfClass(this.targetClass, this.getTargetableArea(this.getFollowDistance()), TargetUtil.createPredicateNonAlly(faction));

			if (list.isEmpty()) {
				return false;
			} else {
				Collections.sort(list, this.sorter);
				this.targetEntity = list.get(0);
				return true;
			}
		} else {
			return false;
		}
		/*
		 * else { this.targetEntity = (T)this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX,
		 * this.taskOwner.posY +
		 * (double)this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), new
		 * Function<EntityPlayer, Double>() {
		 * 
		 * @Nullable public Double apply(@Nullable EntityPlayer p_apply_1_) { ItemStack itemstack =
		 * p_apply_1_.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		 * 
		 * if (itemstack.getItem() == Items.SKULL) { int i = itemstack.getItemDamage(); boolean flag =
		 * EntityAIPetNearestAttackTarget.this.taskOwner instanceof
		 * EntitySkeleton && i == 0; boolean flag1 = EntityAIPetNearestAttackTarget.this.taskOwner
		 * instanceof EntityZombie && i == 2; boolean flag2 = EntityAIPetNearestAttackTarget.this.taskOwner instanceof
		 * EntityCreeper && i == 4;
		 * 
		 * if (flag || flag1 || flag2) { return 0.5D; } }
		 * 
		 * return 1.0D; } }, (Predicate<EntityPlayer>)this.targetEntitySelector); return this.targetEntity != null; }
		 */
	}

	protected AxisAlignedBB getTargetableArea(double targetDistance) {
		return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.mob.setTarget(this.targetEntity);
		super.start();
	}

	public static class Sorter implements Comparator<Entity> {
		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.entity.distanceToSqr(p_compare_1_);
			double d1 = this.entity.distanceToSqr(p_compare_2_);

			if (d0 < d1) {
				return -1;
			} else {
				return d0 > d1 ? 1 : 0;
			}
		}
	}

}
