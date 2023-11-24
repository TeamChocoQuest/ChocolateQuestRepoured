package team.cqr.cqrepoured.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import team.cqr.cqrepoured.CQRMain;

public class CQRDamageTypes {
	
	public static final ResourceKey<DamageType> LASER = register("laser");
	public static final ResourceKey<DamageType> ELECTRIC = register("electric");
			
	protected static ResourceKey<DamageType> register(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, CQRMain.prefix("damage-type/" + id));
	}

}
