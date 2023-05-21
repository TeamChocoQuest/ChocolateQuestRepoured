package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;

public class ModelSpectrelordGeo extends AbstractModelHumanoidGeo<EntityCQRSpectreLord> {

	public ModelSpectrelordGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRSpectreLord animatable) {
		return CQRAnimations.Entity.SPECTRE_LORD;
	}

}
