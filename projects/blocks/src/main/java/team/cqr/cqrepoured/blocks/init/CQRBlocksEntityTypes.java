package team.cqr.cqrepoured.blocks.init;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.blocks.entity.CQRPrimedTNT;
import team.cqr.cqrepoured.common.services.CQRServices;

public class CQRBlocksEntityTypes {

	public static final RegistryObject<EntityType<CQRPrimedTNT>> CQR_PRIMED_TNT = CQRServices.ENTITY_TYPE.registerSized(CQRPrimedTNT::new, "cqr_primed_tnt", 1.0F, 1.0F, 3);
	
}
