package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMummy;

public class ModelCQRMummyGeo extends AbstractModelHumanoidGeo<EntityCQRMummy> {

	public ModelCQRMummyGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.MUMMY);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRMummy animatable) {
		return CQRAnimations.Entity.MUMMY;
	}

}
