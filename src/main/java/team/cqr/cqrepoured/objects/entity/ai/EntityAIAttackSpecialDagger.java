package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.swords.ItemDagger;

public class EntityAIAttackSpecialDagger extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialDagger() {
		super(true, false, 10, 100);
	}

	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemDagger;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemDagger;
	}

	@Override
	public boolean isInterruptible(AbstractEntityCQR entity) {
		return false;
	}

	@Override
	public void startAttack(AbstractEntityCQR attacker, EntityLivingBase target) {

	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, EntityLivingBase target, int tick) {

	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, EntityLivingBase target) {

	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {

	}
}
