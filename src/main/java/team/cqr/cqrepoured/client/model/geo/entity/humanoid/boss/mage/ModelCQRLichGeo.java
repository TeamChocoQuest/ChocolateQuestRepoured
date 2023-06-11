package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.entity.boss.EntityCQRLich;

public class ModelCQRLichGeo extends AbstractModelMageGeo<EntityCQRLich> {

	public ModelCQRLichGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, STANDARD_MODEL_HIDDEN_MAGE, STANDARD_TEXTURE_HIDDEN_MAGE, entityName, CQRAnimations.Entity.LICH);
	}
	
	@Override
	public ResourceLocation getAnimationResource(EntityCQRLich animatable) {
		if(animatable.isIdentityHidden()) {
			return super.getAnimationResource(animatable);
		}
		return CQRAnimations.Entity.LICH;
	}

}
