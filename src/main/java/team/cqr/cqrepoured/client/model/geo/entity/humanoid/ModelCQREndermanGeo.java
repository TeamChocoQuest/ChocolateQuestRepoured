package team.cqr.cqrepoured.client.model.geo.entity.humanoid;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class ModelCQREndermanGeo<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelHumanoidGeo<T> {
	
	protected final ResourceLocation STANDARD_ENDERMAN_ANIMATIONS = CQRMain.prefix("animations/biped_enderman.animation.json");

	public ModelCQREndermanGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName, CQRAnimations.Entity.ENDERMAN);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return CQRAnimations.Entity.ENDERMAN;
	}

}
