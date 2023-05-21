package team.cqr.cqrepoured.client.model.geo.entity.boss;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.geo.AbstractModelGeoCQRStandardBiped;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.boss.AbstractEntityCQRMageBase;

public class AbstractModelMageGeo<T extends AbstractEntityCQRMageBase & IAnimatableCQR> extends AbstractModelGeoCQRStandardBiped<T> {
	
	static final ResourceLocation STANDARD_ANIMATIONS_HIDDEN_MAGE = CQRMain.prefix("animations/biped_standard.animation.json");
	static final ResourceLocation STANDARD_MODEL_HIDDEN_MAGE = CQRMain.prefix("geo/entity/boss/mage/biped_mage_base.geo.json");
	static final ResourceLocation STANDARD_TEXTURE_HIDDEN_MAGE = CQRMain.prefix("textures/entity/boss/mage_hidden.png");
	
	protected final ResourceLocation MODEL_HIDDEN;
	protected final ResourceLocation ANIMATIONS_HIDDEN;
	protected final ResourceLocation TEXTURE_HIDDEN;
	
	public AbstractModelMageGeo(ResourceLocation model, ResourceLocation textureDefault, ResourceLocation modelHidden, ResourceLocation animsHidden, ResourceLocation texHidden, String entityName) {
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
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

}
