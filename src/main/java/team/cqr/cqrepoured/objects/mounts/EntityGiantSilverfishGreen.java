package team.cqr.cqrepoured.objects.mounts;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.objects.entity.bases.EntityCQRGiantSilverfishBase;

public class EntityGiantSilverfishGreen extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishGreen(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GIANT_SILVERFISH_GREEN;
	}

}
