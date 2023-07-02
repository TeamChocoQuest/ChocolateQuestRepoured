package team.cqr.cqrepoured.entity.profile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.registries.ForgeRegistries;

public record EntityBaseData(
		MobType entityType
		// TODO: To be extended => intelligence of mob maybe?
	) {
	
	public static final Codec<EntityBaseData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				// TODO: Create a mixin that introduces a map that holds the mobtypes by their "id" and then create a proper codec here 
			).apply(instance, EntityBaseData::new);
	});

}
