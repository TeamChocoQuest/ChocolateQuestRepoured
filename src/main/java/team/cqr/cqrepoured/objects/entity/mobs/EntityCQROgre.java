package team.cqr.cqrepoured.objects.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.factions.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CQRConfig;

public class EntityCQROgre extends AbstractEntityCQR {

	public EntityCQROgre(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Ogre;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_OGRE;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

}
