package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.client.model.geo.entity.AbstractModelHumanoidGeo;
import team.cqr.cqrepoured.entity.IAnimatableCQR;
import team.cqr.cqrepoured.entity.boss.AbstractEntityCQRMageBase;

public class AbstractModelMageGeo<T extends AbstractEntityCQRMageBase & IAnimatableCQR> extends AbstractModelHumanoidGeo<T> {
	
	static final ResourceLocation STANDARD_MODEL_HIDDEN_MAGE = CQRMain.prefix("geo/entity/boss/mage/biped_mage_base.geo.json");
	static final ResourceLocation STANDARD_TEXTURE_HIDDEN_MAGE = CQRMain.prefix("textures/entity/boss/mage_hidden.png");
	
	protected final ResourceLocation MODEL_HIDDEN;
	protected final ResourceLocation TEXTURE_HIDDEN;
	
	public AbstractModelMageGeo(ResourceLocation model, ResourceLocation textureDefault, ResourceLocation modelHidden, ResourceLocation texHidden, String entityName, final ResourceLocation... hierarchy) {
		super(model, textureDefault, entityName, merge(CQRAnimations.Entity.MAGE_HIDDEN, hierarchy));
		
		this.MODEL_HIDDEN = modelHidden;
		this.TEXTURE_HIDDEN = texHidden;
	}
	
	@Override
	public ResourceLocation getTextureResource(T entity) {
		if(entity.isIdentityHidden()) {
			return TEXTURE_HIDDEN;
		}
		return super.getTextureResource(entity);
	}
	
	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return CQRAnimations.Entity.MAGE_HIDDEN;
	}
	
	@Override
	public ResourceLocation getModelResource(T object) {
		if(object.isIdentityHidden()) {
			return MODEL_HIDDEN;
		}
		return super.getModelResource(object);
	}
	
	@Override
	protected String getHeadBoneIdent() {
		return STANDARD_HEAD_IDENT;
	}

}
