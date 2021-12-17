package team.cqr.cqrepoured.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

public abstract class ItemFuelUsing extends Item {
	
	public ItemFuelUsing(Predicate<ItemStack> fuelPredicate) {
		super();
		
		this.predicateValidFuel = fuelPredicate;
		this.setMaxStackSize(1);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}
	
	public boolean hasFuelTag(ItemStack stack) {
		if(stack == null) {
			return false;
		}
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("cqr_fuel_item_fuel", Constants.NBT.TAG_INT);
	}
	
	protected final Predicate<ItemStack> predicateValidFuel;

	public abstract int getMaxFuel();
	
	public void removeFuel(ItemStack stack, int amount) {
		this.setFuel(stack, this.getFuelInItem(stack) - amount);
	}
	
	public int getFuelInItem(ItemStack stack) {
		if(this.hasFuelTag(stack)) {
			return stack.getTagCompound().getInteger("cqr_fuel_item_fuel");
		}
		return 0;
	}
	
	public void addFuel(ItemStack stack, int amount) {
		this.setFuel(stack, amount + getFuelInItem(stack));
	}
	
	public void setFuel(ItemStack stack, int amount) {
		amount = amount < 0 ? 0 : amount;
		stack.getTagCompound().setInteger("cqr_fuel_item_fuel", amount);
	}
	
	public float getFuelInStackInPercent(ItemStack stack) {
		return (float)this.getFuelInItem(stack) / (float)this.getMaxFuel();
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
		return 1.0D - this.getFuelInStackInPercent(stack);
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRGB(this.getFuelInStackInPercent(stack) / 3.0F, 1.0F, 1.0F);
	}
	
	public List<ItemStack> getFuelItemsInInventory(IInventory inventoryToQuery) {
		List<ItemStack> result = new ArrayList<>();
		for(int i = 0; i < inventoryToQuery.getFieldCount(); i++) {
			ItemStack cis = inventoryToQuery.getStackInSlot(i);
			if(this.isValidFuel(cis)) {
				result.add(cis);
			}
		}
		return result;
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack def = super.getDefaultInstance();
		
		if(!def.hasTagCompound()) {
			def.setTagCompound(new NBTTagCompound());
		}
		
		this.setFuel(def, this.getMaxFuel());
		
		return def;
		
	}
	
	public boolean isValidFuel(ItemStack item) {
		return this.predicateValidFuel.test(item);
	}
	
	@Override
	public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return this.getFuelInItem(newStack) > 0;
	}
	
}
