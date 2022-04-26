package team.cqr.cqrepoured.entity.ai;

import java.util.List;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIOpenCloseDoor extends AbstractCQREntityAI<AbstractEntityCQR> {

	private final BlockPos.Mutable doorPos = new BlockPos.Mutable();
	private DoorBlock doorBlock;
	private Direction doorEnterFacing;
	private boolean hasStoppedDoorInteraction;
	private double entityPositionX;
	private double entityPositionZ;
	private int tick;

	public EntityAIOpenCloseDoor(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (!this.entity.canOpenDoors()) {
			return false;
		}
		if (!this.entity.isPathFinding()) {
			return false;
		}

		this.doorPos.set(this.entity.getX(), this.entity.getY(), this.entity.getZ());
		BlockState state = this.world.getBlockState(this.doorPos);
		if (state.getBlock() instanceof DoorBlock) {
			this.doorBlock = (DoorBlock) state.getBlock();
			this.doorEnterFacing = this.entity.getDirection().getOpposite();
			return canMoveThroughDoor(this.world, this.doorPos, this.doorEnterFacing, true);
		}

		Path path = this.entity.getNavigation().getPath();
		int end = Math.min(path.getNextNodeIndex() + 1, path.getNodeCount());
		for (int i = path.getNextNodeIndex() -1; i < end; i++) {
			PathPoint pathPoint = path.getNode(i);
			this.doorPos.set(pathPoint.x, pathPoint.y, pathPoint.z);
			if (this.entity.distanceToSqr(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D) >= 1.5D * 1.5D) {
				continue;
			}

			state = this.world.getBlockState(this.doorPos);
			if (state.getBlock() instanceof DoorBlock) {
				this.doorBlock = (DoorBlock) state.getBlock();
				if (i > 0) {
					PathPoint pathPoint1 = path.getNode(i - 1);
					this.doorEnterFacing = Direction.getNearest(pathPoint1.x - pathPoint.x, pathPoint1.y - pathPoint.y, pathPoint1.z - pathPoint.z);
				} else {
					this.doorEnterFacing = this.entity.getDirection().getOpposite();
				}
				return canMoveThroughDoor(this.world, this.doorPos, this.doorEnterFacing, true);
			}
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.hasStoppedDoorInteraction;
	}

	@Override
	public void start() {
		this.entityPositionX = this.doorPos.getX() + 0.5D - this.entity.getX();
		this.entityPositionZ = this.doorPos.getZ() + 0.5D - this.entity.getZ();
	}

	@Override
	public void stop() {
		this.hasStoppedDoorInteraction = false;
		this.tick = 0;

		this.closeDoor();
	}

	@Override
	public void tick() {
		if (this.tick++ > 60) {
			this.hasStoppedDoorInteraction = true;
		}

		this.openDoor();
		double dx = this.doorPos.getX() + 0.5D - this.entity.getX();
		double dz = this.doorPos.getZ() + 0.5D - this.entity.getZ();
		double d = this.entityPositionX * dx + this.entityPositionZ * dz;

		if (d < 0.0D && (MathHelper.floor(this.entity.getX()) != this.doorPos.getX() || MathHelper.floor(this.entity.getY()) != this.doorPos.getY() || MathHelper.floor(this.entity.getZ()) != this.doorPos.getZ())) {
			this.hasStoppedDoorInteraction = true;
		}
	}

	private boolean openDoor() {
		BlockState state = this.world.getBlockState(this.doorPos);
		if (!(state.getBlock() instanceof DoorBlock)) {
			return false;
		}
		// Still necessary?
		//state = state.getActualState(this.world, this.doorPos);
		boolean doorOpen = state.getValue(DoorBlock.OPEN);
		Direction doorFacing = state.getValue(DoorBlock.FACING);
		Direction.Axis blockedAxis = doorOpen ? doorFacing.getClockWise().getAxis() : doorFacing.getAxis(); //OLD instead of getClockWise() -> rotateY()
		if (this.doorEnterFacing.getAxis() != blockedAxis) {
			return false;
		}
		if (state.getMaterial() == Material.WOOD) {
			this.doorBlock.setOpen(this.world, state, this.doorPos, !doorOpen);
			return true;
		}
		if (!doorOpen) {
			BlockPos pos = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? this.doorPos.below() : this.doorPos;
			BlockPos.Mutable mutablePos = new BlockPos.Mutable();
			if (this.isPressurePlate(mutablePos.set(pos).move(this.doorEnterFacing))) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.set(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.getClockWise()).move(Direction.UP), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.set(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.getCounterClockWise()).move(Direction.UP), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.set(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.getClockWise()), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.set(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.getCounterClockWise()), this.doorEnterFacing)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPressurePlate(BlockPos pos) {
		BlockState state = this.world.getBlockState(pos);
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block.is(BlockTags.PRESSURE_PLATES) || ( block instanceof PressurePlateBlock && (material == Material.WOOD || material == Material.STONE));
	}

	private boolean activateButtonOrLeverWithOrientation(BlockPos pos, Direction facing) {
		BlockState state = this.world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof AbstractButtonBlock && state.getValue(DirectionalBlock.FACING) == facing) {
			block.use(state, this.world, pos, null, Hand.MAIN_HAND, new BlockRayTraceResult(Vector3d.atBottomCenterOf(pos), facing, pos, false));
			//block.onBlockActivated(this.world, pos, state, null, Hand.MAIN_HAND, facing, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		if (block instanceof LeverBlock && state.getValue(LeverBlock.FACING) == facing) {
			block.use(state, this.world, pos, null, Hand.MAIN_HAND, new BlockRayTraceResult(Vector3d.atBottomCenterOf(pos), facing, pos, false));
			//block.onBlockActivated(this.world, pos, state, null, Hand.MAIN_HAND, facing, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	private boolean closeDoor() {
		BlockState state = this.world.getBlockState(this.doorPos);
		if (!(state.getBlock() instanceof DoorBlock)) {
			return false;
		}
		if (state.getMaterial() != Material.WOOD) {
			return false;
		}
		boolean shouldCloseDoor = true;
		double x = this.entity.getX();
		double y = this.entity.getY();
		double z = this.entity.getZ();
		double r = 4.0D;
		AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		List<AbstractEntityCQR> allies = this.world.getEntitiesOfClass(AbstractEntityCQR.class, aabb, e -> TargetUtil.isAllyCheckingLeaders(this.entity, e));
		for (AbstractEntityCQR ally : allies) {
			if (ally == this.entity) {
				continue;
			}
			if (!ally.isPathFinding()) { //OLD: hasPath()
				continue;
			}
			if (ally.distanceToSqr(this.entity) >= r * r) {
				continue;
			}
			if (MathHelper.floor(ally.getX()) == this.doorPos.getX() && MathHelper.floor(ally.getY()) == this.doorPos.getY() && MathHelper.floor(ally.getZ()) == this.doorPos.getZ()) {
				shouldCloseDoor = false;
				break;
			}
			Path path = ally.getNavigation().getPath();
			int end = Math.min(path.getNextNodeIndex() + 1, path.getNodeCount());
			for (int i = path.getNextNodeIndex() -1; i < end; i++) {
				PathPoint pathPoint = path.getNode(i);
				if (pathPoint.x == this.doorPos.getX() && pathPoint.y == this.doorPos.getY() && pathPoint.z == this.doorPos.getZ()) {
					shouldCloseDoor = false;
					break;
				}
			}
		}
		if (shouldCloseDoor) {
			this.doorBlock.setOpen(this.world, state, this.doorPos, false);
			return true;
		}
		return false;
	}

	public static boolean canMoveThroughDoor(IBlockReader world, BlockPos pos, Direction enterFacing, boolean canOpenCloseDoors) {
		BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof DoorBlock)) {
			return false;
		}
		//Still necessary?
		//state = state.getActualState(world, pos);
		boolean doorOpen = state.getValue(DoorBlock.OPEN);
		Direction doorFacing = state.getValue(DoorBlock.FACING);
		Direction.Axis blockedAxis = doorOpen ? doorFacing.getClockWise().getAxis() : doorFacing.getAxis();
		if (enterFacing.getAxis() != blockedAxis) {
			return true;
		}
		if (!canOpenCloseDoors) {
			return false;
		}
		if (state.getMaterial() == Material.WOOD) {
			return true;
		}
		if (doorOpen) {
			// TODO check for levers which can be deactivated? -> requires a check for every other possible redstone signal...
			return false;
		}
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.below();
		}
		if (isPressurePlate(world, mutablePos.set(pos).move(enterFacing))) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.set(pos).move(enterFacing).move(enterFacing.getClockWise()).move(Direction.UP), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.set(pos).move(enterFacing).move(enterFacing.getCounterClockWise()).move(Direction.UP), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.set(pos).move(enterFacing).move(enterFacing.getClockWise()), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.set(pos).move(enterFacing).move(enterFacing.getCounterClockWise()), enterFacing)) {
			return true;
		}
		return false;
	}

	private static boolean isPressurePlate(IBlockReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block.is(BlockTags.PRESSURE_PLATES) || (block instanceof PressurePlateBlock && (material == Material.WOOD || material == Material.STONE));
	}

	private static boolean isButtonOrLeverWithOrientation(IBlockReader world, BlockPos pos, Direction facing) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof AbstractButtonBlock) {
			return state.getValue(DirectionalBlock.FACING) == facing;
		}
		if (block instanceof LeverBlock) {
			return state.getValue(LeverBlock.FACING)/*.getFacing()*/ == facing;
		}
		return false;
	}

}
