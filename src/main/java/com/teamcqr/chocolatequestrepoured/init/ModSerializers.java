package com.teamcqr.chocolatequestrepoured.init;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;

/**
 * Copyright (c) 15 Feb 2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class ModSerializers {
	public static final DataSerializer<Vec3d> VEC3D = new DataSerializer<Vec3d>() {
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

		@Override
		public DataParameter<Vec3d> createKey(int id) {
			return new DataParameter<Vec3d>(id, this);
		}

		@Override
		public Vec3d copyValue(Vec3d value) {
			return value;
		}
	};

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
		// Create a new DataSerializerEntry (can't register the serializer directly)
		// and add it to the forge registry list so our classes can use it.
		// The register() function takes an IForgeRegistryEntry so we create that here from the DataSerializerEntry.
		event.getRegistry().register(new DataSerializerEntry(VEC3D).setRegistryName(Reference.MODID, "serializerVec3d"));
	}
}
