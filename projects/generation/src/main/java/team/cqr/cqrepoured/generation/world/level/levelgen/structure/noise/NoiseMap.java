package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import team.cqr.cqrepoured.generation.util.SectionMap;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureSection;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise.NoiseContributionCache.NoiseConfiguration;

public class NoiseMap extends SectionMap<NoiseSection, ByteArrayTag> {

	public NoiseMap(StructureLevel level, int groundLevel, NoiseConfiguration noiseConfigNegative, NoiseConfiguration noiseConfigPositive) {
		super(level.getCenter(), new Int2ObjectOpenHashMap<>());

		BlockMap blockMap = new BlockMap(level, groundLevel);
		NoiseContributionCache noiseUtilNegative = new NoiseContributionCache(noiseConfigNegative);
		NoiseContributionCache noiseUtilPositive = new NoiseContributionCache(noiseConfigPositive);

		for (BlockSection blockSection : blockMap.getSections()) {
			SectionPos sectionPos = blockSection.getPos();
			if (sectionPos.minBlockY() < groundLevel) {
				continue;
			}
			sectionPos.blocksInside().forEach(pos -> {
				if (pos.getY() < groundLevel) {
					return;
				}
				int blockData = blockSection.get(pos.getX(), pos.getY(), pos.getZ());
				if (!BlockMap.isSet(blockData)) {
					return;
				}
				if (BlockMap.allNeighborsSet(blockData)) {
					return;
				}
				int x0 = BlockMap.isNeighborNotSet(blockData, Direction.WEST) ? -noiseConfigNegative.radiusXZ() : 0;
				int x1 = BlockMap.isNeighborNotSet(blockData, Direction.EAST) ? noiseConfigNegative.radiusXZ() : 0;
				int y0 = BlockMap.isNeighborNotSet(blockData, Direction.DOWN) ? -noiseConfigNegative.radiusDown() : 0;
				int y1 = BlockMap.isNeighborNotSet(blockData, Direction.UP) ? noiseConfigNegative.radiusUp() : 0;
				int z0 = BlockMap.isNeighborNotSet(blockData, Direction.NORTH) ? -noiseConfigNegative.radiusXZ() : 0;
				int z1 = BlockMap.isNeighborNotSet(blockData, Direction.SOUTH) ? noiseConfigNegative.radiusXZ() : 0;
				y0 = Math.max(pos.getY() + y0, groundLevel) - pos.getY();
				for (int dx = x0; dx <= x1; dx++) {
					for (int dy = y0; dy <= y1; dy++) {
						for (int dz = z0; dz <= z1; dz++) {
							double n1 = noiseUtilNegative.getBeardContribution(dx, dy, dz, pos.getY() + dy - groundLevel);
							setIfLess(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, n1);
						}
					}
				}
			});
		}

		for (StructureSection section : level.getSections()) {
			SectionPos sectionPos = section.getPos();
			if (sectionPos.minBlockY() >= groundLevel) {
				continue;
			}
			BlockSection blockSection = blockMap.getSection(sectionPos);
			if (blockSection == null) {
				continue;
			}
			sectionPos.blocksInside().forEach(pos -> {
				if (pos.getY() >= groundLevel) {
					return;
				}
				int blockData = blockSection.get(pos.getX(), pos.getY(), pos.getZ());
				if (!BlockMap.isSet(blockData)) {
					return;
				}
				if (BlockMap.allNeighborsSet(blockData)) {
					return;
				}
				int x0 = BlockMap.isNeighborNotSet(blockData, Direction.WEST) ? -noiseConfigPositive.radiusXZ() : 0;
				int x1 = BlockMap.isNeighborNotSet(blockData, Direction.EAST) ? noiseConfigPositive.radiusXZ() : 0;
				int y0 = BlockMap.isNeighborNotSet(blockData, Direction.DOWN) ? -noiseConfigPositive.radiusDown() : 0;
				int y1 = BlockMap.isNeighborNotSet(blockData, Direction.UP) ? noiseConfigPositive.radiusUp() : 0;
				int z0 = BlockMap.isNeighborNotSet(blockData, Direction.NORTH) ? -noiseConfigPositive.radiusXZ() : 0;
				int z1 = BlockMap.isNeighborNotSet(blockData, Direction.SOUTH) ? noiseConfigPositive.radiusXZ() : 0;
				y1 = Math.min(pos.getY() + y1, groundLevel - 1) - pos.getY();
				for (int dx = x0; dx <= x1; dx++) {
					for (int dy = y0; dy <= y1; dy++) {
						for (int dz = z0; dz <= z1; dz++) {
							double n1 = noiseUtilPositive.getBeardContribution(dx, dy, dz, pos.getY() + dy - groundLevel);
							setIfGreater(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, n1);
						}
					}
				}
			});
		}
	}

	public NoiseMap(CompoundTag nbt) {
		super(nbt);
	}

	@Override
	protected NoiseSection readSectionFromTag(SectionPos pos, ByteArrayTag sectionNbt) {
		return new NoiseSection(pos, sectionNbt);
	}

	@Override
	protected NoiseSection createSection(SectionPos pos) {
		return new NoiseSection(pos);
	}

	public double get(int x, int y, int z) {
		NoiseSection section = getSectionFromBlock(x, y, z);
		return section == null ? 0.0D : section.get(x, y, z);
	}

	void setIfLess(int x, int y, int z, double n) {
		getOrCreateSectionFromBlock(x, y, z).setIfLess(x, y, z, n);
	}

	void setIfGreater(int x, int y, int z, double n) {
		getOrCreateSectionFromBlock(x, y, z).setIfGreater(x, y, z, n);
	}

}
