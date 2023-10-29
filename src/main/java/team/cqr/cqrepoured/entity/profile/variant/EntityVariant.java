package team.cqr.cqrepoured.entity.profile.variant;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.RandomSource;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.LazyLoadField;

public class EntityVariant {
	protected final int weight;
	protected final SizeEntry size;
	protected final DamageEntry damageConfig;
	protected final List<AttributeEntry> attributes;
	protected final List<AssetEntry> assets;
	
	private final LazyLoadField<CQRWeightedRandom<AssetEntry>> assetWeightedList = new LazyLoadField<>(this::generateWeightedAssetList);
	
	public static final Codec<EntityVariant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.optionalFieldOf("weight", 0).forGetter(EntityVariant::weight),
				SizeEntry.CODEC.fieldOf("size").forGetter(EntityVariant::size),
				DamageEntry.CODEC.fieldOf("damage").forGetter(EntityVariant::damageConfig),
				AttributeEntry.CODEC.listOf().fieldOf("attributes").forGetter(EntityVariant::attributes),
				//TODO: Change to weighted list!
				AssetEntry.CODEC.listOf().fieldOf("assets").forGetter(ev -> {return ev.assets;})
				
			).apply(instance, EntityVariant::new);
	});
	
	public EntityVariant(int weight, SizeEntry size, DamageEntry damageConfig, List<AttributeEntry> attributes, List<AssetEntry> assets) {
		this.weight = weight;
		this.size = size;
		this.damageConfig = damageConfig;
		this.attributes = attributes;
		this.assets = assets;
	}
	
	private final CQRWeightedRandom<AssetEntry> generateWeightedAssetList() {
		CQRWeightedRandom<AssetEntry> result = new CQRWeightedRandom<>();
		for(AssetEntry variant : this.assets) {
			result.add(variant, variant.weight());
		}
		
		return result;
	}
	
	@Nullable
	public AssetEntry getRandomAssets() {
		return this.assetWeightedList.get().next();
	}
	
	@Nullable
	public AssetEntry getRandomAssets(final Random random) {
		return this.assetWeightedList.get().next(random);
	}
	
	@Nullable
	public AssetEntry getRandomAssets(final RandomSource random) {
		return this.assetWeightedList.get().next(random);
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
