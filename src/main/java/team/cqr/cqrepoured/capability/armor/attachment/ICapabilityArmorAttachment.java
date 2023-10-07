package team.cqr.cqrepoured.capability.armor.attachment;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface ICapabilityArmorAttachment extends INBTSerializable<CompoundTag> {
	
	public void setAttachment(@Nonnull ItemStack attachment);
	
	@Nonnull
	public ItemStack getAttachment();
	
	@Override
	public default CompoundTag serializeNBT() {
		if (this.getAttachment().isEmpty()) {
			return new CompoundTag();
		}
		return this.getAttachment().serializeNBT();
	}

	@Override
	public default void deserializeNBT(CompoundTag nbt) {
		if (nbt.isEmpty()) {
			return;
		}
		ItemStack stack = ItemStack.of(nbt);
		this.setAttachment(stack);
	}

}
