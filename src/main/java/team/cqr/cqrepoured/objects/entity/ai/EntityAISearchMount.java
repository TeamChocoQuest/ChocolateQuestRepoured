package team.cqr.cqrepoured.objects.entity.ai;

import java.util.List;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAISearchMount extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected static final double MOUNT_SEARCH_RADIUS = 16;
	protected static final double DISTANCE_TO_MOUNT = 2.0D;
	protected static final boolean FORCE_MOUNTING = true;
	protected static final double WALK_SPEED_TO_MOUNT = 1.0D;

	protected EntityAnimal entityToMount = null;

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
			Vec3d vec1 = this.entity.getPositionVector().add(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			Vec3d vec2 = this.entity.getPositionVector().subtract(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
			List<EntityAnimal> possibleMounts = this.world.getEntitiesWithinAABB(EntityAnimal.class, aabb, input -> TargetUtil.PREDICATE_MOUNTS.apply(input) && this.entity.getEntitySenses().canSee(input));
			if (!possibleMounts.isEmpty()) {
				this.entityToMount = TargetUtil.getNearestEntity(this.entity, possibleMounts);
				return true;
			}
		}
		return false;
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
			} else if (this.entityToMount instanceof EntityPig) {
				((EntityPig) this.entityToMount).setSaddled(true);
			}
			this.entity.getNavigator().clearPath();
			this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);
		}
	}

	@Override
	public void resetTask() {
		this.entityToMount = null;
		this.entity.getNavigator().clearPath();
	}

}
