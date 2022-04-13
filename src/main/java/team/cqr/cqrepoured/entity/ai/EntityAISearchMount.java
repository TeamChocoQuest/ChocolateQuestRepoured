package team.cqr.cqrepoured.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAISearchMount extends AbstractCQREntityAI<AbstractEntityCQR> {

	public static final String TAG_TAMED_BY_CQR_MOB = "tamed_by_cqr_mob";
	protected static final double MOUNT_SEARCH_RADIUS = 16;
	protected static final double DISTANCE_TO_MOUNT = 2.0D;
	protected static final boolean FORCE_MOUNTING = true;
	protected static final double WALK_SPEED_TO_MOUNT = 1.0D;

	protected EntityLiving entityToMount = null;

	public EntityAISearchMount(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canMountEntity()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.random.nextInt(10) == 0) {
			boolean hasSaddle = hasSaddle();
			Vec3d vec1 = this.entity.getPositionVector().add(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			Vec3d vec2 = this.entity.getPositionVector().subtract(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
			List<EntityLiving> possibleMounts = this.world.getEntitiesWithinAABB(EntityLiving.class, aabb, input -> {
				if (!TargetUtil.PREDICATE_MOUNTS.apply(input)) {
					return false;
				}
				if (!this.entity.getEntitySenses().canSee(input)) {
					return false;
				}
				return hasSaddle || input.getTags().contains(TAG_TAMED_BY_CQR_MOB);
			});
			if (!possibleMounts.isEmpty()) {
				this.entityToMount = TargetUtil.getNearestEntity(this.entity, possibleMounts);
				return true;
			}
		}
		return false;
	}

	private boolean hasSaddle() {
		return isSaddle(entity.getHeldItemMainhand()) || isSaddle(entity.getHeldItemOffhand());
	}

	private boolean isSaddle(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == Items.SADDLE;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.canMountEntity()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.entityToMount == null) {
			return false;
		}
		if (hasSaddle() || entityToMount.getTags().contains(TAG_TAMED_BY_CQR_MOB)) {
			return false;
		}
		if (!this.entityToMount.isEntityAlive()) {
			return false;
		}
		if (this.entityToMount.isBeingRidden()) {
			return false;
		}
		if (this.entity.getDistance(this.entityToMount) > 16.0D) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistance(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToMount, WALK_SPEED_TO_MOUNT);
		}
	}

	@Override
	public void updateTask() {
		if (this.entity.getDistance(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToMount, WALK_SPEED_TO_MOUNT);
		} else {
			if (this.entityToMount instanceof AbstractHorse) {
				AbstractHorse horse = (AbstractHorse) this.entityToMount;
				horse.setOwnerUniqueId(this.entity.getPersistentID());

				this.world.setEntityState(horse, (byte) 7);
				horse.setHorseTamed(true);
				horse.setHorseSaddled(true);
				// Should that stay? -> Arlo says yes.
				horse.replaceItemInInventory(400, new ItemStack(Items.SADDLE));
				horse.replaceItemInInventory(401, new ItemStack(Items.IRON_HORSE_ARMOR));
			}
			this.entity.getNavigator().clearPath();
			this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);

			if (!entityToMount.getTags().contains(TAG_TAMED_BY_CQR_MOB)) {
				entityToMount.addTag(TAG_TAMED_BY_CQR_MOB);

				if (isSaddle(entity.getHeldItemMainhand())) {
					entity.getHeldItemMainhand().shrink(1);
				} else if (isSaddle(entity.getHeldItemOffhand())) {
					entity.getHeldItemOffhand().shrink(1);
				}
			}
		}
	}

	@Override
	public void resetTask() {
		this.entityToMount = null;
		this.entity.getNavigator().clearPath();
	}

}
