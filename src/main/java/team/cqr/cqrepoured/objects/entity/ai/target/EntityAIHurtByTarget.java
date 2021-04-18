package team.cqr.cqrepoured.objects.entity.ai.target;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.IFakeWeapon;
import team.cqr.cqrepoured.objects.items.ISupportWeapon;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityAIHurtByTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected EntityLivingBase attackTarget;
	protected int prevRevengeTimer;

	public EntityAIHurtByTarget(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			return false;
		}
		if (this.entity.getRevengeTimer() == this.prevRevengeTimer) {
			return false;
		}
		EntityLivingBase revengeTarget = this.entity.getRevengeTarget();
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(revengeTarget)) {
			return false;
		}
		if (!revengeTarget.isEntityAlive()) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
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
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public void startExecuting() {
		this.prevRevengeTimer = this.entity.getRevengeTimer();
		this.trySetAttackTarget(this.entity);
		CQRFaction faction = this.entity.getFaction();
		if (faction != null && faction.isEnemy(this.attackTarget) && !(this.entity.getLeader() instanceof EntityPlayer)) {
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
		if (!EntitySelectors.IS_ALIVE.apply(possibleAlly)) {
			return false;
		}
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!isAllyCheckingLeadersWhenAttacked(this.entity, possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigator().getPathToEntityLiving(this.entity);
		return path != null && path.getCurrentPathLength() <= 20 && getPathComplexity(path) <= 4;
	}

	protected boolean trySetAttackTarget(AbstractEntityCQR ally) {
		ItemStack stack = ally.getHeldItemMainhand();
		if (stack.getItem() instanceof ISupportWeapon) {
			return false;
		}
		if (stack.getItem() instanceof IFakeWeapon) {
			return false;
		}
		if (!isEnemyCheckingLeadersWhenAttacked((AbstractEntityCQR) ally, this.attackTarget)) {
			return false;
		}
		EntityLivingBase oldAttackTarget = ally.getAttackTarget();
		if (oldAttackTarget != null && ally.getEntitySenses().canSee(oldAttackTarget) && ally.getDistanceSq(oldAttackTarget) < ally.getDistanceSq(this.attackTarget)) {
			return false;
		}
		ally.setAttackTarget(this.attackTarget);
		return true;
	}

	private static boolean isAllyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, EntityLivingBase possibleAlly) {
		CQRFaction faction = entity.getFaction();
		EntityLivingBase leader = entity.getLeader();
		if (!(leader instanceof EntityPlayer)) {
			// non-player leader
			if (possibleAlly == leader) {
				return true;
			}
			if (possibleAlly instanceof AbstractEntityCQR) {
				EntityLivingBase possibleAllyLeader = ((AbstractEntityCQR) possibleAlly).getLeader();
				if (leader != null && possibleAllyLeader == leader) {
					return true;
				}
				if (possibleAllyLeader instanceof EntityPlayer) {
					return false;
				}
			}
			if (faction.isAlly(possibleAlly)) {
				return true;
			}
			return false;
		} else {
			// player leader
			if (possibleAlly == leader) {
				return true;
			}
			if (possibleAlly instanceof AbstractEntityCQR && ((AbstractEntityCQR) possibleAlly).getLeader() == leader) {
				return true;
			}
			return false;
		}
	}

	private static boolean isEnemyCheckingLeadersWhenAttacked(AbstractEntityCQR entity, EntityLivingBase possibleEnemy) {
		CQRFaction faction = entity.getFaction();
		EntityLivingBase leader = entity.getLeader();
		if (!(leader instanceof EntityPlayer)) {
			// non-player leader
			if (possibleEnemy == leader) {
				return false;
			}
			if (faction.isAlly(possibleEnemy)) {
				return false;
			}
			if (possibleEnemy instanceof AbstractEntityCQR) {
				EntityLivingBase possibleEnemyLeader = ((AbstractEntityCQR) possibleEnemy).getLeader();
				if (leader != null && possibleEnemyLeader == leader) {
					return false;
				}
				if (possibleEnemyLeader instanceof EntityPlayer && faction.isAlly(possibleEnemyLeader)) {
					return false;
				}
			}
			return true;
		} else {
			// player leader
			if (possibleEnemy == leader) {
				return false;
			}
			if (possibleEnemy instanceof AbstractEntityCQR && ((AbstractEntityCQR) possibleEnemy).getLeader() == leader) {
				return false;
			}
			CQRFaction possibleEnemyFaction = FactionRegistry.instance().getFactionOf(possibleEnemy);
			if (possibleEnemyFaction != null) {
				if (!possibleEnemyFaction.isEnemy(leader)) {
					return false;
				}
			} else {
				if (!(possibleEnemy instanceof EntityMob)) {
					return false;
				}
			}
			return true;
		}
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
