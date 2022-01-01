package team.cqr.cqrepoured.world.structure.generation.generation.preparable;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.world.structure.generation.structurefile.BlockStatePalette;

public class PreparableBlockInfo extends PreparablePosInfo {

	private final BlockState state;
	@Nullable
	private final CompoundNBT tileEntityData;

	public PreparableBlockInfo(BlockPos pos, BlockState state, @Nullable CompoundNBT tileEntityData) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntityData);
	}

	public PreparableBlockInfo(int x, int y, int z, BlockState state, @Nullable CompoundNBT tileEntityData) {
		super(x, y, z);
		this.state = state;
		this.tileEntityData = tileEntityData;
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		BlockState transformedState = this.state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = null;

		if (this.tileEntityData != null) {
			tileEntity = transformedState.getBlock().createTileEntity(world, transformedState);
			if (tileEntity != null) {
				this.tileEntityData.setInteger("x", pos.getX());
				this.tileEntityData.setInteger("y", pos.getY());
				this.tileEntityData.setInteger("z", pos.getZ());
				tileEntity.readFromNBT(this.tileEntityData);
				if (tileEntity instanceof SkullTileEntity) {
					if (transformedState.getValue(SkullBlock.FACING) == Direction.UP) {
						SkullTileEntity skull = (SkullTileEntity) tileEntity;
						skull.setSkullRotation(placement.getMirror().mirrorRotation(skull.skullRotation, 16));
						skull.setSkullRotation(placement.getRotation().rotate(skull.skullRotation, 16));
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
		BlockState transformedState = this.state.withMirror(placement.getMirror()).withRotation(placement.getRotation());
		TileEntity tileEntity = null;

		if (this.tileEntityData != null) {
			tileEntity = transformedState.getBlock().createTileEntity(world, transformedState);
			if (tileEntity != null) {
				this.tileEntityData.setInteger("x", pos.getX());
				this.tileEntityData.setInteger("y", pos.getY());
				this.tileEntityData.setInteger("z", pos.getZ());
				tileEntity.readFromNBT(this.tileEntityData);
				if (tileEntity instanceof SkullTileEntity) {
					if (transformedState.getValue(SkullBlock.FACING) == Direction.UP) {
						SkullTileEntity skull = (SkullTileEntity) tileEntity;
						skull.setSkullRotation(placement.getMirror().mirrorRotation(skull.skullRotation, 16));
						skull.setSkullRotation(placement.getRotation().rotate(skull.skullRotation, 16));
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

		return this.prepareDebug(world, placement, pos, transformedState, tileEntity);
	}

	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity) {
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity) {
		return new GeneratableBlockInfo(pos, state, tileEntity);
	}

	public BlockState getState() {
		return this.state;
	}

	@Nullable
	public CompoundNBT getTileEntityData() {
		return this.tileEntityData;
	}

	public static class Factory implements IFactory<TileEntity> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, BlockState state, Supplier<TileEntity> tileEntitySupplier) {
			return new PreparableBlockInfo(x, y, z, state, IFactory.writeTileEntityToNBT(tileEntitySupplier.get()));
		}

	}

	public static class Serializer implements ISerializer<PreparableBlockInfo> {

		@Override
		public void write(PreparableBlockInfo preparable, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = (palette.idFor(preparable.state) << 1) | (preparable.tileEntityData != null ? 1 : 0);
			ByteBufUtils.writeVarInt(buf, data, 5);
			if (preparable.tileEntityData != null) {
				ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
				nbtList.appendTag(preparable.tileEntityData);
			}
		}

		@Override
		public PreparableBlockInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, ListNBT nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			BlockState state = palette.stateFor(data >>> 1);
			CompoundNBT tileEntityData = null;
			if ((data & 1) == 1) {
				tileEntityData = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
			}
			return new PreparableBlockInfo(x, y, z, state, tileEntityData);
		}

		@Override
		@Deprecated
		public PreparableBlockInfo read(int x, int y, int z, IntArrayNBT nbtIntArray, BlockStatePalette palette, ListNBT nbtList) {
			int[] intArray = nbtIntArray.getIntArray();
			BlockState state = palette.stateFor(intArray[1]);
			CompoundNBT tileEntityData = null;
			if (intArray.length > 2) {
				tileEntityData = nbtList.getCompoundTagAt(intArray[2]);
			}
			return new PreparableBlockInfo(x, y, z, state, tileEntityData);
		}

	}

}
