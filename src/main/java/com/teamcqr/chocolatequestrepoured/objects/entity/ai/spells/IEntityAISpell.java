package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

public interface IEntityAISpell {

	boolean shouldExecute();

	boolean shouldContinueExecuting();

	boolean isInterruptible();

	void startExecuting();

	void resetTask();

	void updateTask();

	void startChargingSpell();

	void chargeSpell();

	void startCastingSpell();

	void castSpell();

	boolean isCharging();

	boolean isCasting();

	int getWeight();

	boolean ignoreWeight();

}
