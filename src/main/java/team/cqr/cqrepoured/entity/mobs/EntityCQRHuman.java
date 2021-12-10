package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRHuman extends AbstractEntityCQR {

	public EntityCQRHuman(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_HUMAN;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Human;
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PLAYERS;
	}

	@Override
	public int getTextureCount() {
		return 10;
	}

}
