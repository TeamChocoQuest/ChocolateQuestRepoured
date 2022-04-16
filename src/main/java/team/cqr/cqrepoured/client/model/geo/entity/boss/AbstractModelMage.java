package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.boss.AbstractEntityCQRMageBase;

public class AbstractModelMage<T extends AbstractEntityCQRMageBase & IAnimatableCQR> extends AbstractModelGeoCQRStandardBiped<T> {

	protected final ResourceLocation MODEL_HIDDEN;
	protected final ResourceLocation ANIMATIONS_HIDDEN;
	protected final ResourceLocation TEXTURE_HIDDEN;
	
	public AbstractModelMage(ResourceLocation model, ResourceLocation textureDefault, ResourceLocation modelHidden, ResourceLocation animsHidden, ResourceLocation texHidden, String entityName) {
		super(model, textureDefault, entityName);
		
		this.MODEL_HIDDEN = modelHidden;
		this.ANIMATIONS_HIDDEN = animsHidden;
		this.TEXTURE_HIDDEN = texHidden;
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if(entity.isIdentityHidden()) {
			return TEXTURE_HIDDEN;
		}
		return super.getTextureLocation(entity);
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		if(animatable.isIdentityHidden()) {
			return ANIMATIONS_HIDDEN;
		}
		return super.getAnimationFileLocation(animatable);
	}
	
	@Override
	public ResourceLocation getModelLocation(T object) {
		if(object.isIdentityHidden()) {
			return MODEL_HIDDEN;
		}
		return super.getModelLocation(object);
	}

}
