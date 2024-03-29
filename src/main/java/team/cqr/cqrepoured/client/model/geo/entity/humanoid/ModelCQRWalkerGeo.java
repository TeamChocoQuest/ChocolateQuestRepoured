package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;

public class ModelCQRWalkerGeo extends AbstractModelHumanoidGeo<EntityCQRWalker> {

	public ModelCQRWalkerGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.WALKER);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

	@Override
	public ResourceLocation getAnimationResource(EntityCQRWalker animatable) {
		return CQRAnimations.Entity.WALKER;
	}
	
}
