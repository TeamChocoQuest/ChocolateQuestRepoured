package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;

public class ModelCQROrcGeo extends AbstractModelHumanoidGeo<EntityCQROrc> {

	public ModelCQROrcGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQROrc animatable) {
		return CQRAnimations.Entity.ORC;
	}

}
