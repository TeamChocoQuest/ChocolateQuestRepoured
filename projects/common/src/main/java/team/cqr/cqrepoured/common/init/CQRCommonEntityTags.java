package team.cqr.cqrepoured.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import team.cqr.cqrepoured.common.CQRepoured;

public class CQRCommonEntityTags {

	public static final TagKey<EntityType<?>> DUMMY_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, CQRepoured.prefix("dummy_entities"));
	
}
