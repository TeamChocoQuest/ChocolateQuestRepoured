package team.cqr.cqrepoured.blocks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import team.cqr.cqrepoured.common.CQRepoured;

public class CQRBlocksDamageTypes {

	public static final ResourceKey<DamageType> SPIKES = create("spikes");
	
	private static ResourceKey<DamageType> create(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, CQRepoured.prefix(id));
	}
	
}
