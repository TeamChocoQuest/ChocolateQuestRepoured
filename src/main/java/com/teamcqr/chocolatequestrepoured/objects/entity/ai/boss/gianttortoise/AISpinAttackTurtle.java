package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;

public class AISpinAttackTurtle extends AnimationAI<EntityCQRGiantTortoise> {

	public AISpinAttackTurtle(EntityCQRGiantTortoise entity, Animation spinAnim, Animation spinUpAnim, Animation spinDownAnim) {
		super(entity);
	}

	@Override
	public Animation getAnimation() {
		return EntityCQRGiantTortoise.ANIMATION_SPIN;
	}

	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
