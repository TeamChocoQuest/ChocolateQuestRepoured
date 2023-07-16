package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import java.util.EnumSet;
import java.util.List;

public class EntityAISearchMount extends AbstractCQREntityAI<AbstractEntityCQR> {

	public static final String TAG_TAMED_BY_CQR_MOB = "tamed_by_cqr_mob";
	protected static final double MOUNT_SEARCH_RADIUS = 16;
	protected static final double DISTANCE_TO_MOUNT = 2.0D;
	protected static final boolean FORCE_MOUNTING = true;
	protected static final double WALK_SPEED_TO_MOUNT = 1.0D;

	protected MobEntity entityToMount = null;

	public EntityAISearchMount(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.entity.canMountEntity()) {
			return false;
		}
		if (this.entity.getVehicle() != null) {
			return false;
		}
		if (this.random.nextInt(10) == 0) {
			boolean hasSaddle = hasSaddle();
			Vec3 vec1 = this.entity.position().add(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			Vec3 vec2 = this.entity.position().subtract(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			AABB aabb = new AABB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
			List<MobEntity> possibleMounts = this.world.getEntitiesOfClass(MobEntity.class, aabb, input -> {
				if (!TargetUtil.PREDICATE_MOUNTS.apply(input)) {
					return false;
				}
				if (!this.entity.getSensing().hasLineOfSight(input)) {
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
		return isSaddle(entity.getMainHandItem()) || isSaddle(entity.getOffhandItem());
	}

	private boolean isSaddle(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == Items.SADDLE;
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.entity.canMountEntity()) {
			return false;
		}
		if (this.entity.getVehicle() != null) {
			return false;
		}
		if (this.entityToMount == null) {
			return false;
		}
		if (!hasSaddle() && !entityToMount.getTags().contains(TAG_TAMED_BY_CQR_MOB)) {
			return false;
		}
		if (!this.entityToMount.isAlive()) {
			return false;
		}
		if (this.entityToMount.isVehicle()) {
			return false;
		}
		if (this.entity.distanceTo(this.entityToMount) > 16.0D) {
			return false;
		}
		return this.entity.isPathFinding();
	}

	@Override
	public void start() {
		if (this.entity.distanceTo(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigation().moveTo(this.entityToMount, WALK_SPEED_TO_MOUNT);
		}
	}

	@Override
	public void tick() {
		if (this.entity.distanceTo(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigation().moveTo(this.entityToMount, WALK_SPEED_TO_MOUNT);
		} else {
			if (this.entityToMount instanceof AbstractHorseEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity) this.entityToMount;
				horse.setOwnerUUID(this.entity.getUUID());

				//TODO: Replace
				//this.world.setEntityState(horse, (byte) 7);
				horse.setTamed(true);
				horse.equipSaddle(SoundSource.AMBIENT);
				// Should that stay? -> Arlo says yes.
				horse.setSlot(400, new ItemStack(Items.SADDLE));
				horse.setSlot(401, new ItemStack(Items.IRON_HORSE_ARMOR));
			}
			this.entity.getNavigation().stop();
			this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);

			if (!entityToMount.getTags().contains(TAG_TAMED_BY_CQR_MOB)) {
				entityToMount.addTag(TAG_TAMED_BY_CQR_MOB);

				if (isSaddle(entity.getMainHandItem())) {
					entity.getMainHandItem().shrink(1);
				} else if (isSaddle(entity.getOffhandItem())) {
					entity.getOffhandItem().shrink(1);
				}
			}
		}
	}

	@Override
	public void stop() {
		this.entityToMount = null;
		this.entity.getNavigation().stop();
	}

}
