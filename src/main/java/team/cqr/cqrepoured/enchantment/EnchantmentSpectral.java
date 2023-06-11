package team.cqr.cqrepoured.enchantment;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EnchantmentSpectral extends DamageEnchantment {

	public EnchantmentSpectral() {
		super(Rarity.VERY_RARE, 0, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public float getDamageBonus(int level, MobType creatureType) {
		if (creatureType == CQRCreatureAttributes.VOID) {
			return level * 1.5F;
		}
		return 0;
	}
	
	@Override
	public void doPostAttack(LivingEntity user, Entity target, int level) {
		if (!(target instanceof LivingEntity)) {
			return;
		}
		LivingEntity livingTarget = (LivingEntity) target;
		if (livingTarget.getMobType() != CQRCreatureAttributes.VOID) {
			return;
		}
		int i = 20 + user.getRandom().nextInt(10 * level);
		livingTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, i, 2));
	}
	
}
