package team.cqr.cqrepoured.init;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataSerializerEntry;
import team.cqr.cqrepoured.CQRMain;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CQRSerializers {

	public static final EntityDataSerializer<Vec3> VEC3D = new EntityDataSerializer<Vec3>() {
		@Override
		public void write(FriendlyByteBuf buf, Vec3 value) {
			buf.writeDouble(value.x);
			buf.writeDouble(value.y);
			buf.writeDouble(value.z);
		}

		@Override
		public Vec3 read(FriendlyByteBuf buf) {
			return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
		
		@Override
		public EntityDataAccessor<Vec3> createAccessor(int id) {
			return new EntityDataAccessor<>(id, this);
		}

		@Override
		public Vec3 copy(Vec3 value) {
			return value;
		}
	};
	
	static boolean registered = false;

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
		//Yes, this is sadly necessary. Without it the event handler got called two times for me
		if(registered) {
			return;
		}
		// Create a new DataSerializerEntry (can't register the serializer directly)
		// and add it to the forge registry list so our classes can use it.
		// The register() function takes an IForgeRegistryEntry so we create that here from the DataSerializerEntry.
		event.getRegistry().register(new DataSerializerEntry(VEC3D).setRegistryName(CQRMain.MODID, "serializer_vector_double"));
		registered = true;
	}

}
