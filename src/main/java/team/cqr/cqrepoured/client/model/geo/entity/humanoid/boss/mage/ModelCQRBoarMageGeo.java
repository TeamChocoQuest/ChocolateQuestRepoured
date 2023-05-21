package team.cqr.cqrepoured.client.model.geo.entity.humanoid.boss.mage;

import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.init.CQRAnimations;
import team.cqr.cqrepoured.entity.boss.EntityCQRBoarmage;

public class ModelCQRBoarMageGeo extends AbstractModelMageGeo<EntityCQRBoarmage> {

	public ModelCQRBoarMageGeo(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
		super(model, textureDefault, STANDARD_MODEL_HIDDEN_MAGE, STANDARD_TEXTURE_HIDDEN_MAGE, entityName, CQRAnimations.Entity.BOAR_MAGE);
	}
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQRBoarmage animatable) {
		if(animatable.isIdentityHidden()) {
			return super.getAnimationFileLocation(animatable);
		}
		return CQRAnimations.Entity.BOAR_MAGE;
	}

}
