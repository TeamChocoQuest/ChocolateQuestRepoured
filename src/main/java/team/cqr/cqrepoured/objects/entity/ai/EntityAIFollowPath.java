package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.pathfinding.Path;

public class EntityAIFollowPath extends AbstractCQREntityAI<AbstractEntityCQR> {

	private boolean hasPath;
	private int ticksToWait;
	private int tick;

	public EntityAIFollowPath(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.getPath().getSize() <= 1) {
			return false;
		}
		return this.entity.hasHomePositionCQR();
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.hasPath = false;
		this.ticksToWait = 0;
		this.tick = 0;
	}

	@Override
	public void updateTask() {
		Path path = this.entity.getPath();
		Path.PathNode currentNode = path.getNode(this.entity.getCurrentPathTargetPoint());

		if (currentNode != null) {
			BlockPos pos = this.entity.getHomePositionCQR().add(currentNode.getPos());

			if (this.entity.hasPath()) {
				this.entity.getLookHelper().setLookPosition(pos.getX() + 0.5D, pos.getY() + this.entity.getEyeHeight(), pos.getZ() + 0.5D, 30.0F, 30.0F);
			} else if (this.hasPath) {
				this.hasPath = false;
				if (this.entity.getDistanceSq(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D) <= 1.0D) {
					this.ticksToWait = MathHelper.getInt(this.random, currentNode.getWaitingTimeMin(), currentNode.getWaitingTimeMax());
				}
			} else if (this.ticksToWait > 0) {
				this.ticksToWait--;
				this.entity.rotationYaw = currentNode.getWaitingRotation();
				long time = this.world.getWorldTime() % 24000;
				if (time < currentNode.getTimeMin() || time > currentNode.getTimeMax()) {
					this.ticksToWait = 0;
				}
			} else {
				this.calculateNextNode();
			}
		} else {
			this.entity.getNavigator().clearPath();
			this.calculateNextNode();
		}
	}

	private void calculateNextNode() {
		if (this.tick > 0) {
			this.tick--;
			return;
		}

		Path path = this.entity.getPath();
		Path.PathNode prevNode = path.getNode(this.entity.getPrevPathTargetPoint());
		Path.PathNode currentNode = path.getNode(this.entity.getCurrentPathTargetPoint());
		Path.PathNode nextNode = null;

		if (currentNode != null) {
			nextNode = currentNode.getNextNode(this.world, this.entity.getRNG(), prevNode);
		} else {
			BlockPos pos = new BlockPos(this.entity).subtract(this.entity.getHomePositionCQR());
			double min = Double.MAX_VALUE;
			for (Path.PathNode node : path.getNodes()) {
				double dist = pos.distanceSq(node.getPos());
				if (dist < min) {
					min = dist;
					nextNode = node;
				}
			}
		}

		if (nextNode != null) {
			this.entity.setCurrentPathTargetPoint(nextNode.getIndex());
			BlockPos pos = this.entity.getHomePositionCQR().add(nextNode.getPos());
			this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), Math.sqrt(0.75D));
			this.hasPath = true;
		} else {
			this.tick = 40;
		}
	}

}
