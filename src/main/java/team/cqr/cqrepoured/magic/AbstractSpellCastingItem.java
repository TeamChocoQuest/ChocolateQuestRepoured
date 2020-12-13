package team.cqr.cqrepoured.magic;


import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.cqr.cqrepoured.capability.itemhandler.item.CapabilityItemHandlerItemProvider;

public abstract class AbstractSpellCastingItem extends Item {
	
	public AbstractSpellCastingItem() {
		super();
		this.setMaxStackSize(1);
	}

	abstract float getCastingSpeedModifier();
	abstract float getManaCostModifier();
	abstract float getSpellPowerModifier();
	
	abstract int getSpellSlotCount();
	
	public float getCastingSpeedModifier(ItemStack castingItem) {
		//TODO: Add capapbility taht stores this certain float value
		return 1;
	}
	
	public float getManaCostModifier(ItemStack castingItem) {
		//TODO: Add capapbility taht stores this certain float value
		return 1;
	}
	
	public float getSpellPowerModifier(ItemStack castingItem) {
		//TODO: Add capapbility taht stores this certain float value
		return 1;
	}
	
	@Nullable
	public AbstractSpell getCurrentSpell(ItemStack castingItem) {
		//TODO: Add capability that stores the slot-id of the currently selected spell
		return null;
	}
	
	//Inventory
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityItemHandlerItemProvider.createProvider(stack, this.getSpellSlotCount());
	}
}
