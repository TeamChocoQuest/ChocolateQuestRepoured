package team.cqr.cqrepoured.world.structure.generation;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

public class StructureDungeonCQR extends Structure<NoFeatureConfig> {

	public StructureDungeonCQR(Codec<NoFeatureConfig> pCodec) {
		super(pCodec);
	}
	
	@Override
	public Decoration step() {
		Decoration.
		return super.step();
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return null;
	}

}
