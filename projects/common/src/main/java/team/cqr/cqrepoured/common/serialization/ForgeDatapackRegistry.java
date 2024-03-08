package team.cqr.cqrepoured.common.serialization;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.common.CQRepoured;

public record ForgeDatapackRegistry<T>(
		ResourceKey<Registry<T>> registryKey,
		Codec<T> objectCodec,
		Codec<Holder<T>> registryCodec
	) {
	
	public ForgeDatapackRegistry(final String datapackPath, final Codec<T> codec) {
		this(CQRepoured.prefix(datapackPath), codec);
	}
	
	public ForgeDatapackRegistry(final ResourceLocation id, final Codec<T> codec) {
		this(ResourceKey.createRegistryKey(id), codec);
	}
	
	public ForgeDatapackRegistry(final ResourceKey<Registry<T>> resourceKey, final Codec<T> codec) {
		this(resourceKey, codec, RegistryFileCodec.create(resourceKey, codec));
	}

}
