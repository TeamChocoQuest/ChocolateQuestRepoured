package team.cqr.cqrepoured.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.item.ItemStack;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public abstract class ItemMagazineBased extends ItemLore {

	public static final String CONSTANT_AMMO_NBT_KEY = "cqr_magazine_item_ammo";

	protected final Predicate<ItemStack> predicateAmmo;

	public ItemMagazineBased(Properties itemProperties, Predicate<ItemStack> fuelPredicate) {
		super(itemProperties);

		this.predicateAmmo = fuelPredicate;
	}

	public abstract int getMaxAmmo();

	protected abstract int getMaxProcessedItemsPerReloadCycle();

	protected abstract int getAmmoForSingleAmmoItem(ItemStack ammoItem);

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}
	
	public boolean hasMagazineTag(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}
		return stack.hasTag() && stack.getTag().contains(CONSTANT_AMMO_NBT_KEY, Constants.NBT.TAG_INT);
	}

	public void removeAmmoFromItem(ItemStack stack, int amount) {
		this.setAmmo(stack, this.getAmmoInItem(stack) - amount);
	}

	public int getAmmoInItem(ItemStack stack) {
		if (this.hasMagazineTag(stack)) {
			return stack.getTag().getInt(CONSTANT_AMMO_NBT_KEY);
		}
		return 0;
	}

	public void addAmmoToItem(ItemStack stack, int amount) {
		this.setAmmo(stack, amount + getAmmoInItem(stack));
	}

	public void setAmmo(ItemStack stack, int amount) {
		amount = amount < 0 ? 0 : amount;
		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}
		stack.getTag().putInt(CONSTANT_AMMO_NBT_KEY, amount);
	}

	public float getAmmoInItemInPercent(ItemStack stack) {
		return (float) this.getAmmoInItem(stack) / (float) this.getMaxAmmo();
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1.0D - this.getAmmoInItemInPercent(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRgb(this.getAmmoInItemInPercent(stack) / 3.0F, 1.0F, 1.0F);
	}

	public List<ItemStack> getAmmoItemsInInventory(PlayerInventory playerInventory) {
		List<ItemStack> result = new ArrayList<>();
		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack stack = playerInventory.getItem(i);
			if (this.isValidAmmo(stack)) {
				result.add(stack);
			}
		}
		return result;
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack def = super.getDefaultInstance();

		if (!def.hasTag()) {
			def.setTag(new CompoundNBT());
		}

		this.setAmmo(def, this.getMaxAmmo());

		return def;
	}

	protected void reloadFromInventory(PlayerInventory playerInventory, ItemStack stack, boolean removeItems) {
		this.reloadFromAmmoItems(playerInventory, this.getAmmoItemsInInventory(playerInventory), stack, removeItems);
	}

	protected void reloadFromAmmoItems(PlayerInventory playerInventory, List<ItemStack> fuelItems, ItemStack stack, boolean removeItems) {
		if (fuelItems.isEmpty()) {
			return;
		}
		int freeFuel = this.getMaxAmmo() - this.getAmmoInItem(stack);
		int refueled = 0;
		int processedItems = 0;
		for (int itemIndx = 0; itemIndx < fuelItems.size(); itemIndx++) {
			ItemStack fuel = fuelItems.get(itemIndx);
			final int stackSize = fuel.getCount();
			for (int i = 0; i < stackSize; i++) {
				int fc = this.getAmmoForSingleAmmoItem(fuel);
				if (fc > freeFuel) {
					continue;
				} else {
					freeFuel -= fc;
					refueled += fc;
					if (removeItems) {
						fuel.shrink(1);
					}
				}
				processedItems++;
				// Exit the loop when too many items were processed
				if (processedItems >= this.getMaxProcessedItemsPerReloadCycle()) {
					break;
				}
			}
			if (fuel.isEmpty() && removeItems) {
				playerInventory.removeItem(fuel);
			}
			// Exit the loop when too many items were processed
			if (processedItems >= this.getMaxProcessedItemsPerReloadCycle()) {
				break;
			}
		}

		this.addAmmoToItem(stack, refueled);
	}

	public boolean isValidAmmo(ItemStack item) {
		return this.predicateAmmo.test(item);
	}

	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return this.getAmmoInItem(newStack) > 0;
	}

}
