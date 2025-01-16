package team.cqr.cqrepoured.init;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.datasync.CQRDataSerializer;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
@EventBusSubscriber(modid = CQRMain.MODID)
public class CQRSerializers {

	public static final DataSerializer<Vec3d> VEC3D = new CQRDataSerializer<Vec3d>() {
		@Override
		public void write(PacketBuffer buf, Vec3d value) {
			buf.writeDouble(value.x);
			buf.writeDouble(value.y);
			buf.writeDouble(value.z);
		}

		@Override
		public Vec3d read(PacketBuffer buf) throws IOException {
			return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
	};
//	public static final DataSerializer<TortoiseAnimation> TORTOISE_STATE = new EnumDataSerializer<>(TortoiseAnimation.class);

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
		// Create a new DataSerializerEntry (can't register the serializer directly)
		// and add it to the forge registry list so our classes can use it.
		// The register() function takes an IForgeRegistryEntry so we create that here from the DataSerializerEntry.
		event.getRegistry().registerAll(
				new DataSerializerEntry(VEC3D).setRegistryName(CQRMain.MODID, "serializerVec3d"));
//				new DataSerializerEntry(TORTOISE_STATE).setRegistryName(CQRMain.MODID, "tortoise_state"));
	}

}
