package de.DerToaster.AnimoLib;

public interface IAnimatedEntity{
	
	public void onTick();
	public AnimationHandler getAnimationHandler();
	public void setAnimation(String id);
	public String getCurrentAnimation();
	public default void onLivingUpdate() {
		getAnimationHandler().onEntityTick();
	}

}
