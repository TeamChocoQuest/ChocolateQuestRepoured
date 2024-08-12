package team.cqr.cqrepoured.common.services.interfaces;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public interface BlockEntityService {

	public <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(String name, BlockEntityType<T> type);
	
}
