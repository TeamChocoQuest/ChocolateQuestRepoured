package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

public interface IEntityAISpell {

	public int getWeight();

	public boolean ignoreWeight();

	public boolean shouldExecute();

	public boolean shouldContinueExecuting();

	public boolean isInterruptible();

	public void startExecuting();

	public void resetTask();

	public void updateTask();

}
