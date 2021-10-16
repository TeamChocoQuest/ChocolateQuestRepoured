package team.cqr.cqrepoured.objects.entity.ai.target.exterminator;

import java.util.function.Function;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.objects.entity.ai.target.EntityAICQRNearestAttackTarget;
import team.cqr.cqrepoured.objects.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAITargetElectrocute extends EntityAICQRNearestAttackTarget {

	private Function<Object, EntityLivingBase> funcGetElectrocuteTarget;
	private Function<EntityLivingBase, Object> funcSetElectrocuteTarget;
	
	public EntityAITargetElectrocute(AbstractEntityCQR entity, Function<Object, EntityLivingBase> funcGetElectrocuteTarget, Function<EntityLivingBase, Object> funcSetElectrocuteTarget) {
		super(entity);
		
		this.funcGetElectrocuteTarget = funcGetElectrocuteTarget;
		this.funcSetElectrocuteTarget = funcSetElectrocuteTarget;
	}
	
	@Override
	protected EntityLivingBase wrapperGetAttackTarget() {
		return this.funcGetElectrocuteTarget.apply(null);
	}
	
	@Override
	protected void wrapperSetAttackTarget(EntityLivingBase target) {
		this.funcSetElectrocuteTarget.apply(target);
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
	protected boolean isSuitableTargetEnemy(EntityLivingBase possibleTarget) {
		return super.isSuitableTargetEnemy(possibleTarget) && TargetUtil.PREDICATE_CAN_BE_ELECTROCUTED.apply(possibleTarget) && !TargetUtil.PREDICATE_IS_ELECTROCUTED.apply(possibleTarget);
	}

}
