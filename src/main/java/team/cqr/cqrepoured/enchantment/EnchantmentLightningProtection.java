package team.cqr.cqrepoured.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import team.cqr.cqrepoured.CQRMain;

public class EnchantmentLightningProtection extends Enchantment {

	public EnchantmentLightningProtection() {
		this(Rarity.RARE, EnchantmentType.ARMOR_HEAD, new EquipmentSlotType[] { EquipmentSlotType.HEAD });
	}

	private EnchantmentLightningProtection(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("lightning_protection");
		this.setRegistryName(CQRMain.MODID, "lightning_protection");
	}

	@Override
	public int getMaxLevel() {
		return 8;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

}
