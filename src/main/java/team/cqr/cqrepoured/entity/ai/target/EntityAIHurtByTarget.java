package team.cqr.cqrepoured.entity.ai.target;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.Difficulty;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.item.IFakeWeapon;
import team.cqr.cqrepoured.item.ISupportWeapon;

import java.util.List;

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
		if (this.entity.getLastHurtByMobTimestamp() == this.prevRevengeTimer) {
			return false;
		}
		LivingEntity revengeTarget = this.entity.getLastHurtByMob();
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(revengeTarget)) {
			return false;
		}
		if (!revengeTarget.isAlive()) {
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
		this.prevRevengeTimer = this.entity.getLastHurtByMobTimestamp();
		this.trySetAttackTarget(this.entity);
		Faction faction = this.entity.getFaction();
		if (faction != null && faction.isEnemy(this.attackTarget) && !(this.entity.getLeader() instanceof Player)) {
			this.callForHelp();
		}
	}

	protected void callForHelp() {
		double x = this.entity.getX();
		double y = this.entity.getY() + this.entity.getEyeHeight();
		double z = this.entity.getZ();
		double r = CQRConfig.SERVER_CONFIG.mobs.alertRadius.get();
		AABB aabb = new AABB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		List<AbstractEntityCQR> allies = this.world.getEntitiesOfClass(AbstractEntityCQR.class, aabb, this::isSuitableAlly);
		for (AbstractEntityCQR ally : allies) {
			this.trySetAttackTarget(ally);
		}
	}

	protected boolean isSuitableAlly(AbstractEntityCQR possibleAlly) {
		if (!EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(possibleAlly)) {
			return false;
		}
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!isAllyCheckingLeadersWhenAttacked(this.entity, possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigation().createPath(this.entity, 1 /* accuracy */);
		if (path == null) {
			return false;
		}
		PathPoint end = path.getEndNode();
		if (end == null) {
			return false;
		}
		if (this.entity.distanceToSqr(end.x, end.y, end.z) > MAX_PATH_END_TO_TARGET_DISTANCE_SQ) {
			return false;
		}
		if (path.getNodeCount() > MAX_PATH_LENGTH) {
			return false;
		}
		return getPathComplexity(path) <= MAX_PATH_COMPLEXITY;
	}

	protected boolean trySetAttackTarget(AbstractEntityCQR ally) {
		ItemStack stack = ally.getMainHandItem();
		if (stack.getItem() instanceof ISupportWeapon) {
			return false;
		}
		if (stack.getItem() instanceof IFakeWeapon) {
			return false;
		}
		if (!isEnemyCheckingLeadersWhenAttacked(ally, this.attackTarget)) {
			return false;
		}
		LivingEntity oldAttackTarget = ally.getTarget();
		if (oldAttackTarget != null && ally.getSensing().hasLineOfSight(oldAttackTarget) && ally.distanceToSqr(oldAttackTarget) < ally.distanceToSqr(this.attackTarget)) {
			return false;
		}
		ally.setTarget(this.attackTarget);
		return true;
	}

	private static boolean isAllyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, LivingEntity possibleAlly) {
		LivingEntity leader = TargetUtil.getLeaderOrOwnerRecursive(entity);
		LivingEntity targetLeader = TargetUtil.getLeaderOrOwnerRecursive(possibleAlly);
		if (!(leader instanceof Player) && targetLeader instanceof Player) {
			return false;
		}
		return TargetUtil.isAllyCheckingLeaders(leader, targetLeader);
	}

	private static boolean isEnemyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, LivingEntity possibleEnemy) {
		LivingEntity leader = TargetUtil.getLeaderOrOwnerRecursive(entity);
		if (!(leader instanceof Player)) {
			return !TargetUtil.isAllyCheckingLeaders(leader, possibleEnemy);
		}
		return TargetUtil.isEnemyCheckingLeaders(leader, possibleEnemy);
	}

	private static int getPathComplexity(Path path) {
		if (path.getNodeCount() == 0) {
			return 0;
		}
		int pathComplexity = 0;
		Axis prevPrevPrevAxis = null;
		Axis prevPrevAxis = null;
		Axis prevAxis = null;
		int prevX = path.getNode(0).x;
		int prevZ = path.getNode(0).z;
		for (int i = 0; i < path.getNodeCount(); i++) {
			PathPoint point = path.getNode(i);
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
