package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.util.BlockPlacingHelper.IBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public abstract class GeneratablePosInfo implements IBlockInfo {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final int x;
	private final int y;
	private final int z;

	protected GeneratablePosInfo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected GeneratablePosInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, GeneratableDungeon dungeon) {
		return this.place(world, chunk, blockStorage, MUTABLE.setPos(this.x, this.y, this.z), dungeon);
	}

	protected abstract boolean place(World world, Chunk chunk, ExtendedBlockStorage blockStorage, BlockPos pos, GeneratableDungeon dungeon);

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public int getChunkX() {
		return this.x >> 4;
	}

	public int getChunkY() {
		return this.y >> 4;
	}

	public int getChunkZ() {
		return this.z >> 4;
	}

}
