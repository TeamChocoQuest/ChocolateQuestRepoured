package team.cqr.cqrepoured.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import team.cqr.cqrepoured.CQRMain;

public class EnchantmentLightningProtection extends Enchantment {

	public EnchantmentLightningProtection() {
		this(Rarity.RARE, EnumEnchantmentType.ARMOR_HEAD, new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD });
	}

	private EnchantmentLightningProtection(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
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
