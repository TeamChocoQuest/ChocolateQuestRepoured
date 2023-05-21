package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;

public class ModelCQRMandrilGeo extends AbstractModelHumanoidGeo<EntityCQRMandril> {
	
	protected final ResourceLocation STANDARD_MANDRIL_ANIMATIONS = CQRMain.prefix("animations/biped_mandril.animation.json");

	public ModelCQRMandrilGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.MANDRIL);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRMandril animatable) {
		return CQRAnimations.Entity.MANDRIL;
	}

}
