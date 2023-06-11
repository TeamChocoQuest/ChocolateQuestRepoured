package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class ModelCQRNecromancerGeo extends AbstractModelMageGeo<EntityCQRNecromancer> {

	public ModelCQRNecromancerGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, STANDARD_MODEL_HIDDEN_MAGE, STANDARD_TEXTURE_HIDDEN_MAGE, entityName, CQRAnimations.Entity.NECROMANCER);
	}
	
	@Override
	public ResourceLocation getAnimationResource(EntityCQRNecromancer animatable) {
		if(animatable.isIdentityHidden()) {
			return super.getAnimationResource(animatable);
		}
		return CQRAnimations.Entity.NECROMANCER;
	}

}
