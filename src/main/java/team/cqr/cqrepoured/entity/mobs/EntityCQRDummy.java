package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;

public class EntityCQRDummy extends AbstractEntityCQR {

	public EntityCQRDummy(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Dummy;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.ALL_ALLY;
	}

	@Override
	protected void registerGoals() {

	}

	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	
}
