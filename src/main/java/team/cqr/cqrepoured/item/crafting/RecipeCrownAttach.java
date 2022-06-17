package team.cqr.cqrepoured.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.item.armor.ItemCrown;

public class RecipeCrownAttach implements IRecipe<IInventory> {
	
	protected final ResourceLocation ID;
	public static final ResourceLocation TYPE_ID = CQRMain.prefix("crown_attach");
	
	public RecipeCrownAttach(final ResourceLocation idIn) {
		super();
		this.ID = idIn;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
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
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlotType.HEAD && !ItemCrown.hasCrown(stack)) {
					helmet = stack;
				} else {
					return false;
				}
			}
		}
		return !helmet.isEmpty() && !crown.isEmpty();
	}
	
	@Override
	public ItemStack assemble(IInventory inv) {
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
				} else if (helmet == ItemStack.EMPTY && stack.getMaxStackSize() == 1 && MobEntity.getEquipmentSlotForItem(stack) == EquipmentSlotType.HEAD && !ItemCrown.hasCrown(stack)) {
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
		CompoundNBT nbt = copy.getOrCreateTag();
		if (nbt == null) {
			nbt = new CompoundNBT();
			copy.setTag(nbt);
		}
		nbt.put(ItemCrown.NBT_KEY_CROWN, crown.save(new CompoundNBT()));
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth * pHeight >= 2;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
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
	public IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOptional(ID).get();
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeCrownAttach> {

		@Override
		public RecipeCrownAttach fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			return new RecipeCrownAttach(pRecipeId);
		}

		@Override
		public RecipeCrownAttach fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
			return new RecipeCrownAttach(pRecipeId);
		}

		@Override
		public void toNetwork(PacketBuffer pBuffer, RecipeCrownAttach pRecipe) {
			
		}
		
	}
	
	public static class RecipeType implements IRecipeType<RecipeCrownAttach> {
        @Override
        public String toString() {
            return RecipeCrownAttach.TYPE_ID.toString();
        }
    }

}
