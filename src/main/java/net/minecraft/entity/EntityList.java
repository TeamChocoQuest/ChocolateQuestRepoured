package net.minecraft.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityList {

	@Nullable
	public static Entity createEntityFromNBT(CompoundNBT tag, World worldIn) {
		try {
			Entity ent = createEntityByIDFromName(null, worldIn)
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Entity createEntityByIDFromName(ResourceLocation id, World world) {
		return ForgeRegistries.ENTITIES.getValue(id).create(world);
	}

	public static ResourceLocation getKey(Entity ent) {
		return ent.getType().getRegistryName();
	}

}
