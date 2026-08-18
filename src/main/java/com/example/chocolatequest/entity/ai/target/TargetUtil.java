package com.example.chocolatequest.entity.ai.target;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.faction.EDefaultFaction;
import com.example.chocolatequest.faction.IFactionRelated;

public class TargetUtil {

	public static final Predicate<LivingEntity> PREDICATE_ATTACK_TARGET = input -> {
		if (input == null) {
			return false;
		}
		return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(input);
	};

	public static final Predicate<LivingEntity> PREDICATE_CAN_BE_ELECTROCUTED = input -> {
		if (input == null) return false;
		return net.minecraft.world.entity.EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(input) && input.isAlive();
	};

	public static final Predicate<LivingEntity> PREDICATE_IS_ELECTROCUTED = input -> false;

	public static final Predicate<Mob> PREDICATE_MOUNTS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(input)) {
			return false;
		}
		if (input.isVehicle()) {
			return false;
		}
		/*
		 * if (input instanceof AbstractHorse && ((AbstractHorse) input).isTame()) {
		 * return false;
		 * }
		 */
		return input instanceof AbstractHorse;
	};

	public static final Predicate<TamableAnimal> PREDICATE_PETS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(input)) {
			return false;
		}
		if (input.getOwnerUUID() != null) {
			return false;
		}
		return input instanceof Cat || input instanceof Wolf || input instanceof TamableAnimal;
	};

	public static final Predicate<Entity> PREDICATE_LIVING = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelector.ENTITY_STILL_ALIVE.test(input)) {
			return false;
		}
		return input instanceof LivingEntity;
	};

	
	
	public static final <T extends Entity> T getNearestEntity(Mob entity, List<T> list) {
		T nearestEntity = null;
		double min = Double.MAX_VALUE;
		for (T otherEntity : list) {
			double distance = entity.distanceToSqr(otherEntity);
			if (distance < min) {
				nearestEntity = otherEntity;
				min = distance;
			}
		}
		return nearestEntity;
	}

	@Nullable
	public static final Vec3 getPositionNearTarget(Level world, Mob entity, BlockPos target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, new Vec3(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D), minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3 getPositionNearTarget(Level world, Mob entity, Entity target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, target.position(), minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3 getPositionNearTarget(Level world, Mob entity, Vec3 target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, target, target, minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3 getPositionNearTarget(Level world, Mob entity, Vec3 target, Vec3 vec, double minDist, double dxz, double dy) {
		MutableBlockPos mutablePos = new MutableBlockPos();
		int tries = 200;
		for (int i = 0; i < tries; i++) {
			double x = target.x + world.getRandom().nextDouble() * dxz * 2.0D - dxz;
			double y = target.y + 1.0D + world.getRandom().nextDouble() * dy * 2.0D - dy;
			double z = target.z + world.getRandom().nextDouble() * dxz * 2.0D - dxz;
			if (i < tries * 3 / 5 && (x - vec.x) * (x - vec.x) + (z - vec.z) * (z - vec.z) < minDist * minDist) {
				continue;
			}
			boolean flag = false;
			mutablePos.set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
			for (int k = 0; k < 4; k++) {
				BlockState state = world.getBlockState(mutablePos);
				if (state.blocksMotion()) {
					AABB aabb = state.getShape(world, mutablePos).bounds();
					if (y >= mutablePos.getY() + aabb.maxY) {
						y = mutablePos.getY() + aabb.maxY;
						flag = true;
						break;
					}
				}
				mutablePos.setY(mutablePos.getY() - 1);
			}
			if (!flag) {
				continue;
			}
			if (!world.noCollision(entity.getBoundingBox().move(x - entity.getX(), y - entity.getY(), z - entity.getZ()))) {
				continue;
			}
			if (i < tries * 3 / 5) {
				double oldX = entity.getX();
				double oldY = entity.getY();
				double oldZ = entity.getZ();
				entity.setPos(x, y, z);
				entity.setOnGround(true);
				Path path = entity.getNavigation().createPath(vec.x, vec.y, vec.z, 1 /* accuracy */);
				int l = path != null ? path.getNodeCount() : 100;
				entity.setPos(oldX, oldY, oldZ);
				if (l > dxz * 2) {
					continue;
				}
			}
			return new Vec3(x, y, z);
		}
		return null;
	}

	public static class Sorter implements Comparator<Entity> {

		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity entity1, Entity entity2) {
			double d1 = this.entity.distanceToSqr(entity1);
			double d2 = this.entity.distanceToSqr(entity2);

			if (d1 < d2) {
				return -1;
			} else if (d1 > d2) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public static boolean areInSameParty(Entity ent1, Entity ent2) {
		if (ent1 instanceof AbstractEntityCQR) {
			return (ent2 instanceof AbstractEntityCQR && ((AbstractEntityCQR) ent2).getLeader() == ((AbstractEntityCQR) ent1).getLeader());
		}
		return false;
	}

	public static boolean isAllyCheckingLeaders(LivingEntity entity, LivingEntity target) {
		if (entity instanceof AbstractEntityCQR cqr
				&& cqr.isTavernAlly(target)) {
			return true;
		}
		LivingEntity leader = getLeaderOrOwnerRecursive(entity);
		if (leader instanceof Player) {
			entity = leader;
		}
		LivingEntity targetLeader = getLeaderOrOwnerRecursive(target);
		if (targetLeader instanceof Player) {
			target = targetLeader;
		}

		if (entity == target) {
			return true;
		}

		if (entity instanceof Player) {
			if (target instanceof Player) {
				return false;
			} else {
				LivingEntity temp = entity;
				entity = target;
				target = temp;
			}
		}

		EDefaultFaction faction = entity instanceof IFactionRelated ? ((IFactionRelated) entity).getDefaultFaction() : null;
		if (faction != null && target instanceof IFactionRelated ? faction.isAlly(((IFactionRelated)target).getDefaultFaction()) : false) {
			return true;
		}

		return false;
	}

	public static boolean isEnemyCheckingLeaders(LivingEntity entity, LivingEntity target) {
		if (entity instanceof AbstractEntityCQR cqr
				&& cqr.isTavernAlly(target)) {
			return false;
		}
		LivingEntity leader = getLeaderOrOwnerRecursive(entity);
		if (leader instanceof Player) {
			entity = leader;
		}
		LivingEntity targetLeader = getLeaderOrOwnerRecursive(target);
		if (targetLeader instanceof Player) {
			target = targetLeader;
		}

		if (entity == target) {
			return false;
		}

		if (entity instanceof Player) {
			if (target instanceof Player) {
				return false;
			} else {
				LivingEntity temp = entity;
				entity = target;
				target = temp;
			}
		}

						EDefaultFaction faction = entity instanceof IFactionRelated ? ((IFactionRelated) entity).getDefaultFaction() : null;
		if(faction != null) {
			if (target instanceof Player) {
				return !faction.isAlly(EDefaultFaction.PLAYERS);
			} else {
				if (target instanceof IFactionRelated) {
					if (!faction.isEnemy(((IFactionRelated)target).getDefaultFaction())) {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return true;
	}

	public static LivingEntity getLeaderOrOwnerRecursive(LivingEntity entity) {
		int i = 10;
		while (i-- > 0) {
			if (entity instanceof AbstractEntityCQR && ((AbstractEntityCQR) entity).hasLeader()) {
				entity = ((AbstractEntityCQR) entity).getLeader();
				continue;
			}
			if (entity instanceof TamableAnimal && ((TamableAnimal) entity).getOwner() instanceof LivingEntity) {
				entity = (LivingEntity) ((TamableAnimal) entity).getOwner();
				continue;
			}
			break;
		}
		return entity;
	}

}

