package team.cqr.cqrepoured.gentest.preparable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.gentest.DungeonPlacement;
import team.cqr.cqrepoured.gentest.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.gentest.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

public class PreparableBlockInfo extends PreparablePosInfo {

	private final IBlockState state;
	@Nullable
	private final NBTTagCompound tileEntityData;

	public PreparableBlockInfo(BlockPos pos, IBlockState state, @Nullable NBTTagCompound tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntityData);
	}

	public PreparableBlockInfo(int x, int y, int z, IBlockState state, @Nullable NBTTagCompound tileEntityData) {
		super(x, y, z);
		this.state = state;
		this.tileEntityData = tileEntityData;
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		IBlockState transformedState = this.state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = null;

		if (this.tileEntityData != null) {
			tileEntity = transformedState.getBlock().createTileEntity(world, transformedState);
			if (tileEntity != null) {
				this.tileEntityData.setInteger("x", pos.getX());
				this.tileEntityData.setInteger("y", pos.getY());
				this.tileEntityData.setInteger("z", pos.getZ());
				tileEntity.readFromNBT(this.tileEntityData);
				if (tileEntity instanceof TileEntitySkull) {
					if (transformedState.getValue(BlockSkull.FACING) == EnumFacing.UP) {
						((TileEntitySkull) tileEntity)
								.setSkullRotation(placement.getMirror().mirrorRotation(((TileEntitySkull) tileEntity).getSkullRotation(), 16));
						((TileEntitySkull) tileEntity).setSkullRotation(placement.getRotation().rotate(((TileEntitySkull) tileEntity).getSkullRotation(), 16));
					}
				} else {
					tileEntity.mirror(placement.getMirror());
					tileEntity.rotate(placement.getRotation());
				}
				this.tileEntityData.removeTag("x");
				this.tileEntityData.removeTag("y");
				this.tileEntityData.removeTag("z");
			}
		}

		return this.prepare(world, placement, pos, transformedState, tileEntity);
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		IBlockState transformedState = this.state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = null;

		if (this.tileEntityData != null) {
			tileEntity = transformedState.getBlock().createTileEntity(world, transformedState);
			if (tileEntity != null) {
				this.tileEntityData.setInteger("x", pos.getX());
				this.tileEntityData.setInteger("y", pos.getY());
				this.tileEntityData.setInteger("z", pos.getZ());
				tileEntity.readFromNBT(this.tileEntityData);
				if (tileEntity instanceof TileEntitySkull) {
					if (transformedState.getValue(BlockSkull.FACING) == EnumFacing.UP) {
						((TileEntitySkull) tileEntity)
								.setSkullRotation(placement.getMirror().mirrorRotation(((TileEntitySkull) tileEntity).getSkullRotation(), 16));
						((TileEntitySkull) tileEntity).setSkullRotation(placement.getRotation().rotate(((TileEntitySkull) tileEntity).getSkullRotation(), 16));
					}
				} else {
					tileEntity.mirror(placement.getMirror());
					tileEntity.rotate(placement.getRotation());
				}
				this.tileEntityData.removeTag("x");
				this.tileEntityData.removeTag("y");
				this.tileEntityData.removeTag("z");
			}
		}

		return this.prepareDebug(world, placement, pos, transformedState, null);
	}

	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity) {
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity) {
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public IBlockState getState() {
		return this.state;
	}

	@Nullable
	public NBTTagCompound getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Serializer implements ISerializer<PreparableBlockInfo> {

		@Override
		public void write(PreparableBlockInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = (palette.idFor(preparable.state) << 1) | (preparable.tileEntityData != null ? 1 : 0);
			ByteBufUtils.writeVarInt(buf, data, 5);
			if (preparable.tileEntityData != null) {
				ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
				nbtList.appendTag(preparable.tileEntityData);
			}
		}

		@Override
		public PreparableBlockInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			IBlockState state = palette.stateFor(data >>> 1);
			NBTTagCompound tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			}
			return new PreparableBlockInfo(x, y, z, state, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableBlockInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			IBlockState state = palette.stateFor(intArray[1]);
			NBTTagCompound tileEntityData = null;
			if (intArray.length > 2) {
				tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			}
			return new PreparableBlockInfo(x, y, z, state, tileEntityData);
		}

	}

}
