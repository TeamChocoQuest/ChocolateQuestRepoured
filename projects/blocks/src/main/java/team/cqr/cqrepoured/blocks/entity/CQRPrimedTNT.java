package team.cqr.cqrepoured.blocks.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.blocks.init.CQRBlocksEntityTypes;

public class CQRPrimedTNT extends PrimedTnt {

	public CQRPrimedTNT(Level worldIn) {
		this(CQRBlocksEntityTypes.CQR_PRIMED_TNT.get(), worldIn);
	}
	
	public CQRPrimedTNT(EntityType<CQRPrimedTNT> type, Level world) {
		super(type, world);
	}

	public CQRPrimedTNT(Level worldIn, double x, double y, double z, LivingEntity igniter) {
		super(worldIn, x, y, z, igniter);
	}
	
}
