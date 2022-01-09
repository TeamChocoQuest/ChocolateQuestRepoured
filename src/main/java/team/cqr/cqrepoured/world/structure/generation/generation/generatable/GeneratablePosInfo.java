package team.cqr.cqrepoured.world.structure.generation.generation.generatable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import team.cqr.cqrepoured.util.BlockPlacingHelper.IBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public abstract class GeneratablePosInfo implements IGeneratable, IBlockInfo {

	private static final BlockPos.Mutable MUTABLE = new BlockPos.Mutable();
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
	public void generate(World world, GeneratableDungeon dungeon) {
		Chunk chunk = world.getChunk(this.x >> 4, this.z >> 4);
		if (this.y < 0 || this.y > 256) {
			return;
		}
		ChunkSection blockStorage = chunk.getSections()[this.y >> 4];
		if (blockStorage == null) {
			blockStorage = new ChunkSection(this.y >> 4 << 4/*, world.dimensionType().hasSkyLight()*/);
			if (this.place(world, chunk, blockStorage, dungeon)) {
				chunk.getSections()[this.y >> 4] = blockStorage;
			}
		} else {
			this.place(world, chunk, blockStorage, dungeon);
		}
	}

	@Override
	public boolean place(World world, Chunk chunk, ChunkSection blockStorage, GeneratableDungeon dungeon) {
		return this.place(world, chunk, blockStorage, MUTABLE.set(this.x, this.y, this.z), dungeon);
	}

	protected abstract boolean place(World world, Chunk chunk, ChunkSection blockStorage, BlockPos pos, GeneratableDungeon dungeon);

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
