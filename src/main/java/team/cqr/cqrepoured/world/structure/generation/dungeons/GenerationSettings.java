package team.cqr.cqrepoured.world.structure.generation.dungeons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GenerationSettings(PositionFinder positionFinder, DungeonInhabitantMap inhabitants, ProtectionInfo protectionInfo) {

	public static final Codec<GenerationSettings> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				PositionFinder.CODEC.fieldOf("positionFinder").forGetter(GenerationSettings::positionFinder),
				DungeonInhabitantMap.CODEC.fieldOf("inhabitants").forGetter(GenerationSettings::inhabitants),
				ProtectionInfo.CODEC.fieldOf("protectionInfo").forGetter(GenerationSettings::protectionInfo))
				.apply(instance, GenerationSettings::new);
	});

}
