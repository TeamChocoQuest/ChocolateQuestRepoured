package team.cqr.cqrepoured.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.world.entity.EquipmentSlot;

public class EnchantmentLightningProtection extends Enchantment {

	public EnchantmentLightningProtection() {
		this(Rarity.RARE, EnchantmentType.ARMOR_HEAD, new EquipmentSlot[] { EquipmentSlot.HEAD });
	}

	private EnchantmentLightningProtection(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMaxLevel() {
		return 8;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
	
	@Override
	public int getMinLevel() {
		return 1;
	}

}
