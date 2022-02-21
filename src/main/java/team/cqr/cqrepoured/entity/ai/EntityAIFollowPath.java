package team.cqr.cqrepoured.entity.ai;

import java.util.EnumSet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.pathfinding.CQRNPCPath;

public class EntityAIFollowPath extends AbstractCQREntityAI<AbstractEntityCQR> {

	private boolean hasPath;
	private int ticksToWait;
	private int tick;

	public EntityAIFollowPath(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.MOVE/*, Flag.JUMP, Flag.LOOK*/));
	}

	@Override
	public boolean canUse() {
		if (this.entity.getPath().getSize() <= 1) {
			return false;
		}
		return this.entity.hasHomePositionCQR();
	}

	@Override
	public void stop() {
		this.entity.getNavigation().stop();
		this.hasPath = false;
		this.ticksToWait = 0;
		this.tick = 0;
	}

	@Override
	public void tick() {
		CQRNPCPath path = this.entity.getPath();
		CQRNPCPath.PathNode currentNode = path.getNode(this.entity.getCurrentPathTargetPoint());

		if (currentNode != null) {
			BlockPos pos = this.entity.getHomePositionCQR().offset(currentNode.getPos());

			if (this.entity.isPathFinding()) {
				this.entity.getLookControl().setLookAt(pos.getX() + 0.5D, pos.getY() + this.entity.getEyeHeight(), pos.getZ() + 0.5D, 30.0F, 30.0F);
			} else if (this.hasPath) {
				this.hasPath = false;
				if (this.entity.distanceToSqr(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D) <= 1.0D) {
					this.ticksToWait = MathHelper.nextInt(this.random, currentNode.getWaitingTimeMin(), currentNode.getWaitingTimeMax());
				}
			} else if (this.ticksToWait > 0) {
				this.ticksToWait--;
				this.entity.yRot = currentNode.getWaitingRotation();
				long time = this.world.getGameTime() % 24000;
				if (time < currentNode.getTimeMin() || time > currentNode.getTimeMax()) {
					this.ticksToWait = 0;
				}
			} else {
				this.calculateNextNode();
			}
		} else {
			this.entity.getNavigation().stop();
			this.calculateNextNode();
		}
	}

	private void calculateNextNode() {
		if (this.tick > 0) {
			this.tick--;
			return;
		}

		CQRNPCPath path = this.entity.getPath();
		CQRNPCPath.PathNode prevNode = path.getNode(this.entity.getPrevPathTargetPoint());
		CQRNPCPath.PathNode currentNode = path.getNode(this.entity.getCurrentPathTargetPoint());
		CQRNPCPath.PathNode nextNode = null;

		if (currentNode != null) {
			nextNode = currentNode.getNextNode(this.world, this.entity.getRandom(), prevNode);
		} else {
			BlockPos pos = this.entity.blockPosition().subtract(this.entity.getHomePositionCQR());
			double min = Double.MAX_VALUE;
			for (CQRNPCPath.PathNode node : path.getNodes()) {
				double dist = pos.distSqr(node.getPos());
				if (dist < min) {
					min = dist;
					nextNode = node;
				}
			}
		}

		if (nextNode != null) {
			this.entity.setCurrentPathTargetPoint(nextNode.getIndex());
			BlockPos pos = this.entity.getHomePositionCQR().offset(nextNode.getPos());
			this.entity.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), Math.sqrt(0.75D));
			this.hasPath = true;
		} else {
			this.tick = 40;
		}
	}

}
