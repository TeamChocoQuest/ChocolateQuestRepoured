package team.cqr.cqrepoured.protection.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import team.cqr.cqrepoured.common.CQRepoured;

public class CQRProtectionEntityTags {
	
	public static final TagKey<EntityType<?>> IGNORED_EXPLOSION_SOURCE = create("protection/ignored_explosion_source");
	
	private static TagKey<EntityType<?>> create(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, CQRepoured.prefix(id));
	}

}
