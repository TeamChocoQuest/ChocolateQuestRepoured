package team.cqr.cqrepoured.enchantment;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EnchantmentSpectral extends DamageEnchantment {

	public EnchantmentSpectral() {
		super(Rarity.VERY_RARE, 0, EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public float getDamageBonus(int level, CreatureAttribute creatureType) {
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
		livingTarget.addEffect(new EffectInstance(Effects.WEAKNESS, i, 2));
	}
	
}
