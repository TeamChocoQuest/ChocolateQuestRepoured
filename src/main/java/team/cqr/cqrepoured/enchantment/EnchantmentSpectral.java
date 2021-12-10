package team.cqr.cqrepoured.enchantment;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EnchantmentSpectral extends EnchantmentDamage {

	public EnchantmentSpectral() {
		super(Rarity.VERY_RARE, 0, EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND);
		this.setName("spectral");
		this.setRegistryName(CQRMain.MODID, "spectral");
	}

	@Override
	public String getName() {
		return "enchantment." + this.name;
	}

	@Override
	public boolean isTreasureEnchantment() {
		return true;
	}

	@Override
	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
		// TODO make this effective against vanilla endermen
		if (creatureType == CQRCreatureAttributes.VOID) {
			return level * 1.5F;
		}
		return 0;
	}

	@Override
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
		if (!(target instanceof EntityLivingBase)) {
			return;
		}
		EntityLivingBase livingTarget = (EntityLivingBase) target;
		if (livingTarget.getCreatureAttribute() != CQRCreatureAttributes.VOID) {
			return;
		}
		int i = 20 + user.getRNG().nextInt(10 * level);
		livingTarget.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, i, 2));
	}

}
