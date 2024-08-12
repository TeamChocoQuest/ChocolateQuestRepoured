package team.cqr.cqrepoured.common.services.interfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraftforge.registries.RegistryObject;

public interface EntityTypeService {

	public <T extends Entity>  RegistryObject<EntityType<T>> registerSized(EntityFactory<T> factory, final String entityName, float width, float height, int updateInterval);

}
