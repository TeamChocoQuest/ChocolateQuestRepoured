package team.cqr.cqrepoured.capability.armor.attachment;

import net.minecraft.world.item.ItemStack;

public class CapabilityArmorAttachment implements ICapabilityArmorAttachment {

	private ItemStack attachment = ItemStack.EMPTY;
	
	@Override
	public void setAttachment(ItemStack attachment) {
		this.attachment = attachment;
	}

	@Override
	public ItemStack getAttachment() {
		return this.attachment;
	}

}
