package team.cqr.cqrepoured.entity;

import net.minecraft.util.RandomSource;

public interface ITextureVariants {

	public default int getTextureCount() {
		return 1;
	}

	public default int getTextureIndex() {
		return 0;
	}

	public default int getTextureVariant(RandomSource rng) {
		return rng.nextInt(this.getTextureCount());
	}

}
