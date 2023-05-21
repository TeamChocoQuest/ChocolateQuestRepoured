package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;

public class ModelCQRHumanGeo extends AbstractModelHumanoidGeo<EntityCQRHuman> {

	public ModelCQRHumanGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.HUMAN);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRHuman animatable) {
		return CQRAnimations.Entity.HUMAN;
	}

}
