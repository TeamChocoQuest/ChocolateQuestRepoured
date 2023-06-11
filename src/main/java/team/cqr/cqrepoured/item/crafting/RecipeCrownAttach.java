package team.cqr.cqrepoured.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.item.armor.ItemArmorCrown;

public class RecipeCrownAttach implements Recipe<Container> {
	
	protected final ResourceLocation ID;
	public static final ResourceLocation TYPE_ID = CQRMain.prefix("crown_attach");
	
	public RecipeCrownAttach(final ResourceLocation idIn) {
		super();
		this.ID = idIn;
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		ItemStack helmet = ItemStack.EMPTY;
		ItemStack crown = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == CQRItems.KING_CROWN.get()) {
					if (crown == ItemStack.EMPTY) {
						crown = stack;
					} else {
						return false;
					}
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.HEAD && !ItemArmorCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty() && !crown.isEmpty();
	}
	
	@Override
	public ItemStack assemble(Container inv) {
		ItemStack helmet = ItemStack.EMPTY;
		ItemStack crown = ItemStack.EMPTY;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == CQRItems.KING_CROWN.get()) {
					if (crown == ItemStack.EMPTY) {
						crown = stack;
					} else {
						return ItemStack.EMPTY;
					}
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlot.HEAD && !ItemArmorCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
		if (helmet.isEmpty() || crown.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = helmet.copy();
		CompoundTag nbt = copy.getOrCreateTag();
		if (nbt == null) {
			nbt = new CompoundTag();
			copy.setTag(nbt);
		}
		nbt.put(ItemArmorCrown.NBT_KEY_CROWN, crown.save(new CompoundTag()));
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CQRRecipeTypes.CROWN_ATTACH_SERIALIZER.get();
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
		return Registry.RECIPE_TYPE.getOptional(ID).get();
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeCrownAttach> {

		@Override
		public RecipeCrownAttach fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			return new RecipeCrownAttach(pRecipeId);
		}

		@Override
		public RecipeCrownAttach fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			return new RecipeCrownAttach(pRecipeId);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, RecipeCrownAttach pRecipe) {
			
		}
		
	}
	
	public static class RecipeType implements net.minecraft.world.item.crafting.RecipeType<RecipeCrownAttach> {
        @Override
        public String toString() {
            return RecipeCrownAttach.TYPE_ID.toString();
        }
    }

}
