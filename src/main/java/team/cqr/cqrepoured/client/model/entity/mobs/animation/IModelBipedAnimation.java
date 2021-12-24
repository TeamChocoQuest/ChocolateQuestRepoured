package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public interface IModelBipedAnimation {

	boolean canApply(AbstractEntityCQR entity);

	void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity);

}
