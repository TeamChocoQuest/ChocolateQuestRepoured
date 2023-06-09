package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;

public class ModelCQRPirateGeo extends AbstractModelHumanoidGeo<EntityCQRPirate> {

	public ModelCQRPirateGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.PIRATE);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRPirate animatable) {
		return CQRAnimations.Entity.PIRATE;
	}

}
