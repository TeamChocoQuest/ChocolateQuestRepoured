package team.cqr.cqrepoured.world.structure.generation.generation.part;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEmptyInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

public class PlateauDungeonPart implements IDungeonPart {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private final int startX;
	private final int startZ;
	private final int endX;
	private final int endY;
	private final int endZ;
	private final int wallSize;
	private final IBlockState supportHillBlock;
	private final IBlockState supportHillTopBlock;
	private final int[][] ground;

	protected PlateauDungeonPart(int startX, int startZ, int endX, int endY, int endZ, int wallSize, @Nullable IBlockState supportHillBlock,
			@Nullable IBlockState supportHillTopBlock, int[][] ground) {
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
				MUTABLE.setPos(x, 0, z);
				IBlockState state1 = this.supportHillBlock;
				IBlockState state2 = this.supportHillTopBlock;
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
					BlockPlacingHelper.setBlockState(world, MUTABLE, state1, null, 16, dungeon);
					dungeon.mark(MUTABLE.getX() >> 4, MUTABLE.getY() >> 4, MUTABLE.getZ() >> 4);
					MUTABLE.setY(MUTABLE.getY() + 1);
				}
				if (MUTABLE.getY() < end) {
					BlockPlacingHelper.setBlockState(world, MUTABLE, state2, null, 16, dungeon);
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
				int x2 = Math.abs(x1);
				if (x2 > 0) x2--;
				int z2 = Math.abs(z1);
				if (z2 > 0) z2--;
				double dist = Math.sqrt(x2 * x2 + z2 * z2);
				int y1 = (int) Math.round(y + (this.ground[x + x1 - this.startX][z + z1 - this.startZ] - y) * Math.max((1 - dist / this.wallSize), 0));
				if (y1 > max) {
					max = y1;
				}
			}
		}
		return max;
	}

	private static boolean isGround(World world, Chunk chunk, BlockPos pos) {
		IBlockState state = chunk.getBlockState(pos);
		Material material = state.getMaterial();
		return material.blocksMovement() && material != Material.WOOD && material != Material.LEAVES && material != Material.PLANTS;
	}

	//TODO: Adjust in 1.17+ to automatically adapt to the world!
	public static final int LOWEST_WORLD_Y = 0;
	
	private static int getHeight(World world, int x, int y, int z) {
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		MUTABLE.setPos(x, y, z);
		boolean upwards = isGround(world, chunk, MUTABLE);

		while (MUTABLE.getY() >= LOWEST_WORLD_Y) {
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
		return MUTABLE.getY();
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
		private IBlockState supportHillBlock;
		private IBlockState supportHillTopBlock;
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
					this.ground[i][j] = endY + 1;
				}
			}
		}

		public Builder setSupportHillBlock(@Nullable IBlockState state) {
			this.supportHillBlock = state;
			return this;
		}

		public Builder setSupportHillTopBlock(@Nullable IBlockState state) {
			this.supportHillTopBlock = state;
			return this;
		}

		public void markGround(CQStructure structure, BlockPos pos, Mirror mirror, Rotation rotation) {
			List<PreparablePosInfo> blocks = structure.getBlockInfoList();
			BlockPos size = structure.getSize();
			BlockPos offset = Offset.NORTH_EAST.apply(BlockPos.ORIGIN, structure, mirror, rotation);
			int offsetX = offset.getX() == 0 ? 0 : offset.getX() - 1;
			int offsetZ = offset.getZ() == 0 ? 0 : offset.getZ() - 1;
			for (int x = 0; x < structure.getSize().getX(); x++) {
				for (int z = 0; z < structure.getSize().getZ(); z++) {
					MutableBlockPos transformed = DungeonPlacement.transform(x, 0, z, mirror, rotation);
					int x1 = transformed.getX();
					int z1 = transformed.getZ();
					if (x1 + pos.getX() < this.startX || x1 + pos.getX() > this.endX) {
						continue;
					}
					if (z1 + pos.getZ() < this.startZ || z1 + pos.getZ() > this.endZ) {
						continue;
					}
					int y = Math.min(this.endY + 1 - pos.getY(), size.getY() - 1);
					while (y >= 0 && blocks.get((x * size.getY() + y) * size.getZ() + z) instanceof PreparableEmptyInfo) {
						y--;
					}
					if (y < 0) {
						this.ground[offsetX + x1][offsetZ + z1] = -1;
					} else {
						this.ground[offsetX + x1][offsetZ + z1] = Math.min(pos.getY() + y + 2, this.endY + 1);
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
