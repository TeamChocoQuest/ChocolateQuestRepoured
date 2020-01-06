package de.DerToaster.AnimoLib;

public interface IAnimatedModel {

	public AnimationHandler getHandler();

	public void onSetRotateAngles(IAnimatedEntity entity);

	public void onRender(IAnimatedEntity entity);

}
