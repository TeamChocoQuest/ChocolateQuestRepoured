package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class ModelWalkerKingGeo<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelHumanoidGeo<T> {

	public ModelWalkerKingGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.WALKER_KING);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return CQRAnimations.Entity.WALKER_KING;
	}

}
