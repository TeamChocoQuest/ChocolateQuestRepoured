package team.cqr.cqrepoured.client.model.geo;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class AbstractModelGeoCQRStandardBiped<T extends AbstractEntityCQR & IAnimatableCQR> extends AbstractModelGeoCQRBase<T> {

	public AbstractModelGeoCQRStandardBiped(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, entityName);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return STANDARD_BIPED_ANIMATIONS;
	}

}
