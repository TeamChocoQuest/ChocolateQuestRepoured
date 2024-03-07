package team.cqr.cqrepoured.common.datapack;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.common.registration.RegistrationIDSupplier;

public interface DatapackLoaderHelper {
	
	/* Access methods */
	public static <T> Optional<T> getValueGeneral(final CodecJsonDataManager<T> manager, final ResourceLocation id) {
		return Optional.ofNullable(manager.getData().getOrDefault(id, null));
	}
	
	/* Access codecs */
	public static <T extends RegistrationIDSupplier> Codec<T> byNameCodec(final Function<ResourceLocation, T> retrievalFunction) {
		return byNameCodecFor(T::getId, retrievalFunction);
	}
	
	public static <T> Codec<T> byNameCodecFor(final Function<T, ResourceLocation> idRetrievalFunction, final Function<ResourceLocation, T> retrievalFunction) {
		return ResourceLocation.CODEC.flatXmap((rs) -> {
			return Optional.ofNullable(retrievalFunction.apply(rs)).map(DataResult::success).orElseGet(() -> {
				return DataResult.error(() -> {
					return "Unknown element id:" + rs.toString();
				});
			});
		}, (obj) -> {
			return Optional.ofNullable(idRetrievalFunction.apply(obj)).map(DataResult::success).orElseGet(() -> {
				return DataResult.error(() -> {
					return "Element with unknown id:" + obj.toString();
				});
			});
		});
	}
	
}
