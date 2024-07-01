package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.generation.init.CQRBlocks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

public class PreparableMapInfo implements IBlockInfo {

	private final Direction facing;
	private final byte scale;
	private final Direction orientation;
	private final boolean lockOrientation;
	private final int originX;
	private final int originZ;
	private final int offsetX;
	private final int offsetZ;
	private final boolean fillMap;
	private final int fillRadius;

	public PreparableMapInfo(Direction facing, TileEntityMap tileEntityMap) {
		this(facing, (byte) tileEntityMap.getScale(), tileEntityMap.getOrientation(), tileEntityMap.lockOrientation(), tileEntityMap.getOriginX(), tileEntityMap.getOriginZ(),
				tileEntityMap.getOffsetX(), tileEntityMap.getOffsetZ(), tileEntityMap.fillMap(), tileEntityMap.getFillRadius());
	}

	public PreparableMapInfo(Direction facing, byte scale, Direction orientation, boolean lockOrientation, int originX, int originZ, int offsetX, int offsetZ, boolean fillMap, int fillRadius) {
		this.facing = facing;
		this.scale = scale;
		this.orientation = orientation;
		this.lockOrientation = lockOrientation;
		this.originX = originX;
		this.originZ = originZ;
		this.offsetX = offsetX;
		this.offsetZ = offsetZ;
		this.fillMap = fillMap;
		this.fillRadius = fillRadius;
	}

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		Direction transformedFacing = placement.rotation().rotate(placement.mirror().mirror(this.facing));
		ItemFrame entity = placement.entityFactory().createEntity(world -> new ItemFrame(world, transformedPos.immutable(), transformedFacing));
		switch (this.orientation) {
		case EAST:
			entity.setRotation(entity.getRotation() + 3);
			break;
		case SOUTH:
			entity.setRotation(entity.getRotation() + 2);
			break;
		case WEST:
			entity.setRotation(entity.getRotation() + 1);
			break;
		default:
			break;
		}

		int x1 = this.offsetX * (128 << this.scale);
		int z1 = this.offsetZ * (128 << this.scale);
		int x2 = this.originX;
		int z2 = this.originZ;
		switch (placement.mirror()) {
		case LEFT_RIGHT:
			if (!this.lockOrientation) {
				if (this.orientation.getAxis() == Axis.Z) {
					entity.setRotation(entity.getRotation() + 2);
				}
				z1 = -z1;
			} else {
				if (this.orientation.getAxis() == Axis.X) {
					z1 = -z1;
				}
				if (this.orientation.getAxis() == Axis.Z) {
					x1 = -x1;
				}
			}
			z2 = -z2;
			break;
		case FRONT_BACK:
			if (!this.lockOrientation) {
				if (this.orientation.getAxis() == Axis.X) {
					entity.setRotation(entity.getRotation() + 2);
				}
				x1 = -x1;
			} else {
				if (this.orientation.getAxis() == Axis.X) {
					z1 = -z1;
				}
				if (this.orientation.getAxis() == Axis.Z) {
					x1 = -x1;
				}
			}
			x2 = -x2;
			break;
		default:
			break;
		}

		int x3 = x1;
		int z3 = z1;
		int x4 = x2;
		int z4 = z2;
		switch (placement.rotation()) {
		case COUNTERCLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setRotation(entity.getRotation() + 1);
				x1 = z3;
				z1 = -x3;
			}
			x2 = z4;
			z2 = -x4;
			break;
		case CLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setRotation(entity.getRotation() + 3);
				x1 = -z3;
				z1 = x3;
			}
			x2 = -z4;
			z2 = x4;
			break;
		case CLOCKWISE_180:
			if (!this.lockOrientation) {
				entity.setRotation(entity.getRotation() + 2);
				x1 = -x3;
				z1 = -z3;
			}
			x2 = -x4;
			z2 = -z4;
			break;
		default:
			break;
		}

		level.addEntity(entity);
		// TODO generate map
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedState = placement.transform(CQRBlocks.MAP_PLACEHOLDER.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, this.facing));
		level.setBlockState(transformedPos, transformedState, blockEntity -> {
			if (blockEntity instanceof TileEntityMap) {
				((TileEntityMap) blockEntity).set(this.scale, this.orientation, this.lockOrientation, this.originX, this.originZ, this.offsetX, this.offsetZ, this.fillMap, this.fillRadius);
			}
		});
	}

	public Direction getFacing() {
		return this.facing;
	}

	public int getScale() {
		return this.scale;
	}

	public Direction getOrientation() {
		return this.orientation;
	}

	public boolean isLockOrientation() {
		return this.lockOrientation;
	}

	public int getOriginX() {
		return this.originX;
	}

	public int getOriginZ() {
		return this.originZ;
	}

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetZ() {
		return this.offsetZ;
	}

	public boolean isFillMap() {
		return this.fillMap;
	}

	public int getFillRadius() {
		return this.fillRadius;
	}

	public static class Factory implements IBlockInfoFactory<TileEntityMap> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState state, LazyOptional<TileEntityMap> blockEntitySupplier) {
			return new PreparableMapInfo(state.getValue(HorizontalDirectionalBlock.FACING), blockEntitySupplier.orElseThrow(NullPointerException::new));
		}

	}

	public static class Serializer implements IBlockInfoSerializer<PreparableMapInfo> {

		@Override
		public void write(PreparableMapInfo mapInfo, DataOutput out, SimplePalette palette) throws IOException {
			out.writeByte(mapInfo.facing.get2DDataValue());
			out.writeByte(mapInfo.scale);
			out.writeByte(mapInfo.orientation.get2DDataValue());
			out.writeBoolean(mapInfo.lockOrientation);
			out.writeByte(mapInfo.originX);
			out.writeByte(mapInfo.originZ);
			out.writeByte(mapInfo.offsetX);
			out.writeByte(mapInfo.offsetZ);
			out.writeBoolean(mapInfo.fillMap);
			out.writeShort(mapInfo.fillRadius);
		}

		@Override
		public PreparableMapInfo read(DataInput in, SimplePalette palette) throws IOException {
			Direction facing = Direction.from2DDataValue(in.readByte());
			byte scale = in.readByte();
			Direction orientation = Direction.from2DDataValue(in.readByte());
			boolean lockOrientation = in.readBoolean();
			byte originX = in.readByte();
			byte originZ = in.readByte();
			byte offsetX = in.readByte();
			byte offsetZ = in.readByte();
			boolean fillMap = in.readBoolean();
			short fillRadius = in.readShort();
			return new PreparableMapInfo(facing, scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
		}

	}

}
