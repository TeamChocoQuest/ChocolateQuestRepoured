package team.cqr.cqrepoured.objects.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemGoldenFeather extends ItemLore {

	public ItemGoldenFeather() {
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(385);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// mainhand
		if (!isSelected) {
			return;
		}
		if (entityIn.fallDistance <= 0.0F) {
			return;
		}
		worldIn.spawnParticle(EnumParticleTypes.CLOUD, entityIn.posX, entityIn.posY, entityIn.posZ, (itemRand.nextFloat() - 0.5F) / 2.0F, -0.5D,
				(itemRand.nextFloat() - 0.5F) / 2.0F);
	}

}
