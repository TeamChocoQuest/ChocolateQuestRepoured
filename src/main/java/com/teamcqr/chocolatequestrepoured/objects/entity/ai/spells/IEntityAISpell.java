package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

public interface IEntityAISpell {

	public boolean shouldExecute();

	public boolean shouldContinueExecuting();

	public boolean isInterruptible();

	public void startExecuting();

	public void resetTask();

	public void updateTask();

	public void startChargingSpell();

	public void chargeSpell();

	public void startCastingSpell();

	public void castSpell();

	public boolean isCharging();

	public boolean isCasting();

	public int getWeight();

	public boolean ignoreWeight();

}
