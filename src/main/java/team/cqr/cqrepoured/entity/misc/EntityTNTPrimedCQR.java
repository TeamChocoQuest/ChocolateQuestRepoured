package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.item.TNTEntity;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityTNTPrimedCQR extends TNTEntity {

	public EntityTNTPrimedCQR(World worldIn) {
		this(CQREntityTypes.TNT_CQR.get(), worldIn);
	}
	
	public EntityTNTPrimedCQR(EntityType<EntityTNTPrimedCQR> type, World world) {
		super(type, world);
	}

	public EntityTNTPrimedCQR(World worldIn, double x, double y, double z, LivingEntity igniter) {
		super(worldIn, x, y, z, igniter);
	}

}
