package team.cqr.cqrepoured.gentest.generatable;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.gentest.generatable.GeneratablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;
import team.cqr.cqrepoured.util.BlockPlacingHelper;

public class GeneratableBlockInfo extends GeneratablePosInfo {

	private final IBlockState state;
	@Nullable
	private final TileEntity tileEntity;

	public GeneratableBlockInfo(int x, int y, int z, IBlockState state, @Nullable TileEntity tileEntity) {
		super(x, y, z);
		this.state = state;
		this.tileEntity = tileEntity;
	}

	public GeneratableBlockInfo(BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntity);
	}

	@Override
	protected boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, BlockPos pos, GeneratableDungeon dungeon) {
		IBlockState oldState = blockStorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		int oldLight = oldState.getLightValue(world, pos);
		if (!BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, this.state, this.tileEntity, 16)) {
			return false;
		}
		if (oldLight > 0 && blockStorage.getBlockLight(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15) > 0) {
			dungeon.markRemovedLight(pos.getX(), pos.getY(), pos.getZ(), oldLight);
		}
		return true;
	}

	@Override
	public boolean hasTileEntity() {
		return this.tileEntity != null;
	}

	@Override
	public boolean hasSpecialShape() {
		return !this.state.isFullCube() && !this.state.isFullBlock();
	}

	public IBlockState getState() {
		return this.state;
	}

	@Nullable
	public TileEntity getTileEntity() {
		return this.tileEntity;
	}

	public static class Serializer implements ISerializer<GeneratableBlockInfo> {

		@Override
		public void write(GeneratableBlockInfo generatable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = (palette.idFor(generatable.state) << 1) | (generatable.tileEntity != null ? 1 : 0);
			ByteBufUtils.writeVarInt(buf, data, 5);
			if (generatable.tileEntity != null) {
				ByteBufUtils.writeVarInt(buf, nbtList.tagCount(), 5);
				nbtList.appendTag(generatable.tileEntity.writeToNBT(new NBTTagCompound()));
			}
		}

		@Override
		public GeneratableBlockInfo read(World world, int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			int data = ByteBufUtils.readVarInt(buf, 5);
			IBlockState state = palette.stateFor(data >>> 1);
			TileEntity tileEntity = null;
			if ((data & 1) == 1) {
				NBTTagCompound compound = nbtList.getCompoundTagAt(ByteBufUtils.readVarInt(buf, 5));
				tileEntity = state.getBlock().createTileEntity(world, state);
				tileEntity.readFromNBT(compound);
			}
			return new GeneratableBlockInfo(x, y, z, state, tileEntity);
		}

	}

}
