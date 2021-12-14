package team.cqr.cqrepoured.entity;

import java.util.Random;

public interface ITextureVariants {
	
	public default int getTextureCount() {
		return 1;
	}
	public default int getTextureIndex() {
		return 0;
	}
	
	public default int getTextureVariant(Random rng) {
		return rng.nextInt(this.getTextureCount());
	}

}
