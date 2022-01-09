package team.cqr.cqrepoured.world.structure.generation.generation.part;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEmptyInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class PlateauDungeonPart implements IDungeonPart {

	private static final BlockPos.Mutable MUTABLE = new BlockPos.Mutable();
	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endY;
	private final int endZ;
	private final int wallSize;
	private final BlockState supportHillBlock;
	private final BlockState supportHillTopBlock;
	private final int[][] ground;

	protected PlateauDungeonPart(int startX, int startZ, int endX, int endY, int endZ, int wallSize, @Nullable BlockState supportHillBlock,
                                 @Nullable BlockState supportHillTopBlock, int[][] ground) {
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
		this.wallSize = wallSize;
		this.supportHillBlock = supportHillBlock;
		this.supportHillTopBlock = supportHillTopBlock;
		this.ground = ground;
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		for (int x = this.startX - this.wallSize; x <= this.endX + this.wallSize; x++) {
			for (int z = this.startZ - this.wallSize; z <= this.endZ + this.wallSize; z++) {
				MUTABLE.set(x, 0, z);
				BlockState state1 = this.supportHillBlock;
				BlockState state2 = this.supportHillTopBlock;
				if (state1 == null || state2 == null) {
					Biome biome = world.getBiome(MUTABLE);
					if (state1 == null) {
						state1 = biome.fillerBlock;
					}
					if (state2 == null) {
						state2 = biome.topBlock;
					}
				}

				int y = getHeight(world, x, this.endY + 1, z);
				int end = this.interpolatedHeight(x, y, z);

				MUTABLE.setY(y);
				while (MUTABLE.getY() < end - 1) {
					BlockPlacingHelper.setBlockState(world, MUTABLE, state1, null, 16, false);
					dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
					MUTABLE.setY(MUTABLE.getY() + 1);
				}
				if (MUTABLE.getY() < end) {
					BlockPlacingHelper.setBlockState(world, MUTABLE, state2, null, 16, false);
					dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
				}
			}
		}
	}

	private int interpolatedHeight(int x, int y, int z) {
		int max = y;
		int r = this.wallSize + 1;
		for (int x1 = -r; x1 <= r; x1++) {
			if (x + x1 < this.startX || x + x1 > this.endX) {
				continue;
			}
			for (int z1 = -r; z1 <= r; z1++) {
				if (z + z1 < this.startZ || z + z1 > this.endZ) {
					continue;
				}
				double dist = Math.sqrt(x1 * x1 + z1 * z1);
				int y1 = (int) Math.round(y + (this.ground[x + x1 - this.startX][z + z1 - this.startZ] - y) * Math.max((1 - dist / this.wallSize), 0));
				if (y1 > max) {
					max = y1;
				}
			}
		}
		return max;
	}

	private static boolean isGround(World world, Chunk chunk, BlockPos pos) {
		BlockState state = chunk.getBlockState(pos);
		Material material = state.getMaterial();
		return material.blocksMotion() && material != Material.WOOD && material != Material.LEAVES && material != Material.PLANT;
	}

	private static int getHeight(World world, int x, int y, int z) {
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		MUTABLE.set(x, y, z);
		boolean upwards = isGround(world, chunk, MUTABLE);

		while (true) {
			boolean isGround = isGround(world, chunk, MUTABLE);
			if (upwards) {
				if (!isGround) {
					return MUTABLE.getY();
				}
				MUTABLE.setY(MUTABLE.getY() + 1);
			} else {
				if (isGround) {
					return MUTABLE.getY() + 1;
				}
				MUTABLE.setY(MUTABLE.getY() - 1);
			}
		}
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

	public int getEndY() {
		return this.endY;
	}

	public int getEndZ() {
		return this.endZ;
	}

	public static class Builder implements IDungeonPartBuilder {

		private final int startX;
		private final int startZ;
		private final int endX;
		private final int endY;
		private final int endZ;
		private final int wallSize;
		private BlockState supportHillBlock;
		private BlockState supportHillTopBlock;
		private final int[][] ground;

		public Builder(int startX, int startZ, int endX, int endY, int endZ, int wallSize) {
			this.startX = Math.min(startX, endX);
			this.startZ = Math.min(startZ, endZ);
			this.endX = Math.max(startX, endX);
			this.endY = endY;
			this.endZ = Math.max(startZ, endZ);
			this.wallSize = wallSize;
			this.ground = new int[this.endX - this.startX + 1][this.endZ - this.startZ + 1];
			for (int i = 0; i < this.ground.length; i++) {
				for (int j = 0; j < this.ground[i].length; j++) {
					this.ground[i][j] = endY;
				}
			}
		}

		public Builder setSupportHillBlock(@Nullable BlockState state) {
			this.supportHillBlock = state;
			return this;
		}

		public Builder setSupportHillTopBlock(@Nullable BlockState state) {
			this.supportHillTopBlock = state;
			return this;
		}

		public void markGround(CQStructure structure, BlockPos pos) {
			List<PreparablePosInfo> blocks = structure.getBlockInfoList();
			BlockPos size = structure.getSize();
			for (int x = 0; x < structure.getSize().getX(); x++) {
				if (x + pos.getX() < this.startX || x + pos.getX() > this.endX) {
					continue;
				}
				for (int z = 0; z < structure.getSize().getZ(); z++) {
					if (z + pos.getZ() < this.startZ || z + pos.getZ() > this.endZ) {
						continue;
					}
					int y = Math.min(this.endY + 1 - pos.getY(), size.getY() - 1);
					while (y >= 0 && blocks.get((x * size.getY() + y) * size.getZ() + z) instanceof PreparableEmptyInfo) {
						y--;
					}
					if (y < 0) {
						this.ground[x][z] = -1;
					} else {
						this.ground[x][z] = Math.min(pos.getY() + y + 2, this.endY + 1);
					}
				}
			}
		}

		@Override
		public PlateauDungeonPart build(World world, DungeonPlacement placement) {
			return new PlateauDungeonPart(this.startX, this.startZ, this.endX, this.endY, this.endZ, this.wallSize, this.supportHillBlock,
					this.supportHillTopBlock, this.ground);
		}

	}

}
