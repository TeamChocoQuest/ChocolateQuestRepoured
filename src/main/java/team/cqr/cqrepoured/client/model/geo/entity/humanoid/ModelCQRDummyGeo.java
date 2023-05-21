package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;

public class ModelCQRDummyGeo extends AbstractModelHumanoidGeo<EntityCQRDummy> {

	public ModelCQRDummyGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.DUMMY);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRDummy animatable) {
		return CQRAnimations.Entity.DUMMY;
	}

}
