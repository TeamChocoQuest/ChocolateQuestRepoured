package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQRTriton extends AbstractEntityCQR {

	public EntityCQRTriton(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Triton;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.TRITONS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_TRITON;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public int getTextureCount() {
		return 2;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.0F;
	}

}
