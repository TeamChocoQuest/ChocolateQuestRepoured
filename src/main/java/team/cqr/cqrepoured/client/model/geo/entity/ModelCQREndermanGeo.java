package team.cqr.cqrepoured.client.model.geo.entity;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class ModelCQREndermanGeo<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelGeoCQRStandardBiped<T> {
	
	protected final ResourceLocation STANDARD_ENDERMAN_ANIMATIONS = CQRMain.prefix("animations/biped_enderman.animation.json");

	public ModelCQREndermanGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return STANDARD_ENDERMAN_ANIMATIONS;
	}

}
