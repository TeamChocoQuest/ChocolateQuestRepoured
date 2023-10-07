package team.cqr.cqrepoured.capability.armor.attachment;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegisterCapability
public interface ICapabilityArmorAttachment extends INBTSerializable<CompoundTag> {
	
	public void setAttachment(@Nonnull ItemStack attachment);
	
	@Nonnull
	public ItemStack getAttachment();
	
	@Override
	public default CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		if (this.getAttachment().isEmpty()) {
			return tag;
		}
		tag.putString("id", ForgeRegistries.ITEMS.getKey(this.getAttachment().getItem()).toString());
		tag.put("data", this.getAttachment().serializeNBT());
		
		return tag;
	}

	@Override
	public default void deserializeNBT(CompoundTag nbt) {
		if (nbt.isEmpty()) {
			return;
		}
		String id = nbt.getString("id");
		if (id.isEmpty()) {
			return;
		}
		ResourceLocation resLoc = new ResourceLocation(id);
		if (ForgeRegistries.ITEMS.containsKey(resLoc)) {
			Item item = ForgeRegistries.ITEMS.getValue(resLoc);
			
			ItemStack stack = new ItemStack(item);
			Tag tag = nbt.get("data");
			if (tag instanceof CompoundTag ct && !ct.isEmpty()) {
				stack.deserializeNBT(ct);
			}
			this.setAttachment(stack);
		}
	}

}
