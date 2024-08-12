package team.cqr.cqrepoured.blocks.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.blocks.block.entity.BossBlockEntity;
import team.cqr.cqrepoured.common.services.CQRServices;

public class CQRBlocksBlockEntityTypes {

	public static final RegistryObject<BlockEntityType<BossBlockEntity>> BOSS_BLOCK = CQRServices.BLOCK_ENTITY_TYPE.registerBlockEntityType("boss_block", BlockEntityType.Builder.of(
			BossBlockEntity::new,
			CQRBlocksBlocks.BOSS.get()
		).build(null));
	
}
