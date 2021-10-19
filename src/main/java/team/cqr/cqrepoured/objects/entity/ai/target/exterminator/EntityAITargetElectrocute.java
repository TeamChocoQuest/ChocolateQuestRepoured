package team.cqr.cqrepoured.objects.entity.ai.target.exterminator;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAITargetElectrocute extends EntityAICQRNearestAttackTarget {

	private Supplier<EntityLivingBase> funcGetElectrocuteTarget;
	private Consumer<EntityLivingBase> funcSetElectrocuteTarget;

	public EntityAITargetElectrocute(AbstractEntityCQR entity, Supplier<EntityLivingBase> funcGetElectrocuteTarget, Consumer<EntityLivingBase> funcSetElectrocuteTarget) {
		super(entity);

		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcSetElectrocuteTarget = funcSetElectrocuteTarget;
	}

	@Override
	protected EntityLivingBase wrapperGetAttackTarget() {
		return this.funcGetElectrocuteTarget.get();
	}

	@Override
	protected void wrapperSetAttackTarget(EntityLivingBase target) {
		this.funcSetElectrocuteTarget.accept(target);
	}

	@Override
	protected boolean canTargetAlly() {
		return false;
	}

	@Override
	protected boolean isSuitableTargetAlly(EntityLivingBase possibleTarget) {
		return false;
	}

	@Override
	protected boolean isStillSuitableTarget(EntityLivingBase possibleTarget) {
		return possibleTarget != null && this.entity.getDistance(possibleTarget) <= (this.entity.getWidth() + 4 * CQRConfig.general.electricFieldEffectSpreadRange) && super.isStillSuitableTarget(possibleTarget) && this.entity.canEntityBeSeen(possibleTarget);
	}

	@Override
	protected boolean isSuitableTargetEnemy(EntityLivingBase possibleTarget) {
		return possibleTarget != null && this.entity.getDistance(possibleTarget) <= (this.entity.getWidth() + CQRConfig.general.electricFieldEffectSpreadRange * 2) && super.isSuitableTargetEnemy(possibleTarget) && TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(possibleTarget) && !TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(possibleTarget);
	}

}
