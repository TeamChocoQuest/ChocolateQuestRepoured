package team.cqr.cqrepoured.generation.world.level.levelgen.structure.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.SimplePalette;
import net.minecraftforge.common.util.LazyOptional;
import team.cqr.cqrepoured.common.io.DataIOUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonPlacement;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;

public class BlockInfo implements IBlockInfo {

	private final BlockState blockState;
	@Nullable
	private final CompoundTag blockEntityTag;

	public BlockInfo(BlockState blockState, @Nullable CompoundTag blockEntityTag) {
		this.blockState = blockState;
		this.blockEntityTag = blockEntityTag;
	}

	@Override
	public void prepare(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedBlockState = placement.transform(this.blockState);

		level.setBlockState(transformedPos, transformedBlockState, this.blockEntityTag);
	}

	@Override
	public void prepareNoProcessing(StructureLevel level, BlockPos pos, DungeonPlacement placement) {
		BlockPos transformedPos = placement.transform(pos);
		BlockState transformedBlockState = placement.transform(this.blockState);

		level.setBlockState(transformedPos, transformedBlockState, this.blockEntityTag);
	}

	public BlockState getBlockState() {
		return this.blockState;
	}

	@Nullable
	public CompoundTag getBlockEntityTag() {
		return this.blockEntityTag;
	}

	public static class Factory implements IBlockInfoFactory<BlockEntity> {

		@Override
		public IBlockInfo create(Level level, BlockPos pos, BlockState blockState, LazyOptional<BlockEntity> blockEntitySupplier) {
			return new BlockInfo(blockState, blockEntitySupplier.map(IBlockInfoFactory::writeBlockEntityToNBT)
					.orElse(null));
		}

	}

	public static class Serializer implements IBlockInfoSerializer<BlockInfo> {

		@Override
		public void write(BlockInfo blockInfo, DataOutput out, SimplePalette palette) throws IOException {
			int data = (palette.idFor(blockInfo.blockState) << 1) | (blockInfo.blockEntityTag != null ? 1 : 0);
			DataIOUtil.writeVarInt(out, data);
			if (blockInfo.blockEntityTag != null) {
				NbtIo.write(blockInfo.blockEntityTag, out);
			}
		}

		@Override
		public BlockInfo read(DataInput in, SimplePalette palette) throws IOException {
			int data = DataIOUtil.readVarInt(in);
			BlockState blockState = palette.stateFor(data >>> 1);
			CompoundTag blockEntityTag = null;
			if ((data & 1) == 1) {
				blockEntityTag = NbtIo.read(in);
			}
			return new BlockInfo(blockState, blockEntityTag);
		}

	}

}
