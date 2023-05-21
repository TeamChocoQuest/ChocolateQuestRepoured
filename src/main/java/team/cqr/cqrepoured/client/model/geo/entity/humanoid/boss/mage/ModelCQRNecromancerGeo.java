package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.entity.boss.EntityCQRNecromancer;

public class ModelCQRNecromancerGeo extends AbstractModelMageGeo<EntityCQRNecromancer> {

	public ModelCQRNecromancerGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, STANDARD_MODEL_HIDDEN_MAGE, STANDARD_TEXTURE_HIDDEN_MAGE, entityName, CQRAnimations.Entity.NECROMANCER);
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRNecromancer animatable) {
		if(animatable.isIdentityHidden()) {
			return super.getAnimationFileLocation(animatable);
		}
		return CQRAnimations.Entity.NECROMANCER;
	}

}
