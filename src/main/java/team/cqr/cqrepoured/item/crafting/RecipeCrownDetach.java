package team.cqr.cqrepoured.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class RecipeCrownDetach implements IRecipe<IInventory> {
	
	protected final ResourceLocation ID;
	public static final ResourceLocation TYPE_ID = CQRMain.prefix("crown_detach");
	
	public RecipeCrownDetach(final ResourceLocation idIn) {
		super();
		this.ID = idIn;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlotType.HEAD && ItemArmorCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty();
	}

	@Override
	public ItemStack assemble(IInventory inv) {
		ItemStack helmet = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (helmet == ItemStack.EMPTY && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlotType.HEAD && ItemArmorCrown.hasCrown(stack)) {
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
	public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
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
	public IRecipeSerializer<?> getSerializer() {
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
	public IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeCrownDetach> {

		@Override
		public RecipeCrownDetach fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			return new RecipeCrownDetach(pRecipeId);
		}

		@Override
		public RecipeCrownDetach fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
			return new RecipeCrownDetach(pRecipeId);
		}

		@Override
		public void toNetwork(PacketBuffer pBuffer, RecipeCrownDetach pRecipe) {

		}

	}

	public static class RecipeType implements IRecipeType<RecipeCrownDetach> {
		@Override
		public String toString() {
			return RecipeCrownDetach.TYPE_ID.toString();
		}
	}

}
