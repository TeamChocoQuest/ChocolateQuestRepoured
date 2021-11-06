package team.cqr.cqrepoured.structuregen.generation.part;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.generation.generatable.IGeneratable;

public abstract class DungeonPart implements IGeneratable {

	public abstract boolean isGenerated();

	public static class Registry {

		public interface ISerializer<T extends DungeonPart> {

			NBTTagCompound write(T part, NBTTagCompound compound);

			T read(World world, NBTTagCompound compound);

		}

		private static final Map<Class<? extends DungeonPart>, String> CLASS_2_ID = new HashMap<>();
		private static final Map<String, ISerializer<?>> ID_2_SERIALIZER = new HashMap<>();

		static {
			register(BlockDungeonPart.class, "dungeon_part_block", new BlockDungeonPart.Serializer());
			register(MultiBlockDungeonPart.class, "dungeon_part_block_special", new MultiBlockDungeonPart.Serializer());
			register(CoverDungeonPart.class, "dungeon_part_cover", new CoverDungeonPart.Serializer());
			register(EntityDungeonPart.class, "dungeon_part_entity", new EntityDungeonPart.Serializer());
			register(PlateauDungeonPart.class, "dungeon_part_plateau", new PlateauDungeonPart.Serializer());
		}

		private static <T extends DungeonPart> void register(Class<T> clazz, String id, ISerializer<T> serializer) {
			if (CLASS_2_ID.containsKey(clazz)) {
				throw new IllegalArgumentException("Duplicate entry for class: " + clazz.getSimpleName());
			}
			if (ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("Duplicate entry for id: " + id);
			}
			CLASS_2_ID.put(clazz, id);
			ID_2_SERIALIZER.put(id, serializer);
		}

		@SuppressWarnings("unchecked")
		public static <T extends DungeonPart> NBTTagCompound write(T part) {
			if (!CLASS_2_ID.containsKey(part.getClass())) {
				throw new IllegalArgumentException("Class not registered: " + part.getClass().getSimpleName());
			}
			NBTTagCompound compound = new NBTTagCompound();
			String id = CLASS_2_ID.get(part.getClass());
			compound.setString("id", id);
			ISerializer<T> serializer = (ISerializer<T>) ID_2_SERIALIZER.get(id);
			return serializer.write(part, compound);
		}

		public static DungeonPart read(World world, NBTTagCompound compound) {
			String id = compound.getString("id");
			if (!ID_2_SERIALIZER.containsKey(id)) {
				throw new IllegalArgumentException("No serializer registered for id: " + id);
			}
			ISerializer<?> serializer = ID_2_SERIALIZER.get(id);
			return serializer.read(world, compound);
		}

	}

}
