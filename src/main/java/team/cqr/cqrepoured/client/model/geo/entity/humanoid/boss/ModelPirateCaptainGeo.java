package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class ModelPirateCaptainGeo extends AbstractModelHumanoidGeo<EntityCQRPirateCaptain> {

	public ModelPirateCaptainGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRPirateCaptain animatable) {
		return CQRAnimations.Entity.PIRATE_CAPTAIN;
	}

}
