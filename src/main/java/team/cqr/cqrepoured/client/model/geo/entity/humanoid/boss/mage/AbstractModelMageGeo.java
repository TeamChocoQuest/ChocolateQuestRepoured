package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.util.ResourceLocation;
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
	
	public AbstractModelMageGeo(ResourceLocation model, ResourceLocation textureDefault, ResourceLocation modelHidden, ResourceLocation texHidden, String entityName) {
		super(model, textureDefault, entityName);
		
		this.MODEL_HIDDEN = modelHidden;
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
		return CQRAnimations.Entity.MAGE_HIDDEN;
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
