package team.cqr.cqrepoured.common.services.interfaces;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public interface BlockService {

	public <T extends Block> RegistryObject<T> registerBlock(String id, Supplier<T> blockSupplier);
	
}
