package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public class GeneratableBlockInfo extends GeneratablePosInfo {

	private final BlockState state;
	@Nullable
	private final TileEntity tileEntity;

	public GeneratableBlockInfo(int x, int y, int z, BlockState state, @Nullable TileEntity tileEntity) {
		super(x, y, z);
		this.state = state;
		this.tileEntity = tileEntity;
	}

	public GeneratableBlockInfo(BlockPos pos, BlockState state, @Nullable TileEntity tileEntity) {
		this(pos.getX(), pos.getY(), pos.getZ(), state, tileEntity);
	}

	@Override
	protected boolean place(World world, Chunk chunk, ChunkSection blockStorage, BlockPos pos, GeneratableDungeon dungeon) {
		BlockState oldState = blockStorage.getBlockState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
		int oldLight = oldState.getLightValue(world, pos);
		if (!BlockPlacingHelper.setBlockState(world, chunk, blockStorage, pos, this.state, this.tileEntity, 16)) {
			return false;
		}
		if (oldLight > 0 && world.getLightEmission(pos)/*blockStorage.getBlockLight(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15)*/ > 0) {
			dungeon.markRemovedLight(pos.getX(), pos.getY(), pos.getZ(), oldLight);
		}
		return true;
	}

	public BlockState getState() {
		return this.state;
	}

	@Nullable
	public TileEntity getTileEntity() {
		return this.tileEntity;
	}

}
