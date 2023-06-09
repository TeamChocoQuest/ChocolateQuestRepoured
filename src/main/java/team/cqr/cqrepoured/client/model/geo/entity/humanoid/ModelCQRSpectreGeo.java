package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;

public class ModelCQRSpectreGeo extends AbstractModelHumanoidGeo<EntityCQRSpectre> {

	public ModelCQRSpectreGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.SPECTRE);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRSpectre animatable) {
		return CQRAnimations.Entity.SPECTRE;
	}

}
