package net.minecraft.entity;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;

public class EntityList {

	@Nullable
	public static Entity createEntityFromNBT(CompoundTag tag, Level worldIn) {
		ResourceLocation resourcelocation = new ResourceLocation(tag.getString("id"));
		
		Entity entity = createEntityByIDFromName(resourcelocation, worldIn);

        if (entity == null)
        {
            CQRMain.logger.warn("Skipping Entity with id {}", (Object)resourcelocation);
        }
        else
        {
            try
            {
            entity.load(tag);
            }
            catch (Exception e)
            {
               CQRMain.logger.error("An Entity {}({}) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        tag.getString("id"), entity.getName(), e);
                entity = null;
            }
        }

        return entity;
	}
	
	public static Entity createEntityByIDFromName(ResourceLocation id, Level world) {
		return ForgeRegistries.ENTITY_TYPES.getValue(id).create(world);
	}

	public static ResourceLocation getKey(Entity ent) {
		return EntityType.getKey(ent.getType());
	}

}
