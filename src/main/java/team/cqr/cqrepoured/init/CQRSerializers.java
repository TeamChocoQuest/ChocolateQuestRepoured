package team.cqr.cqrepoured.init;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import team.cqr.cqrepoured.CQRMain;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
//@EventBusSubscriber(modid = CQRMain.MODID)
public class CQRSerializers {

	public static final IDataSerializer<Vector3d> VEC3D = new IDataSerializer<Vector3d>() {
		@Override
		public void write(PacketBuffer buf, Vector3d value) {
			buf.writeDouble(value.x);
			buf.writeDouble(value.y);
			buf.writeDouble(value.z);
		}

		@Override
		public Vector3d read(PacketBuffer buf) {
			return new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
		
		@Override
		public DataParameter<Vector3d> createAccessor(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public Vector3d copy(Vector3d value) {
			return value;
		}
	};

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
		// Create a new DataSerializerEntry (can't register the serializer directly)
		// and add it to the forge registry list so our classes can use it.
		// The register() function takes an IForgeRegistryEntry so we create that here from the DataSerializerEntry.
		event.getRegistry().register(new DataSerializerEntry(VEC3D).setRegistryName(CQRMain.MODID, "serializerVec3d"));
	}

}
