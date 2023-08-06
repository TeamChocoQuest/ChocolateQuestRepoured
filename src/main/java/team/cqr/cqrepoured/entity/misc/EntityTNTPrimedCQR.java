package team.cqr.cqrepoured.entity.misc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityTNTPrimedCQR extends PrimedTnt {

	public EntityTNTPrimedCQR(Level worldIn) {
		this(CQREntityTypes.TNT_CQR.get(), worldIn);
	}
	
	public EntityTNTPrimedCQR(EntityType<EntityTNTPrimedCQR> type, Level world) {
		super(type, world);
	}

	public EntityTNTPrimedCQR(Level worldIn, double x, double y, double z, LivingEntity igniter) {
		super(worldIn, x, y, z, igniter);
	}

}
