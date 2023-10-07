package team.cqr.cqrepoured.item.crafting;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.capability.armor.attachment.ICapabilityArmorAttachment;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.serialization.CodecUtil;

public class ItemAttachRecipe implements Recipe<CraftingContainer> {
	
	private ResourceLocation id;
	private Item attachment;
	private EquipmentSlot slot;
	private TagKey<Item> allowedBaseItems;
	
	public static final Codec<ItemAttachRecipe> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ForgeRegistries.ITEMS.getCodec().fieldOf("attachment").forGetter(obj -> obj.attachment),
				CodecUtil.EQUIPMENT_SLOT_CODEC.fieldOf("slot").forGetter(obj -> obj.slot),
				TagKey.hashedCodec(Registries.ITEM).fieldOf("allowed-bases").forGetter(obj -> obj.allowedBaseItems)
			).apply(instance, ItemAttachRecipe::new);
	});
	
	public ItemAttachRecipe(Item attachment, EquipmentSlot slot, TagKey<Item> allowedBases) {
		this.attachment = attachment;
		this.slot = slot;
		this.allowedBaseItems = allowedBases;
	}
	
	@Nullable
	protected Tuple<ItemStack, ItemStack> getTopAndBottom(CraftingContainer pContainer) {
		int topY = 0;
		ItemStack top = null;
		ItemStack bottom = null;
		int slot = 0;
		for (int y = 0; y < pContainer.getHeight(); y++) {
			for (int x = 0; x < pContainer.getWidth(); x++) {
				ItemStack item = pContainer.getItem(slot);
				@NotNull LazyOptional<ICapabilityArmorAttachment> lazyOpt = item.getCapability(CQRCapabilities.ARMOR_ATTACHMENT);
				if (top == null) {
					// If this has an attached item => continue, we can't use it!
					if (lazyOpt.isPresent()) {
						ICapabilityArmorAttachment cap = lazyOpt.resolve().get();
						if (cap != null && (cap.getAttachment() != null || !cap.getAttachment().isEmpty())) {
							continue;
						}
					}
					if (item.is(this.attachment)) {
						top = item;
					}
				} else if (topY < y && bottom == null) {
					if (item.getItem().getEquipmentSlot(item).equals(this.slot)) {
						if (item.is(this.allowedBaseItems)) {
							// Check if that item has an attachment
							// => Check for capability and it's content
							if (lazyOpt.isPresent()) {
								ICapabilityArmorAttachment cap = lazyOpt.resolve().get();
								if (cap != null && (cap.getAttachment() == null || cap.getAttachment().isEmpty())) {
									bottom = item;
								}
							}
						}
					}
				}
				if (top != null && bottom != null) {
					return new Tuple<>(top, bottom);
				}
				slot++;
			}
		}
		return null;
	}

	@Override
	public boolean matches(CraftingContainer pContainer, Level pLevel) {
		return this.getTopAndBottom(pContainer) != null;
	}

	@Override
	public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
		ItemStack result = ItemStack.EMPTY;
		Tuple<ItemStack, ItemStack> topAndBottom = this.getTopAndBottom(pContainer);
		if (topAndBottom != null) {
			result = topAndBottom.getB().copy();
			// Attach top
			@NotNull LazyOptional<ICapabilityArmorAttachment> lazyOpt = result.getCapability(CQRCapabilities.ARMOR_ATTACHMENT);
			if (lazyOpt.isPresent()) {
				ICapabilityArmorAttachment cap = lazyOpt.resolve().get();
				if (cap != null && (cap.getAttachment() == null || cap.getAttachment().isEmpty())) {
					cap.setAttachment(topAndBottom.getA().copy());
				}
			}

		}
		return result;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= 2;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
		return null;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}
	
	public void setId(ResourceLocation id) {
		if (this.id == null) {
			this.id = id;
		}
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return null;
	}
	
	public static class Serializer implements RecipeSerializer<ItemAttachRecipe> {
		
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public ItemAttachRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			DataResult<ItemAttachRecipe> dr = ItemAttachRecipe.CODEC.parse(JsonOps.INSTANCE, pSerializedRecipe);
			Optional<ItemAttachRecipe> or = dr.result();
			if (or.isPresent()) {
				or.get().setId(pRecipeId);
				return or.get();
			} 
			return null;
		}

		@Override
		public @Nullable ItemAttachRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			ItemAttachRecipe result = pBuffer.readJsonWithCodec(ItemAttachRecipe.CODEC);
			result.setId(pRecipeId);
			return result;
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, ItemAttachRecipe pRecipe) {
			pBuffer.writeJsonWithCodec(ItemAttachRecipe.CODEC, pRecipe);
		}
	}

}
