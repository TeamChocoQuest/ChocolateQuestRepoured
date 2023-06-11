package team.cqr.cqrepoured.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentLightningProtection extends Enchantment {

	public EnchantmentLightningProtection() {
		this(Rarity.RARE, EnchantmentType.ARMOR_HEAD, new EquipmentSlotType[] { EquipmentSlotType.HEAD });
	}

	private EnchantmentLightningProtection(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
