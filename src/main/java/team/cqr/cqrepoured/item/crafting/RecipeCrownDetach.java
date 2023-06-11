package team.cqr.cqrepoured.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.entity.MobEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class RecipeCrownDetach implements Recipe<Container> {
	
	protected final ResourceLocation ID;
	public static final ResourceLocation TYPE_ID = CQRMain.prefix("crown_detach");
	
	public RecipeCrownDetach(final ResourceLocation idIn) {
		super();
		this.ID = idIn;
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.HEAD && ItemArmorCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty();
	}

	@Override
	public ItemStack assemble(Container inv) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.HEAD && ItemArmorCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		if (helmet.isEmpty()) {
			return ItemStack.EMPTY;
		}

		return ItemStack.of(helmet.getOrCreateTag().getCompound(ItemArmorCrown.NBT_KEY_CROWN));
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(Container inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.hasTag()) {
				ItemStack copy = stack.copy();
				copy.getOrCreateTag().remove(ItemArmorCrown.NBT_KEY_CROWN);
				if (copy.getOrCreateTag().isEmpty()) {
					copy.setTag(null);
				}
				ret.set(i, copy);
			}
		}
		return ret;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CQRRecipeTypes.CROWN_DETACH_SERIALIZER.get();
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return this.ID;
	}

	@Override
	public net.minecraft.world.item.crafting.RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeCrownDetach> {

		@Override
		public RecipeCrownDetach fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			return new RecipeCrownDetach(pRecipeId);
		}

		@Override
		public RecipeCrownDetach fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			return new RecipeCrownDetach(pRecipeId);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, RecipeCrownDetach pRecipe) {

		}

	}

	public static class RecipeType implements net.minecraft.world.item.crafting.RecipeType<RecipeCrownDetach> {
		@Override
		public String toString() {
			return RecipeCrownDetach.TYPE_ID.toString();
		}
	}

}
