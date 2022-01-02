package team.cqr.cqrepoured.entity.ai.target;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Difficulty;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;

public class EntityAIHurtByTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	private static final double MAX_PATH_END_TO_TARGET_DISTANCE_SQ = 2.0D;
	private static final int MAX_PATH_LENGTH = 20;
	private static final double MAX_PATH_COMPLEXITY = 4;
	protected LivingEntity attackTarget;
	protected int prevRevengeTimer;

	public EntityAIHurtByTarget(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.world.getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}
		if (this.entity.getRevengeTimer() == this.prevRevengeTimer) {
			return false;
		}
		LivingEntity revengeTarget = this.entity.getRevengeTarget();
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(revengeTarget)) {
			return false;
		}
		if (!revengeTarget.isEntityAlive()) {
			return false;
		}
		Faction faction = this.entity.getFaction();
		if (faction == null) {
			return false;
		}
		if (!isEnemyCheckingLeadersWhenAttacked(this.entity, revengeTarget)) {
			return false;
		}
		if (!this.entity.isInSightRange(revengeTarget)) {
			return false;
		}
		this.attackTarget = revengeTarget;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		this.prevRevengeTimer = this.entity.getRevengeTimer();
		this.trySetAttackTarget(this.entity);
		Faction faction = this.entity.getFaction();
		if (faction != null && faction.isEnemy(this.attackTarget) && !(this.entity.getLeader() instanceof PlayerEntity)) {
			this.callForHelp();
		}
	}

	protected void callForHelp() {
		double x = this.entity.posX;
		double y = this.entity.posY + this.entity.getEyeHeight();
		double z = this.entity.posZ;
		double r = CQRConfig.mobs.alertRadius;
		AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		List<AbstractEntityCQR> allies = this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb, this::isSuitableAlly);
		for (AbstractEntityCQR ally : allies) {
			this.trySetAttackTarget(ally);
		}
	}

	protected boolean isSuitableAlly(AbstractEntityCQR possibleAlly) {
		if (!EntityPredicates.IS_ALIVE.apply(possibleAlly)) {
			return false;
		}
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!isAllyCheckingLeadersWhenAttacked(this.entity, possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigator().getPathToEntityLiving(this.entity);
		if (path == null) {
			return false;
		}
		PathPoint end = path.getFinalPathPoint();
		if (end == null) {
			return false;
		}
		if (this.entity.getDistanceSq(end.x, end.y, end.z) > MAX_PATH_END_TO_TARGET_DISTANCE_SQ) {
			return false;
		}
		if (path.getCurrentPathLength() > MAX_PATH_LENGTH) {
			return false;
		}
		return getPathComplexity(path) <= MAX_PATH_COMPLEXITY;
	}

	protected boolean trySetAttackTarget(AbstractEntityCQR ally) {
		ItemStack stack = ally.getHeldItemMainhand();
		if (stack.getItem() instanceof ISupportWeapon) {
			return false;
		}
		if (stack.getItem() instanceof IFakeWeapon) {
			return false;
		}
		if (!isEnemyCheckingLeadersWhenAttacked(ally, this.attackTarget)) {
			return false;
		}
		LivingEntity oldAttackTarget = ally.getAttackTarget();
		if (oldAttackTarget != null && ally.getEntitySenses().canSee(oldAttackTarget) && ally.getDistanceSq(oldAttackTarget) < ally.getDistanceSq(this.attackTarget)) {
			return false;
		}
		ally.setAttackTarget(this.attackTarget);
		return true;
	}

	private static boolean isAllyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, LivingEntity possibleAlly) {
		LivingEntity leader = TargetUtil.getLeaderOrOwnerRecursive(entity);
		LivingEntity targetLeader = TargetUtil.getLeaderOrOwnerRecursive(possibleAlly);
		if (!(leader instanceof PlayerEntity) && targetLeader instanceof PlayerEntity) {
			return false;
		}
		return TargetUtil.isAllyCheckingLeaders(leader, targetLeader);
	}

	private static boolean isEnemyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, LivingEntity possibleEnemy) {
		LivingEntity leader = TargetUtil.getLeaderOrOwnerRecursive(entity);
		if (!(leader instanceof PlayerEntity)) {
			return !TargetUtil.isAllyCheckingLeaders(leader, possibleEnemy);
		}
		return TargetUtil.isEnemyCheckingLeaders(leader, possibleEnemy);
	}

	private static int getPathComplexity(Path path) {
		if (path.getCurrentPathLength() == 0) {
			return 0;
		}
		int pathComplexity = 0;
		Axis prevPrevPrevAxis = null;
		Axis prevPrevAxis = null;
		Axis prevAxis = null;
		int prevX = path.getPathPointFromIndex(0).x;
		int prevZ = path.getPathPointFromIndex(0).z;
		for (int i = 0; i < path.getCurrentPathLength(); i++) {
			PathPoint point = path.getPathPointFromIndex(i);
			int x = point.x;
			int z = point.z;
			Axis axis;
			if (x != prevX) {
				axis = Axis.X;
			} else if (z != prevZ) {
				axis = Axis.Z;
			} else {
				axis = Axis.Y;
			}

			if (prevAxis == axis && prevAxis != prevPrevAxis && prevPrevAxis == prevPrevPrevAxis) {
				pathComplexity++;
			}

			prevPrevPrevAxis = prevPrevAxis;
			prevPrevAxis = prevAxis;
			prevAxis = axis;
			prevX = x;
			prevZ = z;
		}
		return pathComplexity;
	}

}
