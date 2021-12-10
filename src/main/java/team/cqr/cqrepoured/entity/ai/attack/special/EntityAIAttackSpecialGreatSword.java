package team.cqr.cqrepoured.entity.ai.attack.special;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;

public class EntityAIAttackSpecialGreatSword extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialGreatSword() {
		super(true, false, 10, 100);
	}

	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemGreatSword;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemGreatSword;
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
