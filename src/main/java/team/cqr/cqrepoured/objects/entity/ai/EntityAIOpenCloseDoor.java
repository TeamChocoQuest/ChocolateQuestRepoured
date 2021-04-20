package team.cqr.cqrepoured.objects.entity.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAIOpenCloseDoor extends AbstractCQREntityAI<AbstractEntityCQR> {

	private final BlockPos.MutableBlockPos doorPos = new BlockPos.MutableBlockPos();
	private BlockDoor doorBlock;
	private EnumFacing doorEnterFacing;
	private boolean hasStoppedDoorInteraction;
	private double entityPositionX;
	private double entityPositionZ;
	private int tick;

	public EntityAIOpenCloseDoor(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canOpenDoors()) {
			return false;
		}
		if (!this.entity.hasPath()) {
			return false;
		}
		
		this.doorPos.setPos(this.entity);
		IBlockState state = this.world.getBlockState(this.doorPos);
		if (state.getBlock() instanceof BlockDoor) {
			this.doorBlock = (BlockDoor) state.getBlock();
			this.doorEnterFacing = this.entity.getHorizontalFacing().getOpposite();
			return canMoveThroughDoor(this.world, this.doorPos, this.doorEnterFacing, true);
		}
		
		Path path = this.entity.getNavigator().getPath();
		int end = Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength());
		for (int i = path.getCurrentPathIndex(); i < end; i++) {
			PathPoint pathPoint = path.getPathPointFromIndex(i);
			this.doorPos.setPos(pathPoint.x, pathPoint.y, pathPoint.z);
			if (this.entity.getDistanceSq(this.doorPos.getX() + 0.5D, this.doorPos.getY(), this.doorPos.getZ() + 0.5D) >= 1.5D * 1.5D) {
				continue;
			}

			state = this.world.getBlockState(this.doorPos);
			if (state.getBlock() instanceof BlockDoor) {
				this.doorBlock = (BlockDoor) state.getBlock();
				if (i > 0) {
					PathPoint pathPoint1 = path.getPathPointFromIndex(i - 1);
					this.doorEnterFacing = EnumFacing.getFacingFromVector(pathPoint1.x - pathPoint.x, pathPoint1.y - pathPoint.y, pathPoint1.z - pathPoint.z);
				} else {
					this.doorEnterFacing = this.entity.getHorizontalFacing().getOpposite();
				}
				return canMoveThroughDoor(this.world, this.doorPos, this.doorEnterFacing, true);
			}
		}
	
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.hasStoppedDoorInteraction;
	}

	@Override
	public void startExecuting() {
		this.entityPositionX = this.doorPos.getX() + 0.5D - this.entity.posX;
		this.entityPositionZ = this.doorPos.getZ() + 0.5D - this.entity.posZ;
	}

	@Override
	public void resetTask() {
		this.hasStoppedDoorInteraction = false;
		this.tick = 0;

		this.closeDoor();
	}

	@Override
	public void updateTask() {
		if (this.tick++ > 60) {
			this.hasStoppedDoorInteraction = true;
		}

		this.openDoor();
		double dx = this.doorPos.getX() + 0.5D - this.entity.posX;
		double dz = this.doorPos.getZ() + 0.5D - this.entity.posZ;
		double d = this.entityPositionX * dx + this.entityPositionZ * dz;

		if (d < 0.0D
				&& (MathHelper.floor(this.entity.posX) != this.doorPos.getX()
				|| MathHelper.floor(this.entity.posY) != this.doorPos.getY()
				|| MathHelper.floor(this.entity.posZ) != this.doorPos.getZ())) {
			this.hasStoppedDoorInteraction = true;
		}
	}

	private boolean openDoor() {
		IBlockState state = this.world.getBlockState(this.doorPos);
		if (!(state.getBlock() instanceof BlockDoor)) {
			return false;
		}
		state = state.getActualState(this.world, this.doorPos);
		boolean doorOpen = state.getValue(BlockDoor.OPEN);
		EnumFacing doorFacing = state.getValue(BlockDoor.FACING);
		EnumFacing.Axis blockedAxis = doorOpen ? doorFacing.rotateY().getAxis() : doorFacing.getAxis();
		if (this.doorEnterFacing.getAxis() != blockedAxis) {
			return false;
		}
		if (state.getMaterial() == Material.WOOD) {
			this.doorBlock.toggleDoor(this.world, this.doorPos, !doorOpen);
			return true;
		}
		if (!doorOpen) {
			BlockPos pos = state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER ? this.doorPos.down() : this.doorPos;
			BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
			if (this.isPressurePlate(mutablePos.setPos(pos).move(this.doorEnterFacing))) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.setPos(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.rotateY()).move(EnumFacing.UP), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.setPos(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.rotateYCCW()).move(EnumFacing.UP), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.setPos(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.rotateY()), this.doorEnterFacing)) {
				return true;
			}
			if (this.activateButtonOrLeverWithOrientation(mutablePos.setPos(pos).move(this.doorEnterFacing).move(this.doorEnterFacing.rotateYCCW()), this.doorEnterFacing)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPressurePlate(BlockPos pos) {
		IBlockState state = this.world.getBlockState(pos);
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block instanceof BlockPressurePlate && (material == Material.WOOD || material == Material.ROCK);
	}

	private boolean activateButtonOrLeverWithOrientation(BlockPos pos, EnumFacing facing) {
		IBlockState state = this.world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof BlockButton && state.getValue(BlockButton.FACING) == facing) {
			block.onBlockActivated(this.world, pos, state, null, EnumHand.MAIN_HAND, facing, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		if (block instanceof BlockLever && state.getValue(BlockLever.FACING).getFacing() == facing) {
			block.onBlockActivated(this.world, pos, state, null, EnumHand.MAIN_HAND, facing, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	private boolean closeDoor() {
		IBlockState state = this.world.getBlockState(this.doorPos);
		if (!(state.getBlock() instanceof BlockDoor)) {
			return false;
		}
		if (state.getMaterial() != Material.WOOD) {
			return false;
		}
		boolean shouldCloseDoor = true;
		double x = this.entity.posX;
		double y = this.entity.posY;
		double z = this.entity.posZ;
		double r = 4.0D;
		AxisAlignedBB aabb = new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r * 0.5D, z + r);
		List<AbstractEntityCQR> allies = this.world.getEntitiesWithinAABB(AbstractEntityCQR.class, aabb, e -> TargetUtil.isAllyCheckingLeaders(this.entity, e));
		for (AbstractEntityCQR ally : allies) {
			if (ally == this.entity) {
				continue;
			}
			if (!ally.hasPath()) {
				continue;
			}
			if (ally.getDistanceSq(this.entity) >= r * r) {
				continue;
			}
			if (MathHelper.floor(ally.posX) == this.doorPos.getX()
					&& MathHelper.floor(ally.posY) == this.doorPos.getY()
					&& MathHelper.floor(ally.posZ) == this.doorPos.getZ()) {
				shouldCloseDoor = false;
				break;
			}
			Path path = ally.getNavigator().getPath();
			int end = Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength());
			for (int i = path.getCurrentPathIndex(); i < end; i++) {
				PathPoint pathPoint = path.getPathPointFromIndex(i);
				if (pathPoint.x == this.doorPos.getX()
						&& pathPoint.y == this.doorPos.getY()
						&& pathPoint.z == this.doorPos.getZ()) {
					shouldCloseDoor = false;
					break;
				}
			}
		}
		if (shouldCloseDoor) {
			this.doorBlock.toggleDoor(this.world, this.doorPos, false);
			return true;
		}
		return false;
	}

	public static boolean canMoveThroughDoor(IBlockAccess world, BlockPos pos, EnumFacing enterFacing, boolean canOpenCloseDoors) {
		IBlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockDoor)) {
			return false;
		}
		state = state.getActualState(world, pos);
		boolean doorOpen = state.getValue(BlockDoor.OPEN);
		EnumFacing doorFacing = state.getValue(BlockDoor.FACING);
		EnumFacing.Axis blockedAxis = doorOpen ? doorFacing.rotateY().getAxis() : doorFacing.getAxis();
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
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		if (state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER) {
			pos = pos.down();
		}
		if (isPressurePlate(world, mutablePos.setPos(pos).move(enterFacing))) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.setPos(pos).move(enterFacing).move(enterFacing.rotateY()).move(EnumFacing.UP), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.setPos(pos).move(enterFacing).move(enterFacing.rotateYCCW()).move(EnumFacing.UP), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.setPos(pos).move(enterFacing).move(enterFacing.rotateY()), enterFacing)) {
			return true;
		}
		if (isButtonOrLeverWithOrientation(world, mutablePos.setPos(pos).move(enterFacing).move(enterFacing.rotateYCCW()), enterFacing)) {
			return true;
		}
		return false;
	}

	private static boolean isPressurePlate(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block instanceof BlockPressurePlate && (material == Material.WOOD || material == Material.ROCK);
	}

	private static boolean isButtonOrLeverWithOrientation(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof BlockButton) {
			return state.getValue(BlockButton.FACING) == facing;
		}
		if (block instanceof BlockLever) {
			return state.getValue(BlockLever.FACING).getFacing() == facing;
		}
		return false;
	}

}
