package team.cqr.cqrepoured.world.structure.generation.generation.part;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public class CoverDungeonPart implements IDungeonPart {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final IBlockState coverBlock;
	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endZ;

	protected CoverDungeonPart(int startX, int startZ, int endX, int endZ, IBlockState coverBlock) {
		this.coverBlock = coverBlock;
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		for (int cx = this.startX >> 4; cx <= this.endX >> 4; cx++) {
			for (int cz = this.startZ >> 4; cz <= this.endZ >> 4; cz++) {
				Chunk chunk = world.getChunk(cx, cz);

				for (int x = 0; x < 16; x++) {
					if ((cx << 4) + x < this.startX || (cx << 4) + x > this.endX) {
						continue;
					}
					for (int z = 0; z < 16; z++) {
						if ((cz << 4) + z < this.startZ || (cz << 4) + z > this.endZ) {
							continue;
						}

						MUTABLE.setPos((cx << 4) + x, chunk.getTopFilledSegment() + 15, (cz << 4) + z);
						while (MUTABLE.getY() >= 0) {
							IBlockState state = chunk.getBlockState(MUTABLE);
							if (state.getBlock() == Blocks.AIR) {
								MUTABLE.setY(MUTABLE.getY() - 1);
							} else {
								if (state.getBlock() != this.coverBlock.getBlock()) {
									MUTABLE.setY(MUTABLE.getY() + 1);
									BlockPlacingHelper.setBlockState(world, MUTABLE, this.coverBlock, null, 16);
									dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
								}
								break;
							}
						}
					}
				}
			}
		}
	}

	public IBlockState getCoverBlock() {
		return this.coverBlock;
	}

	public int getStartX() {
		return this.startX;
	}

	public int getStartZ() {
		return this.startZ;
	}

	public int getEndX() {
		return this.endX;
	}

	public int getEndZ() {
		return this.endZ;
	}

	public static class Builder implements IDungeonPartBuilder {

		private final int startX;
		private final int startZ;
		private final int endX;
		private final int endZ;
		private final IBlockState coverBlock;

		public Builder(int startX, int startZ, int endX, int endZ, IBlockState coverBlock) {
			this.startX = Math.min(startX, endX);
			this.startZ = Math.min(startZ, endZ);
			this.endX = Math.max(startX, endX);
			this.endZ = Math.max(startZ, endZ);
			this.coverBlock = coverBlock;
		}

		@Override
		public CoverDungeonPart build(World world, DungeonPlacement placement) {
			return new CoverDungeonPart(this.startX, this.startZ, this.endX, this.endZ, this.coverBlock);
		}

	}

}
