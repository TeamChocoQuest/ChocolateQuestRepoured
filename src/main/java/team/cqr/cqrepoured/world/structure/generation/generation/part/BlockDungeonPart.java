package team.cqr.cqrepoured.world.structure.generation.generation.part;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.BlockPlacingHelper;
import team.cqr.cqrepoured.world.structure.generation.generation.DungeonPlacement;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.generatable.IGeneratable;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;

import java.util.*;
import java.util.stream.Collectors;

public class BlockDungeonPart implements IDungeonPart, IProtectable {

	protected final List<GeneratableChunkInfo> chunks;

	protected BlockDungeonPart(Collection<GeneratableChunkInfo> chunks) {
		this.chunks = new ArrayList<>(chunks);
	}

	@Override
	public void generate(World world, GeneratableDungeon dungeon) {
		for (GeneratableChunkInfo chunk : this.chunks) {
			chunk.generate(world, dungeon);
		}
	}

	public Collection<GeneratableChunkInfo> getChunks() {
		return Collections.unmodifiableCollection(this.chunks);
	}

	@Override
	public BlockPos minPos() {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		for (GeneratableChunkInfo chunk : this.chunks) {
			for (GeneratablePosInfo block : chunk.blocks) {
				minX = Math.min(block.getX(), minX);
				minY = Math.min(block.getY(), minY);
				minZ = Math.min(block.getZ(), minZ);
			}
		}
		return new BlockPos(minX, minY, minZ);
	}

	@Override
	public BlockPos maxPos() {
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for (GeneratableChunkInfo chunk : this.chunks) {
			for (GeneratablePosInfo block : chunk.blocks) {
				maxX = Math.max(block.getX(), maxX);
				maxY = Math.max(block.getY(), maxY);
				maxZ = Math.max(block.getZ(), maxZ);
			}
		}
		return new BlockPos(maxX, maxY, maxZ);
	}

	public static class GeneratableChunkInfo implements IGeneratable {

		private final int chunkX;
		private final int chunkY;
		private final int chunkZ;
		private final List<GeneratablePosInfo> blocks;

		protected GeneratableChunkInfo(int chunkX, int chunkY, int chunkZ, Collection<GeneratablePosInfo> blocks) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.chunkZ = chunkZ;
			this.blocks = new ArrayList<>(blocks);
		}

		@Override
		public void generate(World world, GeneratableDungeon dungeon) {
			BlockPlacingHelper.setBlockStates(world, this.chunkX, this.chunkY, this.chunkZ, dungeon, (world1, chunk, blockStorage, dungeon1) -> {
				boolean flag = false;
				for (GeneratablePosInfo generatable : this.blocks) {
					flag |= generatable.place(world1, chunk, blockStorage, dungeon1);
				}
				if (flag) {
					dungeon1.mark(this.chunkX, this.chunkY, this.chunkZ);
				}
				return flag;
			});
			this.blocks.clear();
		}

		public int getChunkX() {
			return this.chunkX;
		}

		public int getChunkY() {
			return this.chunkY;
		}

		public int getChunkZ() {
			return this.chunkZ;
		}

		public Collection<GeneratablePosInfo> getBlocks() {
			return Collections.unmodifiableCollection(this.blocks);
		}

	}

	public static class Builder implements IDungeonPartBuilder {

		private static final Comparator<GeneratablePosInfo> CQR_COMPARATOR = (g1, g2) -> {
			if (g1.getChunkY() < g2.getChunkY()) {
				return -1;
			}
			if (g1.getChunkY() > g2.getChunkY()) {
				return 1;
			}
			if (g1.getChunkX() < g2.getChunkX()) {
				return -1;
			}
			if (g1.getChunkX() > g2.getChunkX()) {
				return 1;
			}
			if (g1.getChunkZ() < g2.getChunkZ()) {
				return -1;
			}
			if (g1.getChunkZ() > g2.getChunkZ()) {
				return 1;
			}

			if (g1.getY() < g2.getY()) {
				return -1;
			}
			if (g1.getY() > g2.getY()) {
				return 1;
			}
			if (g1.getX() < g2.getX()) {
				return -1;
			}
			if (g1.getX() > g2.getX()) {
				return 1;
			}
			if (g1.getZ() < g2.getZ()) {
				return -1;
			}
			if (g1.getZ() > g2.getZ()) {
				return 1;
			}
			return 0;
		};

		private final List<PreparablePosInfo> blocks = new ArrayList<>();

		public Builder add(PreparablePosInfo block) {
			this.blocks.add(block);
			return this;
		}

		public Builder addAll(Collection<? extends PreparablePosInfo> blocks) {
			this.blocks.addAll(blocks);
			return this;
		}

		@Override
		public BlockDungeonPart build(World world, DungeonPlacement placement) {
			List<GeneratablePosInfo> list = this.blocks.stream().map(preparable -> preparable.prepare(world, placement)).filter(Objects::nonNull).collect(Collectors.toList());
			list.sort(CQR_COMPARATOR);
			List<GeneratableChunkInfo> list1 = new ArrayList<>();

			for (int i = 0; i < list.size(); i++) {
				List<GeneratablePosInfo> list2 = new ArrayList<>();
				GeneratablePosInfo first = list.get(i);
				list2.add(first);

				while (i < list.size() - 1) {
					GeneratablePosInfo next = list.get(i + 1);
					if (first.getChunkX() != next.getChunkX() || first.getChunkY() != next.getChunkY() || first.getChunkZ() != next.getChunkZ()) {
						break;
					}
					list2.add(next);
					i++;
				}

				list1.add(new GeneratableChunkInfo(first.getChunkX(), first.getChunkY(), first.getChunkZ(), list2));
			}

			return new BlockDungeonPart(list1);
		}

	}

}
