package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;

public class ModelCQRIllagerGeo extends AbstractModelHumanoidGeo<EntityCQRIllager> {

	public ModelCQRIllagerGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.ILLAGER);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRIllager animatable) {
		return CQRAnimations.Entity.ILLAGER;
	}

}
