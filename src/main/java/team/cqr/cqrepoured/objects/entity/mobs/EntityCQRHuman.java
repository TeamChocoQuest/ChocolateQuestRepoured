package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

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
