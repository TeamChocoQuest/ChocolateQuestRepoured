package team.cqr.cqrepoured.enchantment;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EnchantmentSpectral extends DamageEnchantment {

	public EnchantmentSpectral() {
		super(Rarity.VERY_RARE, 0, EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND);
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
	public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
		// TODO make this effective against vanilla endermen
		if (creatureType == CQRCreatureAttributes.VOID) {
			return level * 1.5F;
		}
		return 0;
	}

	@Override
	public void onEntityDamaged(LivingEntity user, Entity target, int level) {
		if (!(target instanceof LivingEntity)) {
			return;
		}
		LivingEntity livingTarget = (LivingEntity) target;
		if (livingTarget.getCreatureAttribute() != CQRCreatureAttributes.VOID) {
			return;
		}
		int i = 20 + user.getRNG().nextInt(10 * level);
		livingTarget.addPotionEffect(new EffectInstance(Effects.WEAKNESS, i, 2));
	}

}
