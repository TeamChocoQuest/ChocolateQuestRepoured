package team.cqr.cqrepoured.common.entity.profile.variant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import de.dertoaster.multihitboxlib.entity.hitbox.HitboxProfile;
import de.dertoaster.multihitboxlib.util.LazyLoadField;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.common.datapack.EntityProfileDatapackRegistries;
import team.cqr.cqrepoured.common.entity.profile.variant.extradata.IVariantExtraData;

public class EntityVariant {
	protected final int weight;
	protected final SizeEntry size;
	protected final DamageEntry damageConfig;
	protected final List<AttributeEntry> attributes;
	protected final List<AssetEntry> assets;
	protected final Optional<HitboxProfile> hitboxConfig;
	// TODO: Change extra data to be a Map<ResourceLocation, Object> instead
	protected final List<IVariantExtraData<?>> extraDataList;
	protected final Map<Codec<? extends IVariantExtraData<?>>, Object> extraDataMap = new HashMap<>();
	
	private final LazyLoadField<SimpleWeightedRandomList<AssetEntry>> assetWeightedList = new LazyLoadField<>(this::generateWeightedAssetList);
	
	public static final Codec<EntityVariant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.optionalFieldOf("weight", 0).forGetter(EntityVariant::weight),
				SizeEntry.CODEC.fieldOf("size").forGetter(EntityVariant::size),
				DamageEntry.CODEC.fieldOf("damage").forGetter(EntityVariant::damageConfig),
				AttributeEntry.CODEC.listOf().fieldOf("attributes").forGetter(EntityVariant::attributes),
				AssetEntry.CODEC.listOf().fieldOf("assets").forGetter(ev -> {return ev.assets;}),
				HitboxProfile.CODEC.optionalFieldOf("hitbox").forGetter(ev -> {return ev.hitboxConfig;}),
				EntityProfileDatapackRegistries.VARIANT_EXTRA_DATA_DISPATCHER.dispatchedCodec().listOf().fieldOf("extra-data").forGetter(EntityVariant::extraDataList)
				
			).apply(instance, EntityVariant::new);
	});
	
	public EntityVariant(int weight, SizeEntry size, DamageEntry damageConfig, List<AttributeEntry> attributes, List<AssetEntry> assets, Optional<HitboxProfile> hbProfile, List<IVariantExtraData<?>> extraData) {
		this.weight = weight;
		this.size = size;
		this.damageConfig = damageConfig;
		this.attributes = attributes;
		this.assets = assets;
		this.hitboxConfig = hbProfile;
		this.extraDataList = extraData;
		
		this.extraDataList.forEach( ed -> {
				this.extraDataMap.put(ed.getType(), ed.getExtraData());
			}
		);
	}
	
	public Optional<HitboxProfile> getOptHitboxProfile() {
		return this.hitboxConfig;
	}
	
	private final SimpleWeightedRandomList<AssetEntry> generateWeightedAssetList() {
		SimpleWeightedRandomList.Builder<AssetEntry> builder = new SimpleWeightedRandomList.Builder<>();
		for(AssetEntry variant : this.assets) {
			builder.add(variant, variant.weight());
		}
		
		return builder.build();
	}
	
	@Nullable
	public AssetEntry getRandomAssets(final RandomSource random) {
		return this.assetWeightedList.get().getRandomValue(random).get();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getExtraData(final RegistryObject<Codec<? extends IVariantExtraData<? extends T>>> key) {
		T result = null;
		final Codec<? extends IVariantExtraData<? extends T>> actualKey = key.get();
		try {
			Object object = this.extraDataMap.getOrDefault(actualKey, null);
			if (object != null) {
				result = (T) object;
			}
		} catch(ClassCastException ex) {
			this.extraDataMap.remove(actualKey);
			return null;
		}
		
		return result;
	}
	
	public List<IVariantExtraData<?>> extraDataList() {
		return this.extraDataList;
	}
	
	public int getRandomAssetIndex(final RandomSource random) {
		AssetEntry randomEntry = this.getRandomAssets(random);
		if (randomEntry != null) {
			return this.assets.indexOf(randomEntry);
		}
		
		return -1;
	}
	
	public int getVariants() {
		return this.assets.size();
	}
	
	@Nullable
	public AssetEntry getAssetAt(int index) {
		if (this.assets.size() >= index || index < 0) {
			return null;
		}
		return this.assets.get(index);
	}

	public int weight() {
		return this.weight;
	}
	
	public SizeEntry size() {
		return this.size;
	}
	
	public DamageEntry damageConfig() {
		return this.damageConfig;
	}
	
	public List<AttributeEntry> attributes() {
		return attributes;
	}

	public boolean hasAssetVariants() {
		return this.assets.size() > 0;
	}
	
	@Nullable
	public AssetEntry getAssetEntry(int index) {
		return this.assets.get(index);
	}
	
}
