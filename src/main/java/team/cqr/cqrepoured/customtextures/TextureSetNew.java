package team.cqr.cqrepoured.customtextures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import de.dertoaster.multihitboxlib.util.LazyLoadField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.util.CQRWeightedRandom;

public class TextureSetNew {

	public static final String RES_LOC_DOMAIN_CQR_ASSET_SYNCH = "";
	
	private Map<EntityType<?>, CQRWeightedRandom<ResourceLocation>> entries;
	private final LazyLoadField<List<ResourceLocation>> ctsTextures = new LazyLoadField<>(() -> {
		Set<ResourceLocation> result = new HashSet<>();
		
		TextureSetNew.this.entries().entrySet().forEach(entry -> {
			entry.getValue().getEntries().forEach(wo -> {
				ResourceLocation rs = wo.object();
				if (rs.getNamespace().equalsIgnoreCase(RES_LOC_DOMAIN_CQR_ASSET_SYNCH)) {
					result.add(rs);
				}
			});
		});
		
		return new ArrayList<>(result);
	});
	
	public static final Codec<TextureSetNew> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.unboundedMap(ForgeRegistries.ENTITY_TYPES.getCodec(), CQRWeightedRandom.createCodec(ResourceLocation.CODEC)).fieldOf("entries").forGetter(TextureSetNew::entries)
		).apply(instance, TextureSetNew::new);
	});
	
	public TextureSetNew(Map<EntityType<?>, CQRWeightedRandom<ResourceLocation>> entries) {
		this.entries = entries;
	}
	
	public Map<EntityType<?>, CQRWeightedRandom<ResourceLocation>> entries() {
		return this.entries;
	}

	@Nullable
	public ResourceLocation getRandomTextureFor(Entity ent) {
		if (!this.entries.containsKey(ent.getType())) {
			return null;
		}
		
		return this.entries.get(ent.getType()).next();
	}
	
	// For MHLib asset synchers => will grab those and search for the files to synch
	public List<ResourceLocation> getCTSTextures() {
		return this.ctsTextures.get();
	}
	
	public static ResourceLocation prefixAssetSynch(final String path) {
		return new ResourceLocation(RES_LOC_DOMAIN_CQR_ASSET_SYNCH, path);
	}
	public static ResourceLocation prefixAssetSynch(final ResourceLocation id) {
		return prefixAssetSynch(id.getPath());
	}

}
