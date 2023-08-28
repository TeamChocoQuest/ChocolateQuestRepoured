package team.cqr.cqrepoured.util;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.registration.RegistrationIDSupplier;

public class CQRCodecJsonDataManager<T extends Object & RegistrationIDSupplier> extends CodecJsonDataManager<T> {

	protected Codec<T> byNameCodec = this.createByNameCodec();

	public CQRCodecJsonDataManager(String folderName, Codec<T> codec) {
		super(folderName, codec);
	}
	
	protected Codec<T> createByNameCodec() {
		return CQRDatapackLoaders.byNameCodec(this.getData()::get);
	}

	public CQRCodecJsonDataManager(String folderName, Codec<T> codec, Gson gson) {
		super(folderName, codec, gson);
	}
	
	public Codec<T> byNameCodec() {
		return this.byNameCodec ;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
		super.apply(jsons, resourceManager, profiler);
		
		this.data.entrySet().forEach(entry -> {
			entry.getValue().setId(entry.getKey());
		});
	};
	
}