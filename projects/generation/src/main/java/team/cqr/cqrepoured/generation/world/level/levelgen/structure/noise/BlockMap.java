package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.generation.util.SectionMap;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureLevel;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.StructureSection;

class BlockMap extends SectionMap<BlockSection, Tag> {

	BlockMap(StructureLevel level, int groundLevel) {
		super(level.getCenter(), new Int2ObjectOpenHashMap<>());

		for (StructureSection section : level.getSections()) {
			section.getPos().blocksInside().forEach(pos -> {
				BlockState state = section.getBlockState(pos);
				if (state == null || state.isAir()) {
					return;
				}
				setAndUpdateNeighbors(pos.getX(), pos.getY(), pos.getZ());
				if (pos.getY() == groundLevel) {
					setAndUpdateNeighbors(pos.getX(), pos.getY() - 1, pos.getZ());
				}
			});
		}
	}

	@Override
	protected BlockSection readSectionFromTag(int index, Tag sectionNbt) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected BlockSection readSectionFromTag(SectionPos pos, Tag sectionNbt) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected BlockSection createSection(SectionPos pos) {
		return new BlockSection(pos);
	}

	public static boolean isSet(int i) {
		return (i & 1) == 1;
	}

	public static boolean allNeighborsSet(int i) {
		return (i & 0b1111110) == 0b1111110;
	}

	public static boolean isNeighborNotSet(int i, Direction direction) {
		return (i & 2 << direction.ordinal()) == 0;
	}

	public int get(int x, int y, int z) {
		BlockSection section = getSectionFromBlock(x, y, z);
		return section != null ? section.get(x, y, z) : 0;
	}

	void setAndUpdateNeighbors(int x, int y, int z) {
		set(x, y, z);
		for (Direction direction : Direction.values()) {
			updateNeighbor(x, y, z, direction);
		}
	}

	void set(int x, int y, int z) {
		getOrCreateSectionFromBlock(x, y, z).set(x, y, z);
	}

	void updateNeighbor(int x, int y, int z, Direction direction) {
		setNeighbor(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ(), direction.getOpposite());
	}

	void setNeighbor(int x, int y, int z, Direction direction) {
		getOrCreateSectionFromBlock(x, y, z).setNeighbor(x, y, z, direction);
	}

}
