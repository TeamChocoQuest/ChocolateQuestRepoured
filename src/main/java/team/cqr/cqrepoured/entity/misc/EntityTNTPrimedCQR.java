package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;

public class EntityTNTPrimedCQR extends EntityTNTPrimed {

	public EntityTNTPrimedCQR(World worldIn) {
		super(worldIn);
	}

	public EntityTNTPrimedCQR(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
		super(worldIn, x, y, z, igniter);
	}

}
