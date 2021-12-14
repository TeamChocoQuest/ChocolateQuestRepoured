package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableMapInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableMapInfo extends PreparablePosInfo {

	private final EnumFacing facing;
	private final byte scale;
	private final EnumFacing orientation;
	private final boolean lockOrientation;
	private final int originX;
	private final int originZ;
	private final int offsetX;
	private final int offsetZ;
	private final boolean fillMap;
	private final int fillRadius;

	public PreparableMapInfo(BlockPos pos, EnumFacing facing, TileEntityMap tileEntityMap) {
		this(pos.getX(), pos.getY(), pos.getZ(), facing, tileEntityMap);
	}

	public PreparableMapInfo(int x, int y, int z, EnumFacing facing, TileEntityMap tileEntityMap) {
		this(x, y, z, facing, (byte) tileEntityMap.getScale(), tileEntityMap.getOrientation(), tileEntityMap.lockOrientation(), tileEntityMap.getOriginX(), tileEntityMap.getOriginZ(), tileEntityMap.getOffsetX(), tileEntityMap.getOffsetZ(), tileEntityMap.fillMap(), tileEntityMap.getFillRadius());
	}

	public PreparableMapInfo(BlockPos pos, EnumFacing facing, byte scale, EnumFacing orientation, boolean lockOrientation, int originX, int originZ, int offsetX, int offsetZ, boolean fillMap, int fillRadius) {
		this(pos.getX(), pos.getY(), pos.getZ(), facing, scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
	}

	public PreparableMapInfo(int x, int y, int z, EnumFacing facing, byte scale, EnumFacing orientation, boolean lockOrientation, int originX, int originZ, int offsetX, int offsetZ, boolean fillMap, int fillRadius) {
		super(x, y, z);
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
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		EnumFacing transformedFacing = placement.getRotation().rotate(placement.getMirror().mirror(this.facing));
		EntityItemFrame entity = new EntityItemFrame(world, pos.toImmutable(), transformedFacing);
		switch (this.orientation) {
		case EAST:
			entity.setItemRotation(entity.getRotation() + 3);
			break;
		case SOUTH:
			entity.setItemRotation(entity.getRotation() + 2);
			break;
		case WEST:
			entity.setItemRotation(entity.getRotation() + 1);
			break;
		default:
			break;
		}

		int x1 = this.offsetX * (128 << this.scale);
		int z1 = this.offsetZ * (128 << this.scale);
		int x2 = this.originX;
		int z2 = this.originZ;
		switch (placement.getMirror()) {
		case LEFT_RIGHT:
			if (!this.lockOrientation) {
				if (this.orientation.getAxis() == Axis.Z) {
					entity.setItemRotation(entity.getRotation() + 2);
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
					entity.setItemRotation(entity.getRotation() + 2);
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
		switch (placement.getRotation()) {
		case COUNTERCLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 1);
				x1 = z3;
				z1 = -x3;
			}
			x2 = z4;
			z2 = -x4;
			break;
		case CLOCKWISE_90:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 3);
				x1 = -z3;
				z1 = x3;
			}
			x2 = -z4;
			z2 = x4;
			break;
		case CLOCKWISE_180:
			if (!this.lockOrientation) {
				entity.setItemRotation(entity.getRotation() + 2);
				x1 = -x3;
				z1 = -z3;
			}
			x2 = -x4;
			z2 = -z4;
			break;
		default:
			break;
		}

		return new GeneratableMapInfo(pos, entity, pos.getX() + x2, pos.getZ() + z2, pos.getX() + x1 + x2, pos.getZ() + z1 + z2, this.scale, this.fillMap, this.fillRadius);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		IBlockState state = CQRBlocks.MAP_PLACEHOLDER.getDefaultState().withProperty(BlockHorizontal.FACING, this.facing);
		state = state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntityMap tileEntity = new TileEntityMap();
		// TODO tile entity data does not get rotated/mirrored
		tileEntity.set(this.scale, this.orientation, this.lockOrientation, this.originX, this.originZ, this.offsetX, this.offsetZ, this.fillMap, this.fillRadius);
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public int getScale() {
		return this.scale;
	}

	public EnumFacing getOrientation() {
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

	public static class Factory implements IFactory<TileEntityMap> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntityMap> tileEntitySupplier) {
			return new PreparableMapInfo(x, y, z, state.getValue(BlockHorizontal.FACING), tileEntitySupplier.get());
		}

	}

	public static class Serializer implements ISerializer<PreparableMapInfo> {

		@Override
		public void write(PreparableMapInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setByte("facing", (byte) preparable.facing.getHorizontalIndex());
			compound.setByte("scale", preparable.scale);
			compound.setByte("orientation", (byte) preparable.orientation.getHorizontalIndex());
			compound.setBoolean("lockOrientation", preparable.lockOrientation);
			compound.setByte("originX", (byte) preparable.originX);
			compound.setByte("originZ", (byte) preparable.originZ);
			compound.setByte("offsetX", (byte) preparable.offsetX);
			compound.setByte("offsetZ", (byte) preparable.offsetZ);
			compound.setBoolean("fillMap", preparable.fillMap);
			compound.setShort("fillRadius", (short) preparable.fillRadius);
			ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
			nbtList.appendTag(compound);
		}

		@Override
		public PreparableMapInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			NBTTagCompound compound = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			EnumFacing facing = EnumFacing.byHorizontalIndex(compound.getInteger("facing"));
			byte scale = compound.getByte("scale");
			EnumFacing orientation = EnumFacing.byHorizontalIndex(compound.getInteger("orientation"));
			boolean lockOrientation = compound.getBoolean("lockOrientation");
			byte originX = compound.getByte("originX");
			byte originZ = compound.getByte("originZ");
			byte offsetX = compound.getByte("offsetX");
			byte offsetZ = compound.getByte("offsetZ");
			boolean fillMap = compound.getBoolean("fillMap");
			short fillRadius = compound.getShort("fillRadius");
			return new PreparableMapInfo(x, y, z, facing, scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
		}

		@Override
		@Deprecated
		public PreparableMapInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			NBTTagCompound compound = nbtList.getCompoundTagAt(intArray[0]);
			EnumFacing facing = EnumFacing.byHorizontalIndex(compound.getInteger("facing"));
			byte scale = compound.getByte("scale");
			EnumFacing orientation = EnumFacing.byHorizontalIndex(compound.getInteger("orientation"));
			boolean lockOrientation = compound.getBoolean("lockOrientation");
			byte originX = compound.getByte("originX");
			byte originZ = compound.getByte("originZ");
			byte offsetX = compound.getByte("offsetX");
			byte offsetZ = compound.getByte("offsetZ");
			boolean fillMap = compound.getBoolean("fillMap");
			short fillRadius = compound.getShort("fillRadius");
			return new PreparableMapInfo(x, y, z, facing, scale, orientation, lockOrientation, originX, originZ, offsetX, offsetZ, fillMap, fillRadius);
		}

	}

}
