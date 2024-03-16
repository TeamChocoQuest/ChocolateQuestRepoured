package team.cqr.cqrepoured.generation.util;

import net.minecraft.core.SectionPos;
import net.minecraft.nbt.Tag;

public abstract class Section<T extends Tag> {

	private final SectionPos pos;

	public Section(SectionPos pos) {
		this.pos = pos;
	}

	public abstract T save();

	public SectionPos getPos() {
		return pos;
	}

}
