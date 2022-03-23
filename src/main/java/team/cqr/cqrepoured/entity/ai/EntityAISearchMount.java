package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class EntityAISearchMount extends AbstractCQREntityAI<AbstractEntityCQR> {

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

	protected boolean belongsToPlayerEntity(@Nonnull UUID uuid) {
		/*
		 * if (this.world instanceof WorldServer) {
		 * WorldServer server = (WorldServer) this.world;
		 * Entity byUUID = server.getEntityFromUuid(uuid);
		 * return byUUID != null && byUUID instanceof EntityPlayer;
		 * }
		 */

		return this.world.getPlayerByUUID(uuid) != null;
	}

	protected boolean isMountOwnedByPlayer(MobEntity mount) {

		if (mount instanceof AbstractHorseEntity) {
			AbstractHorseEntity horse = (AbstractHorseEntity) mount;
			if (horse.getOwnerUUID() != null) {
				return this.belongsToPlayerEntity(horse.getOwnerUUID());
			}
		}

		if (mount instanceof IEntityOwnable) {
			IEntityOwnable ownable = (IEntityOwnable) mount;
			if (ownable.getOwner() != null) {
				return ownable.getOwner() instanceof PlayerEntity;
			}
			if (ownable.getOwnerId() != null) {
				return this.belongsToPlayerEntity(ownable.getOwnerId());
			}
		}
		return false;
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
			Vector3d vec1 = this.entity.position().add(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			Vector3d vec2 = this.entity.position().subtract(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
			List<MobEntity> possibleMounts = this.world.getEntitiesOfClass(MobEntity.class, aabb, input -> TargetUtil.PREDICATE_MOUNTS.apply(input) && !this.isMountOwnedByPlayer(input) && this.entity.getSensing().canSee(input));
			if (!possibleMounts.isEmpty()) {
				this.entityToMount = TargetUtil.getNearestEntity(this.entity, possibleMounts);
				return true;
			}
		}
		return false;
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
				horse.equipSaddle(SoundCategory.AMBIENT);
				// Should that stay? -> Arlo says yes.
				horse.setSlot(400, new ItemStack(Items.SADDLE));
				horse.setSlot(401, new ItemStack(Items.IRON_HORSE_ARMOR));
			}
			this.entity.getNavigation().stop();
			this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);
		}
	}

	@Override
	public void stop() {
		this.entityToMount = null;
		this.entity.getNavigation().stop();
	}

}
