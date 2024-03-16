package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.Tag;
import team.cqr.cqrepoured.generation.util.Section;
import team.cqr.cqrepoured.generation.util.SectionUtil;

class BlockSection extends Section<Tag> {

	private final byte[] blockData = new byte[16 * 16 * 16];

	public BlockSection(SectionPos pos) {
		super(pos);
	}
	
	@Override
	public Tag save() {
		throw new UnsupportedOperationException();
	}

	public int get(int x, int y, int z) {
		return blockData[SectionUtil.index(x, y, z)];
	}

	void set(int x, int y, int z) {
		blockData[SectionUtil.index(x, y, z)] |= 1;
	}

	void setNeighbor(int x, int y, int z, Direction direction) {
		blockData[SectionUtil.index(x, y, z)] |= 2 << direction.ordinal();
	}

}
