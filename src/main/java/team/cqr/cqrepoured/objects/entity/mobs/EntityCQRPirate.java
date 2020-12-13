package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQRPirate extends AbstractEntityCQR {

	public EntityCQRPirate(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Pirate;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PIRATE;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_PIRATE;
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

}
